package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.BauabschnittDto;
import de.muenchen.isi.api.dto.BaugebietDto;
import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfrageerstellungAbfragevarianteAngelegtDto;
import java.util.List;
import org.junit.jupiter.api.Test;

class WohneinheitenDistributionValidatorTest {

    private WohneinheitenDistributionValidator wohneinheitenDistributionValidator =
        new WohneinheitenDistributionValidator();

    @Test
    void isValid() {
        assertThat(
            this.wohneinheitenDistributionValidator.isValid(new AbfrageerstellungAbfragevarianteAngelegtDto(), null),
            is(true)
        );

        // --

        var abfragevariante = new AbfrageerstellungAbfragevarianteAngelegtDto();
        abfragevariante.setGesamtanzahlWe(150);

        assertThat(this.wohneinheitenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfrageerstellungAbfragevarianteAngelegtDto();
        abfragevariante.setGesamtanzahlWe(150);

        var bauabschnitt1 = new BauabschnittDto();

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1));

        assertThat(this.wohneinheitenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfrageerstellungAbfragevarianteAngelegtDto();
        abfragevariante.setGesamtanzahlWe(150);

        var baugebiet1 = new BaugebietDto();
        baugebiet1.setGesamtanzahlWe(50);
        var baugebiet2 = new BaugebietDto();
        baugebiet2.setGesamtanzahlWe(40);
        var baugebiet3 = new BaugebietDto();
        baugebiet3.setGesamtanzahlWe(60);

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2, baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1));

        assertThat(this.wohneinheitenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfrageerstellungAbfragevarianteAngelegtDto();
        abfragevariante.setGesamtanzahlWe(150);

        baugebiet1 = new BaugebietDto();
        baugebiet1.setGesamtanzahlWe(50);
        baugebiet2 = new BaugebietDto();
        baugebiet2.setGesamtanzahlWe(40);
        baugebiet3 = new BaugebietDto();
        baugebiet3.setGesamtanzahlWe(60);

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2));
        var bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.wohneinheitenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfrageerstellungAbfragevarianteAngelegtDto();
        abfragevariante.setGesamtanzahlWe(150);

        baugebiet1 = new BaugebietDto();
        baugebiet1.setGesamtanzahlWe(50);
        baugebiet2 = new BaugebietDto();
        baugebiet2.setGesamtanzahlWe(40);
        baugebiet3 = new BaugebietDto();
        baugebiet3.setGesamtanzahlWe(60);
        var baugebiet4 = new BaugebietDto();
        baugebiet4.setGesamtanzahlWe(0);
        var baugebiet5 = new BaugebietDto();
        baugebiet5.setGesamtanzahlWe(null);

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2));
        bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet3, baugebiet4, baugebiet5));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.wohneinheitenDistributionValidator.isValid(abfragevariante, null), is(true));

        // --

        abfragevariante = new AbfrageerstellungAbfragevarianteAngelegtDto();
        abfragevariante.setGesamtanzahlWe(150);

        baugebiet1 = new BaugebietDto();
        baugebiet1.setGesamtanzahlWe(49);
        baugebiet2 = new BaugebietDto();
        baugebiet2.setGesamtanzahlWe(40);
        baugebiet3 = new BaugebietDto();
        baugebiet3.setGesamtanzahlWe(60);

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2));
        bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.wohneinheitenDistributionValidator.isValid(abfragevariante, null), is(false));

        // --

        abfragevariante = new AbfrageerstellungAbfragevarianteAngelegtDto();
        abfragevariante.setGesamtanzahlWe(149);

        baugebiet1 = new BaugebietDto();
        baugebiet1.setGesamtanzahlWe(50);
        baugebiet2 = new BaugebietDto();
        baugebiet2.setGesamtanzahlWe(40);
        baugebiet3 = new BaugebietDto();
        baugebiet3.setGesamtanzahlWe(60);

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2));
        bauabschnitt2 = new BauabschnittDto();
        bauabschnitt2.setBaugebiete(List.of(baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        assertThat(this.wohneinheitenDistributionValidator.isValid(abfragevariante, null), is(false));
    }
}
