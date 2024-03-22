package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.mapper.BauratendateiDomainMapperImpl;
import de.muenchen.isi.domain.model.bauratendatei.BauratendateiWohneinheitenModel;
import java.math.BigDecimal;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BauratendateiInputServiceTest {

    private BauratendateiInputService bauratendateiInputService = new BauratendateiInputService(
        new BauratendateiDomainMapperImpl()
    );

    @Test
    void equals() {
        final var basis = new HashMap<String, BigDecimal>();
        basis.put("1", BigDecimal.ONE.movePointRight(0));
        basis.put("2", BigDecimal.ONE.movePointRight(1));
        basis.put("3", BigDecimal.ONE.movePointRight(2));
        final var inputs = new HashMap<String, BigDecimal>();
        inputs.put("1", BigDecimal.valueOf(100, 2));
        inputs.put("2", BigDecimal.valueOf(100, 1));
        inputs.put("3", BigDecimal.valueOf(10000, 2));
        var result = bauratendateiInputService.equals(basis, inputs);
        assertThat(result, is(true));

        basis.clear();
        basis.put("1", BigDecimal.valueOf(5.55));
        basis.put("2", BigDecimal.ONE.movePointRight(1));
        basis.put("3", BigDecimal.ONE.movePointRight(2));
        inputs.clear();
        inputs.put("1", BigDecimal.valueOf(100, 2));
        inputs.put("2", BigDecimal.valueOf(100, 1));
        inputs.put("3", BigDecimal.valueOf(10000, 2));
        result = bauratendateiInputService.equals(basis, inputs);
        assertThat(result, is(false));

        basis.clear();
        basis.put("1", BigDecimal.ONE.movePointRight(0));
        basis.put("2", BigDecimal.ONE.movePointRight(1));
        basis.put("3", BigDecimal.ONE.movePointRight(2));
        inputs.clear();
        inputs.put("1", BigDecimal.valueOf(5.55));
        inputs.put("2", BigDecimal.valueOf(100, 1));
        inputs.put("3", BigDecimal.valueOf(10000, 2));
        result = bauratendateiInputService.equals(basis, inputs);
        assertThat(result, is(false));

        basis.clear();
        basis.put("1", BigDecimal.ONE.movePointRight(0));
        basis.put("2", BigDecimal.ONE.movePointRight(1));
        inputs.clear();
        inputs.put("1", BigDecimal.valueOf(100, 2));
        inputs.put("2", BigDecimal.valueOf(100, 1));
        inputs.put("3", BigDecimal.valueOf(10000, 2));
        result = bauratendateiInputService.equals(basis, inputs);
        assertThat(result, is(false));

        basis.clear();
        basis.put("1", BigDecimal.ONE.movePointRight(0));
        basis.put("2", BigDecimal.ONE.movePointRight(1));
        basis.put("3", BigDecimal.ONE.movePointRight(2));
        inputs.clear();
        inputs.put("1", BigDecimal.valueOf(100, 2));
        inputs.put("2", BigDecimal.valueOf(100, 1));
        result = bauratendateiInputService.equals(basis, inputs);
        assertThat(result, is(false));

        basis.clear();
        inputs.clear();
        result = bauratendateiInputService.equals(basis, inputs);
        assertThat(result, is(true));
    }

    @Test
    void concatJahrAndFoerderart() {
        final var bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2024");
        bauratendateiWohneinheiten.setFoerderart("förderart1");
        var result = bauratendateiInputService.concatJahrAndFoerderart(bauratendateiWohneinheiten);
        assertThat(result, is("2024förderart1"));

        bauratendateiWohneinheiten.setJahr("2024");
        bauratendateiWohneinheiten.setFoerderart("");
        result = bauratendateiInputService.concatJahrAndFoerderart(bauratendateiWohneinheiten);
        assertThat(result, is("2024"));

        bauratendateiWohneinheiten.setJahr("2024");
        bauratendateiWohneinheiten.setFoerderart(null);
        result = bauratendateiInputService.concatJahrAndFoerderart(bauratendateiWohneinheiten);
        assertThat(result, is("2024null"));

        bauratendateiWohneinheiten.setJahr("");
        bauratendateiWohneinheiten.setFoerderart("förderart1");
        result = bauratendateiInputService.concatJahrAndFoerderart(bauratendateiWohneinheiten);
        assertThat(result, is("förderart1"));

        bauratendateiWohneinheiten.setJahr("null");
        bauratendateiWohneinheiten.setFoerderart("förderart1");
        result = bauratendateiInputService.concatJahrAndFoerderart(bauratendateiWohneinheiten);
        assertThat(result, is("nullförderart1"));

        bauratendateiWohneinheiten.setJahr("");
        bauratendateiWohneinheiten.setFoerderart("");
        result = bauratendateiInputService.concatJahrAndFoerderart(bauratendateiWohneinheiten);
        assertThat(result, is(""));

        bauratendateiWohneinheiten.setJahr(null);
        bauratendateiWohneinheiten.setFoerderart(null);
        result = bauratendateiInputService.concatJahrAndFoerderart(bauratendateiWohneinheiten);
        assertThat(result, is("nullnull"));
    }
}
