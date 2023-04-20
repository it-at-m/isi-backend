package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.infrastruktureinrichtung.InfrastruktureinrichtungDto;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import org.junit.jupiter.api.Test;

public class FertigstellungsjahrValidatorTest {

    private final FertigstellungsjahrValidator validator = new FertigstellungsjahrValidator();

    @Test
    void isValid() {
        final InfrastruktureinrichtungDto value = new InfrastruktureinrichtungDto();

        assertThat(this.validator.isValid(null, null), is(false));
        assertThat(this.validator.isValid(value, null), is(false));

        value.setFertigstellungsjahr(2023);
        assertThat(this.validator.isValid(value, null), is(true));

        value.setFertigstellungsjahr(null);
        value.setStatus(StatusInfrastruktureinrichtung.BESTAND);
        assertThat(this.validator.isValid(value, null), is(true));
    }
}
