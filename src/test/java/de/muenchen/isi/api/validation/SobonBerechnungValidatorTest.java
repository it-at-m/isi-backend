package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.AbfragevarianteBauleitplanverfahrenDto;
import de.muenchen.isi.api.dto.FoerdermixDto;
import de.muenchen.isi.api.dto.common.SobonBerechnungDto;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class SobonBerechnungValidatorTest {

    private final SobonBerechnungValidator validator = new SobonBerechnungValidator();

    @Test
    void isValidAVSachbearbeitung() {
        AbfragevarianteBauleitplanverfahrenDto test = new AbfragevarianteBauleitplanverfahrenDto();
        SobonBerechnungDto dto = new SobonBerechnungDto();

        FoerdermixDto foerdermixDto = new FoerdermixDto();
        foerdermixDto.setBezeichnung("Bezeichnung");
        foerdermixDto.setBezeichnungJahr("BezeichnungJahr");
        foerdermixDto.setFoerderarten(new ArrayList<>());

        dto.setIsASobonBerechnung(true);
        dto.setSobonFoerdermix(foerdermixDto);
        assertThat(validator.isValid(dto, null), is(true));

        dto.setIsASobonBerechnung(true);
        dto.setSobonFoerdermix(null);
        assertThat(validator.isValid(dto, null), is(false));

        dto.setIsASobonBerechnung(false);
        dto.setSobonFoerdermix(foerdermixDto);
        assertThat(validator.isValid(dto, null), is(false));

        dto.setIsASobonBerechnung(false);
        dto.setSobonFoerdermix(null);
        assertThat(validator.isValid(dto, null), is(true));
    }
}
