package de.muenchen.isi.infrastructure.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import de.muenchen.isi.infrastructure.csv.SobonOrientierungswertCsv;
import de.muenchen.isi.infrastructure.csv.StaedtebaulicheOrientierungswertCsv;
import de.muenchen.isi.infrastructure.entity.enums.Altersklasse;
import de.muenchen.isi.infrastructure.entity.enums.Einrichtungstyp;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CsvRepositoryTest {

    private static final String NAME_CSV_TESTFILE_SOBON = "testfile_SoBoNOrientierungswerteSozialeInfrastruktur.csv";

    private static final String NAME_CSV_TESTFILE_SOBON_NOT_VALID =
        "testfile_SoBoNOrientierungswerteSozialeInfrastrukturNotValid.csv";

    private static final String NAME_CSV_TESTFILE_SOBON_EMPTY =
        "testfile_SoBoNOrientierungswerteSozialeInfrastrukturEmpty.csv";

    private static final String NAME_CSV_TESTFILE_SO = "testfile_StaedtebaulicheOrientierungswerte.csv";

    private static final String NAME_CSV_TESTFILE_SO_NOT_VALID =
        "testfile_StaedtebaulicheOrientierungswerteNotValid.csv";

    private static final String NAME_CSV_TESTFILE_SO_EMPTY = "testfile_StaedtebaulicheOrientierungswerteEmpty.csv";
    private final CsvRepository csvRepository = new CsvRepository();
    private InputStreamReader testfileSobon;
    private InputStreamReader testfileSobonNotValid;
    private InputStreamReader testfileSobonEmpty;
    private InputStreamReader testfileSo;
    private InputStreamReader testfileSoNotValid;
    private InputStreamReader testfileSoEmpty;

    @BeforeEach
    void beforeEach() throws IOException {
        Resource resource = new ClassPathResource(NAME_CSV_TESTFILE_SOBON);
        this.testfileSobon = new InputStreamReader(resource.getInputStream());
        resource = new ClassPathResource(NAME_CSV_TESTFILE_SOBON_NOT_VALID);
        this.testfileSobonNotValid = new InputStreamReader(resource.getInputStream());
        resource = new ClassPathResource(NAME_CSV_TESTFILE_SOBON_EMPTY);
        this.testfileSobonEmpty = new InputStreamReader(resource.getInputStream());

        resource = new ClassPathResource(NAME_CSV_TESTFILE_SO);
        this.testfileSo = new InputStreamReader(resource.getInputStream());
        resource = new ClassPathResource(NAME_CSV_TESTFILE_SO_NOT_VALID);
        this.testfileSoNotValid = new InputStreamReader(resource.getInputStream());
        resource = new ClassPathResource(NAME_CSV_TESTFILE_SO_EMPTY);
        this.testfileSoEmpty = new InputStreamReader(resource.getInputStream());
    }

    @Test
    void readAllStaedtebaulicheOrientierungswertCsv()
        throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        final List<StaedtebaulicheOrientierungswertCsv> resultList =
            this.csvRepository.readAllStaedtebaulicheOrientierungswertCsv(this.testfileSo);

        final var firstExpected = new StaedtebaulicheOrientierungswertCsv();
        firstExpected.setFoerderArt("GW-freifinanziert");
        firstExpected.setDurchschnittlicheGrundflaeche(90L);
        firstExpected.setBelegungsdichte(BigDecimal.valueOf(210, 2));

        final var lastExpected = new StaedtebaulicheOrientierungswertCsv();
        lastExpected.setFoerderArt("GW-freifinanziert");
        lastExpected.setDurchschnittlicheGrundflaeche(150L);
        lastExpected.setBelegungsdichte(BigDecimal.valueOf(348, 2));

        assertThat(resultList.size(), is(5));
        assertThat(resultList.get(0), is(firstExpected));
        assertThat(resultList.get(resultList.size() - 1), is(lastExpected));
    }

    @Test
    void readAllStaedtebaulicheOrientierungswertCsvNotValid() {
        Assertions.assertThrows(
            CsvDataTypeMismatchException.class,
            () -> this.csvRepository.readAllStaedtebaulicheOrientierungswertCsv(this.testfileSoNotValid)
        );
    }

    @Test
    void readAllStaedtebaulicheOrientierungswertCsvEmpty() {
        Assertions.assertThrows(
            CsvRequiredFieldEmptyException.class,
            () -> this.csvRepository.readAllStaedtebaulicheOrientierungswertCsv(this.testfileSoEmpty)
        );
    }

    @Test
    void readAllSobonOrientierungswertSozialeInfrastrukturCsv()
        throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        final List<SobonOrientierungswertCsv> resultList =
            this.csvRepository.readAllSobonOrientierungswertSozialeInfrastrukturCsv(this.testfileSobon);

        final var firstExpected = new SobonOrientierungswertCsv();
        firstExpected.setEinrichtungstyp(Einrichtungstyp.KINDERKRIPPE);
        firstExpected.setAltersklasse(Altersklasse.NULL_ZWEI);
        firstExpected.setFoerderArt("1-2-FH");
        firstExpected.setEinwohnerJahr1NachErsterstellung(BigDecimal.valueOf(2877, 4));
        firstExpected.setEinwohnerJahr2NachErsterstellung(BigDecimal.valueOf(2610, 4));
        firstExpected.setEinwohnerJahr3NachErsterstellung(BigDecimal.valueOf(2118, 4));
        firstExpected.setEinwohnerJahr4NachErsterstellung(BigDecimal.valueOf(1725, 4));
        firstExpected.setEinwohnerJahr5NachErsterstellung(BigDecimal.valueOf(1436, 4));
        firstExpected.setEinwohnerJahr6NachErsterstellung(BigDecimal.valueOf(1240, 4));
        firstExpected.setEinwohnerJahr7NachErsterstellung(BigDecimal.valueOf(1060, 4));
        firstExpected.setEinwohnerJahr8NachErsterstellung(BigDecimal.valueOf(850, 4));
        firstExpected.setEinwohnerJahr9NachErsterstellung(BigDecimal.valueOf(776, 4));
        firstExpected.setEinwohnerJahr10NachErsterstellung(BigDecimal.valueOf(716, 4));
        firstExpected.setMittelwertEinwohnerJeWohnung(BigDecimal.valueOf(1541, 4));
        firstExpected.setFaktor1EinwohnerJeWohnung(BigDecimal.valueOf(2569, 4));
        firstExpected.setFaktorEinwohnerJeWohnung(BigDecimal.valueOf(12569, 4));
        firstExpected.setPerzentil75ProzentEinwohnerJeWohnung(BigDecimal.valueOf(1937, 4));
        firstExpected.setPerzentil75ProzentGerundetEinwohnerJeWohnung(BigDecimal.valueOf(19, 2));

        final var lastExpected = new SobonOrientierungswertCsv();
        lastExpected.setEinrichtungstyp(Einrichtungstyp.N_N);
        lastExpected.setAltersklasse(Altersklasse.ALLE_EWO);
        lastExpected.setFoerderArt("1-2-FH");
        lastExpected.setEinwohnerJahr1NachErsterstellung(BigDecimal.valueOf(19188, 4));
        lastExpected.setEinwohnerJahr2NachErsterstellung(BigDecimal.valueOf(20447, 4));
        lastExpected.setEinwohnerJahr3NachErsterstellung(BigDecimal.valueOf(21063, 4));
        lastExpected.setEinwohnerJahr4NachErsterstellung(BigDecimal.valueOf(21379, 4));
        lastExpected.setEinwohnerJahr5NachErsterstellung(BigDecimal.valueOf(21478, 4));
        lastExpected.setEinwohnerJahr6NachErsterstellung(BigDecimal.valueOf(21448, 4));
        lastExpected.setEinwohnerJahr7NachErsterstellung(BigDecimal.valueOf(21495, 4));
        lastExpected.setEinwohnerJahr8NachErsterstellung(BigDecimal.valueOf(21416, 4));
        lastExpected.setEinwohnerJahr9NachErsterstellung(BigDecimal.valueOf(21326, 4));
        lastExpected.setEinwohnerJahr10NachErsterstellung(BigDecimal.valueOf(21237, 4));
        lastExpected.setMittelwertEinwohnerJeWohnung(BigDecimal.valueOf(21048, 4));
        lastExpected.setFaktor1EinwohnerJeWohnung(BigDecimal.valueOf(1093, 4));
        lastExpected.setFaktorEinwohnerJeWohnung(BigDecimal.valueOf(11093, 4));
        lastExpected.setPerzentil75ProzentEinwohnerJeWohnung(BigDecimal.valueOf(23349, 4));
        lastExpected.setPerzentil75ProzentGerundetEinwohnerJeWohnung(BigDecimal.valueOf(233, 2));

        assertThat(resultList.size(), is(35));
        assertThat(resultList.get(0), is(firstExpected));
        assertThat(resultList.get(resultList.size() - 1), is(lastExpected));
    }

    @Test
    void readAllSobonOrientierungswertSozialeInfrastrukturCsvEmpty() {
        Assertions.assertThrows(
            CsvRequiredFieldEmptyException.class,
            () -> this.csvRepository.readAllSobonOrientierungswertSozialeInfrastrukturCsv(this.testfileSobonEmpty)
        );
    }

    @Test
    void readAllSobonOrientierungswertSozialeInfrastrukturCsvNotValid() {
        Assertions.assertThrows(
            CsvDataTypeMismatchException.class,
            () -> this.csvRepository.readAllSobonOrientierungswertSozialeInfrastrukturCsv(this.testfileSobonNotValid)
        );
    }
}
