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
import de.muenchen.isi.domain.model.calculation.LangfristigerPlanungsursaechlicherBedarfModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculationService {

    public static final int DIVISION_SCALE = 10;

    public static final List<Integer> SUMMATION_PERIODS = List.of(10, 15, 20);

    public static final String SUMMATION_PERIOD_NAME = "Summe erste %d J.";

    public static final String SUMMATION_TOTAL_NAME = "Gesamt";

    private final PlanungsursaechlicheWohneinheitenService planungsursaechlicheWohneinheitenService;

    public void calculateAndAppendLangfristigerPlanungsursaechlicherBedarfToEachAbfragevarianteOfAbfrage(
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
        for (final var abfragevariante : abfragevarianten) {
            this.calculateAndAppendLangfristigerPlanungsursaechlicherBedarfToAbfragevariante(abfragevariante);
        }
    }

    public void calculateAndAppendLangfristigerPlanungsursaechlicherBedarfToAbfragevariante(
        final AbfragevarianteModel abfragevariante
    ) throws CalculationException {
        final List<BauabschnittModel> bauabschnitte;
        final SobonOrientierungswertJahr sobonOrientierungswertJahr;
        final LocalDate stammdatenGueltigAb;
        final LangfristigerPlanungsursaechlicherBedarfModel langfristigerPlanungsursaechlicherBedarf;
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
            abfragevarianteBauleitplanverfahren.setLangfristigerPlanungsursaechlicherBedarf(
                langfristigerPlanungsursaechlicherBedarf
            );
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
            abfragevarianteBaugenehmigungsverfahren.setLangfristigerPlanungsursaechlicherBedarf(
                langfristigerPlanungsursaechlicherBedarf
            );
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
            abfragevarianteWeiteresVerfahren.setLangfristigerPlanungsursaechlicherBedarf(
                langfristigerPlanungsursaechlicherBedarf
            );
        } else {
            throw new CalculationException(
                "Die Berechnung kann für diese Art von Abfragevariante nicht durchgeführt werden."
            );
        }
    }

    public LangfristigerPlanungsursaechlicherBedarfModel calculateLangfristigerPlanungsursaechlicherBedarf(
        final List<BauabschnittModel> bauabschnitte,
        final SobonOrientierungswertJahr sobonOrientierungswertJahr,
        final LocalDate stammdatenGueltigAb
    ) {
        if (ObjectUtils.anyNull(bauabschnitte, sobonOrientierungswertJahr, stammdatenGueltigAb)) {
            return null;
        }

        final var bedarf = new LangfristigerPlanungsursaechlicherBedarfModel();
        final var wohneinheiten = planungsursaechlicheWohneinheitenService.calculatePlanungsursaechlicheWohneinheiten(
            bauabschnitte,
            sobonOrientierungswertJahr,
            stammdatenGueltigAb
        );
        bedarf.setWohneinheiten(wohneinheiten);
        return bedarf;
    }
}
