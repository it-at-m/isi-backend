package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.AbfragevarianteDto;
import de.muenchen.isi.api.dto.BauabschnittDto;
import de.muenchen.isi.api.dto.BaugebietDto;
import de.muenchen.isi.api.dto.BaurateDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TechnicalAttributesValidatorTest {

    private final TechnicalAttributesValidator technicalAttributesValidator =
            new TechnicalAttributesValidator();

    @Test
    void isValidTechnicalBauabschnittAndTechnicalBaugebiet() {
        BaurateDto baurateDto = new BaurateDto();
        BaugebietDto baugebietDto = new BaugebietDto();
        baugebietDto.setTechnical(true);
        baugebietDto.setBauraten(List.of(baurateDto));
        BauabschnittDto bauabschnittDto = new BauabschnittDto();
        bauabschnittDto.setTechnical(true);
        bauabschnittDto.setBaugebiete(List.of(baugebietDto));
        AbfragevarianteDto abfragevarianteDto = new AbfragevarianteDto();
        abfragevarianteDto.setBauabschnitte(List.of(bauabschnittDto));

        assertThat(this.technicalAttributesValidator.isValid(abfragevarianteDto, null), is(true));
    }

    @Test
    void isValidTechnicalBauabschnittAndNotTechnicalBaugebiet() {
        BaurateDto baurateDto = new BaurateDto();
        BaugebietDto baugebietDto = new BaugebietDto();
        baugebietDto.setTechnical(false);
        baugebietDto.setBauraten(List.of(baurateDto));
        BauabschnittDto bauabschnittDto = new BauabschnittDto();
        bauabschnittDto.setTechnical(true);
        bauabschnittDto.setBaugebiete(List.of(baugebietDto));
        AbfragevarianteDto abfragevarianteDto = new AbfragevarianteDto();
        abfragevarianteDto.setBauabschnitte(List.of(bauabschnittDto));

        assertThat(this.technicalAttributesValidator.isValid(abfragevarianteDto, null), is(true));
    }

    @Test
    void isValidNotTechnicalBauabschnittAndNotTechnicalBaugebiet() {
        BaurateDto baurateDto = new BaurateDto();
        BaugebietDto baugebietDto = new BaugebietDto();
        baugebietDto.setTechnical(false);
        baugebietDto.setBauraten(List.of(baurateDto));
        BauabschnittDto bauabschnittDto = new BauabschnittDto();
        bauabschnittDto.setTechnical(false);
        bauabschnittDto.setBaugebiete(List.of(baugebietDto));
        AbfragevarianteDto abfragevarianteDto = new AbfragevarianteDto();
        abfragevarianteDto.setBauabschnitte(List.of(bauabschnittDto));

        assertThat(this.technicalAttributesValidator.isValid(abfragevarianteDto, null), is(true));
    }

    @Test
    void isNotValidNotTechnicalBauabschnittAndTechnicalBaugebiet() {
        BaurateDto baurateDto = new BaurateDto();
        BaugebietDto baugebietDto = new BaugebietDto();
        baugebietDto.setTechnical(true);
        baugebietDto.setBauraten(List.of(baurateDto));
        BauabschnittDto bauabschnittDto = new BauabschnittDto();
        bauabschnittDto.setTechnical(false);
        bauabschnittDto.setBaugebiete(List.of(baugebietDto));
        AbfragevarianteDto abfragevarianteDto = new AbfragevarianteDto();
        abfragevarianteDto.setBauabschnitte(List.of(bauabschnittDto));

        assertThat(this.technicalAttributesValidator.isValid(abfragevarianteDto, null), is(false));
    }
}
