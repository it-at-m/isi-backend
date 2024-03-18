package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.FoerdermixDto;
import org.junit.jupiter.api.Test;

public class ConditionalNotEmptyFoerdermixValidatorTest {

    public final FoerdermixNotEmptyValidator validator = new FoerdermixNotEmptyValidator();

    @Test
    public void isValidFoerdermixDto() {
        FoerdermixDto foerdermixDto = new FoerdermixDto();
        foerdermixDto.setBezeichnung(null);
        foerdermixDto.setBezeichnungJahr(null);

        assertThat(this.validator.isValid(foerdermixDto, null), is(false));

        foerdermixDto.setBezeichnung("");
        foerdermixDto.setBezeichnungJahr("");

        assertThat(this.validator.isValid(foerdermixDto, null), is(false));

        foerdermixDto.setBezeichnung("Bezeichnung");
        foerdermixDto.setBezeichnungJahr("");

        assertThat(this.validator.isValid(foerdermixDto, null), is(false));

        foerdermixDto.setBezeichnung("");
        foerdermixDto.setBezeichnungJahr("BezeichnungJahr");

        assertThat(this.validator.isValid(foerdermixDto, null), is(false));

        foerdermixDto.setBezeichnung("Bezeichnung");
        foerdermixDto.setBezeichnungJahr("BezeichnungJahr");

        assertThat(this.validator.isValid(foerdermixDto, null), is(true));
    }
}
