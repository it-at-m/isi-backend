package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.BauabschnittDto;
import de.muenchen.isi.api.dto.BaugebietDto;
import de.muenchen.isi.api.dto.BaurateDto;
import de.muenchen.isi.api.dto.abfrageAngelegt.AbfragevarianteBaugenehmigungsverfahrenAngelegtDto;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EarliestBaurateIsRealisierungVonValidatorTest {

    private final EarliestBaurateIsRealisierungVonValidator validator = new EarliestBaurateIsRealisierungVonValidator();

    @Test
    void isValidRealisierungVonNullAndBauabschnitteNull() {
        final var abfragevariante = new AbfragevarianteBaugenehmigungsverfahrenAngelegtDto();
        abfragevariante.setRealisierungVon(null);
        abfragevariante.setBauabschnitte(null);
        assertThat(validator.isValid(abfragevariante, null), is(true));
    }

    @Test
    void isValidRealisierungVonNotNullAndBauabschnitteNull() {
        final var abfragevariante = new AbfragevarianteBaugenehmigungsverfahrenAngelegtDto();
        abfragevariante.setRealisierungVon(2020);
        abfragevariante.setBauabschnitte(null);
        assertThat(validator.isValid(abfragevariante, null), is(true));
    }

    @Test
    void isValidRealisierungVonNullAndBauabschnitteNotNull() {
        final var abfragevariante = new AbfragevarianteBaugenehmigungsverfahrenAngelegtDto();
        abfragevariante.setRealisierungVon(null);
        abfragevariante.setBauabschnitte(getBauabschnitte());
        assertThat(validator.isValid(abfragevariante, null), is(true));
    }

    @Test
    void isValidRealisierungVonIdEarliestAndBauabschnitteNotNull() {
        final var abfragevariante = new AbfragevarianteBaugenehmigungsverfahrenAngelegtDto();
        abfragevariante.setRealisierungVon(2020);
        abfragevariante.setBauabschnitte(getBauabschnitte());
        assertThat(validator.isValid(abfragevariante, null), is(true));
    }

    @Test
    void isValidRealisierungVonBeforeEarliestBaurate() {
        final var abfragevariante = new AbfragevarianteBaugenehmigungsverfahrenAngelegtDto();
        abfragevariante.setRealisierungVon(2019);
        abfragevariante.setBauabschnitte(getBauabschnitte());
        assertThat(validator.isValid(abfragevariante, null), is(false));
    }

    @Test
    void isValidRealisierungVonAfterEarliestBaurate() {
        final var abfragevariante = new AbfragevarianteBaugenehmigungsverfahrenAngelegtDto();
        abfragevariante.setRealisierungVon(2021);
        abfragevariante.setBauabschnitte(getBauabschnitte());
        assertThat(validator.isValid(abfragevariante, null), is(false));
    }

    public List<BauabschnittDto> getBauabschnitte() {
        final var bauabschnitte = new ArrayList<BauabschnittDto>();

        var bauabschnitt = new BauabschnittDto();
        bauabschnitt.setBaugebiete(new ArrayList<>());
        var baugebiet = new BaugebietDto();
        baugebiet.setBauraten(new ArrayList<>());
        var baurate = new BaurateDto();
        baurate.setJahr(2020);
        baugebiet.getBauraten().add(baurate);
        baurate = new BaurateDto();
        baurate.setJahr(2021);
        baugebiet.getBauraten().add(baurate);
        baurate = new BaurateDto();
        baurate.setJahr(2022);
        baugebiet.getBauraten().add(baurate);
        bauabschnitt.getBaugebiete().add(baugebiet);

        baugebiet = new BaugebietDto();
        baugebiet.setBauraten(new ArrayList<>());
        baurate = new BaurateDto();
        baurate.setJahr(2023);
        baugebiet.getBauraten().add(baurate);
        baurate = new BaurateDto();
        baurate.setJahr(2024);
        baugebiet.getBauraten().add(baurate);
        baurate = new BaurateDto();
        baurate.setJahr(2025);
        baugebiet.getBauraten().add(baurate);
        bauabschnitt.getBaugebiete().add(baugebiet);

        bauabschnitte.add(bauabschnitt);

        bauabschnitt = new BauabschnittDto();
        bauabschnitt.setBaugebiete(new ArrayList<>());
        baugebiet = new BaugebietDto();
        baugebiet.setBauraten(new ArrayList<>());
        baurate = new BaurateDto();
        baurate.setJahr(2021);
        baugebiet.getBauraten().add(baurate);
        baurate = new BaurateDto();
        baurate.setJahr(2022);
        baugebiet.getBauraten().add(baurate);
        bauabschnitt.getBaugebiete().add(baugebiet);

        bauabschnitte.add(bauabschnitt);

        return bauabschnitte;
    }
}
