package de.muenchen.isi.domain.service.calculation;

import de.muenchen.isi.domain.exception.CalculationException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.domain.model.BaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.WeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.calculation.LangfristigerPlanungsursaechlicherBedarfModel;
import de.muenchen.isi.domain.service.AbfrageService;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculationService {

    public static final int DIVISION_SCALE = 10;

    public static final List<Integer> SUMMATION_PERIODS = List.of(10, 15, 20);

    public static final String SUMMATION_PERIOD_NAME = "Summe erste %d J.";

    public static final String SUMMATION_TOTAL_NAME = "Gesamt";

    private final AbfrageService abfrageService;

    private final PlanungsursaechlicheWohneinheitenService planungsursaechlicheWohneinheitenService;

    private final InfrastrukturbedarfService infrastrukturbedarfService;

    /**
     * Dieser Service vereint die Funktionalitäten mehrere Berechnungsservices, um den langfristigen planungsursächlichen Bedarf zu ermitteln.
     *
     * @param abfrageId Die ID der Abfrage, zu der die Abfragevariante gehört.
     * @param abfragevarianteId Die ID der Abfragevariante, für die der Bedarf ermittelt werden soll.
     * @return Das {@link LangfristigerPlanungsursaechlicherBedarfModel}.
     */
    public LangfristigerPlanungsursaechlicherBedarfModel calculateLangfristigerPlanungsursaechlicherBedarf(
        final UUID abfrageId,
        final UUID abfragevarianteId
    ) throws EntityNotFoundException, CalculationException {
        List<BauabschnittModel> bauabschnitte = null;
        SobonOrientierungswertJahr sobonJahr = null;
        LocalDate gueltigAb = null;

        final var abfrage = abfrageService.getById(abfrageId);
        switch (abfrage.getArtAbfrage()) {
            case BAULEITPLANVERFAHREN:
                final var bauleitplanverfahren = (BauleitplanverfahrenModel) abfrage;
                final var optionalAbfragevarianteBauleitplanverfahren = Stream
                    .concat(
                        bauleitplanverfahren.getAbfragevariantenBauleitplanverfahren().stream(),
                        bauleitplanverfahren.getAbfragevariantenSachbearbeitungBauleitplanverfahren().stream()
                    )
                    .filter(abfragevariante -> abfragevariante.getId().equals(abfragevarianteId))
                    .findAny();
                if (optionalAbfragevarianteBauleitplanverfahren.isPresent()) {
                    final var abfragevarianteBauleitplanverfahren = optionalAbfragevarianteBauleitplanverfahren.get();
                    bauabschnitte = abfragevarianteBauleitplanverfahren.getBauabschnitte();
                    sobonJahr = abfragevarianteBauleitplanverfahren.getSobonOrientierungswertJahr();
                    gueltigAb = abfragevarianteBauleitplanverfahren.getStammdatenGueltigAb();
                }
                break;
            case BAUGENEHMIGUNGSVERFAHREN:
                final var baugenehmigungsverfahren = (BaugenehmigungsverfahrenModel) abfrage;
                final var optionalAbfragevarianteBaugenehmigungsverfahren = Stream
                    .concat(
                        baugenehmigungsverfahren.getAbfragevariantenBaugenehmigungsverfahren().stream(),
                        baugenehmigungsverfahren.getAbfragevariantenSachbearbeitungBaugenehmigungsverfahren().stream()
                    )
                    .filter(abfragevariante -> abfragevariante.getId().equals(abfragevarianteId))
                    .findAny();
                if (optionalAbfragevarianteBaugenehmigungsverfahren.isPresent()) {
                    final var abfragevarianteBaugenehmigungsverfahren =
                        optionalAbfragevarianteBaugenehmigungsverfahren.get();
                    bauabschnitte = abfragevarianteBaugenehmigungsverfahren.getBauabschnitte();
                    sobonJahr = abfragevarianteBaugenehmigungsverfahren.getSobonOrientierungswertJahr();
                    gueltigAb = abfragevarianteBaugenehmigungsverfahren.getStammdatenGueltigAb();
                }
                break;
            case WEITERES_VERFAHREN:
                final var weiteresVerfahren = (WeiteresVerfahrenModel) abfrage;
                final var optionalAbfragevarianteWeiteresVerfahren = Stream
                    .concat(
                        weiteresVerfahren.getAbfragevariantenWeiteresVerfahren().stream(),
                        weiteresVerfahren.getAbfragevariantenSachbearbeitungWeiteresVerfahren().stream()
                    )
                    .filter(abfragevariante -> abfragevariante.getId().equals(abfragevarianteId))
                    .findAny();
                if (optionalAbfragevarianteWeiteresVerfahren.isPresent()) {
                    final var abfragevarianteWeiteresVerfahren = optionalAbfragevarianteWeiteresVerfahren.get();
                    bauabschnitte = abfragevarianteWeiteresVerfahren.getBauabschnitte();
                    sobonJahr = abfragevarianteWeiteresVerfahren.getSobonOrientierungswertJahr();
                    gueltigAb = abfragevarianteWeiteresVerfahren.getStammdatenGueltigAb();
                }
                break;
            default:
                throw new CalculationException(
                    "Die Berechnung kann für diese Art von Abfrage nicht durchgeführt werden."
                );
        }

        if (bauabschnitte == null || sobonJahr == null) {
            throw new EntityNotFoundException("Abfragevariante nicht gefunden.");
        }

        final var bedarf = new LangfristigerPlanungsursaechlicherBedarfModel();

        // Ermittlung Wohneinheiten
        final var wohneinheiten = planungsursaechlicheWohneinheitenService.calculatePlanungsursaechlicheWohneinheiten(
            bauabschnitte,
            sobonJahr,
            gueltigAb
        );
        bedarf.setWohneinheiten(wohneinheiten);

        // Ermittlung Bedarf Kinderkrippe
        final var bedarfKinderkrippe = infrastrukturbedarfService.calculateBedarfForKinderkrippeRoundedAndWithMean(
            wohneinheiten,
            sobonJahr,
            InfrastrukturbedarfService.ArtInfrastrukturbedarf.PLANUNGSURSAECHLICH,
            gueltigAb
        );
        bedarf.setBedarfKinderkrippe(bedarfKinderkrippe);

        // Ermittlung Bedarf Kindergarten
        final var bedarfKindergarten = infrastrukturbedarfService.calculateBedarfForKindergartenRoundedAndWithMean(
            wohneinheiten,
            sobonJahr,
            InfrastrukturbedarfService.ArtInfrastrukturbedarf.PLANUNGSURSAECHLICH,
            gueltigAb
        );
        bedarf.setBedarfKindergarten(bedarfKindergarten);

        // Ermittlung aller Einwohner
        final var alleEinwohner = infrastrukturbedarfService.calculateAlleEinwohnerRoundedAndWithMean(
            wohneinheiten,
            sobonJahr
        );
        bedarf.setAlleEinwohner(alleEinwohner);

        return bedarf;
    }
}
