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
class DistributionValidatorTest {

    private DistributionValidator distributionValidator = new DistributionValidator();

    @Test
    void getNonTechnicalBaugebiete() {
        assertThat(
            this.distributionValidator.getNonTechnicalBaugebiete(new AbfragevarianteAngelegtDto()),
            is(List.of())
        );

        // --

        var abfragevariante = new AbfragevarianteAngelegtDto();

        var bauabschnitt1 = new BauabschnittDto();

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1));

        assertThat(this.distributionValidator.getNonTechnicalBaugebiete(abfragevariante), is(List.of()));

        // --

        abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGesamtanzahlWe(150);

        var baugebiet1 = new BaugebietDto();
        baugebiet1.setWeGeplant(50);
        baugebiet1.setTechnical(false);
        var baugebiet2 = new BaugebietDto();
        baugebiet2.setWeGeplant(40);
        baugebiet2.setTechnical(true);
        var baugebiet3 = new BaugebietDto();
        baugebiet3.setWeGeplant(60);
        baugebiet3.setTechnical(false);

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2, baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1));

        assertThat(
            this.distributionValidator.getNonTechnicalBaugebiete(abfragevariante),
            is(List.of(baugebiet1, baugebiet3))
        );
    }

    @Test
    void getBauratenFromAllTechnicalBaugebiete() {
        assertThat(
            this.distributionValidator.getBauratenFromAllTechnicalBaugebiete(new AbfragevarianteAngelegtDto()),
            is(List.of())
        );

        // --

        var abfragevariante = new AbfragevarianteAngelegtDto();

        var bauabschnitt1 = new BauabschnittDto();

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1));

        assertThat(this.distributionValidator.getBauratenFromAllTechnicalBaugebiete(abfragevariante), is(List.of()));

        // --

        abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGesamtanzahlWe(150);

        var baugebiet1 = new BaugebietDto();
        baugebiet1.setWeGeplant(50);
        baugebiet1.setTechnical(true);
        var baugebiet2 = new BaugebietDto();
        baugebiet2.setWeGeplant(40);
        baugebiet2.setTechnical(true);
        var baugebiet3 = new BaugebietDto();
        baugebiet3.setWeGeplant(60);
        baugebiet3.setTechnical(true);

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2, baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1));

        assertThat(this.distributionValidator.getBauratenFromAllTechnicalBaugebiete(abfragevariante), is(List.of()));

        // --

        abfragevariante = new AbfragevarianteAngelegtDto();
        abfragevariante.setGesamtanzahlWe(150);

        baugebiet1 = new BaugebietDto();
        baugebiet1.setTechnical(true);
        var baurate11 = new BaurateDto();
        var baurate12 = new BaurateDto();
        baugebiet1.setBauraten(List.of(baurate11, baurate12));

        baugebiet2 = new BaugebietDto();
        baugebiet2.setTechnical(true);
        var baurate21 = new BaurateDto();
        baugebiet2.setBauraten(List.of(baurate21));

        baugebiet3 = new BaugebietDto();
        baugebiet3.setTechnical(true);
        var baurate31 = new BaurateDto();
        baugebiet3.setBauraten(List.of(baurate31));

        var baugebiet4 = new BaugebietDto();
        baugebiet4.setTechnical(false);
        var baurate41 = new BaurateDto();
        baugebiet4.setBauraten(List.of(baurate41));

        bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2, baugebiet3, baugebiet4));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1));

        assertThat(
            this.distributionValidator.getBauratenFromAllTechnicalBaugebiete(abfragevariante),
            is(List.of(baurate11, baurate12, baurate21, baurate31))
        );
    }
}
