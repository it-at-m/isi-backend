package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.AbfragevarianteDto;
import de.muenchen.isi.api.dto.BauabschnittDto;
import de.muenchen.isi.api.dto.BaugebietDto;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class GeschossflaecheWohnenDistributionValidatorTest {

    private GeschossflaecheWohnenDistributionValidator geschossflaecheWohnenDistributionValidator =
        new GeschossflaecheWohnenDistributionValidator();

    @Test
    void isValid() {
        assertThat(this.geschossflaecheWohnenDistributionValidator.isValid(new AbfragevarianteDto(), null), is(true));

        // --

        var abfragevariante = new AbfragevarianteDto();
        abfragevariante.setGeschossflaecheWohnen(BigDecimal.valueOf(150));

        assertThat(this.geschossflaecheWohnenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfragevarianteDto();
        abfragevariante.setGeschossflaecheWohnen(BigDecimal.valueOf(150));

        var bauabschnitt1 = new BauabschnittDto();

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1));

        assertThat(this.geschossflaecheWohnenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfragevarianteDto();
        abfragevariante.setGeschossflaecheWohnen(BigDecimal.valueOf(150));

        var baugebiet1 = new BaugebietDto();
        baugebiet1.setGeschossflaecheWohnen(BigDecimal.valueOf(50.0));
        var baugebiet2 = new BaugebietDto();
        baugebiet2.setGeschossflaecheWohnen(BigDecimal.valueOf(40));
        var baugebiet3 = new BaugebietDto();
        baugebiet3.setGeschossflaecheWohnen(BigDecimal.valueOf(60));

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2, baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1));

        assertThat(this.geschossflaecheWohnenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfragevarianteDto();
        abfragevariante.setGeschossflaecheWohnen(BigDecimal.valueOf(150));

        baugebiet1 = new BaugebietDto();
        baugebiet1.setGeschossflaecheWohnen(BigDecimal.valueOf(50.0));
        baugebiet2 = new BaugebietDto();
        baugebiet2.setGeschossflaecheWohnen(BigDecimal.valueOf(40));
        baugebiet3 = new BaugebietDto();
        baugebiet3.setGeschossflaecheWohnen(BigDecimal.valueOf(60));

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2));
        var bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.geschossflaecheWohnenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfragevarianteDto();
        abfragevariante.setGeschossflaecheWohnen(BigDecimal.valueOf(150));

        baugebiet1 = new BaugebietDto();
        baugebiet1.setGeschossflaecheWohnen(BigDecimal.valueOf(50.0));
        baugebiet2 = new BaugebietDto();
        baugebiet2.setGeschossflaecheWohnen(BigDecimal.valueOf(40));
        baugebiet3 = new BaugebietDto();
        baugebiet3.setGeschossflaecheWohnen(BigDecimal.valueOf(60));
        var baugebiet4 = new BaugebietDto();
        baugebiet4.setGeschossflaecheWohnen(BigDecimal.valueOf(0.0));
        var baugebiet5 = new BaugebietDto();
        baugebiet5.setGeschossflaecheWohnen(null);

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2));
        bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet3, baugebiet4, baugebiet5));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.geschossflaecheWohnenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfragevarianteDto();
        abfragevariante.setGeschossflaecheWohnen(BigDecimal.valueOf(150));

        baugebiet1 = new BaugebietDto();
        baugebiet1.setGeschossflaecheWohnen(BigDecimal.valueOf(49));
        baugebiet2 = new BaugebietDto();
        baugebiet2.setGeschossflaecheWohnen(BigDecimal.valueOf(40));
        baugebiet3 = new BaugebietDto();
        baugebiet3.setGeschossflaecheWohnen(BigDecimal.valueOf(60));

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2));
        bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.geschossflaecheWohnenDistributionValidator.isValid(abfragevariante, null), is(false));

        // --

        abfragevariante = new AbfragevarianteDto();
        abfragevariante.setGeschossflaecheWohnen(BigDecimal.valueOf(149));

        baugebiet1 = new BaugebietDto();
        baugebiet1.setGeschossflaecheWohnen(BigDecimal.valueOf(50.0));
        baugebiet2 = new BaugebietDto();
        baugebiet2.setGeschossflaecheWohnen(BigDecimal.valueOf(40));
        baugebiet3 = new BaugebietDto();
        baugebiet3.setGeschossflaecheWohnen(BigDecimal.valueOf(60));

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2));
        bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.geschossflaecheWohnenDistributionValidator.isValid(abfragevariante, null), is(false));
    }
}
