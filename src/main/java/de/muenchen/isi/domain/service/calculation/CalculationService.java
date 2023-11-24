package de.muenchen.isi.domain.service.calculation;

import de.muenchen.isi.domain.exception.CalculationException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
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

    public static final String SUMMATION_PERIOD_NAME = "Summe der ersten %d Jahre";

    public static final String SUMMATION_TOTAL_NAME = "Gesamt";

    private final AbfrageService abfrageService;

    private final PlanungsursaechlicheWohneinheitenService planungsursaechlicheWohneinheitenService;

    /**
     * Dieser Service vereint die Funktionalitäten mehrere Berechnungsservices, um den langfristigen planungsursächlichen Bedarf zu ermitteln.
     *
     * @param abfrageId Die ID der Abfrage, zu der die Abfragevariante gehört.
     * @param abfragevarianteId Die ID der Abfragevariante, für die der Bedarf ermittelt werden soll.
     * @param gueltigAb Das Gültigkeitsdatum der Stammdaten, welche die Umlegung diktieren.
     * @return Das {@link LangfristigerPlanungsursaechlicherBedarfModel}.
     */
    public LangfristigerPlanungsursaechlicherBedarfModel calculateLangfristigerPlanungsursaechlicherBedarf(
        final UUID abfrageId,
        final UUID abfragevarianteId,
        final LocalDate gueltigAb
    ) throws EntityNotFoundException, CalculationException {
        List<BauabschnittModel> bauabschnitte = null;
        SobonOrientierungswertJahr sobonJahr = null;

        final var abfrage = abfrageService.getById(abfrageId);
        switch (abfrage.getArtAbfrage()) {
            case BAULEITPLANVERFAHREN:
                final var bauleitplanverfahren = (BauleitplanverfahrenModel) abfrage;
                final var optionalAbfragevariante = Stream
                    .concat(
                        bauleitplanverfahren.getAbfragevariantenBauleitplanverfahren().stream(),
                        bauleitplanverfahren.getAbfragevariantenSachbearbeitungBauleitplanverfahren().stream()
                    )
                    .filter(abfragevariante -> abfragevariante.getId().equals(abfragevarianteId))
                    .findAny();
                if (optionalAbfragevariante.isPresent()) {
                    bauabschnitte = optionalAbfragevariante.get().getBauabschnitte();
                    sobonJahr = optionalAbfragevariante.get().getSobonOrientierungswertJahr();
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
        final var wohneinheiten = planungsursaechlicheWohneinheitenService.calculatePlanungsursaechlicheWohneinheiten(
            bauabschnitte,
            sobonJahr,
            gueltigAb
        );
        bedarf.setWohneinheiten(wohneinheiten);
        return bedarf;
    }
}
