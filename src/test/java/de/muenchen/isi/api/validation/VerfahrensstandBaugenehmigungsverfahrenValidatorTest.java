package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.infrastructure.entity.enums.lookup.Verfahrensstand;
import org.junit.jupiter.api.Test;

class VerfahrensstandBaugenehmigungsverfahrenValidatorTest {

    private final VerfahrensstandBaugenehmigungsverfahrenValidator verfahrensstandValidator =
        new VerfahrensstandBaugenehmigungsverfahrenValidator();

    @Test
    void isValid() {
        assertThat(this.verfahrensstandValidator.isValid(null, null), is(true));
        assertThat(this.verfahrensstandValidator.isValid(Verfahrensstand.UNSPECIFIED, null), is(true));
        assertThat(this.verfahrensstandValidator.isValid(Verfahrensstand.VORBEREITUNG_VORBESCHEID, null), is(true));
        assertThat(this.verfahrensstandValidator.isValid(Verfahrensstand.VORBEREITUNG_BAUGENEHMIGUNG, null), is(true));
        assertThat(this.verfahrensstandValidator.isValid(Verfahrensstand.FREIE_EINGABE, null), is(true));
        assertThat(
            this.verfahrensstandValidator.isValid(Verfahrensstand.RECHTSVERBINDLICHKEIT_AMTSBLATT, null),
            is(false)
        );
    }
}
