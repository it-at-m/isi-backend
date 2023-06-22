package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.BauabschnittDto;
import de.muenchen.isi.api.dto.BaugebietDto;
import de.muenchen.isi.api.dto.BaurateDto;
import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfragevarianteAngelegtDto;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class GeschossflaecheWohnenDistributionValidatorTest {

    private GeschossflaecheWohnenDistributionValidator geschossflaecheWohnenDistributionValidator =
        new GeschossflaecheWohnenDistributionValidator();

    @Test
    void isValidNonTechnicalBaugebiet() {
        assertThat(
            this.geschossflaecheWohnenDistributionValidator.isValid(new AbfragevarianteAngelegtDto(), null),
            is(true)
        );

        // --

        var abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGeschossflaecheWohnen(BigDecimal.valueOf(150));

        assertThat(this.geschossflaecheWohnenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGeschossflaecheWohnen(BigDecimal.valueOf(150));

        var bauabschnitt1 = new BauabschnittDto();

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1));

        assertThat(this.geschossflaecheWohnenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGeschossflaecheWohnen(BigDecimal.valueOf(150));

        var baugebiet1 = new BaugebietDto();
        baugebiet1.setGeschossflaecheWohnen(BigDecimal.valueOf(50));
        baugebiet1.setTechnical(false);
        var baugebiet2 = new BaugebietDto();
        baugebiet2.setGeschossflaecheWohnen(BigDecimal.valueOf(40));
        baugebiet2.setTechnical(false);
        var baugebiet3 = new BaugebietDto();
        baugebiet3.setGeschossflaecheWohnen(BigDecimal.valueOf(60));
        baugebiet3.setTechnical(false);

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2, baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1));

        assertThat(this.geschossflaecheWohnenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGeschossflaecheWohnen(BigDecimal.valueOf(150));

        baugebiet1 = new BaugebietDto();
        baugebiet1.setGeschossflaecheWohnen(BigDecimal.valueOf(50));
        baugebiet1.setTechnical(false);
        baugebiet2 = new BaugebietDto();
        baugebiet2.setGeschossflaecheWohnen(BigDecimal.valueOf(40));
        baugebiet2.setTechnical(false);
        baugebiet3 = new BaugebietDto();
        baugebiet3.setGeschossflaecheWohnen(BigDecimal.valueOf(60));
        baugebiet3.setTechnical(false);

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2));
        var bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.geschossflaecheWohnenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGeschossflaecheWohnen(BigDecimal.valueOf(150));

        baugebiet1 = new BaugebietDto();
        baugebiet1.setGeschossflaecheWohnen(BigDecimal.valueOf(50));
        baugebiet1.setTechnical(false);
        baugebiet2 = new BaugebietDto();
        baugebiet2.setGeschossflaecheWohnen(BigDecimal.valueOf(40));
        baugebiet2.setTechnical(false);
        baugebiet3 = new BaugebietDto();
        baugebiet3.setGeschossflaecheWohnen(BigDecimal.valueOf(60));
        baugebiet3.setTechnical(false);
        var baugebiet4 = new BaugebietDto();
        baugebiet4.setGeschossflaecheWohnen(BigDecimal.valueOf(0));
        baugebiet4.setTechnical(false);
        var baugebiet5 = new BaugebietDto();
        baugebiet5.setGeschossflaecheWohnen(null);
        baugebiet5.setTechnical(false);

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2));
        bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet3, baugebiet4, baugebiet5));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.geschossflaecheWohnenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGeschossflaecheWohnen(BigDecimal.valueOf(150));

        baugebiet1 = new BaugebietDto();
        baugebiet1.setGeschossflaecheWohnen(BigDecimal.valueOf(49));
        baugebiet1.setTechnical(false);
        baugebiet2 = new BaugebietDto();
        baugebiet2.setGeschossflaecheWohnen(BigDecimal.valueOf(40));
        baugebiet2.setTechnical(false);
        baugebiet3 = new BaugebietDto();
        baugebiet3.setGeschossflaecheWohnen(BigDecimal.valueOf(60));
        baugebiet3.setTechnical(false);

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2));
        bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.geschossflaecheWohnenDistributionValidator.isValid(abfragevariante, null), is(false));

        // --

        abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGeschossflaecheWohnen(BigDecimal.valueOf(149));

        baugebiet1 = new BaugebietDto();
        baugebiet1.setGeschossflaecheWohnen(BigDecimal.valueOf(50));
        baugebiet1.setTechnical(false);
        baugebiet2 = new BaugebietDto();
        baugebiet2.setGeschossflaecheWohnen(BigDecimal.valueOf(40));
        baugebiet2.setTechnical(false);
        baugebiet3 = new BaugebietDto();
        baugebiet3.setGeschossflaecheWohnen(BigDecimal.valueOf(60));
        baugebiet3.setTechnical(false);

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2));
        bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.geschossflaecheWohnenDistributionValidator.isValid(abfragevariante, null), is(false));
    }

    @Test
    void isValidTechnicalBaugebiet() {
        assertThat(
            this.geschossflaecheWohnenDistributionValidator.isValid(new AbfragevarianteAngelegtDto(), null),
            is(true)
        );

        // --

        var abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGeschossflaecheWohnen(BigDecimal.valueOf(150));

        assertThat(this.geschossflaecheWohnenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGeschossflaecheWohnen(BigDecimal.valueOf(150));

        var bauabschnitt1 = new BauabschnittDto();

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1));

        assertThat(this.geschossflaecheWohnenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGeschossflaecheWohnen(BigDecimal.valueOf(150));

        var baugebiet1 = new BaugebietDto();
        baugebiet1.setTechnical(true);
        var baugebiet2 = new BaugebietDto();
        baugebiet2.setTechnical(true);
        var baugebiet3 = new BaugebietDto();
        baugebiet3.setTechnical(true);

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2, baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1));

        assertThat(this.geschossflaecheWohnenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGeschossflaecheWohnen(BigDecimal.valueOf(150));

        baugebiet1 = new BaugebietDto();
        baugebiet1.setTechnical(true);
        var baurate11 = new BaurateDto();
        baurate11.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(10));
        var baurate12 = new BaurateDto();
        baurate12.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(50));
        baugebiet1.setBauraten(List.of(baurate11, baurate12));

        baugebiet2 = new BaugebietDto();
        baugebiet2.setTechnical(true);
        var baurate21 = new BaurateDto();
        baurate21.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(50));
        baugebiet2.setBauraten(List.of(baurate21));

        baugebiet3 = new BaugebietDto();
        baugebiet3.setTechnical(true);
        var baurate31 = new BaurateDto();
        baurate31.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(40));
        baugebiet3.setBauraten(List.of(baurate31));

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2, baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1));

        assertThat(this.geschossflaecheWohnenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGeschossflaecheWohnen(BigDecimal.valueOf(149));

        baugebiet1 = new BaugebietDto();
        baugebiet1.setTechnical(true);
        baurate11 = new BaurateDto();
        baurate11.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(10));
        baurate12 = new BaurateDto();
        baurate12.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(50));
        baugebiet1.setBauraten(List.of(baurate11, baurate12));

        baugebiet2 = new BaugebietDto();
        baugebiet2.setTechnical(true);
        baurate21 = new BaurateDto();
        baurate21.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(50));
        baugebiet2.setBauraten(List.of(baurate21));

        baugebiet3 = new BaugebietDto();
        baugebiet3.setTechnical(true);
        baurate31 = new BaurateDto();
        baurate31.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(40));
        baugebiet3.setBauraten(List.of(baurate31));

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2, baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1));

        assertThat(this.geschossflaecheWohnenDistributionValidator.isValid(abfragevariante, null), is(false));

        // --

        abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGeschossflaecheWohnen(BigDecimal.valueOf(151));

        baugebiet1 = new BaugebietDto();
        baugebiet1.setTechnical(true);
        baurate11 = new BaurateDto();
        baurate11.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(10));
        baurate12 = new BaurateDto();
        baurate12.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(50));
        baugebiet1.setBauraten(List.of(baurate11, baurate12));

        baugebiet2 = new BaugebietDto();
        baugebiet2.setTechnical(true);
        baurate21 = new BaurateDto();
        baurate21.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(50));
        baugebiet2.setBauraten(List.of(baurate21));

        baugebiet3 = new BaugebietDto();
        baugebiet3.setTechnical(true);
        baurate31 = new BaurateDto();
        baurate31.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(40));
        baugebiet3.setBauraten(List.of(baurate31));

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2, baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1));

        assertThat(this.geschossflaecheWohnenDistributionValidator.isValid(abfragevariante, null), is(false));

        // --

        abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGeschossflaecheWohnen(BigDecimal.valueOf(150));

        baugebiet1 = new BaugebietDto();
        baugebiet1.setTechnical(true);
        baurate11 = new BaurateDto();
        baurate12 = new BaurateDto();
        baugebiet1.setBauraten(List.of(baurate11, baurate12));

        baugebiet2 = new BaugebietDto();
        baugebiet2.setTechnical(true);
        baurate21 = new BaurateDto();
        baugebiet2.setBauraten(List.of(baurate21));

        baugebiet3 = new BaugebietDto();
        baugebiet3.setTechnical(true);
        baurate31 = new BaurateDto();
        baugebiet3.setBauraten(List.of(baurate31));

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2, baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1));

        assertThat(this.geschossflaecheWohnenDistributionValidator.isValid(abfragevariante, null), is(false));
    }
}
