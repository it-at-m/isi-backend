package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.BauabschnittDto;
import de.muenchen.isi.api.dto.BaugebietDto;
import de.muenchen.isi.api.dto.BaurateDto;
import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfragevarianteAngelegtDto;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class WohneinheitenDistributionValidatorTest {

    private WohneinheitenDistributionValidator wohneinheitenDistributionValidator =
        new WohneinheitenDistributionValidator();

    @Test
    void isValidNonTechnicalBaugebiet() {
        assertThat(this.wohneinheitenDistributionValidator.isValid(new AbfragevarianteAngelegtDto(), null), is(true));

        // --

        var abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGesamtanzahlWe(150);

        assertThat(this.wohneinheitenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGesamtanzahlWe(150);

        var bauabschnitt1 = new BauabschnittDto();

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1));

        assertThat(this.wohneinheitenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGesamtanzahlWe(150);

        var baugebiet1 = new BaugebietDto();
        baugebiet1.setGesamtanzahlWe(50);
        baugebiet1.setTechnical(false);
        var baugebiet2 = new BaugebietDto();
        baugebiet2.setGesamtanzahlWe(40);
        baugebiet2.setTechnical(false);
        var baugebiet3 = new BaugebietDto();
        baugebiet3.setGesamtanzahlWe(60);
        baugebiet3.setTechnical(false);

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2, baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1));

        assertThat(this.wohneinheitenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGesamtanzahlWe(150);

        baugebiet1 = new BaugebietDto();
        baugebiet1.setGesamtanzahlWe(50);
        baugebiet1.setTechnical(false);
        baugebiet2 = new BaugebietDto();
        baugebiet2.setGesamtanzahlWe(40);
        baugebiet2.setTechnical(false);
        baugebiet3 = new BaugebietDto();
        baugebiet3.setGesamtanzahlWe(60);
        baugebiet3.setTechnical(false);

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2));
        var bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.wohneinheitenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGesamtanzahlWe(150);

        baugebiet1 = new BaugebietDto();
        baugebiet1.setGesamtanzahlWe(50);
        baugebiet1.setTechnical(false);
        baugebiet2 = new BaugebietDto();
        baugebiet2.setGesamtanzahlWe(40);
        baugebiet2.setTechnical(false);
        baugebiet3 = new BaugebietDto();
        baugebiet3.setGesamtanzahlWe(60);
        baugebiet3.setTechnical(false);
        var baugebiet4 = new BaugebietDto();
        baugebiet4.setGesamtanzahlWe(0);
        baugebiet4.setTechnical(false);
        var baugebiet5 = new BaugebietDto();
        baugebiet5.setGesamtanzahlWe(null);
        baugebiet5.setTechnical(false);

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2));
        bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet3, baugebiet4, baugebiet5));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.wohneinheitenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGesamtanzahlWe(150);

        baugebiet1 = new BaugebietDto();
        baugebiet1.setGesamtanzahlWe(49);
        baugebiet1.setTechnical(false);
        baugebiet2 = new BaugebietDto();
        baugebiet2.setGesamtanzahlWe(40);
        baugebiet2.setTechnical(false);
        baugebiet3 = new BaugebietDto();
        baugebiet3.setGesamtanzahlWe(60);
        baugebiet3.setTechnical(false);

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2));
        bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.wohneinheitenDistributionValidator.isValid(abfragevariante, null), is(false));

        // --

        abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGesamtanzahlWe(149);

        baugebiet1 = new BaugebietDto();
        baugebiet1.setGesamtanzahlWe(50);
        baugebiet1.setTechnical(false);
        baugebiet2 = new BaugebietDto();
        baugebiet2.setGesamtanzahlWe(40);
        baugebiet2.setTechnical(false);
        baugebiet3 = new BaugebietDto();
        baugebiet3.setGesamtanzahlWe(60);
        baugebiet3.setTechnical(false);

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2));
        bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.wohneinheitenDistributionValidator.isValid(abfragevariante, null), is(false));
    }

    @Test
    void isValidTechnicalBaugebiet() {
        assertThat(this.wohneinheitenDistributionValidator.isValid(new AbfragevarianteAngelegtDto(), null), is(true));

        // --

        var abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGesamtanzahlWe(150);

        assertThat(this.wohneinheitenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGesamtanzahlWe(150);

        var bauabschnitt1 = new BauabschnittDto();

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1));

        assertThat(this.wohneinheitenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGesamtanzahlWe(150);

        var baugebiet1 = new BaugebietDto();
        baugebiet1.setTechnical(true);
        var baugebiet2 = new BaugebietDto();
        baugebiet2.setTechnical(true);
        var baugebiet3 = new BaugebietDto();
        baugebiet3.setTechnical(true);

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2, baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1));

        assertThat(this.wohneinheitenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGesamtanzahlWe(150);

        baugebiet1 = new BaugebietDto();
        baugebiet1.setTechnical(true);
        var baurate11 = new BaurateDto();
        baurate11.setAnzahlWeGeplant(10);
        var baurate12 = new BaurateDto();
        baurate12.setAnzahlWeGeplant(50);
        baugebiet1.setBauraten(List.of(baurate11, baurate12));

        baugebiet2 = new BaugebietDto();
        baugebiet2.setTechnical(true);
        var baurate21 = new BaurateDto();
        baurate21.setAnzahlWeGeplant(50);
        baugebiet2.setBauraten(List.of(baurate21));

        baugebiet3 = new BaugebietDto();
        baugebiet3.setTechnical(true);
        var baurate31 = new BaurateDto();
        baurate31.setAnzahlWeGeplant(40);
        baugebiet3.setBauraten(List.of(baurate31));

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2, baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1));

        assertThat(this.wohneinheitenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGesamtanzahlWe(149);

        baugebiet1 = new BaugebietDto();
        baugebiet1.setTechnical(true);
        baurate11 = new BaurateDto();
        baurate11.setAnzahlWeGeplant(10);
        baurate12 = new BaurateDto();
        baurate12.setAnzahlWeGeplant(50);
        baugebiet1.setBauraten(List.of(baurate11, baurate12));

        baugebiet2 = new BaugebietDto();
        baugebiet2.setTechnical(true);
        baurate21 = new BaurateDto();
        baurate21.setAnzahlWeGeplant(50);
        baugebiet2.setBauraten(List.of(baurate21));

        baugebiet3 = new BaugebietDto();
        baugebiet3.setTechnical(true);
        baurate31 = new BaurateDto();
        baurate31.setAnzahlWeGeplant(40);
        baugebiet3.setBauraten(List.of(baurate31));

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2, baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1));

        assertThat(this.wohneinheitenDistributionValidator.isValid(abfragevariante, null), is(false));

        // --

        abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGesamtanzahlWe(151);

        baugebiet1 = new BaugebietDto();
        baugebiet1.setTechnical(true);
        baurate11 = new BaurateDto();
        baurate11.setAnzahlWeGeplant(10);
        baurate12 = new BaurateDto();
        baurate12.setAnzahlWeGeplant(50);
        baugebiet1.setBauraten(List.of(baurate11, baurate12));

        baugebiet2 = new BaugebietDto();
        baugebiet2.setTechnical(true);
        baurate21 = new BaurateDto();
        baurate21.setAnzahlWeGeplant(50);
        baugebiet2.setBauraten(List.of(baurate21));

        baugebiet3 = new BaugebietDto();
        baugebiet3.setTechnical(true);
        baurate31 = new BaurateDto();
        baurate31.setAnzahlWeGeplant(40);
        baugebiet3.setBauraten(List.of(baurate31));

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2, baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1));

        assertThat(this.wohneinheitenDistributionValidator.isValid(abfragevariante, null), is(false));

        // --

        abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGesamtanzahlWe(150);

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

        assertThat(this.wohneinheitenDistributionValidator.isValid(abfragevariante, null), is(false));
    }
}
