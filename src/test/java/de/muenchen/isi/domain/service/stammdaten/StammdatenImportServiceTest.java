package de.muenchen.isi.domain.service.stammdaten;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import de.muenchen.isi.domain.exception.CsvAttributeErrorException;
import de.muenchen.isi.domain.exception.FileImportFailedException;
import de.muenchen.isi.domain.mapper.StammdatenDomainMapper;
import de.muenchen.isi.domain.mapper.StammdatenDomainMapperImpl;
import de.muenchen.isi.infrastructure.csv.SobonOrientierungswertCsv;
import de.muenchen.isi.infrastructure.csv.StaedtebaulicheOrientierungswertCsv;
import de.muenchen.isi.infrastructure.entity.enums.Altersklasse;
import de.muenchen.isi.infrastructure.entity.enums.Einrichtungstyp;
import de.muenchen.isi.infrastructure.entity.stammdaten.SobonJahr;
import de.muenchen.isi.infrastructure.entity.stammdaten.SobonOrientierungswert;
import de.muenchen.isi.infrastructure.entity.stammdaten.StaedtbaulicherOrientierungwert;
import de.muenchen.isi.infrastructure.repository.CsvRepository;
import de.muenchen.isi.infrastructure.repository.stammdaten.SobonJahrRepository;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    private StammdatenImportService stammdatenImportService;

    @Mock
    private SobonJahrRepository sobonJahrRepository;

    @BeforeEach
    void beforeEach() {
        this.stammdatenImportService =
            new StammdatenImportService(this.csvRepository, this.sobonJahrRepository, this.stammdatenDomainMapper);
        Mockito.reset(this.multipartFile, this.csvRepository, this.sobonJahrRepository);
    }

    @Test
    void importStaedtebaulicheOrientierungswerte()
        throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, CsvAttributeErrorException, FileImportFailedException {
        final InputStream inputStream = new ByteArrayInputStream("the-file".getBytes());
        Mockito.when(this.multipartFile.getInputStream()).thenReturn(inputStream);

        SobonJahr sobonJahr = new SobonJahr();
        sobonJahr.setId(UUID.randomUUID());
        sobonJahr.setJahr(2014);
        sobonJahr.setGueltigAb(LocalDate.parse("2014-01-01"));

        Mockito.when(this.sobonJahrRepository.findById(sobonJahr.getId())).thenReturn(Optional.of(sobonJahr));

        final var csvEntry = new StaedtebaulicheOrientierungswertCsv();
        csvEntry.setFoerderArt("GW-Freifinanziert");
        csvEntry.setDurchschnittlicheGrundflaeche(90L);
        csvEntry.setBelegungsdichte(BigDecimal.valueOf(210, 2));

        Mockito
            .when(this.csvRepository.readAllStaedtebaulicheOrientierungswertCsv(Mockito.any(InputStreamReader.class)))
            .thenReturn(List.of(csvEntry));

        final var entity = new StaedtbaulicherOrientierungwert();
        entity.setFoerderArt("GW-Freifinanziert");
        entity.setDurchschnittlicheGrundflaeche(90L);
        entity.setBelegungsdichte(BigDecimal.valueOf(210, 2));
        sobonJahr.setStaedtebaulicheOrientierungswerte(List.of(entity));

        this.stammdatenImportService.importStaedtebaulicheOrientierungswerte(sobonJahr.getId(), this.multipartFile);

        Mockito
            .verify(this.csvRepository, Mockito.times(1))
            .readAllStaedtebaulicheOrientierungswertCsv(Mockito.any(InputStreamReader.class));
        Mockito.verify(this.sobonJahrRepository, Mockito.times(1)).save(sobonJahr);
    }

    @Test
    void importStaedtebaulicheOrientierungswerteException()
        throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        SobonJahr sobonJahr = new SobonJahr();
        sobonJahr.setId(UUID.randomUUID());
        sobonJahr.setJahr(2014);
        Mockito.when(this.sobonJahrRepository.findById(sobonJahr.getId())).thenReturn(Optional.of(sobonJahr));

        InputStream inputStream = new ByteArrayInputStream("the-file".getBytes());
        Mockito.when(this.multipartFile.getInputStream()).thenReturn(inputStream);
        Mockito
            .doThrow(new CsvDataTypeMismatchException())
            .when(this.csvRepository)
            .readAllStaedtebaulicheOrientierungswertCsv(Mockito.any(InputStreamReader.class));
        Assertions.assertThrows(
            CsvAttributeErrorException.class,
            () ->
                this.stammdatenImportService.importStaedtebaulicheOrientierungswerte(
                        sobonJahr.getId(),
                        this.multipartFile
                    )
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
            () ->
                this.stammdatenImportService.importStaedtebaulicheOrientierungswerte(
                        sobonJahr.getId(),
                        this.multipartFile
                    )
        );
        Mockito.reset(this.csvRepository, this.multipartFile);

        Mockito.when(this.multipartFile.getInputStream()).thenThrow(new IOException());
        Assertions.assertThrows(
            FileImportFailedException.class,
            () ->
                this.stammdatenImportService.importStaedtebaulicheOrientierungswerte(
                        sobonJahr.getId(),
                        this.multipartFile
                    )
        );
    }

    @Test
    void importSoBoNOrientierungswerteSozialeInfrastruktur()
        throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, CsvAttributeErrorException, FileImportFailedException {
        SobonJahr sobonJahr = new SobonJahr();
        sobonJahr.setId(UUID.randomUUID());
        sobonJahr.setJahr(2014);
        Mockito.when(this.sobonJahrRepository.findById(sobonJahr.getId())).thenReturn(Optional.of(sobonJahr));

        final InputStream inputStream = new ByteArrayInputStream("the-file".getBytes());
        Mockito.when(this.multipartFile.getInputStream()).thenReturn(inputStream);

        final var csvEntry = new SobonOrientierungswertCsv();
        csvEntry.setEinrichtungstyp(Einrichtungstyp.KINDERKRIPPE);
        csvEntry.setAltersklasse(Altersklasse.NULL_ZWEI);
        csvEntry.setFoerderArt("1-2-FH");
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

        final var entity = new SobonOrientierungswert();
        entity.setEinrichtungstyp(Einrichtungstyp.KINDERKRIPPE);
        entity.setAltersklasse(Altersklasse.NULL_ZWEI);
        entity.setFoerderArt("1-2-FH");
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
        sobonJahr.setSobonOrientierungswerte(List.of(entity));

        this.stammdatenImportService.importSobonOrientierungswerteSozialeInfrastruktur(
                sobonJahr.getId(),
                this.multipartFile
            );

        Mockito
            .verify(this.csvRepository, Mockito.times(1))
            .readAllSobonOrientierungswertSozialeInfrastrukturCsv(Mockito.any(InputStreamReader.class));
        Mockito.verify(this.sobonJahrRepository, Mockito.times(1)).save(sobonJahr);
    }

    @Test
    void importSoBoNOrientierungswerteSozialeInfrastrukturException()
        throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        SobonJahr sobonJahr = new SobonJahr();
        sobonJahr.setId(UUID.randomUUID());
        sobonJahr.setJahr(2014);
        Mockito.when(this.sobonJahrRepository.findById(sobonJahr.getId())).thenReturn(Optional.of(sobonJahr));

        InputStream inputStream = new ByteArrayInputStream("the-file".getBytes());
        Mockito.when(this.multipartFile.getInputStream()).thenReturn(inputStream);
        Mockito
            .doThrow(new CsvDataTypeMismatchException())
            .when(this.csvRepository)
            .readAllSobonOrientierungswertSozialeInfrastrukturCsv(Mockito.any(InputStreamReader.class));
        Assertions.assertThrows(
            CsvAttributeErrorException.class,
            () ->
                this.stammdatenImportService.importSobonOrientierungswerteSozialeInfrastruktur(
                        sobonJahr.getId(),
                        this.multipartFile
                    )
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
            () ->
                this.stammdatenImportService.importSobonOrientierungswerteSozialeInfrastruktur(
                        sobonJahr.getId(),
                        this.multipartFile
                    )
        );
        Mockito.reset(this.csvRepository, this.multipartFile);

        Mockito.when(this.multipartFile.getInputStream()).thenThrow(new IOException());
        Assertions.assertThrows(
            FileImportFailedException.class,
            () ->
                this.stammdatenImportService.importSobonOrientierungswerteSozialeInfrastruktur(
                        sobonJahr.getId(),
                        this.multipartFile
                    )
        );
    }
}
