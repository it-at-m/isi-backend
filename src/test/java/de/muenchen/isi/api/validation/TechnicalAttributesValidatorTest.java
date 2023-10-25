package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.BauabschnittDto;
import de.muenchen.isi.api.dto.BaugebietDto;
import de.muenchen.isi.api.dto.BaurateDto;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

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

        assertThat(this.technicalAttributesValidator.isValid(List.of(bauabschnittDto), null), is(true));
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

        assertThat(this.technicalAttributesValidator.isValid(List.of(bauabschnittDto), null), is(true));
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

        assertThat(this.technicalAttributesValidator.isValid(List.of(bauabschnittDto), null), is(true));
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

        assertThat(this.technicalAttributesValidator.isValid(List.of(bauabschnittDto), null), is(false));
    }

    @Test
    void isNotValidOnlyTechnicalBauabschnitte() {
        final var bauabschnittDto = new BauabschnittDto();
        bauabschnittDto.setTechnical(true);

        assertThat(this.technicalAttributesValidator.isValid(List.of(bauabschnittDto), null), is(false));
    }

    @Test
    void isNotValidOnlyNonTechnicalBauabschnitte() {
        final var bauabschnittDto = new BauabschnittDto();
        bauabschnittDto.setTechnical(false);

        assertThat(this.technicalAttributesValidator.isValid(List.of(bauabschnittDto), null), is(false));
    }

    @Test
    void isNotValidOnlyNonTechnicalBauabschnitteAndNonTechnicalBaugebieteWithoutBaurate() {
        final var baugebietDto = new BaugebietDto();
        baugebietDto.setTechnical(false);
        final var bauabschnittDto = new BauabschnittDto();
        bauabschnittDto.setTechnical(false);
        bauabschnittDto.setBaugebiete(List.of(baugebietDto));

        assertThat(this.technicalAttributesValidator.isValid(List.of(bauabschnittDto), null), is(false));
    }

    @Test
    void isNotValidOnlyTechnicalBauabschnitteAndTechnicalBaugebieteWithoutBaurate() {
        final var baugebietDto = new BaugebietDto();
        baugebietDto.setTechnical(true);
        final var bauabschnittDto = new BauabschnittDto();
        bauabschnittDto.setTechnical(true);
        bauabschnittDto.setBaugebiete(List.of(baugebietDto));

        assertThat(this.technicalAttributesValidator.isValid(List.of(bauabschnittDto), null), is(false));
    }

    @Test
    void isValidNoBauratenInAbfragevariante() {
        assertThat(this.technicalAttributesValidator.isValid(null, null), is(true));
        assertThat(this.technicalAttributesValidator.isValid(new ArrayList<>(), null), is(true));
    }
}
