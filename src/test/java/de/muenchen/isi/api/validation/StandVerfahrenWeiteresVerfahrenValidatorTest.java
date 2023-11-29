package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import org.junit.jupiter.api.Test;

class StandVerfahrenWeiteresVerfahrenValidatorTest {

    private StandVerfahrenWeiteresVerfahrenValidator standVerfahrenValidator =
        new StandVerfahrenWeiteresVerfahrenValidator();

    @Test
    void isValid() {
        assertThat(this.standVerfahrenValidator.isValid(null, null), is(true));
        assertThat(this.standVerfahrenValidator.isValid(StandVerfahren.UNSPECIFIED, null), is(true));
        assertThat(
            this.standVerfahrenValidator.isValid(StandVerfahren.VORABFRAGE_OHNE_KONKRETEN_STAND, null),
            is(true)
        );
        assertThat(this.standVerfahrenValidator.isValid(StandVerfahren.STRUKTURKONZEPT, null), is(true));
        assertThat(this.standVerfahrenValidator.isValid(StandVerfahren.RAHMENPLANUNG, null), is(true));
        assertThat(this.standVerfahrenValidator.isValid(StandVerfahren.POTENTIALUNTERSUCHUNG, null), is(true));
        assertThat(
            this.standVerfahrenValidator.isValid(StandVerfahren.STAEDTEBAULICHE_SANIERUNGSMASSNAHME, null),
            is(true)
        );
        assertThat(
            this.standVerfahrenValidator.isValid(StandVerfahren.STAEDTEBAULICHE_ENTWICKLUNGSMASSNAHME, null),
            is(true)
        );
        assertThat(this.standVerfahrenValidator.isValid(StandVerfahren.INFO_FEHLT, null), is(true));
        assertThat(this.standVerfahrenValidator.isValid(StandVerfahren.FREIE_EINGABE, null), is(true));
        assertThat(this.standVerfahrenValidator.isValid(StandVerfahren.VORBEREITUNG_BAUGENEHMIGUNG, null), is(false));
    }
}
