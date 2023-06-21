package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.stammdaten.baurate.IdealtypischeBaurateDto;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class RangeIdealtypischeBaurateValidatorTest {

    private RangeIdealtypischeBaurateValidator rangeIdealtypischeBaurateValidator =
        new RangeIdealtypischeBaurateValidator();

    @Test
    void isValid() {
        var idealtypischeBaurate = new IdealtypischeBaurateDto();
        idealtypischeBaurate.setVon(null);
        idealtypischeBaurate.setBisExklusiv(null);
        assertThat(this.rangeIdealtypischeBaurateValidator.isValid(idealtypischeBaurate, null), is(false));

        idealtypischeBaurate = new IdealtypischeBaurateDto();
        idealtypischeBaurate.setVon(BigDecimal.TEN);
        idealtypischeBaurate.setBisExklusiv(null);
        assertThat(this.rangeIdealtypischeBaurateValidator.isValid(idealtypischeBaurate, null), is(false));

        idealtypischeBaurate = new IdealtypischeBaurateDto();
        idealtypischeBaurate.setVon(null);
        idealtypischeBaurate.setBisExklusiv(BigDecimal.TEN);
        assertThat(this.rangeIdealtypischeBaurateValidator.isValid(idealtypischeBaurate, null), is(false));

        idealtypischeBaurate = new IdealtypischeBaurateDto();
        idealtypischeBaurate.setVon(BigDecimal.TEN);
        idealtypischeBaurate.setBisExklusiv(BigDecimal.TEN);
        assertThat(this.rangeIdealtypischeBaurateValidator.isValid(idealtypischeBaurate, null), is(false));

        idealtypischeBaurate = new IdealtypischeBaurateDto();
        idealtypischeBaurate.setVon(BigDecimal.TEN);
        idealtypischeBaurate.setBisExklusiv(BigDecimal.ONE);
        assertThat(this.rangeIdealtypischeBaurateValidator.isValid(idealtypischeBaurate, null), is(false));

        idealtypischeBaurate = new IdealtypischeBaurateDto();
        idealtypischeBaurate.setVon(BigDecimal.ONE);
        idealtypischeBaurate.setBisExklusiv(BigDecimal.TEN);
        assertThat(this.rangeIdealtypischeBaurateValidator.isValid(idealtypischeBaurate, null), is(true));
    }
}
