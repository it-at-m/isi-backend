package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.infrastructure.entity.enums.lookup.Verfahrensstand;
import org.junit.jupiter.api.Test;

class VerfahrensstandBauleitplanverfahrenValidatorTest {

    private final VerfahrensstandBauleitplanverfahrenValidator verfahrensstandValidator =
        new VerfahrensstandBauleitplanverfahrenValidator();

    @Test
    void isValid() {
        assertThat(this.verfahrensstandValidator.isValid(null, null), is(true));
        assertThat(this.verfahrensstandValidator.isValid(Verfahrensstand.UNSPECIFIED, null), is(true));
        assertThat(
            this.verfahrensstandValidator.isValid(Verfahrensstand.VORBEREITUNG_ECKDATENBESCHLUSS, null),
            is(true)
        );
        assertThat(
            this.verfahrensstandValidator.isValid(Verfahrensstand.VORBEREITUNG_WETTBEWERBAUSLOBUNG, null),
            is(true)
        );
        assertThat(
            this.verfahrensstandValidator.isValid(Verfahrensstand.VORBEREITUNG_AUFSTELLUNGSBESCHLUSS, null),
            is(true)
        );
        assertThat(
            this.verfahrensstandValidator.isValid(
                Verfahrensstand.VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG,
                null
            ),
            is(true)
        );
        assertThat(
            this.verfahrensstandValidator.isValid(Verfahrensstand.VORLIEGENDER_SATZUNGSBESCHLUSS, null),
            is(true)
        );
        assertThat(
            this.verfahrensstandValidator.isValid(Verfahrensstand.RECHTSVERBINDLICHKEIT_AMTSBLATT, null),
            is(true)
        );
        assertThat(this.verfahrensstandValidator.isValid(Verfahrensstand.AUFTEILUNGSPLAN, null), is(true));
        assertThat(this.verfahrensstandValidator.isValid(Verfahrensstand.FREIE_EINGABE, null), is(true));
        assertThat(this.verfahrensstandValidator.isValid(Verfahrensstand.VORBEREITUNG_BAUGENEHMIGUNG, null), is(false));
    }
}
