package de.muenchen.isi.domain.service.calculation;

import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.domain.model.BaurateModel;
import de.muenchen.isi.domain.model.calculation.LangfristigerPlanungsursaechlicherBedarfModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import java.time.LocalDate;
import java.util.List;
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

    private final PlanungsursaechlicheWohneinheitenService planungsursaechlicheWohneinheitenService;

    /**
     * Dieser Service vereint die Funktionalitäten mehrere Berechnungsservices, um den langfristigen planungsursächlichen Bedarf zu ermitteln.
     *
     * @param bauabschnitte Eine List von {@link BauabschnittModel}, aus denen die {@link BaurateModel} extrahiert werden.
     * @param sobonJahr Das SoBoN-Jahr, welches die städtebaulichen Orientierungswerte diktiert.
     * @param gueltigAb Das Gültigkeitsdatum der Stammdaten, welche die Umlegung diktieren.
     * @return Das {@link LangfristigerPlanungsursaechlicherBedarfModel}.
     */
    public LangfristigerPlanungsursaechlicherBedarfModel calculateLangfristigerPlanungsursaechlicherBedarf(
        final List<BauabschnittModel> bauabschnitte,
        final SobonOrientierungswertJahr sobonJahr,
        final LocalDate gueltigAb
    ) {
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
