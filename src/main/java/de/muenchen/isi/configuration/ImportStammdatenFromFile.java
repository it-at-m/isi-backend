package de.muenchen.isi.configuration;

import de.muenchen.isi.domain.exception.CsvAttributeErrorException;
import de.muenchen.isi.domain.exception.FileImportFailedException;
import de.muenchen.isi.domain.service.stammdaten.StammdatenImportService;
import de.muenchen.isi.infrastructure.entity.stammdaten.SobonJahr;
import de.muenchen.isi.infrastructure.repository.stammdaten.SobonJahrRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.UUID;
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

    private static final String localPath = "src/main/resources/csv/";
    private static final String osPath = "csv/";
    private final String activeProfile;
    private final SobonJahrRepository sobonJahrRepository;
    private final StammdatenImportService stammdatenImportService;
    private final Boolean deferDatasourceInit;

    public ImportStammdatenFromFile(
        @Value("${spring.jpa.defer-datasource-initialization:true}") final Boolean deferDatasourceInit,
        @Value("${spring.profiles.active:local}") final String activeProfile,
        final SobonJahrRepository sobonJahrRepository,
        final StammdatenImportService stammdatenImportService
    ) {
        this.deferDatasourceInit = deferDatasourceInit;
        this.stammdatenImportService = stammdatenImportService;
        this.sobonJahrRepository = sobonJahrRepository;
        this.activeProfile = activeProfile;
    }

    @Override
    public void run(String... args) {
        if ((deferDatasourceInit != null && deferDatasourceInit.booleanValue())) {
            log.info("START LOADING STAMMDATEN");
            this.clearDatabase();
            if (activeProfile.equals("local")) {
                this.addSobonJahre(localPath);
            } else {
                this.addSobonJahre(osPath);
            }
            log.info("FINISHED LOADING STAMMDATEN");
        }
    }

    private void clearDatabase() {
        this.sobonJahrRepository.deleteAll();
    }

    public void addSobonJahre(String dateipfad) {
        SobonJahr sobonJahr2014 = new SobonJahr();
        sobonJahr2014.setId(UUID.randomUUID());
        sobonJahr2014.setJahr(2014);
        sobonJahr2014.setGueltigAb(LocalDate.parse("2014-01-01"));
        this.sobonJahrRepository.save(sobonJahr2014);
        SobonJahr sobonJahr2017 = new SobonJahr();
        sobonJahr2017.setId(UUID.randomUUID());
        sobonJahr2017.setJahr(2017);
        sobonJahr2017.setGueltigAb(LocalDate.parse("2017-01-01"));
        this.sobonJahrRepository.save(sobonJahr2017);
        SobonJahr sobonJahr2022 = new SobonJahr();
        sobonJahr2022.setId(UUID.randomUUID());
        sobonJahr2022.setJahr(2022);
        sobonJahr2022.setGueltigAb(LocalDate.parse("2022-01-01"));
        this.sobonJahrRepository.save(sobonJahr2022);

        try {
            this.addSobonOrientierungswerte(
                    dateipfad + "SoBoNOrientierungswerteSozialeInfrastruktur2014.csv",
                    sobonJahr2014.getId()
                );
            this.addStaedtebaulicheOrientierungswerte(
                    dateipfad + "StaedteBaulicheOrientierungswerte2014.csv",
                    sobonJahr2014.getId()
                );
            log.info("--------------------2014 Eingespielt----------------------------");
            this.addSobonOrientierungswerte(
                    dateipfad + "SoBoNOrientierungswerteSozialeInfrastruktur2017.csv",
                    sobonJahr2017.getId()
                );
            this.addStaedtebaulicheOrientierungswerte(
                    dateipfad + "StaedteBaulicheOrientierungswerte2017.csv",
                    sobonJahr2017.getId()
                );
            log.info("--------------------2017 Eingespielt----------------------------");
            this.addSobonOrientierungswerte(
                    dateipfad + "SoBoNOrientierungswerteSozialeInfrastruktur2022.csv",
                    sobonJahr2022.getId()
                );
            this.addStaedtebaulicheOrientierungswerte(
                    dateipfad + "StaedteBaulicheOrientierungswerte2022.csv",
                    sobonJahr2022.getId()
                );
            log.info("--------------------2022 Eingespielt----------------------------");
        } catch (CsvAttributeErrorException e) {
            log.error(e.getMessage());
        } catch (FileImportFailedException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void addSobonOrientierungswerte(String filePath, UUID sobonJahrId)
        throws IOException, CsvAttributeErrorException, FileImportFailedException {
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

            MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
            this.stammdatenImportService.importSobonOrientierungswerteSozialeInfrastruktur(sobonJahrId, multipartFile);
        } else {
            log.error("Datei nicht gefunden: " + filePath);
        }
    }

    public void addStaedtebaulicheOrientierungswerte(String filePath, UUID sobonJahrId)
        throws IOException, CsvAttributeErrorException, FileImportFailedException {
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

            MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
            this.stammdatenImportService.importStaedtebaulicheOrientierungswerte(sobonJahrId, multipartFile);
        } else {
            log.error("Datei nicht gefunden: " + filePath);
        }
    }
}
