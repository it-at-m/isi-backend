package de.muenchen.isi.domain.cronjob;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.exception.CronJobFailedException;
import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import de.muenchen.isi.domain.model.filehandling.DokumenteModel;
import de.muenchen.isi.domain.model.filehandling.FilepathModel;
import de.muenchen.isi.domain.service.filehandling.DokumentService;
import de.muenchen.oss.refarch.integration.s3.application.port.out.S3OutPort;
import de.muenchen.oss.refarch.integration.s3.domain.exception.S3Exception;
import de.muenchen.oss.refarch.integration.s3.domain.model.FileMetadata;
import de.muenchen.oss.refarch.integration.s3.domain.model.FileReference;
import de.muenchen.oss.refarch.integration.s3.domain.model.ListResult;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OrphanFileCleanupServiceTest {

    static final String BUCKET = "isi-unittest-bucket";

    @Mock
    private DokumentService dokumentService;

    @Mock
    private S3OutPort s3OutPort;

    private OrphanFileCleanupService orphanFileCleanupService;

    @BeforeEach
    public void beforeEach() {
        this.orphanFileCleanupService = new OrphanFileCleanupService(BUCKET, this.s3OutPort, this.dokumentService);
        Mockito.reset(this.dokumentService, this.s3OutPort);
    }

    @Test
    void cleanUp() throws S3Exception {
        var files = List.of(
            new FileMetadata("folder1/file1.txt", 123L, "etag1", Instant.now()),
            new FileMetadata("folder2/file2.txt", 456L, "etag2", Instant.now()),
            new FileMetadata("folder3/file3.txt", 789L, "etag3", Instant.now()),
            new FileMetadata("folder4/file4.txt", 1011L, "etag4", Instant.now())
        );
        var listResult = new ListResult(files, List.of("folder1/", "folder2/", "folder3/", "folder4/"), false, null);
        Mockito.when(this.s3OutPort.getFilesWithPrefix(BUCKET, "", true)).thenReturn(listResult);

        final DokumenteModel dokumenteModelPage0 = new DokumenteModel();
        dokumenteModelPage0.setLast(false);
        dokumenteModelPage0.setPageNumber(0);
        dokumenteModelPage0.setDokumente(new ArrayList<>());

        DokumentModel dokument = new DokumentModel();
        FilepathModel filepath = new FilepathModel();
        filepath.setPathToFile("folder1/file1.txt");
        dokument.setFilePath(filepath);
        dokumenteModelPage0.getDokumente().add(dokument);

        dokument = new DokumentModel();
        filepath = new FilepathModel();
        filepath.setPathToFile("folder2/file2.txt");
        dokument.setFilePath(filepath);
        dokumenteModelPage0.getDokumente().add(dokument);

        final DokumenteModel dokumenteModelPage1 = new DokumenteModel();
        dokumenteModelPage1.setLast(true);
        dokumenteModelPage1.setPageNumber(1);
        dokumenteModelPage1.setDokumente(new ArrayList<>());

        dokument = new DokumentModel();
        filepath = new FilepathModel();
        filepath.setPathToFile("folder3/file3.txt");
        dokument.setFilePath(filepath);
        dokumenteModelPage1.getDokumente().add(dokument);

        Mockito.when(this.dokumentService.getDokumente(0, 100)).thenReturn(dokumenteModelPage0);
        Mockito.when(this.dokumentService.getDokumente(1, 100)).thenReturn(dokumenteModelPage1);

        this.orphanFileCleanupService.cleanUp();

        Mockito.verify(this.s3OutPort, Mockito.times(1)).getFilesWithPrefix(BUCKET, "", true);
        Mockito.verify(this.dokumentService, Mockito.times(1)).getDokumente(0, 100);
        Mockito.verify(this.dokumentService, Mockito.times(1)).getDokumente(1, 100);
    }

    @Test
    void cleanUpExceptionS3RepositoryGetFilePathFromFolder() throws S3Exception {
        Mockito.when(this.s3OutPort.getFilesWithPrefix(BUCKET, "", true)).thenThrow(new S3Exception("error"));

        Assertions.assertThrows(CronJobFailedException.class, () -> this.orphanFileCleanupService.cleanUp());
        Mockito.verify(this.s3OutPort, Mockito.times(1)).getFilesWithPrefix(BUCKET, "", true);
        Mockito.verify(this.dokumentService, Mockito.times(0)).getDokumente(0, 100);
    }

    @Test
    void determineFilePathsInS3WhichAreNotReferencedByBackendDokuments() throws S3Exception {
        var files = List.of(
            new FileMetadata("folder1/file1.txt", 123L, "etag1", Instant.now()),
            new FileMetadata("folder2/file2.txt", 456L, "etag2", Instant.now()),
            new FileMetadata("folder3/file3.txt", 789L, "etag3", Instant.now()),
            new FileMetadata("folder4/file4.txt", 1011L, "etag4", Instant.now())
        );
        var listResult = new ListResult(files, List.of("folder1/", "folder2/", "folder3/", "folder4/"), false, null);
        Mockito.when(this.s3OutPort.getFilesWithPrefix(BUCKET, "", true)).thenReturn(listResult);

        final DokumenteModel dokumenteModelPage0 = new DokumenteModel();
        dokumenteModelPage0.setLast(false);
        dokumenteModelPage0.setPageNumber(0);
        dokumenteModelPage0.setDokumente(new ArrayList<>());

        DokumentModel dokument = new DokumentModel();
        FilepathModel filepath = new FilepathModel();
        filepath.setPathToFile("folder1/file1.txt");
        dokument.setFilePath(filepath);
        dokumenteModelPage0.getDokumente().add(dokument);

        dokument = new DokumentModel();
        filepath = new FilepathModel();
        filepath.setPathToFile("folder2/file2.txt");
        dokument.setFilePath(filepath);
        dokumenteModelPage0.getDokumente().add(dokument);

        final DokumenteModel dokumenteModelPage1 = new DokumenteModel();
        dokumenteModelPage1.setLast(true);
        dokumenteModelPage1.setPageNumber(1);
        dokumenteModelPage1.setDokumente(new ArrayList<>());

        dokument = new DokumentModel();
        filepath = new FilepathModel();
        filepath.setPathToFile("folder3/file3.txt");
        dokument.setFilePath(filepath);
        dokumenteModelPage1.getDokumente().add(dokument);

        dokument = new DokumentModel();
        filepath = new FilepathModel();
        filepath.setPathToFile("folder5/file5.txt");
        dokument.setFilePath(filepath);
        dokumenteModelPage1.getDokumente().add(dokument);

        Mockito.when(this.dokumentService.getDokumente(0, 100)).thenReturn(dokumenteModelPage0);
        Mockito.when(this.dokumentService.getDokumente(1, 100)).thenReturn(dokumenteModelPage1);

        final Set<String> expected = Set.of("folder4/file4.txt");

        assertThat(
            this.orphanFileCleanupService.determineFilePathsInS3WhichAreNotReferencedByBackendDokuments(),
            is(expected)
        );

        Mockito.verify(this.s3OutPort, Mockito.times(1)).getFilesWithPrefix(BUCKET, "", true);
        Mockito.verify(this.dokumentService, Mockito.times(1)).getDokumente(0, 100);
        Mockito.verify(this.dokumentService, Mockito.times(1)).getDokumente(1, 100);
    }

    @Test
    void determineFilePathsInS3WhichAreNotReferencedByBackendDokumentsException() throws S3Exception {
        Mockito.when(this.s3OutPort.getFilesWithPrefix(BUCKET, "", true)).thenThrow(new S3Exception("error"));

        Assertions.assertThrows(CronJobFailedException.class, () ->
            this.orphanFileCleanupService.determineFilePathsInS3WhichAreNotReferencedByBackendDokuments()
        );
        Mockito.verify(this.s3OutPort, Mockito.times(1)).getFilesWithPrefix(BUCKET, "", true);
        Mockito.verify(this.dokumentService, Mockito.times(0)).getDokumente(0, 100);

        Mockito.reset(this.dokumentService, this.s3OutPort);
    }

    @Test
    void getDokumentPage() throws S3Exception {
        final DokumenteModel dokumenteModel = new DokumenteModel();
        dokumenteModel.setPageNumber(2);
        dokumenteModel.setPageSize(100);
        dokumenteModel.setTotalPages(51);
        dokumenteModel.setTotalElements(5100L);
        dokumenteModel.setLast(false);
        dokumenteModel.setDokumente(new ArrayList<>());

        DokumentModel dokument = new DokumentModel();
        FilepathModel filepath = new FilepathModel();
        filepath.setPathToFile("folder1/file1.txt");
        dokument.setFilePath(filepath);
        dokumenteModel.getDokumente().add(dokument);

        dokument = new DokumentModel();
        filepath = new FilepathModel();
        filepath.setPathToFile("folder2/file2.txt");
        dokument.setFilePath(filepath);
        dokumenteModel.getDokumente().add(dokument);

        dokument = new DokumentModel();
        filepath = new FilepathModel();
        filepath.setPathToFile("folder3/file3.txt");
        dokument.setFilePath(filepath);
        dokumenteModel.getDokumente().add(dokument);

        Mockito.when(this.dokumentService.getDokumente(2, 100)).thenReturn(dokumenteModel);

        assertThat(this.orphanFileCleanupService.getDokumentPage(2), is(dokumenteModel));

        Mockito.verify(this.dokumentService, Mockito.times(1)).getDokumente(2, 100);
    }

    @Test
    void deleteFile() throws S3Exception {
        final var pathToFile = "folder/file.txt";

        this.orphanFileCleanupService.deleteFile(pathToFile);

        Mockito.verify(this.s3OutPort, Mockito.times(1)).deleteFile(new FileReference(BUCKET, pathToFile));
    }

    @Test
    void deleteFileException() throws S3Exception {
        final var pathToFile = "folder/file.txt";

        Mockito.doThrow(new RuntimeException("error_message"))
            .when(this.s3OutPort)
            .deleteFile(new FileReference(BUCKET, pathToFile));

        Assertions.assertThrows(CronJobFailedException.class, () ->
            this.orphanFileCleanupService.deleteFile(pathToFile)
        );

        Mockito.verify(this.s3OutPort, Mockito.times(1)).deleteFile(new FileReference(BUCKET, pathToFile));
    }

    @Test
    void getFilePaths() {
        final DokumenteModel dokumenteModel = new DokumenteModel();
        dokumenteModel.setDokumente(new ArrayList<>());

        DokumentModel dokument = new DokumentModel();
        FilepathModel filepath = new FilepathModel();
        filepath.setPathToFile("folder1/file1.txt");
        dokument.setFilePath(filepath);
        dokumenteModel.getDokumente().add(dokument);

        dokument = new DokumentModel();
        filepath = new FilepathModel();
        filepath.setPathToFile("folder2/file2.txt");
        dokument.setFilePath(filepath);
        dokumenteModel.getDokumente().add(dokument);

        dokument = new DokumentModel();
        filepath = new FilepathModel();
        filepath.setPathToFile("folder3/file3.txt");
        dokument.setFilePath(filepath);
        dokumenteModel.getDokumente().add(dokument);

        final Set<String> expected = Set.of("folder1/file1.txt", "folder2/file2.txt", "folder3/file3.txt");

        assertThat(this.orphanFileCleanupService.getFilePaths(dokumenteModel), is(expected));
    }
}
