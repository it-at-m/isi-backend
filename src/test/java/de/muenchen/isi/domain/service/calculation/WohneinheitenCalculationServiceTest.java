package de.muenchen.isi.domain.service.calculation;

import static de.muenchen.isi.TestConstants.EOF;
import static de.muenchen.isi.TestConstants.FF;
import static de.muenchen.isi.TestConstants.FH;
import static de.muenchen.isi.TestConstants.MM;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.model.calculation.WohneinheitenProFoerderartProJahrModel;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class WohneinheitenCalculationServiceTest {

    private PlanungsursaechlicheWohneinheitenService planungsursaechlicheWohneinheitenService =
        new PlanungsursaechlicheWohneinheitenService(null, null);

    @Test
    void sumWohneinheitenForNumberOfYearsForEachFoerderartEmptyWohneinheiten() {
        final var actual = planungsursaechlicheWohneinheitenService.sumWohneinheitenForNumberOfYearsForEachFoerderart(
            List.of(),
            10
        );

        assertThat(actual, is(List.of()));
    }

    @Test
    void sumWohneinheitenForNumberOfYearsForEachFoerderart() {
        final var jahr1 = 2024;
        final var jahr2 = 2034;
        final var jahr1String = String.valueOf(jahr1);
        final var jahr2String = String.valueOf(jahr2);
        final var summe10Jahre = String.format(CalculationService.SUMMATION_PERIOD_NAME, 10);
        final var summe15Jahre = String.format(CalculationService.SUMMATION_PERIOD_NAME, 15);
        final var summe20Jahre = String.format(CalculationService.SUMMATION_PERIOD_NAME, 20);
        final var wohneinheitenProJahr = List.of(
            new WohneinheitenProFoerderartProJahrModel(FF, jahr1String, new BigDecimal("100")),
            new WohneinheitenProFoerderartProJahrModel(FF, jahr2String, new BigDecimal("25.0000")),
            new WohneinheitenProFoerderartProJahrModel(EOF, jahr1String, new BigDecimal("55.5555555556")),
            new WohneinheitenProFoerderartProJahrModel(EOF, jahr2String, new BigDecimal("75.0000")),
            new WohneinheitenProFoerderartProJahrModel(MM, jahr1String, new BigDecimal("150.0000000000")),
            new WohneinheitenProFoerderartProJahrModel(MM, jahr2String, new BigDecimal("75.0000000000")),
            new WohneinheitenProFoerderartProJahrModel(FH, jahr2String, new BigDecimal("140.6250000000"))
        );

        var actual = planungsursaechlicheWohneinheitenService.sumWohneinheitenForNumberOfYearsForEachFoerderart(
            wohneinheitenProJahr,
            10
        );

        var expected = List.of(
            new WohneinheitenProFoerderartProJahrModel(MM, summe10Jahre, new BigDecimal("150.0000000000")),
            new WohneinheitenProFoerderartProJahrModel(FH, summe10Jahre, new BigDecimal("140.6250000000")),
            new WohneinheitenProFoerderartProJahrModel(FF, summe10Jahre, new BigDecimal("100")),
            new WohneinheitenProFoerderartProJahrModel(EOF, summe10Jahre, new BigDecimal("55.5555555556"))
        );
        assertThat(actual, is(expected));

        actual =
            planungsursaechlicheWohneinheitenService.sumWohneinheitenForNumberOfYearsForEachFoerderart(
                wohneinheitenProJahr,
                15
            );

        expected =
            List.of(
                new WohneinheitenProFoerderartProJahrModel(MM, summe15Jahre, new BigDecimal("225.0000000000")),
                new WohneinheitenProFoerderartProJahrModel(FH, summe15Jahre, new BigDecimal("140.6250000000")),
                new WohneinheitenProFoerderartProJahrModel(FF, summe15Jahre, new BigDecimal("125.0000")),
                new WohneinheitenProFoerderartProJahrModel(EOF, summe15Jahre, new BigDecimal("130.5555555556"))
            );
        assertThat(actual, is(expected));

        actual =
            planungsursaechlicheWohneinheitenService.sumWohneinheitenForNumberOfYearsForEachFoerderart(
                wohneinheitenProJahr,
                20
            );

        expected =
            List.of(
                new WohneinheitenProFoerderartProJahrModel(MM, summe20Jahre, new BigDecimal("225.0000000000")),
                new WohneinheitenProFoerderartProJahrModel(FH, summe20Jahre, new BigDecimal("140.6250000000")),
                new WohneinheitenProFoerderartProJahrModel(FF, summe20Jahre, new BigDecimal("125.0000")),
                new WohneinheitenProFoerderartProJahrModel(EOF, summe20Jahre, new BigDecimal("130.5555555556"))
            );
        assertThat(actual, is(expected));
    }

    @Test
    void sumWohneinheitenOverFoerderartenForEachYearEmptyWohneinheiten() {
        final var actual = planungsursaechlicheWohneinheitenService.sumWohneinheitenOverFoerderartenForEachYear(
            List.of()
        );
        assertThat(actual, is(List.of()));
    }

    @Test
    void sumWohneinheitenOverFoerderartenForEachYear() {
        final var jahr1 = 2024;
        final var jahr2 = 2034;
        final var jahr1String = String.valueOf(jahr1);
        final var jahr2String = String.valueOf(jahr2);
        final var summe10Jahre = String.format(CalculationService.SUMMATION_PERIOD_NAME, 10);
        final var summe15Jahre = String.format(CalculationService.SUMMATION_PERIOD_NAME, 15);
        final var summe20Jahre = String.format(CalculationService.SUMMATION_PERIOD_NAME, 20);
        final var wohneinheitenProJahr = List.of(
            new WohneinheitenProFoerderartProJahrModel(FF, jahr1String, new BigDecimal("100")),
            new WohneinheitenProFoerderartProJahrModel(FF, jahr2String, new BigDecimal("25.0000")),
            new WohneinheitenProFoerderartProJahrModel(EOF, jahr1String, new BigDecimal("55.5555555556")),
            new WohneinheitenProFoerderartProJahrModel(EOF, jahr2String, new BigDecimal("75.0000")),
            new WohneinheitenProFoerderartProJahrModel(MM, jahr1String, new BigDecimal("150.0000000000")),
            new WohneinheitenProFoerderartProJahrModel(MM, jahr2String, new BigDecimal("75.0000000000")),
            new WohneinheitenProFoerderartProJahrModel(FH, jahr2String, new BigDecimal("140.6250000000")),
            new WohneinheitenProFoerderartProJahrModel(MM, summe10Jahre, new BigDecimal("150.0000000000")),
            new WohneinheitenProFoerderartProJahrModel(FH, summe10Jahre, new BigDecimal("140.6250000000")),
            new WohneinheitenProFoerderartProJahrModel(FF, summe10Jahre, new BigDecimal("100")),
            new WohneinheitenProFoerderartProJahrModel(EOF, summe10Jahre, new BigDecimal("55.5555555556")),
            new WohneinheitenProFoerderartProJahrModel(MM, summe15Jahre, new BigDecimal("225.0000000000")),
            new WohneinheitenProFoerderartProJahrModel(FH, summe15Jahre, new BigDecimal("140.6250000000")),
            new WohneinheitenProFoerderartProJahrModel(FF, summe15Jahre, new BigDecimal("125.0000")),
            new WohneinheitenProFoerderartProJahrModel(EOF, summe15Jahre, new BigDecimal("130.5555555556")),
            new WohneinheitenProFoerderartProJahrModel(MM, summe20Jahre, new BigDecimal("225.0000000000")),
            new WohneinheitenProFoerderartProJahrModel(FH, summe20Jahre, new BigDecimal("140.6250000000")),
            new WohneinheitenProFoerderartProJahrModel(FF, summe20Jahre, new BigDecimal("125.0000")),
            new WohneinheitenProFoerderartProJahrModel(EOF, summe20Jahre, new BigDecimal("130.5555555556"))
        );

        var actual = planungsursaechlicheWohneinheitenService.sumWohneinheitenOverFoerderartenForEachYear(
            wohneinheitenProJahr
        );

        var expected = List.of(
            new WohneinheitenProFoerderartProJahrModel(
                CalculationService.SUMMATION_TOTAL_NAME,
                summe10Jahre,
                new BigDecimal("446.1805555556")
            ),
            new WohneinheitenProFoerderartProJahrModel(
                CalculationService.SUMMATION_TOTAL_NAME,
                summe15Jahre,
                new BigDecimal("621.1805555556")
            ),
            new WohneinheitenProFoerderartProJahrModel(
                CalculationService.SUMMATION_TOTAL_NAME,
                jahr1String,
                new BigDecimal("305.5555555556")
            ),
            new WohneinheitenProFoerderartProJahrModel(
                CalculationService.SUMMATION_TOTAL_NAME,
                jahr2String,
                new BigDecimal("315.6250000000")
            ),
            new WohneinheitenProFoerderartProJahrModel(
                CalculationService.SUMMATION_TOTAL_NAME,
                summe20Jahre,
                new BigDecimal("621.1805555556")
            )
        );
        assertThat(actual, is(expected));
    }
}
