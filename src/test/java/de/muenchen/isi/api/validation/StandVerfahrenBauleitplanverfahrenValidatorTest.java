package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import org.junit.jupiter.api.Test;

class StandVerfahrenBauleitplanverfahrenValidatorTest {

    private StandVerfahrenBauleitplanverfahrenValidator standVerfahrenValidator =
        new StandVerfahrenBauleitplanverfahrenValidator();

    @Test
    void isValid() {
        assertThat(this.standVerfahrenValidator.isValid(null, null), is(true));
        assertThat(this.standVerfahrenValidator.isValid(StandVerfahren.UNSPECIFIED, null), is(true));
        assertThat(this.standVerfahrenValidator.isValid(StandVerfahren.VORBEREITUNG_ECKDATENBESCHLUSS, null), is(true));
        assertThat(
            this.standVerfahrenValidator.isValid(StandVerfahren.VORBEREITUNG_WETTBEWERBAUSLOBUNG, null),
            is(true)
        );
        assertThat(
            this.standVerfahrenValidator.isValid(StandVerfahren.VORBEREITUNG_AUFSTELLUNGSBESCHLUSS, null),
            is(true)
        );
        assertThat(
            this.standVerfahrenValidator.isValid(
                    StandVerfahren.VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG,
                    null
                ),
            is(true)
        );
        assertThat(this.standVerfahrenValidator.isValid(StandVerfahren.VORLIEGENDER_SATZUNGSBESCHLUSS, null), is(true));
        assertThat(
            this.standVerfahrenValidator.isValid(StandVerfahren.RECHTSVERBINDLICHKEIT_AMTSBLATT, null),
            is(true)
        );
        assertThat(this.standVerfahrenValidator.isValid(StandVerfahren.AUFTEILUNGSPLAN, null), is(true));
        assertThat(this.standVerfahrenValidator.isValid(StandVerfahren.INFO_FEHLT, null), is(true));
        assertThat(this.standVerfahrenValidator.isValid(StandVerfahren.FREIE_EINGABE, null), is(true));
        assertThat(this.standVerfahrenValidator.isValid(StandVerfahren.VORBEREITUNG_BAUGENEHMIGUNG, null), is(false));
    }
}
