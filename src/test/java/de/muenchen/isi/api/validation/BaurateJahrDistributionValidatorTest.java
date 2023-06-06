package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.BaugebietDto;
import de.muenchen.isi.api.dto.BaurateDto;
import java.util.List;
import org.junit.jupiter.api.Test;

class BaurateJahrDistributionValidatorTest {

    private BaurateJahrDistributionValidator baurateJahrDistributionValidator = new BaurateJahrDistributionValidator();

    @Test
    void isValid() {
        var baurate1 = new BaurateDto();
        baurate1.setJahr(2020);
        var baurate2 = new BaurateDto();
        baurate2.setJahr(2018);
        var baurate3 = new BaurateDto();
        baurate3.setJahr(2021);
        var baurate4 = new BaurateDto();
        baurate4.setJahr(2019);
        var baurate5 = new BaurateDto();
        baurate5.setJahr(2017);

        var baugebiet1 = new BaugebietDto();
        baugebiet1.setRealisierungVon(2017);
        baugebiet1.setTechnical(false);
        baugebiet1.setBauraten(List.of(baurate1, baurate2, baurate3, baurate4, baurate5));

        assertThat(this.baurateJahrDistributionValidator.isValid(baugebiet1, null), is(true));

        // --

        baurate1 = new BaurateDto();
        baurate1.setJahr(2020);
        baurate2 = new BaurateDto();
        baurate2.setJahr(2018);
        baurate3 = new BaurateDto();
        baurate3.setJahr(2021);
        baurate4 = new BaurateDto();
        baurate4.setJahr(2019);
        baurate5 = new BaurateDto();
        baurate5.setJahr(2017);

        baugebiet1 = new BaugebietDto();
        baugebiet1.setRealisierungVon(2016);
        baugebiet1.setTechnical(false);
        baugebiet1.setBauraten(List.of(baurate1, baurate2, baurate3, baurate4, baurate5));

        assertThat(this.baurateJahrDistributionValidator.isValid(baugebiet1, null), is(true));

        // --

        baurate1 = new BaurateDto();
        baurate1.setJahr(2020);
        baurate2 = new BaurateDto();
        baurate2.setJahr(2018);
        baurate3 = new BaurateDto();
        baurate3.setJahr(2021);
        baurate4 = new BaurateDto();
        baurate4.setJahr(2019);
        baurate5 = new BaurateDto();
        baurate5.setJahr(2017);

        baugebiet1 = new BaugebietDto();
        baugebiet1.setRealisierungVon(2018);
        baugebiet1.setTechnical(false);
        baugebiet1.setBauraten(List.of(baurate1, baurate2, baurate3, baurate4, baurate5));

        assertThat(this.baurateJahrDistributionValidator.isValid(baugebiet1, null), is(false));

        // --

        baurate1 = new BaurateDto();
        baurate1.setJahr(null);
        baurate2 = new BaurateDto();
        baurate2.setJahr(null);
        baurate3 = new BaurateDto();
        baurate3.setJahr(null);
        baurate4 = new BaurateDto();
        baurate4.setJahr(null);
        baurate5 = new BaurateDto();
        baurate5.setJahr(null);

        baugebiet1 = new BaugebietDto();
        baugebiet1.setRealisierungVon(2017);
        baugebiet1.setTechnical(false);
        baugebiet1.setBauraten(List.of(baurate1, baurate2, baurate3, baurate4, baurate5));

        assertThat(this.baurateJahrDistributionValidator.isValid(baugebiet1, null), is(true));

        // --

        baurate1 = new BaurateDto();
        baurate1.setJahr(null);
        baurate2 = new BaurateDto();
        baurate2.setJahr(null);
        baurate3 = new BaurateDto();
        baurate3.setJahr(null);
        baurate4 = new BaurateDto();
        baurate4.setJahr(null);
        baurate5 = new BaurateDto();
        baurate5.setJahr(null);

        baugebiet1 = new BaugebietDto();
        baugebiet1.setRealisierungVon(null);
        baugebiet1.setTechnical(false);
        baugebiet1.setBauraten(List.of(baurate1, baurate2, baurate3, baurate4, baurate5));

        assertThat(this.baurateJahrDistributionValidator.isValid(baugebiet1, null), is(true));

        // --

        assertThat(this.baurateJahrDistributionValidator.isValid(new BaugebietDto(), null), is(true));
    }
}
