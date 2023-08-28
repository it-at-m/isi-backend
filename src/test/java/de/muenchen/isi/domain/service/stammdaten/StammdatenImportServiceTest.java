package de.muenchen.isi.domain.service.stammdaten;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import de.muenchen.isi.domain.exception.CsvAttributeErrorException;
import de.muenchen.isi.domain.exception.FileImportFailedException;
import de.muenchen.isi.domain.mapper.StammdatenDomainMapper;
import de.muenchen.isi.domain.mapper.StammdatenDomainMapperImpl;
import de.muenchen.isi.infrastructure.csv.SobonOrientierungswertSozialeInfrastrukturCsv;
import de.muenchen.isi.infrastructure.csv.StaedtebaulicheOrientierungswertCsv;
import de.muenchen.isi.infrastructure.entity.enums.Altersklasse;
import de.muenchen.isi.infrastructure.entity.enums.Einrichtungstyp;
import de.muenchen.isi.infrastructure.entity.enums.Wohnungstyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.stammdaten.SobonOrientierungswertSozialeInfrastruktur;
import de.muenchen.isi.infrastructure.entity.stammdaten.StaedtebaulicheOrientierungswert;
import de.muenchen.isi.infrastructure.repository.CsvRepository;
import de.muenchen.isi.infrastructure.repository.stammdaten.SobonOrientierungswertSozialeInfrastrukturRepository;
import de.muenchen.isi.infrastructure.repository.stammdaten.StaedtebaulicheOrientierungswertRepository;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
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

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private CsvRepository csvRepository;

    @Mock
    private StaedtebaulicheOrientierungswertRepository staedtebaulicheOrientierungswertRepository;

    @Mock
    private SobonOrientierungswertSozialeInfrastrukturRepository sobonOrientierungswertSozialeInfrastrukturRepository;

    private final StammdatenDomainMapper stammdatenDomainMapper = new StammdatenDomainMapperImpl();

    private StammdatenImportService stammdatenImportService;

    @BeforeEach
    void beforeEach() {
        this.stammdatenImportService =
        new StammdatenImportService(
            this.csvRepository,
            this.staedtebaulicheOrientierungswertRepository,
            this.sobonOrientierungswertSozialeInfrastrukturRepository,
            this.stammdatenDomainMapper
        );
        Mockito.reset(
            this.multipartFile,
            this.csvRepository,
            this.staedtebaulicheOrientierungswertRepository,
            this.sobonOrientierungswertSozialeInfrastrukturRepository
        );
    }

    @Test
    void importStaedtebaulicheOrientierungswerte()
        throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, CsvAttributeErrorException, FileImportFailedException {
        final InputStream inputStream = new ByteArrayInputStream("the-file".getBytes());
        Mockito.when(this.multipartFile.getInputStream()).thenReturn(inputStream);

        final var csvEntry = new StaedtebaulicheOrientierungswertCsv();
        csvEntry.setJahr(SobonVerfahrensgrundsaetzeJahr.JAHR_2021);
        csvEntry.setWohnungstyp(Wohnungstyp.GW_FREIFINANZEIRT);
        csvEntry.setDurchschnittlicheGrundflaeche(90L);
        csvEntry.setBelegungsdichte(BigDecimal.valueOf(210, 2));

        Mockito
            .when(this.csvRepository.readAllStaedtebaulicheOrientierungswertCsv(Mockito.any(InputStreamReader.class)))
            .thenReturn(List.of(csvEntry));

        final var entity = new StaedtebaulicheOrientierungswert();
        entity.setJahr(SobonVerfahrensgrundsaetzeJahr.JAHR_2021);
        entity.setWohnungstyp(Wohnungstyp.GW_FREIFINANZEIRT);
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
        csvEntry.setJahr(SobonVerfahrensgrundsaetzeJahr.JAHR_2021);
        csvEntry.setEinrichtungstyp(Einrichtungstyp.KINDERKRIPPE);
        csvEntry.setAltersklasse(Altersklasse.NULL_ZWEI);
        csvEntry.setWohnungstyp(Wohnungstyp.EINS_ZWEI_FH);
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
        csvEntry.setMittelwertEinwohnerJeWohnung(BigDecimal.valueOf(1541, 4));
        csvEntry.setFaktor1EinwohnerJeWohnung(BigDecimal.valueOf(2569, 4));
        csvEntry.setFaktorEinwohnerJeWohnung(BigDecimal.valueOf(12569, 4));
        csvEntry.setPerzentil75ProzentEinwohnerJeWohnung(BigDecimal.valueOf(1937, 4));
        csvEntry.setPerzentil75ProzentGerundetEinwohnerJeWohnung(BigDecimal.valueOf(19, 2));

        Mockito
            .when(
                this.csvRepository.readAllSobonOrientierungswertSozialeInfrastrukturCsv(
                        Mockito.any(InputStreamReader.class)
                    )
            )
            .thenReturn(List.of(csvEntry));

        final var entity = new SobonOrientierungswertSozialeInfrastruktur();
        entity.setJahr(SobonVerfahrensgrundsaetzeJahr.JAHR_2021);
        entity.setEinrichtungstyp(Einrichtungstyp.KINDERKRIPPE);
        entity.setAltersklasse(Altersklasse.NULL_ZWEI);
        entity.setWohnungstyp(Wohnungstyp.EINS_ZWEI_FH);
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
        entity.setMittelwertEinwohnerJeWohnung(BigDecimal.valueOf(1541, 4));
        entity.setFaktor1EinwohnerJeWohnung(BigDecimal.valueOf(2569, 4));
        entity.setFaktorEinwohnerJeWohnung(BigDecimal.valueOf(12569, 4));
        entity.setPerzentil75ProzentEinwohnerJeWohnung(BigDecimal.valueOf(1937, 4));
        entity.setPerzentil75ProzentGerundetEinwohnerJeWohnung(BigDecimal.valueOf(19, 2));
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
