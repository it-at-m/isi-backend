package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.BauabschnittDto;
import de.muenchen.isi.api.dto.BaugebietDto;
import de.muenchen.isi.api.dto.BaurateDto;
import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfragevarianteAngelegtDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TechnicalAttributesValidatorTest {

    private final TechnicalAttributesValidator technicalAttributesValidator = new TechnicalAttributesValidator();

    @Test
    void isValidTechnicalBauabschnittAndTechnicalBaugebietWithBaurate() {
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
    void isValidTechnicalBauabschnittAndNotTechnicalBaugebietWithBaurate() {
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
    void isValidNotTechnicalBauabschnittAndNotTechnicalBaugebietWithBaurate() {
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
    void isNotValidNotTechnicalBauabschnittAndTechnicalBaugebietWithBaurate() {
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

    @Test
    void isNotValidOnlyTechnicalBauabschnitte() {
        final var bauabschnittDto = new BauabschnittDto();
        bauabschnittDto.setTechnical(true);
        final var abfragevarianteDto = new AbfragevarianteAngelegtDto();
        abfragevarianteDto.setBauabschnitte(List.of(bauabschnittDto));

        assertThat(this.technicalAttributesValidator.isValid(abfragevarianteDto, null), is(false));
    }

    @Test
    void isNotValidOnlyNonTechnicalBauabschnitte() {
        final var bauabschnittDto = new BauabschnittDto();
        bauabschnittDto.setTechnical(false);
        final var abfragevarianteDto = new AbfragevarianteAngelegtDto();
        abfragevarianteDto.setBauabschnitte(List.of(bauabschnittDto));

        assertThat(this.technicalAttributesValidator.isValid(abfragevarianteDto, null), is(false));
    }

    @Test
    void isNotValidOnlyNonTechnicalBauabschnitteAndNonTechnicalBaugebieteWithoutBaurate() {
        final var baugebietDto = new BaugebietDto();
        baugebietDto.setTechnical(false);
        final var bauabschnittDto = new BauabschnittDto();
        bauabschnittDto.setTechnical(false);
        bauabschnittDto.setBaugebiete(List.of(baugebietDto));
        final var abfragevarianteDto = new AbfragevarianteAngelegtDto();
        abfragevarianteDto.setBauabschnitte(List.of(bauabschnittDto));

        assertThat(this.technicalAttributesValidator.isValid(abfragevarianteDto, null), is(false));
    }

    @Test
    void isNotValidOnlyTechnicalBauabschnitteAndTechnicalBaugebieteWithoutBaurate() {
        final var baugebietDto = new BaugebietDto();
        baugebietDto.setTechnical(true);
        final var bauabschnittDto = new BauabschnittDto();
        bauabschnittDto.setTechnical(true);
        bauabschnittDto.setBaugebiete(List.of(baugebietDto));
        final var abfragevarianteDto = new AbfragevarianteAngelegtDto();
        abfragevarianteDto.setBauabschnitte(List.of(bauabschnittDto));

        assertThat(this.technicalAttributesValidator.isValid(abfragevarianteDto, null), is(false));
    }

    @Test
    void isValidNoBauratenInAbfragevariante() {
        final var abfragevarianteDto = new AbfragevarianteAngelegtDto();
        assertThat(this.technicalAttributesValidator.isValid(abfragevarianteDto, null), is(false));
    }
}
