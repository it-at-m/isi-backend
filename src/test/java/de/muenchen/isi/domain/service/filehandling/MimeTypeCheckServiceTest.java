package de.muenchen.isi.domain.service.filehandling;

import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.MimeTypeExtractionFailedException;
import de.muenchen.isi.domain.model.filehandling.FilepathModel;
import de.muenchen.isi.domain.model.filehandling.MediaTypeInformationModel;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageClientErrorException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageServerErrorException;
import io.muenchendigital.digiwf.s3.integration.client.exception.PropertyNotSetException;
import io.muenchendigital.digiwf.s3.integration.client.repository.DocumentStorageFileRepository;
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

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MimeTypeCheckServiceTest {

    @Mock
    private DocumentStorageFileRepository documentStorageFileRepository;

    private MimeTypeCheckService mimeTypeCheckService;

    @BeforeEach
    public void beforeEach() {
        this.mimeTypeCheckService = new MimeTypeCheckService(this.documentStorageFileRepository, 5);
        Mockito.reset(this.documentStorageFileRepository);
    }

    @Test
    void getInputStream() throws DocumentStorageException, PropertyNotSetException, DocumentStorageClientErrorException, DocumentStorageServerErrorException, FileHandlingWithS3FailedException, FileHandlingFailedException, IOException {
        final InputStream file = this.getClass().getClassLoader().getResourceAsStream("pdf_for_test.pdf");
        Mockito.when(this.documentStorageFileRepository.getFileInputStream("pathToFile/pdf_for_test.pdf", 5)).thenReturn(file);

        final var filePathModel = new FilepathModel();
        filePathModel.setPathToFile("pathToFile/pdf_for_test.pdf");
        final var result = this.mimeTypeCheckService.getInputStream(filePathModel);

        assertThat(
                file,
                is(file)
        );

        Mockito.verify(this.documentStorageFileRepository, Mockito.times(1)).getFileInputStream("pathToFile/pdf_for_test.pdf", 5);

        file.close();
    }

    @Test
    void getInputStreamException() throws DocumentStorageException, PropertyNotSetException, DocumentStorageClientErrorException, DocumentStorageServerErrorException {
        final var filePathModel = new FilepathModel();
        filePathModel.setPathToFile("pathToFile/pdf_for_test.pdf");

        Mockito.when(this.documentStorageFileRepository.getFileInputStream("pathToFile/pdf_for_test.pdf", 5)).thenThrow(new DocumentStorageException("outermessage", new Exception("innermessage")));
        Assertions.assertThrows(FileHandlingFailedException.class, () -> this.mimeTypeCheckService.getInputStream(filePathModel));
        Mockito.verify(this.documentStorageFileRepository, Mockito.times(1)).getFileInputStream("pathToFile/pdf_for_test.pdf", 5);
        Mockito.reset(this.documentStorageFileRepository);

        Mockito.when(this.documentStorageFileRepository.getFileInputStream("pathToFile/pdf_for_test.pdf", 5)).thenThrow(new PropertyNotSetException("outermessage"));
        Assertions.assertThrows(FileHandlingFailedException.class, () -> this.mimeTypeCheckService.getInputStream(filePathModel));
        Mockito.verify(this.documentStorageFileRepository, Mockito.times(1)).getFileInputStream("pathToFile/pdf_for_test.pdf", 5);
        Mockito.reset(this.documentStorageFileRepository);

        Mockito.when(this.documentStorageFileRepository.getFileInputStream("pathToFile/pdf_for_test.pdf", 5)).thenThrow(new DocumentStorageClientErrorException("outermessage", new HttpClientErrorException(HttpStatus.BAD_REQUEST)));
        Assertions.assertThrows(FileHandlingWithS3FailedException.class, () -> this.mimeTypeCheckService.getInputStream(filePathModel));
        Mockito.verify(this.documentStorageFileRepository, Mockito.times(1)).getFileInputStream("pathToFile/pdf_for_test.pdf", 5);
        Mockito.reset(this.documentStorageFileRepository);

        Mockito.when(this.documentStorageFileRepository.getFileInputStream("pathToFile/pdf_for_test.pdf", 5)).thenThrow(new DocumentStorageServerErrorException("outermessage", new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)));
        Assertions.assertThrows(FileHandlingWithS3FailedException.class, () -> this.mimeTypeCheckService.getInputStream(filePathModel));
        Mockito.verify(this.documentStorageFileRepository, Mockito.times(1)).getFileInputStream("pathToFile/pdf_for_test.pdf", 5);
        Mockito.reset(this.documentStorageFileRepository);
    }

    @Test
    void extractMediaTypeInformationOfFileAndCloseStream() throws MimeTypeExtractionFailedException {
        InputStream file = this.getClass().getClassLoader().getResourceAsStream("pdf_for_test.pdf");
        var result = this.mimeTypeCheckService.extractMediaTypeInformationOfFileAndCloseStream(file);

        var expected = new MediaTypeInformationModel();
        expected.setType("application/pdf");
        expected.setDescription("Portable Document Format");
        expected.setAcronym("PDF");

        assertThat(
                result,
                is(expected)
        );

        file = this.getClass().getClassLoader().getResourceAsStream("svg_for_test.svg");
        result = this.mimeTypeCheckService.extractMediaTypeInformationOfFileAndCloseStream(file);

        expected = new MediaTypeInformationModel();
        expected.setType("image/svg+xml");
        expected.setDescription("Scalable Vector Graphics");
        expected.setAcronym("SVG");

        assertThat(
                result,
                is(expected)
        );

        // Prüfung ob InputStream geschlossen.
        try {
            file.readAllBytes();
            Assertions.fail();
        } catch (final IOException exception) {
            assertThat(
                    exception.getMessage(),
                    is("Stream closed")
            );
        }
    }

}
