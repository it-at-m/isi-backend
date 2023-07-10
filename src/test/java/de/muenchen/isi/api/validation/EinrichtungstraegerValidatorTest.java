package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.infrastruktureinrichtung.InfrastruktureinrichtungDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.KinderkrippeDto;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Einrichtungstraeger;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import org.junit.jupiter.api.Test;

public class EinrichtungstraegerValidatorTest {

    private final EinrichtungstraegerValidator validator = new EinrichtungstraegerValidator();

    @Test
    void isValid() {
        final InfrastruktureinrichtungDto value = new KinderkrippeDto();

        assertThat(this.validator.isValid(null, null), is(false));
        assertThat(this.validator.isValid(value, null), is(true));

        value.setStatus(StatusInfrastruktureinrichtung.BESTAND);
        assertThat(this.validator.isValid(value, null), is(false));

        value.setStatus(StatusInfrastruktureinrichtung.GESICHERTE_PLANUNG_ERW_PLAETZE_BEST_EINR);
        assertThat(this.validator.isValid(value, null), is(false));

        value.setEinrichtungstraeger(Einrichtungstraeger.KITA_STAEDTISCHE_EINRICHTUNG);
        assertThat(this.validator.isValid(value, null), is(true));
    }
}
