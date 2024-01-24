package de.muenchen.isi.domain.service.calculation;

import de.muenchen.isi.domain.exception.CalculationException;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.AbfragevarianteBaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.AbfragevarianteBauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.AbfragevarianteModel;
import de.muenchen.isi.domain.model.AbfragevarianteWeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.domain.model.BaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.WeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.calculation.BedarfeForAbfragevarianteModel;
import de.muenchen.isi.domain.model.calculation.LangfristigerBedarfModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

/**
 * Die Serviceklasse zur Ermittlung der planungsursächlichen und sobonursächlichen Bedarfe.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CalculationService {

    public static final int DIVISION_SCALE = 15;

    public static final String SUMMATION_PERIOD_NAME = "Summe erste %d J.";

    public static final String SUMMATION_TOTAL_NAME = "Gesamt";

    private final PlanungsursaechlicheWohneinheitenService planungsursaechlicheWohneinheitenService;

    private final InfrastrukturbedarfService infrastrukturbedarfService;

    /**
     * Die Methode ermittelt den planungs- und sobonursächlichen {@link LangfristigerBedarfModel} für jede in der Abfrage vorhandene Abfragevariante.
     * Ist keine Berechnung der Bedarfe möglich, so wird der Wert null im planungs- und sobonursächlichen {@link LangfristigerBedarfModel} gesetzt.
     *
     * Handelt es sich um Abfragevarianten mit Wert {@link SobonOrientierungswertJahr#STANDORTABFRAGE} im Attribut
     * {@link AbfragevarianteWeiteresVerfahrenModel#getSobonOrientierungswertJahr()} wird keine Berechnungen des planungsursächlichen Bedarfs durchgeführt.
     *
     * @param abfrage zum Ermitteln der langfristigen planungs- und sobonursächlichen Bedarfe je Abfragevariante.
     * @return die planungs- und sobonursächlichen Bedarfe je Abfragevariante repräsentiert durch die eindeutige ID der Abfragevariante.
     * @throws CalculationException falls keine Berechnung wegen einer nicht gesetzten Art der Abfrage oder Abfragevariante oder nicht vorhandener Stammdaten möglich ist.
     */
    public Map<UUID, BedarfeForAbfragevarianteModel> calculateBedarfeForEachAbfragevarianteOfAbfrage(
        final AbfrageModel abfrage
    ) throws CalculationException {
        List<? extends AbfragevarianteModel> abfragevarianten;
        if (ArtAbfrage.BAULEITPLANVERFAHREN.equals(abfrage.getArtAbfrage())) {
            final var bauleitplanverfahren = (BauleitplanverfahrenModel) abfrage;
            abfragevarianten =
                ListUtils.union(
                    ListUtils.emptyIfNull(bauleitplanverfahren.getAbfragevariantenBauleitplanverfahren()),
                    ListUtils.emptyIfNull(bauleitplanverfahren.getAbfragevariantenSachbearbeitungBauleitplanverfahren())
                );
        } else if (ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN.equals(abfrage.getArtAbfrage())) {
            final var baugenehmigungsverfahren = (BaugenehmigungsverfahrenModel) abfrage;
            abfragevarianten =
                ListUtils.union(
                    ListUtils.emptyIfNull(baugenehmigungsverfahren.getAbfragevariantenBaugenehmigungsverfahren()),
                    ListUtils.emptyIfNull(
                        baugenehmigungsverfahren.getAbfragevariantenSachbearbeitungBaugenehmigungsverfahren()
                    )
                );
        } else if (ArtAbfrage.WEITERES_VERFAHREN.equals(abfrage.getArtAbfrage())) {
            final var weiteresVerfahren = (WeiteresVerfahrenModel) abfrage;
            abfragevarianten =
                ListUtils.union(
                    ListUtils.emptyIfNull(weiteresVerfahren.getAbfragevariantenWeiteresVerfahren()),
                    ListUtils.emptyIfNull(weiteresVerfahren.getAbfragevariantenSachbearbeitungWeiteresVerfahren())
                );
        } else {
            throw new CalculationException("Die Berechnung kann für diese Art von Abfrage nicht durchgeführt werden.");
        }
        final var bedarfeForEachAbfragevariante = new HashMap<UUID, BedarfeForAbfragevarianteModel>();
        for (final var abfragevariante : abfragevarianten) {
            final var bedarfeForAbfragevariante = this.calculateBedarfeForAbfragevariante(abfragevariante);
            bedarfeForEachAbfragevariante.put(abfragevariante.getId(), bedarfeForAbfragevariante);
        }
        return bedarfeForEachAbfragevariante;
    }

    /**
     * Die Methode ermittelt den planungs- und sobonursächlichen {@link LangfristigerBedarfModel} für die im Parameter gegebene Abfragevariante.
     * Ist keine Berechnung der Bedarfe möglich, so wird der Wert null im planungs- und sobonursächlichen {@link LangfristigerBedarfModel} gesetzt.
     *
     * Handelt es sich um eine Abfragevariante mit Wert {@link SobonOrientierungswertJahr#STANDORTABFRAGE} im Attribut
     * {@link AbfragevarianteWeiteresVerfahrenModel#getSobonOrientierungswertJahr()} wird keine Berechnungen des planungsursächlichen Bedarfs durchgeführt.
     *
     * @param abfragevariante zum Ermitteln der langfristigen planungs- und sobonursächlichen Bedarfe.
     * @return die planungs- und sobonursächlichen Bedarfe der Abfragevariante.
     * @throws CalculationException falls keine Berechnung wegen einer nicht gesetzten Art der Abfragevariante oder nicht vorhandener Stammdaten möglich ist.
     */
    public BedarfeForAbfragevarianteModel calculateBedarfeForAbfragevariante(
        final AbfragevarianteModel abfragevariante
    ) throws CalculationException {
        final List<BauabschnittModel> bauabschnitte;
        final SobonOrientierungswertJahr sobonOrientierungswertJahr;
        final LocalDate stammdatenGueltigAb;
        final var bedarfeForAbfragevariante = new BedarfeForAbfragevarianteModel();
        final LangfristigerBedarfModel langfristigerPlanungsursaechlicherBedarf;
        final LangfristigerBedarfModel langfristigerSobonursaechlicherBedarf;
        if (ArtAbfrage.BAULEITPLANVERFAHREN.equals(abfragevariante.getArtAbfragevariante())) {
            final var abfragevarianteBauleitplanverfahren = (AbfragevarianteBauleitplanverfahrenModel) abfragevariante;
            bauabschnitte = abfragevarianteBauleitplanverfahren.getBauabschnitte();
            sobonOrientierungswertJahr = abfragevarianteBauleitplanverfahren.getSobonOrientierungswertJahr();
            stammdatenGueltigAb = abfragevarianteBauleitplanverfahren.getStammdatenGueltigAb();
            langfristigerPlanungsursaechlicherBedarf =
                this.calculateLangfristigerPlanungsursaechlicherBedarf(
                        bauabschnitte,
                        sobonOrientierungswertJahr,
                        stammdatenGueltigAb
                    );
            langfristigerSobonursaechlicherBedarf = null;
        } else if (ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN.equals(abfragevariante.getArtAbfragevariante())) {
            final var abfragevarianteBaugenehmigungsverfahren =
                (AbfragevarianteBaugenehmigungsverfahrenModel) abfragevariante;
            bauabschnitte = abfragevarianteBaugenehmigungsverfahren.getBauabschnitte();
            sobonOrientierungswertJahr = abfragevarianteBaugenehmigungsverfahren.getSobonOrientierungswertJahr();
            stammdatenGueltigAb = abfragevarianteBaugenehmigungsverfahren.getStammdatenGueltigAb();
            langfristigerPlanungsursaechlicherBedarf =
                this.calculateLangfristigerPlanungsursaechlicherBedarf(
                        bauabschnitte,
                        sobonOrientierungswertJahr,
                        stammdatenGueltigAb
                    );
            langfristigerSobonursaechlicherBedarf = null;
        } else if (ArtAbfrage.WEITERES_VERFAHREN.equals(abfragevariante.getArtAbfragevariante())) {
            final var abfragevarianteWeiteresVerfahren = (AbfragevarianteWeiteresVerfahrenModel) abfragevariante;
            bauabschnitte = abfragevarianteWeiteresVerfahren.getBauabschnitte();
            sobonOrientierungswertJahr = abfragevarianteWeiteresVerfahren.getSobonOrientierungswertJahr();
            stammdatenGueltigAb = abfragevarianteWeiteresVerfahren.getStammdatenGueltigAb();
            langfristigerPlanungsursaechlicherBedarf =
                this.calculateLangfristigerPlanungsursaechlicherBedarf(
                        bauabschnitte,
                        sobonOrientierungswertJahr,
                        stammdatenGueltigAb
                    );
            langfristigerSobonursaechlicherBedarf = null;
        } else {
            throw new CalculationException(
                "Die Berechnung kann für diese Art von Abfragevariante nicht durchgeführt werden."
            );
        }
        bedarfeForAbfragevariante.setLangfristigerPlanungsursaechlicherBedarf(langfristigerPlanungsursaechlicherBedarf);
        bedarfeForAbfragevariante.setLangfristigerSobonursaechlicherBedarf(langfristigerSobonursaechlicherBedarf);
        return bedarfeForAbfragevariante;
    }

    /**
     * Die Methode ermittelt den {@link LangfristigerBedarfModel} für die im Parameter gegebenen Werte.
     *
     * Ist auf Basis der übergebenen Methodenparameter keine Berechnung möglich, so wird der Wert null zurückgegeben.
     *
     * @param bauabschnitte zum Ermitteln der Bedarfe.
     * @param sobonOrientierungswertJahr zur Extraktion der korrekten Sobon-Orientierungswerte.
     * @param stammdatenGueltigAb zur Extraktion der Stammdaten welche sich nicht auf ein konkretes Jahr der Sobon-Orientierungswerte beziehen.
     * @return den {@link LangfristigerBedarfModel} oder null falls auf Basis der übergebenen Methodenparameter keine Berechnung möglich ist.
     * @throws CalculationException falls die Stammdaten zur Durchführung der Berechnung nicht geladen werden können.
     */
    public LangfristigerBedarfModel calculateLangfristigerPlanungsursaechlicherBedarf(
        final List<BauabschnittModel> bauabschnitte,
        final SobonOrientierungswertJahr sobonOrientierungswertJahr,
        final LocalDate stammdatenGueltigAb
    ) throws CalculationException {
        if (
            CollectionUtils.isEmpty(bauabschnitte) ||
            ObjectUtils.anyNull(sobonOrientierungswertJahr, stammdatenGueltigAb) ||
            Objects.equals(sobonOrientierungswertJahr, SobonOrientierungswertJahr.STANDORTABFRAGE)
        ) {
            return null;
        }

        final var bedarf = new LangfristigerBedarfModel();

        // Ermittlung Wohneinheiten
        final var wohneinheiten = planungsursaechlicheWohneinheitenService.calculatePlanungsursaechlicheWohneinheiten(
            bauabschnitte,
            sobonOrientierungswertJahr,
            stammdatenGueltigAb
        );
        bedarf.setWohneinheiten(wohneinheiten);

        final var wohneinheitenSumme10Jahre =
            planungsursaechlicheWohneinheitenService.sumWohneinheitenForNumberOfYearsForEachFoerderart(
                wohneinheiten,
                10
            );
        bedarf.setWohneinheitenSumme10Jahre(wohneinheitenSumme10Jahre);

        final var wohneinheitenSumme15Jahre =
            planungsursaechlicheWohneinheitenService.sumWohneinheitenForNumberOfYearsForEachFoerderart(
                wohneinheiten,
                15
            );
        bedarf.setWohneinheitenSumme15Jahre(wohneinheitenSumme15Jahre);

        final var wohneinheitenSumme20Jahre =
            planungsursaechlicheWohneinheitenService.sumWohneinheitenForNumberOfYearsForEachFoerderart(
                wohneinheiten,
                20
            );
        bedarf.setWohneinheitenSumme20Jahre(wohneinheitenSumme20Jahre);

        final var wohneinheitenSumsForGesamt = Stream
            .of(wohneinheiten, wohneinheitenSumme10Jahre, wohneinheitenSumme15Jahre, wohneinheitenSumme20Jahre)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        final var wohneinheitenGesamt =
            planungsursaechlicheWohneinheitenService.sumWohneinheitenOverFoerderartenForEachYear(
                wohneinheitenSumsForGesamt
            );
        bedarf.setWohneinheitenGesamt(wohneinheitenGesamt);

        // Ermittlung Bedarf Kinderkrippe
        final var bedarfKinderkrippe = infrastrukturbedarfService.calculateBedarfForKinderkrippeRounded(
            wohneinheiten,
            sobonOrientierungswertJahr,
            InfrastrukturbedarfService.ArtInfrastrukturbedarf.PLANUNGSURSAECHLICH,
            stammdatenGueltigAb
        );
        bedarf.setBedarfKinderkrippe(bedarfKinderkrippe);
        final var bedarfKinderkrippeMittelwert10 = infrastrukturbedarfService.calculateMeanInfrastrukturbedarfe(
            bedarfKinderkrippe,
            10
        );
        bedarf.setBedarfKinderkrippeMittelwert10(bedarfKinderkrippeMittelwert10);
        final var bedarfKinderkrippeMittelwert15 = infrastrukturbedarfService.calculateMeanInfrastrukturbedarfe(
            bedarfKinderkrippe,
            15
        );
        bedarf.setBedarfKinderkrippeMittelwert15(bedarfKinderkrippeMittelwert15);
        final var bedarfKinderkrippeMittelwert20 = infrastrukturbedarfService.calculateMeanInfrastrukturbedarfe(
            bedarfKinderkrippe,
            20
        );
        bedarf.setBedarfKinderkrippeMittelwert20(bedarfKinderkrippeMittelwert20);

        // Ermittlung Bedarf Kindergarten
        final var bedarfKindergarten = infrastrukturbedarfService.calculateBedarfForKindergartenRounded(
            wohneinheiten,
            sobonOrientierungswertJahr,
            InfrastrukturbedarfService.ArtInfrastrukturbedarf.PLANUNGSURSAECHLICH,
            stammdatenGueltigAb
        );
        bedarf.setBedarfKindergarten(bedarfKindergarten);
        final var bedarfKindergartenMittelwert10 = infrastrukturbedarfService.calculateMeanInfrastrukturbedarfe(
            bedarfKindergarten,
            10
        );
        bedarf.setBedarfKindergartenMittelwert10(bedarfKindergartenMittelwert10);
        final var bedarfKindergartenMittelwert15 = infrastrukturbedarfService.calculateMeanInfrastrukturbedarfe(
            bedarfKindergarten,
            15
        );
        bedarf.setBedarfKindergartenMittelwert15(bedarfKindergartenMittelwert15);
        final var bedarfKindergartenMittelwert20 = infrastrukturbedarfService.calculateMeanInfrastrukturbedarfe(
            bedarfKindergarten,
            20
        );
        bedarf.setBedarfKindergartenMittelwert20(bedarfKindergartenMittelwert20);

        // Ermittlung aller Einwohner
        final var alleEinwohner = infrastrukturbedarfService.calculateAlleEinwohnerRounded(
            wohneinheiten,
            sobonOrientierungswertJahr
        );
        bedarf.setAlleEinwohner(alleEinwohner);
        final var alleEinwohnerMittelwert10 = infrastrukturbedarfService.calculateMeanPersonen(alleEinwohner, 10);
        bedarf.setAlleEinwohnerMittelwert10(alleEinwohnerMittelwert10);
        final var alleEinwohnerMittelwert15 = infrastrukturbedarfService.calculateMeanPersonen(alleEinwohner, 15);
        bedarf.setAlleEinwohnerMittelwert15(alleEinwohnerMittelwert15);
        final var alleEinwohnerMittelwert20 = infrastrukturbedarfService.calculateMeanPersonen(alleEinwohner, 20);
        bedarf.setAlleEinwohnerMittelwert20(alleEinwohnerMittelwert20);

        return bedarf;
    }
}
