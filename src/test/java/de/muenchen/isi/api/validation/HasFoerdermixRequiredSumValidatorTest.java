package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.FoerderartDto;
import de.muenchen.isi.api.dto.FoerdermixDto;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
class HasFoerdermixRequiredSumValidatorTest {

    private final HasFoerdermixRequiredSumValidator hasFoerdermixRequiredSumValidator =
        new HasFoerdermixRequiredSumValidator();

    @Test
    void isValid() {
        var foerdermix = new FoerdermixDto();
        FoerderartDto foerderart = new FoerderartDto();
        foerderart.setBezeichnung("AnteilMuenchenModell");
        foerderart.setAnteilProzent(BigDecimal.valueOf(40));

        FoerderartDto foerderart2 = new FoerderartDto();
        foerderart2.setBezeichnung("AnteilBaugemeinschaft");
        foerderart2.setAnteilProzent(BigDecimal.valueOf(60));

        List<FoerderartDto> foerderarten = new ArrayList<>(Arrays.asList(foerderart, foerderart2));
        foerdermix.setFoerderarten(foerderarten);

        assertThat(
                this.hasFoerdermixRequiredSumValidator.isValid(foerdermix, null),
                is(true)
        );


        foerderart.setAnteilProzent(BigDecimal.valueOf(99.99));
        foerderart2.setAnteilProzent(BigDecimal.valueOf(0.01));

        assertThat(
                this.hasFoerdermixRequiredSumValidator.isValid(foerdermix, null),
                is(true)
        );


        FoerderartDto foerderart3 = new FoerderartDto();
        foerderart3.setBezeichnung("AnteilEinUndZweifamilienhaeuser");
        foerderart3.setAnteilProzent(BigDecimal.valueOf(0));

        foerderarten = new ArrayList<>(Arrays.asList(foerderart, foerderart2,foerderart3));
        foerdermix.setFoerderarten(foerderarten);

        assertThat(
                this.hasFoerdermixRequiredSumValidator.isValid(foerdermix, null),
                is(true)
        );


        foerderart2.setAnteilProzent(BigDecimal.valueOf(0.02));

        assertThat(
                this.hasFoerdermixRequiredSumValidator.isValid(foerdermix, null),
                is(false)
        );


        foerderart.setAnteilProzent(null);
        foerderart2.setAnteilProzent(BigDecimal.ZERO);
        foerderart3.setAnteilProzent(BigDecimal.valueOf(99.99));

        assertThat(
                this.hasFoerdermixRequiredSumValidator.isValid(foerdermix, null),
                is(false)
        );

        assertThat(this.hasFoerdermixRequiredSumValidator.isValid(null, null), is(true));
    }
}
