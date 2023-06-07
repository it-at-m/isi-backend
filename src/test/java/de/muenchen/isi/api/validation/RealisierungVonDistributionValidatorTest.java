package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.BauabschnittDto;
import de.muenchen.isi.api.dto.BaugebietDto;
import de.muenchen.isi.api.dto.BaurateDto;
import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfrageerstellungAbfragevarianteAngelegtDto;
import java.util.List;
import org.junit.jupiter.api.Test;

class RealisierungVonDistributionValidatorTest {

    private RealisierungVonDistributionValidator realisierungVonDistributionValidator =
        new RealisierungVonDistributionValidator();

    @Test
    void isValidForBaugebiete() {
        var abfragevariante = new AbfrageerstellungAbfragevarianteAngelegtDto();
        abfragevariante.setRealisierungVon(2018);

        var baugebiet1 = new BaugebietDto();
        baugebiet1.setTechnical(false);
        baugebiet1.setRealisierungVon(2020);
        var baugebiet2 = new BaugebietDto();
        baugebiet2.setTechnical(false);
        baugebiet2.setRealisierungVon(2018);

        var bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2));

        var baugebiet3 = new BaugebietDto();
        baugebiet3.setTechnical(false);
        baugebiet3.setRealisierungVon(2019);
        var bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.realisierungVonDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfrageerstellungAbfragevarianteAngelegtDto();
        abfragevariante.setRealisierungVon(2017);

        baugebiet1 = new BaugebietDto();
        baugebiet1.setTechnical(false);
        baugebiet1.setRealisierungVon(2020);
        baugebiet2 = new BaugebietDto();
        baugebiet2.setTechnical(false);
        baugebiet2.setRealisierungVon(2018);

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2));

        baugebiet3 = new BaugebietDto();
        baugebiet3.setTechnical(false);
        baugebiet3.setRealisierungVon(2019);
        bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.realisierungVonDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfrageerstellungAbfragevarianteAngelegtDto();
        abfragevariante.setRealisierungVon(2019);

        baugebiet1 = new BaugebietDto();
        baugebiet1.setTechnical(false);
        baugebiet1.setRealisierungVon(2020);
        baugebiet2 = new BaugebietDto();
        baugebiet2.setTechnical(false);
        baugebiet2.setRealisierungVon(2018);

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2));

        baugebiet3 = new BaugebietDto();
        baugebiet3.setTechnical(false);
        baugebiet3.setRealisierungVon(2019);
        bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.realisierungVonDistributionValidator.isValid(abfragevariante, null), is(false));

        // --

        abfragevariante = new AbfrageerstellungAbfragevarianteAngelegtDto();
        abfragevariante.setRealisierungVon(2018);

        baugebiet1 = new BaugebietDto();
        baugebiet1.setTechnical(false);
        baugebiet1.setRealisierungVon(null);
        baugebiet2 = new BaugebietDto();
        baugebiet2.setTechnical(false);
        baugebiet2.setRealisierungVon(null);

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2));

        baugebiet3 = new BaugebietDto();
        baugebiet3.setTechnical(false);
        baugebiet3.setRealisierungVon(null);
        bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.realisierungVonDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfrageerstellungAbfragevarianteAngelegtDto();
        abfragevariante.setRealisierungVon(null);

        baugebiet1 = new BaugebietDto();
        baugebiet1.setTechnical(false);
        baugebiet1.setRealisierungVon(null);
        baugebiet2 = new BaugebietDto();
        baugebiet2.setTechnical(false);
        baugebiet2.setRealisierungVon(null);

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2));

        baugebiet3 = new BaugebietDto();
        baugebiet3.setTechnical(false);
        baugebiet3.setRealisierungVon(null);
        bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.realisierungVonDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfrageerstellungAbfragevarianteAngelegtDto();
        abfragevariante.setRealisierungVon(null);

        assertThat(this.realisierungVonDistributionValidator.isValid(abfragevariante, null), is(true));
    }

    @Test
    void isValidForBauraten() {
        var abfragevariante = new AbfrageerstellungAbfragevarianteAngelegtDto();
        abfragevariante.setRealisierungVon(2018);

        var baurate1 = new BaurateDto();
        baurate1.setJahr(2020);
        var baurate2 = new BaurateDto();
        baurate2.setJahr(2018);

        var baugebiet1 = new BaugebietDto();
        baugebiet1.setTechnical(true);
        baugebiet1.setBauraten(List.of(baurate1, baurate2));

        var bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1));

        var baurate3 = new BaurateDto();
        baurate3.setJahr(2019);

        var baugebiet2 = new BaugebietDto();
        baugebiet2.setTechnical(true);
        baugebiet2.setBauraten(List.of(baurate3));

        var bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet2));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.realisierungVonDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfrageerstellungAbfragevarianteAngelegtDto();
        abfragevariante.setRealisierungVon(2017);

        baurate1 = new BaurateDto();
        baurate1.setJahr(2020);
        baurate2 = new BaurateDto();
        baurate2.setJahr(2018);

        baugebiet1 = new BaugebietDto();
        baugebiet1.setTechnical(true);
        baugebiet1.setBauraten(List.of(baurate1, baurate2));

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1));

        baurate3 = new BaurateDto();
        baurate3.setJahr(2019);

        baugebiet2 = new BaugebietDto();
        baugebiet2.setTechnical(true);
        baugebiet2.setBauraten(List.of(baurate3));

        bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet2));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.realisierungVonDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfrageerstellungAbfragevarianteAngelegtDto();
        abfragevariante.setRealisierungVon(2019);

        baurate1 = new BaurateDto();
        baurate1.setJahr(2020);
        baurate2 = new BaurateDto();
        baurate2.setJahr(2018);

        baugebiet1 = new BaugebietDto();
        baugebiet1.setTechnical(true);
        baugebiet1.setBauraten(List.of(baurate1, baurate2));

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1));

        baurate3 = new BaurateDto();
        baurate3.setJahr(2019);

        baugebiet2 = new BaugebietDto();
        baugebiet2.setTechnical(true);
        baugebiet2.setBauraten(List.of(baurate3));

        bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet2));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.realisierungVonDistributionValidator.isValid(abfragevariante, null), is(false));

        // --

        abfragevariante = new AbfrageerstellungAbfragevarianteAngelegtDto();
        abfragevariante.setRealisierungVon(2018);

        baurate1 = new BaurateDto();
        baurate1.setJahr(null);
        baurate2 = new BaurateDto();
        baurate2.setJahr(null);

        baugebiet1 = new BaugebietDto();
        baugebiet1.setTechnical(true);
        baugebiet1.setBauraten(List.of(baurate1, baurate2));

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1));

        baurate3 = new BaurateDto();
        baurate3.setJahr(null);

        baugebiet2 = new BaugebietDto();
        baugebiet2.setTechnical(true);
        baugebiet1.setBauraten(List.of(baurate3));

        bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet2));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.realisierungVonDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfrageerstellungAbfragevarianteAngelegtDto();
        abfragevariante.setRealisierungVon(null);

        baurate1 = new BaurateDto();
        baurate1.setJahr(null);
        baurate2 = new BaurateDto();
        baurate2.setJahr(null);

        baugebiet1 = new BaugebietDto();
        baugebiet1.setTechnical(true);
        baugebiet1.setBauraten(List.of(baurate1, baurate2));

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1));

        baurate3 = new BaurateDto();
        baurate3.setJahr(null);

        baugebiet2 = new BaugebietDto();
        baugebiet2.setTechnical(true);
        baugebiet1.setBauraten(List.of(baurate3));

        bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet2));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.realisierungVonDistributionValidator.isValid(abfragevariante, null), is(true));
    }

    @Test
    void isValidForBaugebietAndBauraten() {
        var abfragevariante = new AbfrageerstellungAbfragevarianteAngelegtDto();
        abfragevariante.setRealisierungVon(2018);

        var baurate1 = new BaurateDto();
        baurate1.setJahr(2021);
        var baurate2 = new BaurateDto();
        baurate2.setJahr(2020);

        var baugebiet1 = new BaugebietDto();
        baugebiet1.setRealisierungVon(2018);
        baugebiet1.setTechnical(false);
        baugebiet1.setBauraten(List.of(baurate1, baurate2));

        var bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1));

        var baurate3 = new BaurateDto();
        baurate3.setJahr(2019);

        var baugebiet2 = new BaugebietDto();
        baugebiet2.setTechnical(true);
        baugebiet2.setBauraten(List.of(baurate3));

        var bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet2));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.realisierungVonDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfrageerstellungAbfragevarianteAngelegtDto();
        abfragevariante.setRealisierungVon(2017);

        baurate1 = new BaurateDto();
        baurate1.setJahr(2021);
        baurate2 = new BaurateDto();
        baurate2.setJahr(2020);

        baugebiet1 = new BaugebietDto();
        baugebiet1.setRealisierungVon(2018);
        baugebiet1.setTechnical(false);
        baugebiet1.setBauraten(List.of(baurate1, baurate2));

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1));

        baurate3 = new BaurateDto();
        baurate3.setJahr(2019);

        baugebiet2 = new BaugebietDto();
        baugebiet2.setTechnical(true);
        baugebiet2.setBauraten(List.of(baurate3));

        bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet2));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.realisierungVonDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfrageerstellungAbfragevarianteAngelegtDto();
        abfragevariante.setRealisierungVon(2019);

        baurate1 = new BaurateDto();
        baurate1.setJahr(2021);
        baurate2 = new BaurateDto();
        baurate2.setJahr(2020);

        baugebiet1 = new BaugebietDto();
        baugebiet1.setRealisierungVon(2018);
        baugebiet1.setTechnical(false);
        baugebiet1.setBauraten(List.of(baurate1, baurate2));

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1));

        baurate3 = new BaurateDto();
        baurate3.setJahr(2019);

        baugebiet2 = new BaugebietDto();
        baugebiet2.setTechnical(true);
        baugebiet2.setBauraten(List.of(baurate3));

        bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet2));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.realisierungVonDistributionValidator.isValid(abfragevariante, null), is(false));
    }
}
