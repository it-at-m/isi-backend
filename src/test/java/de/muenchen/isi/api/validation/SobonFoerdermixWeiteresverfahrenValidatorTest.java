package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.FoerdermixDto;
import de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung.AbfragevarianteWeiteresVerfahrenInBearbeitungSachbearbeitungDto;
import de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung.AbfragevarianteWeiteresVerfahrenSachbearbeitungInBearbeitungSachbearbeitungDto;
import org.junit.jupiter.api.Test;

public class SobonFoerdermixWeiteresverfahrenValidatorTest {

    private final SobonFoerdermixWeiteresverfahrenAVValidator validatorAV =
        new SobonFoerdermixWeiteresverfahrenAVValidator();

    private final SobonFoerdermixWeiteresVerfahrenAVSachbearbeitungValidator validatorAVSachbearbeitung =
        new SobonFoerdermixWeiteresVerfahrenAVSachbearbeitungValidator();

    @Test
    void isValidAV() {
        AbfragevarianteWeiteresVerfahrenSachbearbeitungInBearbeitungSachbearbeitungDto dto =
            new AbfragevarianteWeiteresVerfahrenSachbearbeitungInBearbeitungSachbearbeitungDto();

        dto.setIsASobonBerechnung(true);
        dto.setSobonFoerdermix(new FoerdermixDto());
        assertThat(validatorAVSachbearbeitung.isValid(dto, null), is(true));

        dto.setIsASobonBerechnung(true);
        dto.setSobonFoerdermix(null);
        assertThat(validatorAVSachbearbeitung.isValid(dto, null), is(false));

        dto.setIsASobonBerechnung(false);
        dto.setSobonFoerdermix(new FoerdermixDto());
        assertThat(validatorAVSachbearbeitung.isValid(dto, null), is(false));

        dto.setIsASobonBerechnung(false);
        dto.setSobonFoerdermix(null);
        assertThat(validatorAVSachbearbeitung.isValid(dto, null), is(true));
    }

    @Test
    void isValidAVSachbearbeitung() {
        AbfragevarianteWeiteresVerfahrenInBearbeitungSachbearbeitungDto dto =
            new AbfragevarianteWeiteresVerfahrenInBearbeitungSachbearbeitungDto();

        dto.setIsASobonBerechnung(true);
        dto.setSobonFoerdermix(new FoerdermixDto());
        assertThat(validatorAV.isValid(dto, null), is(true));

        dto.setIsASobonBerechnung(true);
        dto.setSobonFoerdermix(null);
        assertThat(validatorAV.isValid(dto, null), is(false));

        dto.setIsASobonBerechnung(false);
        dto.setSobonFoerdermix(new FoerdermixDto());
        assertThat(validatorAV.isValid(dto, null), is(false));

        dto.setIsASobonBerechnung(false);
        dto.setSobonFoerdermix(null);
        assertThat(validatorAV.isValid(dto, null), is(true));
    }
}
