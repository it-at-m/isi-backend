package de.muenchen.isi.domain.service.calculation;

import de.muenchen.isi.domain.model.AbfragevarianteModel;
import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.domain.model.BaugebietModel;
import de.muenchen.isi.domain.model.BaurateModel;
import de.muenchen.isi.domain.model.calculation.RealisierungsZeitraumModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenInformationModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class WohneinheitenInformationServiceTest {

    private final WohneinheitenInformationService wohneinheitenInformationService = new WohneinheitenInformationService();

    @Test
    void calculateWohneinheitenInformationForAbfragevariante() {
        var abfragevariante = new AbfragevarianteModel();

        var result = this.wohneinheitenInformationService.calculateWohneinheitenInformation(abfragevariante);

        var expected = this.wohneinheitenInformationService.initEmptyWohneinheitenInformationModel();

        assertThat(
                result,
                is(expected)
        );

        // --

        abfragevariante = new AbfragevarianteModel();

        final var bauabschnitt1 = new BauabschnittModel();

        final var baugebiet1 = new BaugebietModel();
        final var baurate1 = new BaurateModel();
        baurate1.setJahr(2020);
        baurate1.setAnzahlWeGeplant(5);
        baurate1.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(50));
        baurate1.setFoerdermix(null);
        final var baurate2 = new BaurateModel();
        baurate2.setJahr(2022);
        baurate2.setAnzahlWeGeplant(4);
        baurate2.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(40));
        baurate2.setFoerdermix(null);
        baugebiet1.setBauraten(List.of(baurate1, baurate2));

        final var baugebiet2 = new BaugebietModel();
        final var baurate3 = new BaurateModel();
        baurate3.setJahr(2021);
        baurate3.setAnzahlWeGeplant(3);
        baurate3.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(30));
        baurate3.setFoerdermix(null);
        final var baurate4 = new BaurateModel();
        baurate4.setJahr(null);
        baurate4.setAnzahlWeGeplant(null);
        baurate4.setGeschossflaecheWohnenGeplant(null);
        baurate4.setFoerdermix(null);
        baugebiet2.setBauraten(List.of(baurate3, baurate4));

        bauabschnitt1.setBaugebiete(List.of(baugebiet1, baugebiet2));

        final var bauabschnitt2 = new BauabschnittModel();

        final var baugebiet3 = new BaugebietModel();
        final var baurate5 = new BaurateModel();
        baurate5.setJahr(null);
        baurate5.setAnzahlWeGeplant(2);
        baurate5.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(20));
        baurate5.setFoerdermix(null);

        baugebiet3.setBauraten(List.of(baurate5));

        bauabschnitt2.setBaugebiete(List.of(baugebiet3));

        abfragevariante.setBauabschnitte(List.of(bauabschnitt1, bauabschnitt2));

        result = this.wohneinheitenInformationService.calculateWohneinheitenInformation(abfragevariante);

        expected = this.wohneinheitenInformationService.initEmptyWohneinheitenInformationModel();
        expected.getRealisierungsZeitraum().setRealisierungVon(2020);
        expected.getRealisierungsZeitraum().setRealisierungBis(2022);
        expected.setAnzahlWohneinheitenGeplant(14);
        expected.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(140));

        assertThat(
                result,
                is(expected)
        );

    }

    @Test
    void calculateWohneinheitenInformationForBauabschnitt() {
        var bauabschnitt = new BauabschnittModel();

        var result = this.wohneinheitenInformationService.calculateWohneinheitenInformation(bauabschnitt);

        var expected = this.wohneinheitenInformationService.initEmptyWohneinheitenInformationModel();

        assertThat(
                result,
                is(expected)
        );

        // --

        bauabschnitt = new BauabschnittModel();

        final var baugebiet1 = new BaugebietModel();
        final var baurate1 = new BaurateModel();
        baurate1.setJahr(2020);
        baurate1.setAnzahlWeGeplant(5);
        baurate1.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(50));
        baurate1.setFoerdermix(null);
        final var baurate2 = new BaurateModel();
        baurate2.setJahr(2022);
        baurate2.setAnzahlWeGeplant(4);
        baurate2.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(40));
        baurate2.setFoerdermix(null);
        baugebiet1.setBauraten(List.of(baurate1, baurate2));

        final var baugebiet2 = new BaugebietModel();
        final var baurate3 = new BaurateModel();
        baurate3.setJahr(2021);
        baurate3.setAnzahlWeGeplant(3);
        baurate3.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(30));
        baurate3.setFoerdermix(null);
        final var baurate4 = new BaurateModel();
        baurate4.setJahr(null);
        baurate4.setAnzahlWeGeplant(2);
        baurate4.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(20));
        baurate4.setFoerdermix(null);
        baugebiet2.setBauraten(List.of(baurate3, baurate4));

        final var baugebiet3 = new BaugebietModel();
        final var baurate5 = new BaurateModel();
        baurate5.setJahr(null);
        baurate5.setAnzahlWeGeplant(null);
        baurate5.setGeschossflaecheWohnenGeplant(null);
        baurate5.setFoerdermix(null);
        baugebiet3.setBauraten(List.of(baurate5));

        bauabschnitt.setBaugebiete(List.of(baugebiet1, baugebiet2, baugebiet3));

        result = this.wohneinheitenInformationService.calculateWohneinheitenInformation(bauabschnitt);

        expected = this.wohneinheitenInformationService.initEmptyWohneinheitenInformationModel();
        expected.getRealisierungsZeitraum().setRealisierungVon(2020);
        expected.getRealisierungsZeitraum().setRealisierungBis(2022);
        expected.setAnzahlWohneinheitenGeplant(14);
        expected.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(140));

        assertThat(
                result,
                is(expected)
        );
    }

    @Test
    void calculateWohneinheitenInformationForBaugebiet() {
        var baugebiet = new BaugebietModel();

        var result = this.wohneinheitenInformationService.calculateWohneinheitenInformation(baugebiet);

        var expected = this.wohneinheitenInformationService.initEmptyWohneinheitenInformationModel();

        assertThat(
                result,
                is(expected)
        );

        // --

        baugebiet = new BaugebietModel();
        final var baurate1 = new BaurateModel();
        baurate1.setJahr(2021);
        baurate1.setAnzahlWeGeplant(5);
        baurate1.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(50));
        baurate1.setFoerdermix(null);
        final var baurate2 = new BaurateModel();
        baurate2.setJahr(2022);
        baurate2.setAnzahlWeGeplant(4);
        baurate2.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(40));
        baurate2.setFoerdermix(null);
        final var baurate3 = new BaurateModel();
        baurate3.setJahr(2020);
        baurate3.setAnzahlWeGeplant(3);
        baurate3.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(30));
        baurate3.setFoerdermix(null);
        final var baurate4 = new BaurateModel();
        baurate4.setJahr(null);
        baurate4.setAnzahlWeGeplant(2);
        baurate4.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(20));
        baurate4.setFoerdermix(null);
        final var baurate5 = new BaurateModel();
        baurate5.setJahr(null);
        baurate5.setAnzahlWeGeplant(null);
        baurate5.setGeschossflaecheWohnenGeplant(null);
        baurate5.setFoerdermix(null);
        baugebiet.setBauraten(List.of(baurate1, baurate2, baurate3, baurate4, baurate5));

        result = this.wohneinheitenInformationService.calculateWohneinheitenInformation(baugebiet);

        expected = this.wohneinheitenInformationService.initEmptyWohneinheitenInformationModel();
        expected.getRealisierungsZeitraum().setRealisierungVon(2020);
        expected.getRealisierungsZeitraum().setRealisierungBis(2022);
        expected.setAnzahlWohneinheitenGeplant(14);
        expected.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(140));

        assertThat(
                result,
                is(expected)
        );
    }

    @Test
    void calculateWohneinheitenInformationForBaurate() {
        var baurate = new BaurateModel();
        baurate.setJahr(2021);
        baurate.setAnzahlWeGeplant(5);
        baurate.setGeschossflaecheWohnenGeplant(BigDecimal.TEN);
        baurate.setFoerdermix(null);

        var result = this.wohneinheitenInformationService.calculateWohneinheitenInformation(baurate);

        var expected = this.wohneinheitenInformationService.initEmptyWohneinheitenInformationModel();
        expected.getRealisierungsZeitraum().setRealisierungVon(2021);
        expected.getRealisierungsZeitraum().setRealisierungBis(2021);
        expected.setAnzahlWohneinheitenGeplant(5);
        expected.setGeschossflaecheWohnenGeplant(BigDecimal.TEN);

        assertThat(
                result,
                is(expected)
        );

        // --

        baurate = new BaurateModel();
        baurate.setJahr(null);
        baurate.setAnzahlWeGeplant(5);
        baurate.setGeschossflaecheWohnenGeplant(null);
        baurate.setFoerdermix(null);

        result = this.wohneinheitenInformationService.calculateWohneinheitenInformation(baurate);

        expected = this.wohneinheitenInformationService.initEmptyWohneinheitenInformationModel();
        expected.getRealisierungsZeitraum().setRealisierungVon(null);
        expected.getRealisierungsZeitraum().setRealisierungBis(null);
        expected.setAnzahlWohneinheitenGeplant(5);
        expected.setGeschossflaecheWohnenGeplant(null);

        assertThat(
                result,
                is(expected)
        );

        // --

        baurate = new BaurateModel();
        baurate.setJahr(2032);
        baurate.setAnzahlWeGeplant(null);
        baurate.setGeschossflaecheWohnenGeplant(BigDecimal.TEN);
        baurate.setFoerdermix(null);

        result = this.wohneinheitenInformationService.calculateWohneinheitenInformation(baurate);

        expected = this.wohneinheitenInformationService.initEmptyWohneinheitenInformationModel();
        expected.getRealisierungsZeitraum().setRealisierungVon(2032);
        expected.getRealisierungsZeitraum().setRealisierungBis(2032);
        expected.setAnzahlWohneinheitenGeplant(null);
        expected.setGeschossflaecheWohnenGeplant(BigDecimal.TEN);

        assertThat(
                result,
                is(expected)
        );
    }

    @Test
    void sum() {
        var wohneinheitenInformation1 = this.wohneinheitenInformationService.initEmptyWohneinheitenInformationModel();
        var wohneinheitenInformation2 = this.wohneinheitenInformationService.initEmptyWohneinheitenInformationModel();

        var result = this.wohneinheitenInformationService.sum(
                wohneinheitenInformation1,
                wohneinheitenInformation2
        );

        var expected = this.wohneinheitenInformationService.initEmptyWohneinheitenInformationModel();
        expected.setAnzahlWohneinheitenGeplant(0);
        expected.setGeschossflaecheWohnenGeplant(BigDecimal.ZERO);

        assertThat(
                result,
                is(expected)
        );

        // --

        wohneinheitenInformation1 = this.wohneinheitenInformationService.initEmptyWohneinheitenInformationModel();
        wohneinheitenInformation1.getRealisierungsZeitraum().setRealisierungVon(1998);
        wohneinheitenInformation1.getRealisierungsZeitraum().setRealisierungBis(2008);
        wohneinheitenInformation1.setAnzahlWohneinheitenGeplant(5);
        wohneinheitenInformation1.setGeschossflaecheWohnenGeplant(BigDecimal.TEN);
        wohneinheitenInformation2 = this.wohneinheitenInformationService.initEmptyWohneinheitenInformationModel();
        wohneinheitenInformation2.getRealisierungsZeitraum().setRealisierungVon(null);
        wohneinheitenInformation2.getRealisierungsZeitraum().setRealisierungBis(null);
        wohneinheitenInformation2.setAnzahlWohneinheitenGeplant(null);
        wohneinheitenInformation2.setGeschossflaecheWohnenGeplant(null);

        result = this.wohneinheitenInformationService.sum(
                wohneinheitenInformation1,
                wohneinheitenInformation2
        );

        expected = this.wohneinheitenInformationService.initEmptyWohneinheitenInformationModel();
        expected.getRealisierungsZeitraum().setRealisierungVon(1998);
        expected.getRealisierungsZeitraum().setRealisierungBis(2008);
        expected.setAnzahlWohneinheitenGeplant(5);
        expected.setGeschossflaecheWohnenGeplant(BigDecimal.TEN);

        assertThat(
                result,
                is(expected)
        );

        // --

        wohneinheitenInformation1 = this.wohneinheitenInformationService.initEmptyWohneinheitenInformationModel();
        wohneinheitenInformation1.getRealisierungsZeitraum().setRealisierungVon(1998);
        wohneinheitenInformation1.getRealisierungsZeitraum().setRealisierungBis(2008);
        wohneinheitenInformation1.setAnzahlWohneinheitenGeplant(5);
        wohneinheitenInformation1.setGeschossflaecheWohnenGeplant(BigDecimal.TEN);
        wohneinheitenInformation2 = this.wohneinheitenInformationService.initEmptyWohneinheitenInformationModel();
        wohneinheitenInformation2.getRealisierungsZeitraum().setRealisierungVon(1988);
        wohneinheitenInformation2.getRealisierungsZeitraum().setRealisierungBis(2018);
        wohneinheitenInformation2.setAnzahlWohneinheitenGeplant(1);
        wohneinheitenInformation2.setGeschossflaecheWohnenGeplant(BigDecimal.ONE);

        result = this.wohneinheitenInformationService.sum(
                wohneinheitenInformation1,
                wohneinheitenInformation2
        );

        expected = this.wohneinheitenInformationService.initEmptyWohneinheitenInformationModel();
        expected.getRealisierungsZeitraum().setRealisierungVon(1988);
        expected.getRealisierungsZeitraum().setRealisierungBis(2018);
        expected.setAnzahlWohneinheitenGeplant(6);
        expected.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(11));

        assertThat(
                result,
                is(expected)
        );

        // --

        wohneinheitenInformation1 = this.wohneinheitenInformationService.initEmptyWohneinheitenInformationModel();
        wohneinheitenInformation1.getRealisierungsZeitraum().setRealisierungVon(1998);
        wohneinheitenInformation1.getRealisierungsZeitraum().setRealisierungBis(2008);
        wohneinheitenInformation1.setAnzahlWohneinheitenGeplant(-5);
        wohneinheitenInformation1.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(-11));
        wohneinheitenInformation2 = this.wohneinheitenInformationService.initEmptyWohneinheitenInformationModel();
        wohneinheitenInformation2.getRealisierungsZeitraum().setRealisierungVon(1999);
        wohneinheitenInformation2.getRealisierungsZeitraum().setRealisierungBis(2005);
        wohneinheitenInformation2.setAnzahlWohneinheitenGeplant(7);
        wohneinheitenInformation2.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(23));

        result = this.wohneinheitenInformationService.sum(
                wohneinheitenInformation1,
                wohneinheitenInformation2
        );

        expected = this.wohneinheitenInformationService.initEmptyWohneinheitenInformationModel();
        expected.getRealisierungsZeitraum().setRealisierungVon(1998);
        expected.getRealisierungsZeitraum().setRealisierungBis(2008);
        expected.setAnzahlWohneinheitenGeplant(2);
        expected.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(12));

        assertThat(
                result,
                is(expected)
        );

    }

    @Test
    void aggregate() {
        var realisierungsZeitraum1 = new RealisierungsZeitraumModel();
        realisierungsZeitraum1.setRealisierungVon(null);
        realisierungsZeitraum1.setRealisierungBis(null);
        var realisierungsZeitraum2 = new RealisierungsZeitraumModel();
        realisierungsZeitraum2.setRealisierungVon(null);
        realisierungsZeitraum2.setRealisierungBis(null);

        var result = this.wohneinheitenInformationService.aggregate(
                realisierungsZeitraum1,
                realisierungsZeitraum2
        );

        var expected = new RealisierungsZeitraumModel();
        expected.setRealisierungVon(null);
        expected.setRealisierungBis(null);

        assertThat(
                result,
                is(expected)
        );

        // ---

        realisierungsZeitraum1 = new RealisierungsZeitraumModel();
        realisierungsZeitraum1.setRealisierungVon(2021);
        realisierungsZeitraum1.setRealisierungBis(2031);
        realisierungsZeitraum2 = new RealisierungsZeitraumModel();
        realisierungsZeitraum2.setRealisierungVon(null);
        realisierungsZeitraum2.setRealisierungBis(null);

        result = this.wohneinheitenInformationService.aggregate(
                realisierungsZeitraum1,
                realisierungsZeitraum2
        );

        expected = new RealisierungsZeitraumModel();
        expected.setRealisierungVon(2021);
        expected.setRealisierungBis(2031);

        assertThat(
                result,
                is(expected)
        );

        // ---

        realisierungsZeitraum1 = new RealisierungsZeitraumModel();
        realisierungsZeitraum1.setRealisierungVon(2021);
        realisierungsZeitraum1.setRealisierungBis(2031);
        realisierungsZeitraum2 = new RealisierungsZeitraumModel();
        realisierungsZeitraum2.setRealisierungVon(1998);
        realisierungsZeitraum2.setRealisierungBis(1999);

        result = this.wohneinheitenInformationService.aggregate(
                realisierungsZeitraum1,
                realisierungsZeitraum2
        );

        expected = new RealisierungsZeitraumModel();
        expected.setRealisierungVon(1998);
        expected.setRealisierungBis(2031);

        assertThat(
                result,
                is(expected)
        );

        // ---

        realisierungsZeitraum1 = new RealisierungsZeitraumModel();
        realisierungsZeitraum1.setRealisierungVon(1998);
        realisierungsZeitraum1.setRealisierungBis(1999);
        realisierungsZeitraum2 = new RealisierungsZeitraumModel();
        realisierungsZeitraum2.setRealisierungVon(2021);
        realisierungsZeitraum2.setRealisierungBis(2031);

        result = this.wohneinheitenInformationService.aggregate(
                realisierungsZeitraum1,
                realisierungsZeitraum2
        );

        expected = new RealisierungsZeitraumModel();
        expected.setRealisierungVon(1998);
        expected.setRealisierungBis(2031);

        assertThat(
                result,
                is(expected)
        );

        // ---

        realisierungsZeitraum1 = new RealisierungsZeitraumModel();
        realisierungsZeitraum1.setRealisierungVon(2021);
        realisierungsZeitraum1.setRealisierungBis(2031);
        realisierungsZeitraum2 = new RealisierungsZeitraumModel();
        realisierungsZeitraum2.setRealisierungVon(2026);
        realisierungsZeitraum2.setRealisierungBis(2042);

        result = this.wohneinheitenInformationService.aggregate(
                realisierungsZeitraum1,
                realisierungsZeitraum2
        );

        expected = new RealisierungsZeitraumModel();
        expected.setRealisierungVon(2021);
        expected.setRealisierungBis(2042);

        assertThat(
                result,
                is(expected)
        );

    }

    @Test
    void initEmptyWohneinheitenInformationModel() {
        final var expected = new WohneinheitenInformationModel();
        expected.setRealisierungsZeitraum(new RealisierungsZeitraumModel());

        assertThat(
                this.wohneinheitenInformationService.initEmptyWohneinheitenInformationModel(),
                is(expected)
        );
    }

}
