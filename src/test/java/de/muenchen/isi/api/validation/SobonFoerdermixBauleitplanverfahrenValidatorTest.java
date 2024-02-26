package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.FoerdermixDto;
import de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung.AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungDto;
import de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung.AbfragevarianteBauleitplanverfahrenSachbearbeitungInBearbeitungSachbearbeitungDto;
import org.junit.jupiter.api.Test;

public class SobonFoerdermixBauleitplanverfahrenValidatorTest {

    private final SobonFoerdermixBauleitplanverfahrenAVValidator validatorAV =
        new SobonFoerdermixBauleitplanverfahrenAVValidator();

    private final SobonFoerdermixBauleitplanverfahrenAVSachbearbeitungValidator validatorAVSachbearbeitung =
        new SobonFoerdermixBauleitplanverfahrenAVSachbearbeitungValidator();

    @Test
    void isValidAV() {
        AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungDto dto =
            new AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungDto();

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

    @Test
    void isValidAVSachbearbeitung() {
        AbfragevarianteBauleitplanverfahrenSachbearbeitungInBearbeitungSachbearbeitungDto dto =
            new AbfragevarianteBauleitplanverfahrenSachbearbeitungInBearbeitungSachbearbeitungDto();

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
}
