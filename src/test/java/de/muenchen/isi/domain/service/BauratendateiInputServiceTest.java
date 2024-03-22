package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.mapper.BauratendateiDomainMapperImpl;
import de.muenchen.isi.domain.model.bauratendatei.BauratendateiInputModel;
import de.muenchen.isi.domain.model.bauratendatei.BauratendateiWohneinheitenModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    void sumWohneinheitenOfBauratendateiInputs() {
        final var inputs = new ArrayList<BauratendateiInputModel>();

        var bauratendateiInput = new BauratendateiInputModel();
        bauratendateiInput.setWohneinheiten(new ArrayList<>());
        var bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2024");
        bauratendateiWohneinheiten.setFoerderart("förderart1");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(100));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2024");
        bauratendateiWohneinheiten.setFoerderart("förderart2");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(1000));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2025");
        bauratendateiWohneinheiten.setFoerderart("förderart1");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(10000));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2025");
        bauratendateiWohneinheiten.setFoerderart("förderart2");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(100000));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2026");
        bauratendateiWohneinheiten.setFoerderart("förderart1");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(1000000));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2027");
        bauratendateiWohneinheiten.setFoerderart("förderart3");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(10000000));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        inputs.add(bauratendateiInput);

        bauratendateiInput = new BauratendateiInputModel();
        bauratendateiInput.setWohneinheiten(new ArrayList<>());
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2024");
        bauratendateiWohneinheiten.setFoerderart("förderart1");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(700));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2024");
        bauratendateiWohneinheiten.setFoerderart("förderart2");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(7000));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2025");
        bauratendateiWohneinheiten.setFoerderart("förderart1");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(70000));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2025");
        bauratendateiWohneinheiten.setFoerderart("förderart2");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(700000));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2026");
        bauratendateiWohneinheiten.setFoerderart("förderart1");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(7000000));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2028");
        bauratendateiWohneinheiten.setFoerderart("förderart4");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(9));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        inputs.add(bauratendateiInput);

        bauratendateiInput = new BauratendateiInputModel();
        bauratendateiInput.setWohneinheiten(new ArrayList<>());
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2025");
        bauratendateiWohneinheiten.setFoerderart("förderart2");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(888));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        inputs.add(bauratendateiInput);

        final var result = bauratendateiInputService.sumWohneinheitenOfBauratendateiInputs(inputs.stream());

        final var expected = new HashMap<String, BigDecimal>();
        expected.put("2024förderart1", BigDecimal.valueOf(800));
        expected.put("2024förderart2", BigDecimal.valueOf(8000));
        expected.put("2025förderart1", BigDecimal.valueOf(80000));
        expected.put("2025förderart2", BigDecimal.valueOf(800888));
        expected.put("2026förderart1", BigDecimal.valueOf(8000000));
        expected.put("2027förderart3", BigDecimal.valueOf(10000000));
        expected.put("2028förderart4", BigDecimal.valueOf(9));

        assertThat(result, is(expected));
    }

    @Test
    void equalsBauratendateiInput() {
        final var inputs = new ArrayList<BauratendateiInputModel>();

        var bauratendateiInput = new BauratendateiInputModel();
        bauratendateiInput.setWohneinheiten(new ArrayList<>());
        var bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2024");
        bauratendateiWohneinheiten.setFoerderart("förderart1");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(100));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2024");
        bauratendateiWohneinheiten.setFoerderart("förderart2");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(1000));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2025");
        bauratendateiWohneinheiten.setFoerderart("förderart1");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(10000));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2025");
        bauratendateiWohneinheiten.setFoerderart("förderart2");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(100000));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2026");
        bauratendateiWohneinheiten.setFoerderart("förderart1");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(1000000));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2027");
        bauratendateiWohneinheiten.setFoerderart("förderart3");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(10000000));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        inputs.add(bauratendateiInput);

        bauratendateiInput = new BauratendateiInputModel();
        bauratendateiInput.setWohneinheiten(new ArrayList<>());
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2024");
        bauratendateiWohneinheiten.setFoerderart("förderart1");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(700));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2024");
        bauratendateiWohneinheiten.setFoerderart("förderart2");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(7000));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2025");
        bauratendateiWohneinheiten.setFoerderart("förderart1");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(70000));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2025");
        bauratendateiWohneinheiten.setFoerderart("förderart2");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(700000));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2026");
        bauratendateiWohneinheiten.setFoerderart("förderart1");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(7000000));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2028");
        bauratendateiWohneinheiten.setFoerderart("förderart4");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(9));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        inputs.add(bauratendateiInput);

        bauratendateiInput = new BauratendateiInputModel();
        bauratendateiInput.setWohneinheiten(new ArrayList<>());
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2025");
        bauratendateiWohneinheiten.setFoerderart("förderart2");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(888));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        inputs.add(bauratendateiInput);

        bauratendateiInput = new BauratendateiInputModel();
        bauratendateiInput.setWohneinheiten(new ArrayList<>());
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2024");
        bauratendateiWohneinheiten.setFoerderart("förderart1");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(800));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2024");
        bauratendateiWohneinheiten.setFoerderart("förderart2");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(8000));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2025");
        bauratendateiWohneinheiten.setFoerderart("förderart1");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(80000));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2025");
        bauratendateiWohneinheiten.setFoerderart("förderart2");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(800888));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2026");
        bauratendateiWohneinheiten.setFoerderart("förderart1");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(8000000));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2027");
        bauratendateiWohneinheiten.setFoerderart("förderart3");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(10000000.0000));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);
        bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2028");
        bauratendateiWohneinheiten.setFoerderart("förderart4");
        bauratendateiWohneinheiten.setWohneinheiten(BigDecimal.valueOf(900).movePointLeft(2));
        bauratendateiInput.getWohneinheiten().add(bauratendateiWohneinheiten);

        var result = bauratendateiInputService.equals(bauratendateiInput, inputs);
        assertThat(result, is(true));

        result = bauratendateiInputService.equals((BauratendateiInputModel) null, null);
        assertThat(result, is(true));

        result = bauratendateiInputService.equals((BauratendateiInputModel) null, List.of());
        assertThat(result, is(true));
    }

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
