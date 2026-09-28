package de.muenchen.isi.domain.service.filehandling;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.model.filehandling.FilepathModel;
import de.muenchen.isi.domain.model.filehandling.PresignedUrlModel;
import de.muenchen.oss.refarch.integration.s3.application.port.out.S3OutPort;
import de.muenchen.oss.refarch.integration.s3.domain.exception.S3Exception;
import de.muenchen.oss.refarch.integration.s3.domain.model.FileReference;
import de.muenchen.oss.refarch.integration.s3.domain.model.PresignedUrl;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
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
class PresignedUrlCreationServiceTest {

    static final String BUCKET = "isi-unittest-bucket";
    static final Integer FILE_EXPIRATION_DATE = 10;

    @Mock
    private S3OutPort s3OutPort;

    private PresignedUrlCreationService presignedUrlCreationService;

    @BeforeEach
    public void beforeEach() {
        this.presignedUrlCreationService = new PresignedUrlCreationService(BUCKET, FILE_EXPIRATION_DATE, s3OutPort);
        Mockito.reset(this.s3OutPort);
    }

    @Test
    void getFile() throws S3Exception, FileHandlingFailedException, MalformedURLException {
        final var pathToFile = "outerFolder/innerFolder/thefile.pdf";

        final var url =
            "https://storage.de/The-Bucket/outerFolder/innerFolder/thefile.pdf?abc=abcdf4sfskhsdfsfddsghjve884545klnfgv";

        final FileReference fileReference = new FileReference(BUCKET, pathToFile);

        final PresignedUrl presignedUrl = new PresignedUrl(new URL(url), pathToFile, PresignedUrl.Action.GET);
        Mockito.when(
            this.s3OutPort.getPresignedUrl(
                fileReference,
                PresignedUrl.Action.GET,
                Duration.ofMinutes(FILE_EXPIRATION_DATE)
            )
        ).thenReturn(presignedUrl);

        final var expected = new PresignedUrlModel();
        expected.setHttpMethodToUse("GET");
        expected.setUrl(
            "https://storage.de/The-Bucket/outerFolder/innerFolder/thefile.pdf?abc=abcdf4sfskhsdfsfddsghjve884545klnfgv"
        );

        final var filePathModel = new FilepathModel();
        filePathModel.setPathToFile(pathToFile);
        final var result = this.presignedUrlCreationService.getFile(filePathModel);
        assertThat(result, is(expected));
    }

    @Test
    void getFileException() throws S3Exception, FileHandlingFailedException {
        final var pathToFile = "outerFolder/innerFolder/thefile.pdf";

        Mockito.when(
            this.s3OutPort.getPresignedUrl(
                new FileReference(BUCKET, pathToFile),
                PresignedUrl.Action.GET,
                Duration.ofMinutes(FILE_EXPIRATION_DATE)
            )
        ).thenThrow(new S3Exception("outermessage", new Exception("not found")));
        final var filePathModel1 = new FilepathModel();
        filePathModel1.setPathToFile(pathToFile);
        Assertions.assertThrows(FileHandlingFailedException.class, () ->
            this.presignedUrlCreationService.getFile(filePathModel1)
        );
        Mockito.reset(this.s3OutPort);
    }

    @Test
    void saveFile() throws S3Exception, FileHandlingFailedException, MalformedURLException {
        final var pathToFile = "outerFolder/innerFolder/thefile.pdf";

        final var url =
            "https://storage.de/The-Bucket/outerFolder/innerFolder/thefile.pdf?abc=abcdf4sfskhsdfsfddsghjve884545klnfgv";
        final PresignedUrl presignedUrl = new PresignedUrl(new URL(url), pathToFile, PresignedUrl.Action.PUT);

        Mockito.when(
            this.s3OutPort.getPresignedUrl(
                new FileReference(BUCKET, pathToFile),
                PresignedUrl.Action.PUT,
                Duration.ofMinutes(FILE_EXPIRATION_DATE)
            )
        ).thenReturn(presignedUrl);

        final var expected = new PresignedUrlModel();
        expected.setHttpMethodToUse("PUT");
        expected.setUrl(
            "https://storage.de/The-Bucket/outerFolder/innerFolder/thefile.pdf?abc=abcdf4sfskhsdfsfddsghjve884545klnfgv"
        );

        final var filePathModel = new FilepathModel();
        filePathModel.setPathToFile(pathToFile);
        final var result = this.presignedUrlCreationService.saveFile(filePathModel);
        assertThat(result, is(expected));
    }

    @Test
    void saveFileException() throws S3Exception, FileHandlingFailedException {
        final var pathToFile = "outerFolder/innerFolder/thefile.pdf";

        Mockito.when(
            this.s3OutPort.getPresignedUrl(
                new FileReference(BUCKET, pathToFile),
                PresignedUrl.Action.PUT,
                Duration.ofMinutes(FILE_EXPIRATION_DATE)
            )
        ).thenThrow(new S3Exception("outermessage", new Exception("not found")));
        final var filePathModel1 = new FilepathModel();
        filePathModel1.setPathToFile(pathToFile);
        Assertions.assertThrows(FileHandlingFailedException.class, () ->
            this.presignedUrlCreationService.saveFile(filePathModel1)
        );
        Mockito.reset(this.s3OutPort);
    }

    @Test
    void deleteFile() throws S3Exception, FileHandlingFailedException, MalformedURLException {
        final var pathToFile = "outerFolder/innerFolder/thefile.pdf";

        final var url =
            "https://storage.de/The-Bucket/outerFolder/innerFolder/thefile.pdf?abc=abcdf4sfskhsdfsfddsghjve884545klnfgv";

        final PresignedUrl presignedUrl = new PresignedUrl(new URL(url), pathToFile, PresignedUrl.Action.DELETE);
        Mockito.when(
            this.s3OutPort.getPresignedUrl(
                new FileReference(BUCKET, pathToFile),
                PresignedUrl.Action.DELETE,
                Duration.ofMinutes(FILE_EXPIRATION_DATE)
            )
        ).thenReturn(presignedUrl);

        final var expected = new PresignedUrlModel();
        expected.setHttpMethodToUse("DELETE");
        expected.setUrl(
            "https://storage.de/The-Bucket/outerFolder/innerFolder/thefile.pdf?abc=abcdf4sfskhsdfsfddsghjve884545klnfgv"
        );

        final var filePathModel = new FilepathModel();
        filePathModel.setPathToFile(pathToFile);

        final var result = this.presignedUrlCreationService.deleteFile(filePathModel);
        assertThat(result, is(expected));
    }

    @Test
    void deleteFileException() throws S3Exception, FileHandlingFailedException, MalformedURLException {
        final var pathToFile = "outerFolder/innerFolder/thefile.pdf";

        Mockito.when(
            this.s3OutPort.getPresignedUrl(
                new FileReference(BUCKET, pathToFile),
                PresignedUrl.Action.DELETE,
                Duration.ofMinutes(FILE_EXPIRATION_DATE)
            )
        ).thenThrow(new S3Exception("outermessage", new Exception("not found")));

        final var filePathModel1 = new FilepathModel();
        filePathModel1.setPathToFile(pathToFile);
        Assertions.assertThrows(FileHandlingFailedException.class, () ->
            this.presignedUrlCreationService.deleteFile(filePathModel1)
        );
        Mockito.reset(this.s3OutPort);
    }
}
