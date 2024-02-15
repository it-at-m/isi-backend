package de.muenchen.isi.infrastructure.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import de.muenchen.isi.infrastructure.csv.PrognosedatenKitaPlbCsv;
import de.muenchen.isi.infrastructure.csv.SobonOrientierungswertSozialeInfrastrukturCsv;
import de.muenchen.isi.infrastructure.csv.StaedtebaulicheOrientierungswertCsv;
import de.muenchen.isi.infrastructure.entity.enums.Altersklasse;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
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

    private static final String NAME_CSV_TESTFILE_PROGNOSEDATEN_KITA_PLB = "testfile_PrognosedatenKitaPlb.csv";

    private static final String NAME_CSV_TESTFILE_PROGNOSEDATEN_KITA_PLB_NOT_VALID =
        "testfile_PrognosedatenKitaPlbNotValid.csv";

    private static final String NAME_CSV_TESTFILE_PROGNOSEDATEN_KITA_PLB_EMTPY =
        "testfile_PrognosedatenKitaPlbEmpty.csv";

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

    private InputStreamReader testFilePrognosedatenKitaPlb;
    private InputStreamReader testFilePrognosedatenKitaPlbNotValid;
    private InputStreamReader testFilePrognosedatenKitaPlbEmpty;
    private InputStreamReader testfileSobon;
    private InputStreamReader testfileSobonNotValid;
    private InputStreamReader testfileSobonEmpty;
    private InputStreamReader testfileSo;
    private InputStreamReader testfileSoNotValid;
    private InputStreamReader testfileSoEmpty;

    @BeforeEach
    void beforeEach() throws IOException {
        Resource resource = new ClassPathResource(NAME_CSV_TESTFILE_PROGNOSEDATEN_KITA_PLB);
        this.testFilePrognosedatenKitaPlb = new InputStreamReader(resource.getInputStream());
        resource = new ClassPathResource(NAME_CSV_TESTFILE_PROGNOSEDATEN_KITA_PLB_NOT_VALID);
        this.testFilePrognosedatenKitaPlbNotValid = new InputStreamReader(resource.getInputStream());
        resource = new ClassPathResource(NAME_CSV_TESTFILE_PROGNOSEDATEN_KITA_PLB_EMTPY);
        this.testFilePrognosedatenKitaPlbEmpty = new InputStreamReader(resource.getInputStream());

        resource = new ClassPathResource(NAME_CSV_TESTFILE_SOBON);
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
    void readAllPrognosedatenKitaPlbCsv() throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        final List<PrognosedatenKitaPlbCsv> resultList =
            this.csvRepository.readAllPrognosedatenKitaPlbCsv(this.testFilePrognosedatenKitaPlb);

        final var firstExpected = new PrognosedatenKitaPlbCsv();
        firstExpected.setBerichtsstand(LocalDate.parse("2023-12-01"));
        firstExpected.setKitaPlb(5L);
        firstExpected.setAnzahlNullBisZweiJaehrige(BigDecimal.valueOf(11111, 2));
        firstExpected.setAnzahlDreiBisFuenfJaehrigeUndFuenfzigProzentSechsJaehrige(BigDecimal.valueOf(22222, 2));

        final var lastExpected = new PrognosedatenKitaPlbCsv();
        lastExpected.setBerichtsstand(LocalDate.parse("2023-12-01"));
        lastExpected.setKitaPlb(30L);
        lastExpected.setAnzahlNullBisZweiJaehrige(BigDecimal.valueOf(20));
        lastExpected.setAnzahlDreiBisFuenfJaehrigeUndFuenfzigProzentSechsJaehrige(BigDecimal.valueOf(1));

        assertThat(resultList.size(), is(2));
        assertThat(resultList, is(List.of(firstExpected, lastExpected)));
    }

    @Test
    void readAllPrognosedatenKitaPlbCsvNotValid() {
        Assertions.assertThrows(
            CsvDataTypeMismatchException.class,
            () -> this.csvRepository.readAllPrognosedatenKitaPlbCsv(this.testFilePrognosedatenKitaPlbNotValid)
        );
    }

    @Test
    void readAllPrognosedatenKitaPlbCsvEmpty() {
        Assertions.assertThrows(
            CsvRequiredFieldEmptyException.class,
            () -> this.csvRepository.readAllPrognosedatenKitaPlbCsv(this.testFilePrognosedatenKitaPlbEmpty)
        );
    }

    @Test
    void readAllStaedtebaulicheOrientierungswertCsv()
        throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        final List<StaedtebaulicheOrientierungswertCsv> resultList =
            this.csvRepository.readAllStaedtebaulicheOrientierungswertCsv(this.testfileSo);

        final var firstExpected = new StaedtebaulicheOrientierungswertCsv();
        firstExpected.setGueltigAb(LocalDate.parse("2021-01-01"));
        firstExpected.setFoerderartBezeichnung("GW-freifinanziert");
        firstExpected.setDurchschnittlicheGrundflaeche(90L);
        firstExpected.setBelegungsdichte(BigDecimal.valueOf(210, 2));

        final var lastExpected = new StaedtebaulicheOrientierungswertCsv();
        lastExpected.setGueltigAb(LocalDate.parse("2021-01-01"));
        lastExpected.setFoerderartBezeichnung("1-2-FH");
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
        final List<SobonOrientierungswertSozialeInfrastrukturCsv> resultList =
            this.csvRepository.readAllSobonOrientierungswertSozialeInfrastrukturCsv(this.testfileSobon);

        final var firstExpected = new SobonOrientierungswertSozialeInfrastrukturCsv();
        firstExpected.setGueltigAb(LocalDate.parse("2021-01-01"));
        firstExpected.setEinrichtungstyp(InfrastruktureinrichtungTyp.KINDERKRIPPE);
        firstExpected.setAltersklasse(Altersklasse.NULL_ZWEI);
        firstExpected.setFoerderartBezeichnung("1-2-FH");
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
        firstExpected.setStammwertArbeitsgruppe(BigDecimal.valueOf(716, 4));
        final var lastExpected = new SobonOrientierungswertSozialeInfrastrukturCsv();
        lastExpected.setGueltigAb(LocalDate.parse("2021-01-01"));
        lastExpected.setEinrichtungstyp(InfrastruktureinrichtungTyp.UNSPECIFIED);
        lastExpected.setAltersklasse(Altersklasse.ALLE_EWO);
        lastExpected.setFoerderartBezeichnung("GW-freifinanziert");
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
        lastExpected.setStammwertArbeitsgruppe(BigDecimal.valueOf(21237, 4));

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
