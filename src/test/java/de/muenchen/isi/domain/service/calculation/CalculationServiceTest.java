package de.muenchen.isi.domain.service.calculation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import de.muenchen.isi.domain.exception.CalculationException;
import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CalculationServiceTest {

    @Mock
    private PlanungsursaechlicheWohneinheitenService planungsursaechlicheWohneinheitenService;

    @Mock
    private InfrastrukturbedarfService infrastrukturbedarfService;

    private CalculationService calculationService;

    @BeforeEach
    public void beforeEach() {
        this.calculationService =
            new CalculationService(planungsursaechlicheWohneinheitenService, infrastrukturbedarfService);
        Mockito.reset(planungsursaechlicheWohneinheitenService, infrastrukturbedarfService);
    }

    @Test
    void calculateLangfristigerPlanungsursaechlicherBedarfReturnNull() throws CalculationException {
        var result = calculationService.calculateLangfristigerPlanungsursaechlicherBedarf(null, null, null);
        assertThat(result, is(nullValue()));

        result =
            calculationService.calculateLangfristigerPlanungsursaechlicherBedarf(
                null,
                SobonOrientierungswertJahr.JAHR_2017,
                LocalDate.now()
            );
        assertThat(result, is(nullValue()));

        result =
            calculationService.calculateLangfristigerPlanungsursaechlicherBedarf(
                new ArrayList<>(),
                SobonOrientierungswertJahr.JAHR_2017,
                LocalDate.now()
            );
        assertThat(result, is(nullValue()));

        result =
            calculationService.calculateLangfristigerPlanungsursaechlicherBedarf(
                List.of(new BauabschnittModel()),
                null,
                null
            );
        assertThat(result, is(nullValue()));

        result =
            calculationService.calculateLangfristigerPlanungsursaechlicherBedarf(
                List.of(new BauabschnittModel()),
                SobonOrientierungswertJahr.JAHR_2017,
                null
            );
        assertThat(result, is(nullValue()));

        result =
            calculationService.calculateLangfristigerPlanungsursaechlicherBedarf(
                List.of(new BauabschnittModel()),
                null,
                LocalDate.now()
            );
        assertThat(result, is(nullValue()));
    }
}
