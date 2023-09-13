package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.BauabschnittDto;
import de.muenchen.isi.api.dto.BaugebietDto;
import de.muenchen.isi.api.dto.BaurateDto;
import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfragevarianteAngelegtDto;
import java.util.List;
import org.junit.jupiter.api.Test;

public class TechnicalAttributesValidatorTest {

    private final TechnicalAttributesValidator technicalAttributesValidator = new TechnicalAttributesValidator();

    @Test
    void isValidTechnicalBauabschnittAndTechnicalBaugebiet() {
        final var baurateDto = new BaurateDto();
        final var baugebietDto = new BaugebietDto();
        baugebietDto.setTechnical(true);
        baugebietDto.setBauraten(List.of(baurateDto));
        final var bauabschnittDto = new BauabschnittDto();
        bauabschnittDto.setTechnical(true);
        bauabschnittDto.setBaugebiete(List.of(baugebietDto));
        final var abfragevarianteDto = new AbfragevarianteAngelegtDto();
        abfragevarianteDto.setBauabschnitte(List.of(bauabschnittDto));

        assertThat(this.technicalAttributesValidator.isValid(abfragevarianteDto, null), is(true));
    }

    @Test
    void isValidTechnicalBauabschnittAndNotTechnicalBaugebiet() {
        final var baurateDto = new BaurateDto();
        final var baugebietDto = new BaugebietDto();
        baugebietDto.setTechnical(false);
        baugebietDto.setBauraten(List.of(baurateDto));
        final var bauabschnittDto = new BauabschnittDto();
        bauabschnittDto.setTechnical(true);
        bauabschnittDto.setBaugebiete(List.of(baugebietDto));
        final var abfragevarianteDto = new AbfragevarianteAngelegtDto();
        abfragevarianteDto.setBauabschnitte(List.of(bauabschnittDto));

        assertThat(this.technicalAttributesValidator.isValid(abfragevarianteDto, null), is(true));
    }

    @Test
    void isValidNotTechnicalBauabschnittAndNotTechnicalBaugebiet() {
        final var baurateDto = new BaurateDto();
        final var baugebietDto = new BaugebietDto();
        baugebietDto.setTechnical(false);
        baugebietDto.setBauraten(List.of(baurateDto));
        final var bauabschnittDto = new BauabschnittDto();
        bauabschnittDto.setTechnical(false);
        bauabschnittDto.setBaugebiete(List.of(baugebietDto));
        final var abfragevarianteDto = new AbfragevarianteAngelegtDto();
        abfragevarianteDto.setBauabschnitte(List.of(bauabschnittDto));

        assertThat(this.technicalAttributesValidator.isValid(abfragevarianteDto, null), is(true));
    }

    @Test
    void isNotValidNotTechnicalBauabschnittAndTechnicalBaugebiet() {
        final var baurateDto = new BaurateDto();
        final var baugebietDto = new BaugebietDto();
        baugebietDto.setTechnical(true);
        baugebietDto.setBauraten(List.of(baurateDto));
        final var bauabschnittDto = new BauabschnittDto();
        bauabschnittDto.setTechnical(false);
        bauabschnittDto.setBaugebiete(List.of(baugebietDto));
        final var abfragevarianteDto = new AbfragevarianteAngelegtDto();
        abfragevarianteDto.setBauabschnitte(List.of(bauabschnittDto));

        assertThat(this.technicalAttributesValidator.isValid(abfragevarianteDto, null), is(false));
    }
}
