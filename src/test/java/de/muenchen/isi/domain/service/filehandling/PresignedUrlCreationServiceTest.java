package de.muenchen.isi.domain.service.filehandling;

import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.model.filehandling.FilepathModel;
import de.muenchen.isi.domain.model.filehandling.PresignedUrlModel;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageClientErrorException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageServerErrorException;
import io.muenchendigital.digiwf.s3.integration.client.exception.PropertyNotSetException;
import io.muenchendigital.digiwf.s3.integration.client.repository.presignedurl.PresignedUrlRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import reactor.core.publisher.Mono;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PresignedUrlCreationServiceTest {

    @Mock
    private PresignedUrlRepository presignedUrlRepository;

    private PresignedUrlCreationService presignedUrlCreationService;

    @BeforeEach
    public void beforeEach() {
        this.presignedUrlCreationService = new PresignedUrlCreationService(
                this.presignedUrlRepository,
                10
        );
        Mockito.reset(this.presignedUrlRepository);
    }

    @Test
    void getFile() throws DocumentStorageException, PropertyNotSetException, DocumentStorageClientErrorException, DocumentStorageServerErrorException, FileHandlingWithS3FailedException, FileHandlingFailedException {
        final var pathToFile = "outerFolder/innerFolder/thefile.pdf";

        final var presignedUrl = "https://storage.de/The-Bucket/outerFolder/innerFolder/thefile.pdf?abc=abcdf4sfskhsdfsfddsghjve884545klnfgv";

        Mockito.when(this.presignedUrlRepository.getPresignedUrlGetFile(pathToFile, 10)).thenReturn(Mono.just(presignedUrl));

        final var expected = new PresignedUrlModel();
        expected.setHttpMethodToUse("GET");
        expected.setUrl("https://storage.de/The-Bucket/outerFolder/innerFolder/thefile.pdf?abc=abcdf4sfskhsdfsfddsghjve884545klnfgv");

        final var filePathModel = new FilepathModel();
        filePathModel.setPathToFile(pathToFile);

        assertThat(
                this.presignedUrlCreationService.getFile(filePathModel),
                is(expected)
        );
    }

    @Test
    void getFileException() throws DocumentStorageException, PropertyNotSetException, DocumentStorageClientErrorException, DocumentStorageServerErrorException, FileHandlingWithS3FailedException, FileHandlingFailedException {
        final var pathToFile = "outerFolder/innerFolder/thefile.pdf";

        Mockito.when(this.presignedUrlRepository.getPresignedUrlGetFile(pathToFile, 10)).thenThrow(new DocumentStorageClientErrorException("outermessage", new HttpClientErrorException(HttpStatus.NOT_FOUND)));
        final var filePathModel1 = new FilepathModel();
        filePathModel1.setPathToFile(pathToFile);
        Assertions.assertThrows(FileHandlingWithS3FailedException.class, () -> this.presignedUrlCreationService.getFile(filePathModel1));
        Mockito.reset(this.presignedUrlRepository);

        Mockito.when(this.presignedUrlRepository.getPresignedUrlGetFile(pathToFile, 10)).thenThrow(new DocumentStorageClientErrorException("outermessage", new HttpClientErrorException(HttpStatus.NOT_FOUND)));
        try {
            this.presignedUrlCreationService.getFile(filePathModel1);
        } catch (final FileHandlingWithS3FailedException exception) {
            assertThat(
                    exception.getStatusCode(),
                    is(HttpStatus.NOT_FOUND)
            );
        }
        Mockito.reset(this.presignedUrlRepository);

        Mockito.when(this.presignedUrlRepository.getPresignedUrlGetFile(pathToFile, 10)).thenThrow(new DocumentStorageServerErrorException("outermessage", new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)));
        final var filePathModel2 = new FilepathModel();
        filePathModel2.setPathToFile(pathToFile);
        Assertions.assertThrows(FileHandlingWithS3FailedException.class, () -> this.presignedUrlCreationService.getFile(filePathModel2));
        Mockito.reset(this.presignedUrlRepository);

        Mockito.when(this.presignedUrlRepository.getPresignedUrlGetFile(pathToFile, 10)).thenThrow(new DocumentStorageServerErrorException("outermessage", new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)));
        try {
            this.presignedUrlCreationService.getFile(filePathModel1);
        } catch (final FileHandlingWithS3FailedException exception) {
            assertThat(
                    exception.getStatusCode(),
                    is(HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
        Mockito.reset(this.presignedUrlRepository);

        Mockito.when(this.presignedUrlRepository.getPresignedUrlGetFile(pathToFile, 10)).thenThrow(new DocumentStorageException("outermessage", new Exception("innermessage")));
        final var filePathModel3 = new FilepathModel();
        filePathModel3.setPathToFile(pathToFile);
        Assertions.assertThrows(FileHandlingFailedException.class, () -> this.presignedUrlCreationService.getFile(filePathModel3));
        Mockito.reset(this.presignedUrlRepository);

        Mockito.when(this.presignedUrlRepository.getPresignedUrlGetFile(pathToFile, 10)).thenThrow(new PropertyNotSetException("outermessage"));
        final var filePathModel4 = new FilepathModel();
        filePathModel4.setPathToFile(pathToFile);
        Assertions.assertThrows(FileHandlingFailedException.class, () -> this.presignedUrlCreationService.getFile(filePathModel4));
        Mockito.reset(this.presignedUrlRepository);
    }

    @Test
    void saveFile() throws DocumentStorageException, PropertyNotSetException, DocumentStorageClientErrorException, DocumentStorageServerErrorException, FileHandlingWithS3FailedException, FileHandlingFailedException {
        final var pathToFile = "outerFolder/innerFolder/thefile.pdf";

        final var presigneUrl = "https://storage.de/The-Bucket/outerFolder/innerFolder/thefile.pdf?abc=abcdf4sfskhsdfsfddsghjve884545klnfgv";

        Mockito.when(this.presignedUrlRepository.getPresignedUrlSaveFile(pathToFile, 10, null)).thenReturn(presigneUrl);

        final var expected = new PresignedUrlModel();
        expected.setHttpMethodToUse("PUT");
        expected.setUrl("https://storage.de/The-Bucket/outerFolder/innerFolder/thefile.pdf?abc=abcdf4sfskhsdfsfddsghjve884545klnfgv");

        final var filePathModel = new FilepathModel();
        filePathModel.setPathToFile(pathToFile);

        assertThat(
                this.presignedUrlCreationService.saveFile(filePathModel),
                is(expected)
        );
    }

    @Test
    void saveFileException() throws DocumentStorageException, PropertyNotSetException, DocumentStorageClientErrorException, DocumentStorageServerErrorException, FileHandlingWithS3FailedException, FileHandlingFailedException {
        final var pathToFile = "outerFolder/innerFolder/thefile.pdf";

        Mockito.when(this.presignedUrlRepository.getPresignedUrlSaveFile(pathToFile, 10, null)).thenThrow(new DocumentStorageClientErrorException("outermessage", new HttpClientErrorException(HttpStatus.NOT_FOUND)));
        final var filePathModel1 = new FilepathModel();
        filePathModel1.setPathToFile(pathToFile);
        Assertions.assertThrows(FileHandlingWithS3FailedException.class, () -> this.presignedUrlCreationService.saveFile(filePathModel1));
        Mockito.reset(this.presignedUrlRepository);

        Mockito.when(this.presignedUrlRepository.getPresignedUrlGetFile(pathToFile, 10)).thenThrow(new DocumentStorageClientErrorException("outermessage", new HttpClientErrorException(HttpStatus.NOT_FOUND)));
        try {
            this.presignedUrlCreationService.getFile(filePathModel1);
        } catch (final FileHandlingWithS3FailedException exception) {
            assertThat(
                    exception.getStatusCode(),
                    is(HttpStatus.NOT_FOUND)
            );
        }
        Mockito.reset(this.presignedUrlRepository);

        Mockito.when(this.presignedUrlRepository.getPresignedUrlSaveFile(pathToFile, 10, null)).thenThrow(new DocumentStorageServerErrorException("outermessage", new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)));
        final var filePathModel2 = new FilepathModel();
        filePathModel2.setPathToFile(pathToFile);
        Assertions.assertThrows(FileHandlingWithS3FailedException.class, () -> this.presignedUrlCreationService.saveFile(filePathModel2));
        Mockito.reset(this.presignedUrlRepository);

        Mockito.when(this.presignedUrlRepository.getPresignedUrlGetFile(pathToFile, 10)).thenThrow(new DocumentStorageServerErrorException("outermessage", new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)));
        try {
            this.presignedUrlCreationService.getFile(filePathModel1);
        } catch (final FileHandlingWithS3FailedException exception) {
            assertThat(
                    exception.getStatusCode(),
                    is(HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
        Mockito.reset(this.presignedUrlRepository);

        Mockito.when(this.presignedUrlRepository.getPresignedUrlSaveFile(pathToFile, 10, null)).thenThrow(new DocumentStorageException("outermessage", new Exception("innermessage")));
        final var filePathModel3 = new FilepathModel();
        filePathModel3.setPathToFile(pathToFile);
        Assertions.assertThrows(FileHandlingFailedException.class, () -> this.presignedUrlCreationService.saveFile(filePathModel3));
        Mockito.reset(this.presignedUrlRepository);

        Mockito.when(this.presignedUrlRepository.getPresignedUrlSaveFile(pathToFile, 10, null)).thenThrow(new PropertyNotSetException("outermessage"));
        final var filePathModel4 = new FilepathModel();
        filePathModel4.setPathToFile(pathToFile);
        Assertions.assertThrows(FileHandlingFailedException.class, () -> this.presignedUrlCreationService.saveFile(filePathModel4));
        Mockito.reset(this.presignedUrlRepository);
    }

    @Test
    void deleteFile() throws DocumentStorageException, PropertyNotSetException, DocumentStorageClientErrorException, DocumentStorageServerErrorException, FileHandlingWithS3FailedException, FileHandlingFailedException {
        final var pathToFile = "outerFolder/innerFolder/thefile.pdf";

        final var presigneUrl = "https://storage.de/The-Bucket/outerFolder/innerFolder/thefile.pdf?abc=abcdf4sfskhsdfsfddsghjve884545klnfgv";

        Mockito.when(this.presignedUrlRepository.getPresignedUrlDeleteFile(pathToFile, 10)).thenReturn(presigneUrl);

        final var expected = new PresignedUrlModel();
        expected.setHttpMethodToUse("DELETE");
        expected.setUrl("https://storage.de/The-Bucket/outerFolder/innerFolder/thefile.pdf?abc=abcdf4sfskhsdfsfddsghjve884545klnfgv");

        final var filePathModel = new FilepathModel();
        filePathModel.setPathToFile(pathToFile);

        assertThat(
                this.presignedUrlCreationService.deleteFile(filePathModel),
                is(expected)
        );
    }

    @Test
    void deleteFileException() throws DocumentStorageException, PropertyNotSetException, DocumentStorageClientErrorException, DocumentStorageServerErrorException, FileHandlingWithS3FailedException, FileHandlingFailedException {
        final var pathToFile = "outerFolder/innerFolder/thefile.pdf";

        Mockito.when(this.presignedUrlRepository.getPresignedUrlDeleteFile(pathToFile, 10)).thenThrow(new DocumentStorageClientErrorException("outermessage", new HttpClientErrorException(HttpStatus.NOT_FOUND)));
        final var filePathModel1 = new FilepathModel();
        filePathModel1.setPathToFile(pathToFile);
        Assertions.assertThrows(FileHandlingWithS3FailedException.class, () -> this.presignedUrlCreationService.deleteFile(filePathModel1));
        Mockito.reset(this.presignedUrlRepository);

        Mockito.when(this.presignedUrlRepository.getPresignedUrlGetFile(pathToFile, 10)).thenThrow(new DocumentStorageClientErrorException("outermessage", new HttpClientErrorException(HttpStatus.NOT_FOUND)));
        try {
            this.presignedUrlCreationService.getFile(filePathModel1);
        } catch (final FileHandlingWithS3FailedException exception) {
            assertThat(
                    exception.getStatusCode(),
                    is(HttpStatus.NOT_FOUND)
            );
        }
        Mockito.reset(this.presignedUrlRepository);

        Mockito.when(this.presignedUrlRepository.getPresignedUrlDeleteFile(pathToFile, 10)).thenThrow(new DocumentStorageServerErrorException("outermessage", new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)));
        final var filePathModel2 = new FilepathModel();
        filePathModel2.setPathToFile(pathToFile);
        Assertions.assertThrows(FileHandlingWithS3FailedException.class, () -> this.presignedUrlCreationService.deleteFile(filePathModel2));
        Mockito.reset(this.presignedUrlRepository);

        Mockito.when(this.presignedUrlRepository.getPresignedUrlGetFile(pathToFile, 10)).thenThrow(new DocumentStorageServerErrorException("outermessage", new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)));
        try {
            this.presignedUrlCreationService.getFile(filePathModel1);
        } catch (final FileHandlingWithS3FailedException exception) {
            assertThat(
                    exception.getStatusCode(),
                    is(HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
        Mockito.reset(this.presignedUrlRepository);

        Mockito.when(this.presignedUrlRepository.getPresignedUrlDeleteFile(pathToFile, 10)).thenThrow(new DocumentStorageException("outermessage", new Exception("innermessage")));
        final var filePathModel3 = new FilepathModel();
        filePathModel3.setPathToFile(pathToFile);
        Assertions.assertThrows(FileHandlingFailedException.class, () -> this.presignedUrlCreationService.deleteFile(filePathModel3));
        Mockito.reset(this.presignedUrlRepository);

        Mockito.when(this.presignedUrlRepository.getPresignedUrlDeleteFile(pathToFile, 10)).thenThrow(new PropertyNotSetException("outermessage"));
        final var filePathModel4 = new FilepathModel();
        filePathModel4.setPathToFile(pathToFile);
        Assertions.assertThrows(FileHandlingFailedException.class, () -> this.presignedUrlCreationService.deleteFile(filePathModel4));
        Mockito.reset(this.presignedUrlRepository);
    }

}