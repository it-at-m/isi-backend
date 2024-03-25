package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.mapper.BauratendateiDomainMapperImpl;
import de.muenchen.isi.domain.model.bauratendatei.BauratendateiInputModel;
import de.muenchen.isi.domain.model.calculation.BedarfeForAbfragevarianteModel;
import de.muenchen.isi.domain.model.calculation.LangfristigerBedarfModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenProFoerderartProJahrModel;
import de.muenchen.isi.domain.model.common.VerortungModel;
import de.muenchen.isi.domain.model.common.VerortungMultiPolygonModel;
import de.muenchen.isi.domain.model.common.ViertelModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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
    void getViertel() {
        VerortungModel verortung = null;
        var result = bauratendateiInputService.getViertel(verortung);
        assertThat(result, is(Set.of()));

        verortung = new VerortungMultiPolygonModel();
        result = bauratendateiInputService.getViertel(verortung);
        assertThat(result, is(Set.of()));

        verortung = new VerortungMultiPolygonModel();
        var viertel = new HashSet<ViertelModel>();
        verortung.setViertel(viertel);
        result = bauratendateiInputService.getViertel(verortung);
        assertThat(result, is(Set.of()));

        verortung = new VerortungMultiPolygonModel();
        viertel = new HashSet<>();
        var theViertel = new ViertelModel();
        theViertel.setNummer("1");
        viertel.add(theViertel);
        theViertel = new ViertelModel();
        theViertel.setNummer("2");
        viertel.add(theViertel);
        theViertel = new ViertelModel();
        theViertel.setNummer(null);
        viertel.add(theViertel);
        theViertel = new ViertelModel();
        theViertel.setNummer("4");
        viertel.add(theViertel);
        verortung.setViertel(viertel);
        result = bauratendateiInputService.getViertel(verortung);
        assertThat(result, is(Set.of("1", "2", "4")));
    }

    @Test
    void getWohneinheiten() {
        var result = bauratendateiInputService.getWohneinheiten(null, UUID.randomUUID());
        assertThat(result, is(List.of()));

        final var bedarfe = new HashMap<UUID, BedarfeForAbfragevarianteModel>();
        result = bauratendateiInputService.getWohneinheiten(bedarfe, UUID.randomUUID());
        assertThat(result, is(List.of()));

        var uuid = UUID.randomUUID();
        bedarfe.put(uuid, null);
        result = bauratendateiInputService.getWohneinheiten(bedarfe, uuid);
        assertThat(result, is(List.of()));

        var bedarfeForAbfragevariante = new BedarfeForAbfragevarianteModel();
        bedarfe.clear();
        bedarfe.put(uuid, bedarfeForAbfragevariante);
        result = bauratendateiInputService.getWohneinheiten(bedarfe, uuid);
        assertThat(result, is(List.of()));

        bedarfeForAbfragevariante = new BedarfeForAbfragevarianteModel();
        bedarfeForAbfragevariante.setLangfristigerPlanungsursaechlicherBedarf(new LangfristigerBedarfModel());
        bedarfe.clear();
        bedarfe.put(uuid, bedarfeForAbfragevariante);
        result = bauratendateiInputService.getWohneinheiten(bedarfe, uuid);
        assertThat(result, is(List.of()));

        bedarfeForAbfragevariante = new BedarfeForAbfragevarianteModel();
        var langfristigerBedarf = new LangfristigerBedarfModel();
        langfristigerBedarf.setWohneinheiten(
            List.of(
                new WohneinheitenProFoerderartProJahrModel(),
                new WohneinheitenProFoerderartProJahrModel(),
                new WohneinheitenProFoerderartProJahrModel()
            )
        );
        bedarfeForAbfragevariante.setLangfristigerPlanungsursaechlicherBedarf(langfristigerBedarf);
        bedarfe.clear();
        bedarfe.put(uuid, bedarfeForAbfragevariante);
        result = bauratendateiInputService.getWohneinheiten(bedarfe, uuid);
        assertThat(
            result,
            is(
                List.of(
                    new WohneinheitenProFoerderartProJahrModel(),
                    new WohneinheitenProFoerderartProJahrModel(),
                    new WohneinheitenProFoerderartProJahrModel()
                )
            )
        );
    }

    @Test
    void equalsBauratendateiInput() {
        final var inputs = new ArrayList<BauratendateiInputModel>();

        var bauratendateiInput = new BauratendateiInputModel();
        bauratendateiInput.setWohneinheiten(new ArrayList<>());
        var wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2024");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart1");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(100));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2024");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart2");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(1000));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2025");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart1");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(10000));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2025");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart2");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(100000));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2026");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart1");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(1000000));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2027");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart3");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(10000000));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        inputs.add(bauratendateiInput);

        bauratendateiInput = new BauratendateiInputModel();
        bauratendateiInput.setWohneinheiten(new ArrayList<>());
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2024");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart1");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(700));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2024");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart2");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(7000));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2025");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart1");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(70000));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2025");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart2");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(700000));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2026");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart1");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(7000000));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2028");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart4");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(9));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        inputs.add(bauratendateiInput);

        bauratendateiInput = new BauratendateiInputModel();
        bauratendateiInput.setWohneinheiten(new ArrayList<>());
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2025");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart2");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(888));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        inputs.add(bauratendateiInput);

        bauratendateiInput = new BauratendateiInputModel();
        bauratendateiInput.setWohneinheiten(new ArrayList<>());
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2024");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart1");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(800));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2024");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart2");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(8000));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2025");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart1");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(80000));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2025");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart2");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(800888));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2026");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart1");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(8000000));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2027");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart3");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(10000000.0000));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2028");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart4");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(900).movePointLeft(2));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);

        var result = bauratendateiInputService.equals(bauratendateiInput, inputs);
        assertThat(result, is(true));

        result = bauratendateiInputService.equals((BauratendateiInputModel) null, null);
        assertThat(result, is(true));

        result = bauratendateiInputService.equals(null, List.of());
        assertThat(result, is(true));
    }

    @Test
    void sumWohneinheitenOfBauratendateiInputs() {
        final var inputs = new ArrayList<BauratendateiInputModel>();

        var bauratendateiInput = new BauratendateiInputModel();
        bauratendateiInput.setWohneinheiten(new ArrayList<>());
        var wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2024");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart1");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(100));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2024");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart2");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(1000));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2025");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart1");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(10000));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2025");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart2");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(100000));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2026");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart1");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(1000000));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2027");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart3");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(10000000));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        inputs.add(bauratendateiInput);

        bauratendateiInput = new BauratendateiInputModel();
        bauratendateiInput.setWohneinheiten(new ArrayList<>());
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2024");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart1");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(700));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2024");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart2");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(7000));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2025");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart1");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(70000));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2025");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart2");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(700000));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2026");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart1");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(7000000));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2028");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart4");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(9));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
        inputs.add(bauratendateiInput);

        bauratendateiInput = new BauratendateiInputModel();
        bauratendateiInput.setWohneinheiten(new ArrayList<>());
        wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2025");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart2");
        wohneinheitenProFoerderartProJahr.setWohneinheiten(BigDecimal.valueOf(888));
        bauratendateiInput.getWohneinheiten().add(wohneinheitenProFoerderartProJahr);
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
    void equalsMapForWohneinheitenOfJahrFoerderart() {
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
        basis.put("4", BigDecimal.ONE.movePointRight(2));
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
        inputs.put("4", BigDecimal.valueOf(10000, 2));
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
        final var wohneinheitenProFoerderartProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr.setJahr("2024");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart1");
        var result = bauratendateiInputService.concatJahrAndFoerderart(wohneinheitenProFoerderartProJahr);
        assertThat(result, is("2024förderart1"));

        wohneinheitenProFoerderartProJahr.setJahr("2024");
        wohneinheitenProFoerderartProJahr.setFoerderart("");
        result = bauratendateiInputService.concatJahrAndFoerderart(wohneinheitenProFoerderartProJahr);
        assertThat(result, is("2024"));

        wohneinheitenProFoerderartProJahr.setJahr("2024");
        wohneinheitenProFoerderartProJahr.setFoerderart(null);
        result = bauratendateiInputService.concatJahrAndFoerderart(wohneinheitenProFoerderartProJahr);
        assertThat(result, is("2024null"));

        wohneinheitenProFoerderartProJahr.setJahr("");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart1");
        result = bauratendateiInputService.concatJahrAndFoerderart(wohneinheitenProFoerderartProJahr);
        assertThat(result, is("förderart1"));

        wohneinheitenProFoerderartProJahr.setJahr("null");
        wohneinheitenProFoerderartProJahr.setFoerderart("förderart1");
        result = bauratendateiInputService.concatJahrAndFoerderart(wohneinheitenProFoerderartProJahr);
        assertThat(result, is("nullförderart1"));

        wohneinheitenProFoerderartProJahr.setJahr("");
        wohneinheitenProFoerderartProJahr.setFoerderart("");
        result = bauratendateiInputService.concatJahrAndFoerderart(wohneinheitenProFoerderartProJahr);
        assertThat(result, is(""));

        wohneinheitenProFoerderartProJahr.setJahr(null);
        wohneinheitenProFoerderartProJahr.setFoerderart(null);
        result = bauratendateiInputService.concatJahrAndFoerderart(wohneinheitenProFoerderartProJahr);
        assertThat(result, is("nullnull"));
    }
}
