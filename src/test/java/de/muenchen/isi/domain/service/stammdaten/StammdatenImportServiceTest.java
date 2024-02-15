package de.muenchen.isi.domain.service.stammdaten;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import de.muenchen.isi.domain.exception.CsvAttributeErrorException;
import de.muenchen.isi.domain.exception.FileImportFailedException;
import de.muenchen.isi.domain.mapper.StammdatenDomainMapper;
import de.muenchen.isi.domain.mapper.StammdatenDomainMapperImpl;
import de.muenchen.isi.infrastructure.csv.BerichtsdatenKitaPlbCsv;
import de.muenchen.isi.infrastructure.csv.SobonOrientierungswertSozialeInfrastrukturCsv;
import de.muenchen.isi.infrastructure.csv.StaedtebaulicheOrientierungswertCsv;
import de.muenchen.isi.infrastructure.entity.enums.Altersgruppe;
import de.muenchen.isi.infrastructure.entity.enums.Altersklasse;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.stammdaten.BerichtsdatenKitaPlb;
import de.muenchen.isi.infrastructure.entity.stammdaten.SobonOrientierungswertSozialeInfrastruktur;
import de.muenchen.isi.infrastructure.entity.stammdaten.StaedtebaulicheOrientierungswert;
import de.muenchen.isi.infrastructure.repository.CsvRepository;
import de.muenchen.isi.infrastructure.repository.stammdaten.BerichtsdatenKitaPlbRepository;
import de.muenchen.isi.infrastructure.repository.stammdaten.SobonOrientierungswertSozialeInfrastrukturRepository;
import de.muenchen.isi.infrastructure.repository.stammdaten.StaedtebaulicheOrientierungswertRepository;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StammdatenImportServiceTest {

    private final StammdatenDomainMapper stammdatenDomainMapper = new StammdatenDomainMapperImpl();

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private CsvRepository csvRepository;

    @Mock
    private StaedtebaulicheOrientierungswertRepository staedtebaulicheOrientierungswertRepository;

    @Mock
    private SobonOrientierungswertSozialeInfrastrukturRepository sobonOrientierungswertSozialeInfrastrukturRepository;

    @Mock
    private BerichtsdatenKitaPlbRepository prognoseKitaPlbRepository;

    private StammdatenImportService stammdatenImportService;

    @BeforeEach
    void beforeEach() {
        this.stammdatenImportService =
            new StammdatenImportService(
                this.csvRepository,
                this.staedtebaulicheOrientierungswertRepository,
                this.sobonOrientierungswertSozialeInfrastrukturRepository,
                this.prognoseKitaPlbRepository,
                this.stammdatenDomainMapper
            );
        Mockito.reset(
            this.multipartFile,
            this.csvRepository,
            this.staedtebaulicheOrientierungswertRepository,
            this.sobonOrientierungswertSozialeInfrastrukturRepository,
            this.prognoseKitaPlbRepository
        );
    }

    @Test
    void importBerichtsdatenKitaPlb()
        throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, CsvAttributeErrorException, FileImportFailedException {
        final InputStream inputStream = new ByteArrayInputStream("the-file".getBytes());
        Mockito.when(this.multipartFile.getInputStream()).thenReturn(inputStream);

        final var csvEntry = new BerichtsdatenKitaPlbCsv();
        csvEntry.setKitaPlb(99L);
        csvEntry.setBerichtsstand(LocalDate.of(2024, 5, 1));
        csvEntry.setAnzahlNullBisZweiJaehrige(BigDecimal.ONE);
        csvEntry.setAnzahlDreiBisFuenfJaehrigeUndFuenfzigProzentSechsJaehrige(BigDecimal.TEN);

        Mockito
            .when(this.csvRepository.readAllBerichtsdatenKitaPlbCsv(Mockito.any(InputStreamReader.class)))
            .thenReturn(List.of(csvEntry));

        final var entity1 = new BerichtsdatenKitaPlb();
        entity1.setKitaPlb(99L);
        entity1.setBerichtsstand(LocalDate.of(2024, 5, 1));
        entity1.setAltersgruppe(Altersgruppe.NULL_ZWEI_JAEHRIGE);
        entity1.setAnzahlKinder(BigDecimal.ONE);
        final var entity2 = new BerichtsdatenKitaPlb();
        entity2.setKitaPlb(99L);
        entity2.setBerichtsstand(LocalDate.of(2024, 5, 1));
        entity2.setAltersgruppe(Altersgruppe.DREI_FUENF_UND_FUENFZIG_PROZENT_SECHS_JAEHRIGE);
        entity2.setAnzahlKinder(BigDecimal.TEN);
        final var entities = List.of(entity1, entity2);

        this.stammdatenImportService.importBerichtsdatenKitaPlb(this.multipartFile);

        Mockito
            .verify(this.csvRepository, Mockito.times(1))
            .readAllBerichtsdatenKitaPlbCsv(Mockito.any(InputStreamReader.class));
        Mockito.verify(this.prognoseKitaPlbRepository, Mockito.times(1)).saveAll(entities);
    }

    @Test
    void importBerichtsdatenKitaPlbException()
        throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        InputStream inputStream = new ByteArrayInputStream("the-file".getBytes());
        Mockito.when(this.multipartFile.getInputStream()).thenReturn(inputStream);
        Mockito
            .doThrow(new CsvDataTypeMismatchException())
            .when(this.csvRepository)
            .readAllBerichtsdatenKitaPlbCsv(Mockito.any(InputStreamReader.class));
        Assertions.assertThrows(
            CsvAttributeErrorException.class,
            () -> this.stammdatenImportService.importBerichtsdatenKitaPlb(this.multipartFile)
        );
        Mockito.reset(this.csvRepository, this.multipartFile);

        inputStream = new ByteArrayInputStream("the-file".getBytes());
        Mockito.when(this.multipartFile.getInputStream()).thenReturn(inputStream);
        Mockito
            .doThrow(new CsvRequiredFieldEmptyException())
            .when(this.csvRepository)
            .readAllBerichtsdatenKitaPlbCsv(Mockito.any(InputStreamReader.class));
        Assertions.assertThrows(
            CsvAttributeErrorException.class,
            () -> this.stammdatenImportService.importBerichtsdatenKitaPlb(this.multipartFile)
        );
        Mockito.reset(this.csvRepository, this.multipartFile);

        Mockito.when(this.multipartFile.getInputStream()).thenThrow(new IOException());
        Assertions.assertThrows(
            FileImportFailedException.class,
            () -> this.stammdatenImportService.importBerichtsdatenKitaPlb(this.multipartFile)
        );
    }

    @Test
    void importStaedtebaulicheOrientierungswerte()
        throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, CsvAttributeErrorException, FileImportFailedException {
        final InputStream inputStream = new ByteArrayInputStream("the-file".getBytes());
        Mockito.when(this.multipartFile.getInputStream()).thenReturn(inputStream);

        final var csvEntry = new StaedtebaulicheOrientierungswertCsv();
        csvEntry.setGueltigAb(LocalDate.parse("2021-01-01"));
        csvEntry.setFoerderartBezeichnung("freifinanzierte Geschosswohnungsbau");
        csvEntry.setDurchschnittlicheGrundflaeche(90L);
        csvEntry.setBelegungsdichte(BigDecimal.valueOf(210, 2));

        Mockito
            .when(this.csvRepository.readAllStaedtebaulicheOrientierungswertCsv(Mockito.any(InputStreamReader.class)))
            .thenReturn(List.of(csvEntry));

        final var entity = new StaedtebaulicheOrientierungswert();
        entity.setGueltigAb(LocalDate.parse("2021-01-01"));
        entity.setFoerderartBezeichnung("freifinanzierte Geschosswohnungsbau");
        entity.setDurchschnittlicheGrundflaeche(90L);
        entity.setBelegungsdichte(BigDecimal.valueOf(210, 2));
        final List<StaedtebaulicheOrientierungswert> entities = List.of(entity);

        this.stammdatenImportService.importStaedtebaulicheOrientierungswerte(this.multipartFile);

        Mockito
            .verify(this.csvRepository, Mockito.times(1))
            .readAllStaedtebaulicheOrientierungswertCsv(Mockito.any(InputStreamReader.class));
        Mockito.verify(this.staedtebaulicheOrientierungswertRepository, Mockito.times(1)).saveAll(entities);
    }

    @Test
    void importStaedtebaulicheOrientierungswerteException()
        throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        InputStream inputStream = new ByteArrayInputStream("the-file".getBytes());
        Mockito.when(this.multipartFile.getInputStream()).thenReturn(inputStream);
        Mockito
            .doThrow(new CsvDataTypeMismatchException())
            .when(this.csvRepository)
            .readAllStaedtebaulicheOrientierungswertCsv(Mockito.any(InputStreamReader.class));
        Assertions.assertThrows(
            CsvAttributeErrorException.class,
            () -> this.stammdatenImportService.importStaedtebaulicheOrientierungswerte(this.multipartFile)
        );
        Mockito.reset(this.csvRepository, this.multipartFile);

        inputStream = new ByteArrayInputStream("the-file".getBytes());
        Mockito.when(this.multipartFile.getInputStream()).thenReturn(inputStream);
        Mockito
            .doThrow(new CsvRequiredFieldEmptyException())
            .when(this.csvRepository)
            .readAllStaedtebaulicheOrientierungswertCsv(Mockito.any(InputStreamReader.class));
        Assertions.assertThrows(
            CsvAttributeErrorException.class,
            () -> this.stammdatenImportService.importStaedtebaulicheOrientierungswerte(this.multipartFile)
        );
        Mockito.reset(this.csvRepository, this.multipartFile);

        Mockito.when(this.multipartFile.getInputStream()).thenThrow(new IOException());
        Assertions.assertThrows(
            FileImportFailedException.class,
            () -> this.stammdatenImportService.importStaedtebaulicheOrientierungswerte(this.multipartFile)
        );
    }

    @Test
    void importSoBoNOrientierungswerteSozialeInfrastruktur()
        throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, CsvAttributeErrorException, FileImportFailedException {
        final InputStream inputStream = new ByteArrayInputStream("the-file".getBytes());
        Mockito.when(this.multipartFile.getInputStream()).thenReturn(inputStream);

        final var csvEntry = new SobonOrientierungswertSozialeInfrastrukturCsv();
        csvEntry.setGueltigAb(LocalDate.parse("2021-01-01"));
        csvEntry.setEinrichtungstyp(InfrastruktureinrichtungTyp.KINDERKRIPPE);
        csvEntry.setAltersklasse(Altersklasse.NULL_ZWEI);
        csvEntry.setFoerderartBezeichnung("Ein- und Zweifamilienhäuser");
        csvEntry.setEinwohnerJahr1NachErsterstellung(BigDecimal.valueOf(2877, 4));
        csvEntry.setEinwohnerJahr2NachErsterstellung(BigDecimal.valueOf(2610, 4));
        csvEntry.setEinwohnerJahr3NachErsterstellung(BigDecimal.valueOf(2118, 4));
        csvEntry.setEinwohnerJahr4NachErsterstellung(BigDecimal.valueOf(1725, 4));
        csvEntry.setEinwohnerJahr5NachErsterstellung(BigDecimal.valueOf(1436, 4));
        csvEntry.setEinwohnerJahr6NachErsterstellung(BigDecimal.valueOf(1240, 4));
        csvEntry.setEinwohnerJahr7NachErsterstellung(BigDecimal.valueOf(1060, 4));
        csvEntry.setEinwohnerJahr8NachErsterstellung(BigDecimal.valueOf(850, 4));
        csvEntry.setEinwohnerJahr9NachErsterstellung(BigDecimal.valueOf(776, 4));
        csvEntry.setEinwohnerJahr10NachErsterstellung(BigDecimal.valueOf(716, 4));

        Mockito
            .when(
                this.csvRepository.readAllSobonOrientierungswertSozialeInfrastrukturCsv(
                        Mockito.any(InputStreamReader.class)
                    )
            )
            .thenReturn(List.of(csvEntry));

        final var entity = new SobonOrientierungswertSozialeInfrastruktur();
        entity.setGueltigAb(LocalDate.parse("2021-01-01"));
        entity.setEinrichtungstyp(InfrastruktureinrichtungTyp.KINDERKRIPPE);
        entity.setAltersklasse(Altersklasse.NULL_ZWEI);
        entity.setFoerderartBezeichnung("Ein- und Zweifamilienhäuser");
        entity.setEinwohnerJahr1NachErsterstellung(BigDecimal.valueOf(2877, 4));
        entity.setEinwohnerJahr2NachErsterstellung(BigDecimal.valueOf(2610, 4));
        entity.setEinwohnerJahr3NachErsterstellung(BigDecimal.valueOf(2118, 4));
        entity.setEinwohnerJahr4NachErsterstellung(BigDecimal.valueOf(1725, 4));
        entity.setEinwohnerJahr5NachErsterstellung(BigDecimal.valueOf(1436, 4));
        entity.setEinwohnerJahr6NachErsterstellung(BigDecimal.valueOf(1240, 4));
        entity.setEinwohnerJahr7NachErsterstellung(BigDecimal.valueOf(1060, 4));
        entity.setEinwohnerJahr8NachErsterstellung(BigDecimal.valueOf(850, 4));
        entity.setEinwohnerJahr9NachErsterstellung(BigDecimal.valueOf(776, 4));
        entity.setEinwohnerJahr10NachErsterstellung(BigDecimal.valueOf(716, 4));
        final List<SobonOrientierungswertSozialeInfrastruktur> entities = List.of(entity);

        this.stammdatenImportService.importSobonOrientierungswerteSozialeInfrastruktur(this.multipartFile);

        Mockito
            .verify(this.csvRepository, Mockito.times(1))
            .readAllSobonOrientierungswertSozialeInfrastrukturCsv(Mockito.any(InputStreamReader.class));
        Mockito.verify(this.sobonOrientierungswertSozialeInfrastrukturRepository, Mockito.times(1)).saveAll(entities);
    }

    @Test
    void importSoBoNOrientierungswerteSozialeInfrastrukturException()
        throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        InputStream inputStream = new ByteArrayInputStream("the-file".getBytes());
        Mockito.when(this.multipartFile.getInputStream()).thenReturn(inputStream);
        Mockito
            .doThrow(new CsvDataTypeMismatchException())
            .when(this.csvRepository)
            .readAllSobonOrientierungswertSozialeInfrastrukturCsv(Mockito.any(InputStreamReader.class));
        Assertions.assertThrows(
            CsvAttributeErrorException.class,
            () -> this.stammdatenImportService.importSobonOrientierungswerteSozialeInfrastruktur(this.multipartFile)
        );
        Mockito.reset(this.csvRepository, this.multipartFile);

        inputStream = new ByteArrayInputStream("the-file".getBytes());
        Mockito.when(this.multipartFile.getInputStream()).thenReturn(inputStream);
        Mockito
            .doThrow(new CsvRequiredFieldEmptyException())
            .when(this.csvRepository)
            .readAllSobonOrientierungswertSozialeInfrastrukturCsv(Mockito.any(InputStreamReader.class));
        Assertions.assertThrows(
            CsvAttributeErrorException.class,
            () -> this.stammdatenImportService.importSobonOrientierungswerteSozialeInfrastruktur(this.multipartFile)
        );
        Mockito.reset(this.csvRepository, this.multipartFile);

        Mockito.when(this.multipartFile.getInputStream()).thenThrow(new IOException());
        Assertions.assertThrows(
            FileImportFailedException.class,
            () -> this.stammdatenImportService.importSobonOrientierungswerteSozialeInfrastruktur(this.multipartFile)
        );
    }
}
