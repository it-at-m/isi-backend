package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.FoerdermixDto;
import de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung.AbfragevarianteWeiteresVerfahrenSachbearbeitungInBearbeitungSachbearbeitungDto;
import org.junit.jupiter.api.Test;

public class SobonFoerdermixWeiteresverfahrenValidatorTest {

    private final SobonFoerdermixWeiteresverfahrenAVValidator validator =
        new SobonFoerdermixWeiteresverfahrenAVValidator();

    @Test
    void isValid() {
        AbfragevarianteWeiteresVerfahrenSachbearbeitungInBearbeitungSachbearbeitungDto dto =
            new AbfragevarianteWeiteresVerfahrenSachbearbeitungInBearbeitungSachbearbeitungDto();

        dto.setIsASobonBerechnung(true);
        dto.setSobonFoerdermix(new FoerdermixDto());
        assertThat(validator.isValid(dto, null), is(true));

        dto.setIsASobonBerechnung(true);
        dto.setSobonFoerdermix(null);
        assertThat(validator.isValid(dto, null), is(false));

        dto.setIsASobonBerechnung(false);
        dto.setSobonFoerdermix(new FoerdermixDto());
        assertThat(validator.isValid(dto, null), is(false));

        dto.setIsASobonBerechnung(false);
        dto.setSobonFoerdermix(null);
        assertThat(validator.isValid(dto, null), is(true));
    }
}
