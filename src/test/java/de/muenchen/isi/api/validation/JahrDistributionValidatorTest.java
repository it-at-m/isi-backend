package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.BaugebietDto;
import de.muenchen.isi.api.dto.BaurateDto;
import java.util.List;
import org.junit.jupiter.api.Test;

class JahrDistributionValidatorTest {

    private JahrDistributionValidator jahrDistributionValidator = new JahrDistributionValidator();

    @Test
    void isValid() {
        var baugebiet = new BaugebietDto();
        baugebiet.setTechnical(true);

        assertThat(this.jahrDistributionValidator.isValid(baugebiet, null), is(true));

        // --

        baugebiet = new BaugebietDto();
        baugebiet.setTechnical(false);

        assertThat(this.jahrDistributionValidator.isValid(baugebiet, null), is(true));

        // --

        baugebiet = new BaugebietDto();
        baugebiet.setRealisierungVon(2020);
        baugebiet.setTechnical(false);

        var baurate1 = new BaurateDto();
        baurate1.setJahr(2020);
        var baurate2 = new BaurateDto();
        baurate2.setJahr(2021);
        var baurate3 = new BaurateDto();
        baurate3.setJahr(2022);
        baugebiet.setBauraten(List.of(baurate1, baurate2, baurate3));

        assertThat(this.jahrDistributionValidator.isValid(baugebiet, null), is(true));

        // --

        baugebiet = new BaugebietDto();
        baugebiet.setRealisierungVon(2019);
        baugebiet.setTechnical(false);

        baurate1 = new BaurateDto();
        baurate1.setJahr(2020);
        baurate2 = new BaurateDto();
        baurate2.setJahr(2021);
        baurate3 = new BaurateDto();
        baurate3.setJahr(2022);
        baugebiet.setBauraten(List.of(baurate1, baurate2, baurate3));

        assertThat(this.jahrDistributionValidator.isValid(baugebiet, null), is(true));

        // --

        baugebiet = new BaugebietDto();
        baugebiet.setRealisierungVon(2021);
        baugebiet.setTechnical(false);

        baurate1 = new BaurateDto();
        baurate1.setJahr(2020);
        baurate2 = new BaurateDto();
        baurate2.setJahr(2021);
        baurate3 = new BaurateDto();
        baurate3.setJahr(2022);
        baugebiet.setBauraten(List.of(baurate1, baurate2, baurate3));

        assertThat(this.jahrDistributionValidator.isValid(baugebiet, null), is(false));

        // --

        baugebiet = new BaugebietDto();
        baugebiet.setTechnical(false);

        baurate1 = new BaurateDto();
        baurate2 = new BaurateDto();
        baurate3 = new BaurateDto();
        baugebiet.setBauraten(List.of(baurate1, baurate2, baurate3));

        assertThat(this.jahrDistributionValidator.isValid(baugebiet, null), is(true));
    }
}
