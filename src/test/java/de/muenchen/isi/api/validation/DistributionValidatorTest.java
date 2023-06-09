package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.BauabschnittDto;
import de.muenchen.isi.api.dto.BaugebietDto;
import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfrageerstellungAbfragevarianteAngelegtDto;
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
    void getNonTechnicalBaugebiete() {}

    @Test
    void getBauratenFromAllTechnicalBaugebiete() {
        var abfragevariante = new AbfrageerstellungAbfragevarianteAngelegtDto();
        abfragevariante.setGesamtanzahlWe(150);

        var baugebiet1 = new BaugebietDto();
        baugebiet1.setGesamtanzahlWe(50);
        baugebiet1.setTechnical(true);
        var baugebiet2 = new BaugebietDto();
        baugebiet2.setGesamtanzahlWe(40);
        baugebiet2.setTechnical(true);
        var baugebiet3 = new BaugebietDto();
        baugebiet3.setGesamtanzahlWe(60);
        baugebiet3.setTechnical(true);

        var bauabschnitt1 = new BauabschnittDto();
        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2, baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1));

        assertThat(this.distributionValidator.getBauratenFromAllTechnicalBaugebiete(abfragevariante), is(List.of()));
    }
}
