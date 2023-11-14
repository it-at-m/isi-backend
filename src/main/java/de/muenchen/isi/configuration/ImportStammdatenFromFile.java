package de.muenchen.isi.configuration;

import de.muenchen.isi.domain.exception.CsvAttributeErrorException;
import de.muenchen.isi.domain.exception.FileImportFailedException;
import de.muenchen.isi.domain.service.stammdaten.StammdatenImportService;
import de.muenchen.isi.infrastructure.repository.stammdaten.SobonOrientierungswertSozialeInfrastrukturRepository;
import de.muenchen.isi.infrastructure.repository.stammdaten.StaedtebaulicheOrientierungswertRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Component
@Data
@Slf4j
public class ImportStammdatenFromFile implements CommandLineRunner {

    private final StammdatenImportService stammdatenImportService;

    private final StaedtebaulicheOrientierungswertRepository staedtebaulicheOrientierungswertRepository;

    private final SobonOrientierungswertSozialeInfrastrukturRepository sobonOrientierungswertSozialeInfrastrukturRepository;

    private final Boolean deferDatasourceInit;

    private final List<String> csvSobonOrientierungswertSozialeInfrastruktur;

    private final List<String> csvStaedtebaulicheOrientierungswerte;

    public ImportStammdatenFromFile(
        @Value("${spring.jpa.defer-datasource-initialization:true}") final Boolean deferDatasourceInit,
        @Value("${stammdaten.csv-locations.sobon-orientierungswerte-sozialinfrastruktur}") final List<
            String
        > csvSobonOrientierungswertSozialeInfrastruktur,
        @Value("${stammdaten.csv-locations.staedtebauliche-orientierungswerte}") final List<
            String
        > csvStaedtebaulicheOrientierungswerte,
        final StammdatenImportService stammdatenImportService,
        final SobonOrientierungswertSozialeInfrastrukturRepository sobonOrientierungswertSozialeInfrastrukturRepository,
        final StaedtebaulicheOrientierungswertRepository staedtebaulicheOrientierungswertRepository
    ) {
        this.deferDatasourceInit = deferDatasourceInit;
        this.csvSobonOrientierungswertSozialeInfrastruktur = csvSobonOrientierungswertSozialeInfrastruktur;
        this.csvStaedtebaulicheOrientierungswerte = csvStaedtebaulicheOrientierungswerte;
        this.stammdatenImportService = stammdatenImportService;
        this.sobonOrientierungswertSozialeInfrastrukturRepository =
            sobonOrientierungswertSozialeInfrastrukturRepository;
        this.staedtebaulicheOrientierungswertRepository = staedtebaulicheOrientierungswertRepository;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Führt den Import von Stammdaten aus den angegebenen CSV-Dateien durch.
     */
    @Override
    public void run(String... args) throws CsvAttributeErrorException, FileImportFailedException, IOException {
        if (BooleanUtils.isTrue(deferDatasourceInit)) {
            log.info("START IMPORT SOBON STAMMDATEN");
            this.sobonOrientierungswertSozialeInfrastrukturRepository.deleteAll();
            this.staedtebaulicheOrientierungswertRepository.deleteAll();
            for (final var csvFile : this.csvSobonOrientierungswertSozialeInfrastruktur) {
                this.addSobonOrientierungswerteSozialeInfrastruktur(csvFile);
                log.info("Stammdaten zu {} importiert", csvFile);
            }
            for (final var csvFile : this.csvStaedtebaulicheOrientierungswerte) {
                this.addStaedtebaulicheOrientierungswerte(csvFile);
                log.info("Stammdaten zu {} importiert", csvFile);
            }
            log.info("FINISHED IMPORT SOBON STAMMDATEN");
        }
    }

    /**
     * Importiert die Sobon-Orientierungswerte für soziale Infrastruktur aus der angegebenen CSV-Datei.
     *
     * @param filePath Der Dateipfad zur Sobon-Orientierungswerte für soziale Infrastruktur CSV-Datei.
     * @throws IOException                Wenn ein Fehler beim Lesen der Datei auftritt.
     * @throws CsvAttributeErrorException Wenn ein Fehler in den CSV-Attributen auftritt.
     * @throws FileImportFailedException  Wenn der Import der Datei fehlschlägt.
     */
    public void addSobonOrientierungswerteSozialeInfrastruktur(String filePath)
        throws IOException, CsvAttributeErrorException, FileImportFailedException {
        final var multipartFile = this.createMultipartFile(filePath);
        this.stammdatenImportService.importSobonOrientierungswerteSozialeInfrastruktur(multipartFile);
    }

    /**
     * Importiert die städtebaulichen Orientierungswerte aus der angegebenen CSV-Datei.
     *
     * @param filePath Der Dateipfad zur städtebaulichen Orientierungswerte CSV-Datei.
     * @throws IOException                Wenn ein Fehler beim Lesen der Datei auftritt.
     * @throws CsvAttributeErrorException Wenn ein Fehler in den CSV-Attributen auftritt.
     * @throws FileImportFailedException  Wenn der Import der Datei fehlschlägt.
     */
    public void addStaedtebaulicheOrientierungswerte(String filePath)
        throws IOException, CsvAttributeErrorException, FileImportFailedException {
        final var multipartFile = this.createMultipartFile(filePath);
        this.stammdatenImportService.importStaedtebaulicheOrientierungswerte(multipartFile);
    }

    /**
     * Erstellt ein MultipartFile-Objekt aus der angegebenen Datei.
     *
     * @param filePath Der Dateipfad zur CSV-Datei.
     * @return Das MultipartFile-Objekt.
     * @throws IOException sobald ein Fehler beim Lesen der Datei auftritt.
     */
    public MultipartFile createMultipartFile(String filePath) throws IOException {
        final var inputStream = new ClassPathResource(filePath).getInputStream();
        final var tempFile = File.createTempFile("tempfile", ".tmp");
        FileUtils.copyInputStreamToFile(inputStream, tempFile);
        final FileItem fileItem = new DiskFileItem(
            "tempfile",
            Files.probeContentType(tempFile.toPath()),
            false,
            tempFile.getName(),
            (int) tempFile.length(),
            tempFile.getParentFile()
        );
        IOUtils.copy(new FileInputStream(tempFile), fileItem.getOutputStream());
        return new CommonsMultipartFile(fileItem);
    }
}
