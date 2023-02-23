package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtDokument;
import de.muenchen.isi.infrastructure.entity.filehandling.Dokument;
import de.muenchen.isi.infrastructure.entity.filehandling.Filepath;
import de.muenchen.isi.infrastructure.repository.filehandling.DokumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@ExtendWith(MockitoExtension.class)
@SpringBootTest(
        classes = {IsiBackendApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"tomcat.gracefulshutdown.pre-wait-seconds=0"})
@ActiveProfiles(profiles = {TestConstants.SPRING_UNIT_TEST_PROFILE, TestConstants.SPRING_NO_SECURITY_PROFILE})
@MockitoSettings(strictness = Strictness.LENIENT)
public class OptimisticLockingTest {

    @Autowired
    private DokumentRepository dokumentRepository;

    @Test
    void optimisticLocking() {
        var dokument = new Dokument();
        dokument.setArtDokument(ArtDokument.ANLAGE);
        dokument.setTypDokument("PDF");
        final var filePath = new Filepath();
        filePath.setPathToFile("/pathToFile/file.txt");
        dokument.setFilePath(filePath);

        dokument = this.dokumentRepository.saveAndFlush(dokument);
        final var id = dokument.getId();

        final var dokument1 = this.dokumentRepository.findById(id).get();
        final var dokument2 = this.dokumentRepository.findById(id).get();

        dokument1.setArtDokument(ArtDokument.BERECHNUNG);
        dokument2.setArtDokument(ArtDokument.EMAIL);

        this.dokumentRepository.saveAndFlush(dokument1);

        Assertions.assertThrows(ObjectOptimisticLockingFailureException.class, () -> this.dokumentRepository.saveAndFlush(dokument2));

        final var newLoadedDokument2 = this.dokumentRepository.findById(id).get();
        newLoadedDokument2.setArtDokument(ArtDokument.EMAIL);
        this.dokumentRepository.saveAndFlush(newLoadedDokument2);

        this.dokumentRepository.deleteAll();
    }

}
