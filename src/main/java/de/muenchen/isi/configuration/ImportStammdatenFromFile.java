package de.muenchen.isi.configuration;

import de.muenchen.isi.domain.exception.CsvAttributeErrorException;
import de.muenchen.isi.domain.exception.FileImportFailedException;
import de.muenchen.isi.domain.service.stammdaten.StammdatenImportService;
import de.muenchen.isi.infrastructure.repository.stammdaten.SobonOrientierungswertSozialeInfrastrukturRepository;
import de.muenchen.isi.infrastructure.repository.stammdaten.StaedtebaulicheOrientierungswertRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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

    private static final String PATH = "csv/";
    private final StammdatenImportService stammdatenImportService;
    private final StaedtebaulicheOrientierungswertRepository staedtebaulicheOrientierungswertRepository;
    private final SobonOrientierungswertSozialeInfrastrukturRepository sobonOrientierungswertSozialeInfrastrukturRepository;
    private final Boolean deferDatasourceInit;

    public ImportStammdatenFromFile(
        @Value("${spring.jpa.defer-datasource-initialization:true}") final Boolean deferDatasourceInit,
        final StammdatenImportService stammdatenImportService,
        final SobonOrientierungswertSozialeInfrastrukturRepository sobonOrientierungswertSozialeInfrastrukturRepository,
        final StaedtebaulicheOrientierungswertRepository staedtebaulicheOrientierungswertRepository
    ) {
        this.deferDatasourceInit = deferDatasourceInit;
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
    public void run(String... args) {
        if ((deferDatasourceInit != null && deferDatasourceInit.booleanValue())) {
            log.info("START LOADING STAMMDATEN");
            this.clearDatabase();
            this.addSobonJahre(PATH);
            log.info("FINISHED LOADING STAMMDATEN");
        }
    }

    /**
     * Löscht alle vorhandenen Datensätze in der Datenbank.
     */
    private void clearDatabase() {
        this.sobonOrientierungswertSozialeInfrastrukturRepository.deleteAll();
        this.staedtebaulicheOrientierungswertRepository.deleteAll();
    }

    /**
     * Fügt die Sobon-Jahre in die Datenbank ein.
     *
     * @param dateipfad Der Dateipfad zum Verzeichnis der CSV-Dateien.
     */
    public void addSobonJahre(String dateipfad) {
        try {
            this.addSobonOrientierungswerte(dateipfad + "SoBoNOrientierungswerteSozialeInfrastruktur2014.csv");
            this.addStaedtebaulicheOrientierungswerte(dateipfad + "StaedteBaulicheOrientierungswerte2014.csv");
            log.info("--------------------2014 Eingespielt----------------------------");
            this.addSobonOrientierungswerte(dateipfad + "SoBoNOrientierungswerteSozialeInfrastruktur2017.csv");
            this.addStaedtebaulicheOrientierungswerte(dateipfad + "StaedteBaulicheOrientierungswerte2017.csv");
            log.info("--------------------2017 Eingespielt----------------------------");
            this.addSobonOrientierungswerte(dateipfad + "SoBoNOrientierungswerteSozialeInfrastruktur2022.csv");
            this.addStaedtebaulicheOrientierungswerte(dateipfad + "StaedteBaulicheOrientierungswerte2022.csv");
            log.info("--------------------2022 Eingespielt----------------------------");
        } catch (CsvAttributeErrorException e) {
            log.error(e.getMessage());
        } catch (FileImportFailedException e) {
            log.error(e.getMessage());
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Importiert die Sobon-Orientierungswerte aus der angegebenen CSV-Datei.
     *
     * @param filePath Der Dateipfad zur Sobon-Orientierungswerte CSV-Datei.
     * @throws IOException                Wenn ein Fehler beim Lesen der Datei auftritt.
     * @throws CsvAttributeErrorException Wenn ein Fehler in den CSV-Attributen auftritt.
     * @throws FileImportFailedException  Wenn der Import der Datei fehlschlägt.
     */
    public void addSobonOrientierungswerte(String filePath)
        throws IOException, CsvAttributeErrorException, FileImportFailedException {
        MultipartFile multipartFile = createMultipartFile(filePath);
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
        MultipartFile multipartFile = createMultipartFile(filePath);
        this.stammdatenImportService.importStaedtebaulicheOrientierungswerte(multipartFile);
    }

    /**
     * Erstellt ein MultipartFile-Objekt aus der angegebenen Datei.
     *
     * @param filePath Der Dateipfad zur CSV-Datei.
     * @return Das MultipartFile-Objekt.
     * @throws IOException Wenn ein Fehler beim Lesen der Datei auftritt.
     */
    public MultipartFile createMultipartFile(String filePath) throws IOException {
        FileItem fileItem = null;
        InputStream inputStream = new ClassPathResource(filePath).getInputStream();

        if (inputStream != null) {
            File tempFile = File.createTempFile("tempfile", ".tmp");
            FileUtils.copyInputStreamToFile(inputStream, tempFile);
            fileItem =
                new DiskFileItem(
                    "tempfile",
                    Files.probeContentType(tempFile.toPath()),
                    false,
                    tempFile.getName(),
                    (int) tempFile.length(),
                    tempFile.getParentFile()
                );
            IOUtils.copy(new FileInputStream(tempFile), fileItem.getOutputStream());
        } else {
            throw new FileNotFoundException("Datei wurde nicht gefunden");
        }
        return new CommonsMultipartFile(fileItem);
    }
}
