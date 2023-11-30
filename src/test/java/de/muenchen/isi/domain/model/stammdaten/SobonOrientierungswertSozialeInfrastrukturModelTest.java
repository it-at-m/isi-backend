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
        final var expected = BigDecimal.valueOf(3275, SobonOrientierungswertSozialeInfrastrukturModel.SCALE);
        assertThat(result, is(expected));
    }

    @Test
    void getFactorOfDistanceMittelwertEinwohner10JahreToStammwertArbeitsgruppe() {
        final var result = model.getFactorOfDistanceMittelwertEinwohner10JahreToStammwertArbeitsgruppe();
        final var expected = BigDecimal.valueOf(13740, SobonOrientierungswertSozialeInfrastrukturModel.SCALE);
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr1NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr1NachErsterstellung();
        final var expected = BigDecimal.valueOf(4809, SobonOrientierungswertSozialeInfrastrukturModel.SCALE);
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr2NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr2NachErsterstellung();
        final var expected = BigDecimal.valueOf(4740, SobonOrientierungswertSozialeInfrastrukturModel.SCALE);
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr3NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr3NachErsterstellung();
        final var expected = BigDecimal.valueOf(4672, SobonOrientierungswertSozialeInfrastrukturModel.SCALE);
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr4NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr4NachErsterstellung();
        final var expected = BigDecimal.valueOf(4603, SobonOrientierungswertSozialeInfrastrukturModel.SCALE);
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr5NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr5NachErsterstellung();
        final var expected = BigDecimal.valueOf(4534, SobonOrientierungswertSozialeInfrastrukturModel.SCALE);
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr6NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr6NachErsterstellung();
        final var expected = BigDecimal.valueOf(4466, SobonOrientierungswertSozialeInfrastrukturModel.SCALE);
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr7NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr7NachErsterstellung();
        final var expected = BigDecimal.valueOf(4397, SobonOrientierungswertSozialeInfrastrukturModel.SCALE);
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr8NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr8NachErsterstellung();
        final var expected = BigDecimal.valueOf(4328, SobonOrientierungswertSozialeInfrastrukturModel.SCALE);
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr9NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr9NachErsterstellung();
        final var expected = BigDecimal.valueOf(4259, SobonOrientierungswertSozialeInfrastrukturModel.SCALE);
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr10NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr10NachErsterstellung();
        final var expected = BigDecimal.valueOf(4191, SobonOrientierungswertSozialeInfrastrukturModel.SCALE);
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr11NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr11NachErsterstellung();
        final var expected = BigDecimal.valueOf(4149, SobonOrientierungswertSozialeInfrastrukturModel.SCALE);
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr12NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr12NachErsterstellung();
        final var expected = BigDecimal.valueOf(4108, SobonOrientierungswertSozialeInfrastrukturModel.SCALE);
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr13NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr13NachErsterstellung();
        final var expected = BigDecimal.valueOf(4067, SobonOrientierungswertSozialeInfrastrukturModel.SCALE);
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr14NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr14NachErsterstellung();
        final var expected = BigDecimal.valueOf(4026, SobonOrientierungswertSozialeInfrastrukturModel.SCALE);
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr15NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr15NachErsterstellung();
        final var expected = BigDecimal.valueOf(3986, SobonOrientierungswertSozialeInfrastrukturModel.SCALE);
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr16NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr16NachErsterstellung();
        final var expected = BigDecimal.valueOf(3946, SobonOrientierungswertSozialeInfrastrukturModel.SCALE);
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr17NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr17NachErsterstellung();
        final var expected = BigDecimal.valueOf(3906, SobonOrientierungswertSozialeInfrastrukturModel.SCALE);
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr18NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr18NachErsterstellung();
        final var expected = BigDecimal.valueOf(3867, SobonOrientierungswertSozialeInfrastrukturModel.SCALE);
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr19NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr19NachErsterstellung();
        final var expected = BigDecimal.valueOf(3829, SobonOrientierungswertSozialeInfrastrukturModel.SCALE);
        assertThat(result, is(expected));
    }

    @Test
    void getObererRichtwertEinwohnerJahr20NachErsterstellung() {
        final var result = model.getObererRichtwertEinwohnerJahr20NachErsterstellung();
        final var expected = BigDecimal.valueOf(3790, SobonOrientierungswertSozialeInfrastrukturModel.SCALE);
        assertThat(result, is(expected));
    }
}
