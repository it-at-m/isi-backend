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

    private final WesentlicheRechtsgrundlageBaugenehmigungsverfahrenValidator wesentlicheRechtsgrundlageValidator =
        new WesentlicheRechtsgrundlageBaugenehmigungsverfahrenValidator();

    @Test
    void isValid() {
        assertThat(this.wesentlicheRechtsgrundlageValidator.isValid(null, null), is(true));
        assertThat(
            this.wesentlicheRechtsgrundlageValidator.isValid(
                WesentlicheRechtsgrundlage.BEPLANTER_BEREICH_PARAGRAPH_30,
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
            this.wesentlicheRechtsgrundlageValidator.isValid(
                WesentlicheRechtsgrundlage.BEBAUUNGSPLAN_ZUR_WOHNRAUMVERSORGUNG_PARAGRAPH_9_IVM_34,
                null
            ),
            is(true)
        );
        assertThat(
            this.wesentlicheRechtsgrundlageValidator.isValid(
                WesentlicheRechtsgrundlage.BEPLANTER_BEREICH_PARAGRAPH_30_MIT_BEFREIUNG_PARAGRAPH_31,
                null
            ),
            is(true)
        );
        assertThat(
            this.wesentlicheRechtsgrundlageValidator.isValid(WesentlicheRechtsgrundlage.FREIE_EINGABE, null),
            is(true)
        );
    }
}
