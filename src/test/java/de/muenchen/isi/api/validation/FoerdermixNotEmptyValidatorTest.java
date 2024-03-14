package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.FoerdermixDto;
import org.junit.jupiter.api.Test;

public class FoerdermixNotEmptyValidatorTest {

    private final FoerdermixNotEmptyValidator validator = new FoerdermixNotEmptyValidator();

    @Test
    void isValidFoerdermix() {
        FoerdermixDto foerdermixDto = new FoerdermixDto();

        foerdermixDto.setBezeichnungJahr("BezeichnungJahr");
        foerdermixDto.setBezeichnung("Bezeichnung");
        foerdermixDto.setFoerderarten(null);

        assertThat(validator.isValid(foerdermixDto, null), is(true));

        foerdermixDto.setBezeichnungJahr(null);

        assertThat(validator.isValid(foerdermixDto, null), is(false));

        foerdermixDto.setBezeichnungJahr("BezeichnungJahr");
        foerdermixDto.setBezeichnung(null);

        assertThat(validator.isValid(foerdermixDto, null), is(false));

        foerdermixDto.setBezeichnungJahr(null);
        foerdermixDto.setBezeichnung(null);
        assertThat(validator.isValid(foerdermixDto, null), is(false));
    }
}
