package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfrageerstellungAbfragevarianteAngelegtDto;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class RealisierungVonOrSatzungsbeschlussValidatorTest {

    private final RealisierungVonOrSatzungsbeschlussValidator validator =
        new RealisierungVonOrSatzungsbeschlussValidator();

    @Test
    void isValid() {
        final AbfrageerstellungAbfragevarianteAngelegtDto value = new AbfrageerstellungAbfragevarianteAngelegtDto();

        assertThat(this.validator.isValid(null, null), is(false));
        assertThat(this.validator.isValid(value, null), is(false));

        value.setRealisierungVon(2023);
        assertThat(this.validator.isValid(value, null), is(true));

        value.setRealisierungVon(null);
        value.setSatzungsbeschluss(LocalDate.EPOCH);
        assertThat(this.validator.isValid(value, null), is(true));

        value.setRealisierungVon(2023);
        assertThat(this.validator.isValid(value, null), is(true));
    }
}
