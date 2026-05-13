package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.infrastructure.entity.enums.lookup.Verfahrensstand;
import org.junit.jupiter.api.Test;

class VerfahrensstandBauleitplanverfahrenValidatorTest {

    private final VerfahrensstandBauleitplanverfahrenValidator verfahrensstandValidator =
        new VerfahrensstandBauleitplanverfahrenValidator();

    @Test
    void isVerfahrensstandForBauleitplanverfahrenValid() {
        final var allowed = Verfahrensstand.getVerfahrensstandForBauleitplanverfahren();

        assertThat(this.verfahrensstandValidator.isValid(null, null), is(true));

        assertThat(this.verfahrensstandValidator.isValid(Verfahrensstand.FREIE_EINGABE, null), is(true));

        for (final var value : Verfahrensstand.values()) {
            if (!allowed.contains(value)) {
                assertThat(this.verfahrensstandValidator.isValid(value, null), is(false));
            }
        }
    }

    @Test
    void isValid() {
        assertThat(this.verfahrensstandValidator.isValid(null, null), is(true));
        assertThat(this.verfahrensstandValidator.isValid(Verfahrensstand.UNSPECIFIED, null), is(true));
        assertThat(
            this.verfahrensstandValidator.isValid(Verfahrensstand.SIMULIERT_VORBEREITUNG_AUFSTELLUNGSBESCHLUSS, null),
            is(true)
        );
        assertThat(
            this.verfahrensstandValidator.isValid(Verfahrensstand.SIMULIERT_VORBEREITUNG_WETTBEWERBAUSLOBUNG, null),
            is(true)
        );
        assertThat(
            this.verfahrensstandValidator.isValid(Verfahrensstand.VORBEREITUNG_FRUEHZEITIGE_BETEILIGUNG, null),
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
            this.verfahrensstandValidator.isValid(Verfahrensstand.VORBEREITUNG_SATZUNGSBESCHLUSS, null),
            is(true)
        );
        assertThat(
            this.verfahrensstandValidator.isValid(Verfahrensstand.INKRAFTGETRETEN_VEROEFFENTLICHUNG_AMTSBLATT, null),
            is(true)
        );
        assertThat(
            this.verfahrensstandValidator.isValid(Verfahrensstand.INKRAFTGETRETEN_FOERDERMIXPLAN, null),
            is(true)
        );
        assertThat(this.verfahrensstandValidator.isValid(Verfahrensstand.FREIE_EINGABE, null), is(true));
    }
}
