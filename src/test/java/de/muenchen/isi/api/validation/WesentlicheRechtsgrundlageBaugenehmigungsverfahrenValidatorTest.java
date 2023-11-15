package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.infrastructure.entity.enums.lookup.WesentlicheRechtsgrundlage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class WesentlicheRechtsgrundlageBaugenehmigungsverfahrenValidatorTest {

    private WesentlicheRechtsgrundlageBaugenehmigungsverfahrenValidator wesentlicheRechtsgrundlageValidator =
        new WesentlicheRechtsgrundlageBaugenehmigungsverfahrenValidator();

    @Test
    void isValid() {
        assertThat(this.wesentlicheRechtsgrundlageValidator.isValid(null, null), is(true));
        assertThat(
            this.wesentlicheRechtsgrundlageValidator.isValid(
                    WesentlicheRechtsgrundlage.QUALIFIZIERTER_BEBAUUNGSPLAN,
                    null
                ),
            is(true)
        );
        assertThat(
            this.wesentlicheRechtsgrundlageValidator.isValid(
                    WesentlicheRechtsgrundlage.VORHABENSBEZOGENER_BEBAUUNGSPLAN,
                    null
                ),
            is(true)
        );
        assertThat(
            this.wesentlicheRechtsgrundlageValidator.isValid(
                    WesentlicheRechtsgrundlage.EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35,
                    null
                ),
            is(true)
        );
        assertThat(
            this.wesentlicheRechtsgrundlageValidator.isValid(
                    WesentlicheRechtsgrundlage.SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35,
                    null
                ),
            is(true)
        );
        assertThat(
            this.wesentlicheRechtsgrundlageValidator.isValid(WesentlicheRechtsgrundlage.INNENBEREICH, null),
            is(true)
        );
        assertThat(
            this.wesentlicheRechtsgrundlageValidator.isValid(WesentlicheRechtsgrundlage.AUSSENBEREICH, null),
            is(true)
        );
        assertThat(
            this.wesentlicheRechtsgrundlageValidator.isValid(WesentlicheRechtsgrundlage.BEFREIUNG, null),
            is(true)
        );
        assertThat(
            this.wesentlicheRechtsgrundlageValidator.isValid(WesentlicheRechtsgrundlage.INFO_FEHLT, null),
            is(true)
        );
        assertThat(
            this.wesentlicheRechtsgrundlageValidator.isValid(WesentlicheRechtsgrundlage.FREIE_EINGABE, null),
            is(true)
        );
        assertThat(
            this.wesentlicheRechtsgrundlageValidator.isValid(
                    WesentlicheRechtsgrundlage.EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30,
                    null
                ),
            is(false)
        );
        assertThat(
            this.wesentlicheRechtsgrundlageValidator.isValid(
                    WesentlicheRechtsgrundlage.SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_9,
                    null
                ),
            is(false)
        );
    }
}
