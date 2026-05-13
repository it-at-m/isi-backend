package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.infrastructure.entity.enums.lookup.Verfahrensstand;
import org.junit.jupiter.api.Test;

class VerfahrensstandWeiteresVerfahrenValidatorTest {

    private final VerfahrensstandWeiteresVerfahrenValidator verfahrensstandValidator =
        new VerfahrensstandWeiteresVerfahrenValidator();

    @Test
    void isValid() {
        assertThat(this.verfahrensstandValidator.isValid(null, null), is(true));
        assertThat(this.verfahrensstandValidator.isValid(Verfahrensstand.UNSPECIFIED, null), is(true));
        assertThat(
            this.verfahrensstandValidator.isValid(Verfahrensstand.VORABFRAGE_OHNE_KONKRETEN_STAND, null),
            is(true)
        );
        assertThat(this.verfahrensstandValidator.isValid(Verfahrensstand.STRUKTURKONZEPT, null), is(true));
        assertThat(this.verfahrensstandValidator.isValid(Verfahrensstand.RAHMENPLANUNG, null), is(true));
        assertThat(this.verfahrensstandValidator.isValid(Verfahrensstand.POTENTIALUNTERSUCHUNG, null), is(true));
        assertThat(
            this.verfahrensstandValidator.isValid(Verfahrensstand.STAEDTEBAULICHE_SANIERUNGSMASSNAHME, null),
            is(true)
        );
        assertThat(
            this.verfahrensstandValidator.isValid(Verfahrensstand.STAEDTEBAULICHE_ENTWICKLUNGSMASSNAHME, null),
            is(true)
        );
        assertThat(this.verfahrensstandValidator.isValid(Verfahrensstand.FREIE_EINGABE, null), is(true));
        assertThat(this.verfahrensstandValidator.isValid(Verfahrensstand.VORBEREITUNG_BAUGENEHMIGUNG, null), is(false));
    }
}
