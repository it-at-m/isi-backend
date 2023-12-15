package de.muenchen.isi.domain.model.stammdaten;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.TestData;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class SobonOrientierungswertSozialeInfrastrukturModelTest {

    private final SobonOrientierungswertSozialeInfrastrukturModel model =
        TestData.createSobonOrientierungswertSozialeInfrastrukturModel();

    @Test
    void getMittelwertEinwohnerNachErsterstellung10Jahre() {
        final var result = model.getMittelwertEinwohnerNachErsterstellung10Jahre();
        final var expected = BigDecimal.valueOf(
            327500000000000L,
            SobonOrientierungswertSozialeInfrastrukturModel.SCALE
        );
        assertThat(result, is(expected));
    }

    @Test
    void getRatioOfMittelwertEinwohner10JahreToStammwertArbeitsgruppe() {
        final var result = model.getRatioOfMittelwertEinwohner10JahreToStammwertArbeitsgruppe();
        final var expected = BigDecimal.valueOf(
            1374045801526718L,
            SobonOrientierungswertSozialeInfrastrukturModel.SCALE
        );
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr1NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr1NachErsterstellung();
        final var expected = BigDecimal.valueOf(
            480916030534351L,
            SobonOrientierungswertSozialeInfrastrukturModel.SCALE
        );
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr2NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr2NachErsterstellung();
        final var expected = BigDecimal.valueOf(
            474045801526718L,
            SobonOrientierungswertSozialeInfrastrukturModel.SCALE
        );
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr3NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr3NachErsterstellung();
        final var expected = BigDecimal.valueOf(
            467175572519084L,
            SobonOrientierungswertSozialeInfrastrukturModel.SCALE
        );
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr4NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr4NachErsterstellung();
        final var expected = BigDecimal.valueOf(
            460305343511451L,
            SobonOrientierungswertSozialeInfrastrukturModel.SCALE
        );
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr5NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr5NachErsterstellung();
        final var expected = BigDecimal.valueOf(
            453435114503817L,
            SobonOrientierungswertSozialeInfrastrukturModel.SCALE
        );
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr6NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr6NachErsterstellung();
        final var expected = BigDecimal.valueOf(
            446564885496183L,
            SobonOrientierungswertSozialeInfrastrukturModel.SCALE
        );
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr7NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr7NachErsterstellung();
        final var expected = BigDecimal.valueOf(
            439694656488550L,
            SobonOrientierungswertSozialeInfrastrukturModel.SCALE
        );
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr8NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr8NachErsterstellung();
        final var expected = BigDecimal.valueOf(
            432824427480916L,
            SobonOrientierungswertSozialeInfrastrukturModel.SCALE
        );
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr9NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr9NachErsterstellung();
        final var expected = BigDecimal.valueOf(
            425954198473283L,
            SobonOrientierungswertSozialeInfrastrukturModel.SCALE
        );
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr10NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr10NachErsterstellung();
        final var expected = BigDecimal.valueOf(
            419083969465649L,
            SobonOrientierungswertSozialeInfrastrukturModel.SCALE
        );
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr11NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr11NachErsterstellung();
        final var expected = BigDecimal.valueOf(
            414893129770993L,
            SobonOrientierungswertSozialeInfrastrukturModel.SCALE
        );
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr12NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr12NachErsterstellung();
        final var expected = BigDecimal.valueOf(
            410744198473283L,
            SobonOrientierungswertSozialeInfrastrukturModel.SCALE
        );
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr13NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr13NachErsterstellung();
        final var expected = BigDecimal.valueOf(
            406636756488550L,
            SobonOrientierungswertSozialeInfrastrukturModel.SCALE
        );
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr14NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr14NachErsterstellung();
        final var expected = BigDecimal.valueOf(
            402570388923664L,
            SobonOrientierungswertSozialeInfrastrukturModel.SCALE
        );
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr15NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr15NachErsterstellung();
        final var expected = BigDecimal.valueOf(
            398544685034428L,
            SobonOrientierungswertSozialeInfrastrukturModel.SCALE
        );
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr16NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr16NachErsterstellung();
        final var expected = BigDecimal.valueOf(
            394559238184083L,
            SobonOrientierungswertSozialeInfrastrukturModel.SCALE
        );
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr17NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr17NachErsterstellung();
        final var expected = BigDecimal.valueOf(
            390613645802243L,
            SobonOrientierungswertSozialeInfrastrukturModel.SCALE
        );
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr18NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr18NachErsterstellung();
        final var expected = BigDecimal.valueOf(
            386707509344220L,
            SobonOrientierungswertSozialeInfrastrukturModel.SCALE
        );
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr19NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr19NachErsterstellung();
        final var expected = BigDecimal.valueOf(
            382840434250778L,
            SobonOrientierungswertSozialeInfrastrukturModel.SCALE
        );
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr20NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr20NachErsterstellung();
        final var expected = BigDecimal.valueOf(
            379012029908270L,
            SobonOrientierungswertSozialeInfrastrukturModel.SCALE
        );
        assertThat(result, is(expected));
    }
}
