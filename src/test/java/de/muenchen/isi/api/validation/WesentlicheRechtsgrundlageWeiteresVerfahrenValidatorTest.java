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
class WesentlicheRechtsgrundlageWeiteresVerfahrenValidatorTest {

    private final WesentlicheRechtsgrundlageWeiteresVerfahrenValidator wesentlicheRechtsgrundlageValidator =
        new WesentlicheRechtsgrundlageWeiteresVerfahrenValidator();

    @Test
    void isValid() {
        assertThat(this.wesentlicheRechtsgrundlageValidator.isValid(null, null), is(true));
        assertThat(
            this.wesentlicheRechtsgrundlageValidator.isValid(WesentlicheRechtsgrundlage.EINFACHER_BEBAUUNGSPLAN, null),
            is(true)
        );
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
                WesentlicheRechtsgrundlage.BEBAUUNGSPLAN_ZUR_WOHNRAUMVERSORGUNG,
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
            this.wesentlicheRechtsgrundlageValidator.isValid(WesentlicheRechtsgrundlage.FREIE_EINGABE, null),
            is(true)
        );
    }
}
