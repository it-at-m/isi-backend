package de.muenchen.isi.configuration;

import de.muenchen.isi.domain.exception.CsvAttributeErrorException;
import de.muenchen.isi.domain.exception.FileImportFailedException;
import de.muenchen.isi.domain.service.stammdaten.StammdatenImportService;
import de.muenchen.isi.infrastructure.entity.stammdaten.SobonJahr;
import de.muenchen.isi.infrastructure.repository.stammdaten.SobonJahrRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Component
@Data
@Slf4j
public class ImportStammdatenFromFile implements CommandLineRunner {

    private final SobonJahrRepository sobonJahrRepository;

    private final StammdatenImportService stammdatenImportService;

    private final Boolean deferDatasourceInit;

    public ImportStammdatenFromFile(
        @Value("${spring.jpa.defer-datasource-initialization:true}") final Boolean deferDatasourceInit,
        final SobonJahrRepository sobonJahrRepository,
        final StammdatenImportService stammdatenImportService
    ) {
        this.deferDatasourceInit = deferDatasourceInit;
        this.stammdatenImportService = stammdatenImportService;
        this.sobonJahrRepository = sobonJahrRepository;
    }

    @Override
    public void run(String... args) {
        if ((deferDatasourceInit != null && deferDatasourceInit.booleanValue())) {
            log.info("START LOADING STAMMDATEN");
            this.clearDatabase();
            this.addSobonJahre();
            log.info("FINISHED LOADING STAMMDATEN");
        }
    }

    private void clearDatabase() {
        this.sobonJahrRepository.deleteAll();
    }

    public void addSobonJahre() {
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
                    "src/main/resources/csv/SoBoNOrientierungswerteSozialeInfrastruktur2014.csv",
                    sobonJahr2014.getId()
                );
            this.addStaedtebaulicheOrientierungswerte(
                    "src/main/resources/csv/StaedteBaulicheOrientierungswerte2014.csv",
                    sobonJahr2014.getId()
                );
            log.info("--------------------2014 Eingespielt----------------------------");
            this.addSobonOrientierungswerte(
                    "src/main/resources/csv/SoBoNOrientierungswerteSozialeInfrastruktur2017.csv",
                    sobonJahr2017.getId()
                );
            this.addStaedtebaulicheOrientierungswerte(
                    "src/main/resources/csv/StaedteBaulicheOrientierungswerte2017.csv",
                    sobonJahr2017.getId()
                );
            log.info("--------------------2017 Eingespielt----------------------------");
            this.addSobonOrientierungswerte(
                    "src/main/resources/csv/SoBoNOrientierungswerteSozialeInfrastruktur2022.csv",
                    sobonJahr2022.getId()
                );
            this.addStaedtebaulicheOrientierungswerte(
                    "src/main/resources/csv/StaedteBaulicheOrientierungswerte2022.csv",
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
        File file = new File(filePath);
        FileItem fileItem = new DiskFileItem(
            "tempfile",
            Files.probeContentType(file.toPath()),
            false,
            file.getName(),
            (int) file.length(),
            file.getParentFile()
        );

        try {
            IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
        this.stammdatenImportService.importSobonOrientierungswerteSozialeInfrastruktur(sobonJahrId, multipartFile);
    }

    public void addStaedtebaulicheOrientierungswerte(String filePath, UUID sobonJahrId)
        throws IOException, CsvAttributeErrorException, FileImportFailedException {
        File file = new File(filePath);
        FileItem fileItem = new DiskFileItem(
            "tempfile",
            Files.probeContentType(file.toPath()),
            false,
            file.getName(),
            (int) file.length(),
            file.getParentFile()
        );

        try {
            IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());
        } catch (IOException ex) {
            log.info(ex.getMessage());
        }
        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
        this.stammdatenImportService.importStaedtebaulicheOrientierungswerte(sobonJahrId, multipartFile);
    }
}
