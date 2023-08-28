package de.muenchen.isi.domain.service.filehandling;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.MimeTypeExtractionFailedException;
import de.muenchen.isi.domain.exception.MimeTypeNotAllowedException;
import de.muenchen.isi.domain.model.filehandling.FilepathModel;
import de.muenchen.isi.domain.model.filehandling.MimeTypeInformationModel;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageClientErrorException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageServerErrorException;
import io.muenchendigital.digiwf.s3.integration.client.exception.PropertyNotSetException;
import io.muenchendigital.digiwf.s3.integration.client.repository.DocumentStorageFileRepository;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
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

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MimeTypeServiceTest {

    @Mock
    private DocumentStorageFileRepository documentStorageFileRepository;

    private MimeTypeService mimeTypeService;

    @BeforeEach
    public void beforeEach() {
        this.mimeTypeService = new MimeTypeService(this.documentStorageFileRepository, 5, List.of("application/pdf"));
        Mockito.reset(this.documentStorageFileRepository);
    }

    @Test
    void extractMediaTypeInformationForAllowedMediaType()
        throws DocumentStorageException, PropertyNotSetException, DocumentStorageClientErrorException, DocumentStorageServerErrorException, FileHandlingWithS3FailedException, FileHandlingFailedException, MimeTypeExtractionFailedException, MimeTypeNotAllowedException {
        InputStream file = this.getClass().getClassLoader().getResourceAsStream("pdf_for_test.pdf");
        Mockito
            .when(this.documentStorageFileRepository.getFileInputStream("pathToFile/pdf_for_test.pdf", 5))
            .thenReturn(file);

        var filePathModel = new FilepathModel();
        filePathModel.setPathToFile("pathToFile/pdf_for_test.pdf");

        final var result = this.mimeTypeService.extractMediaTypeInformationForAllowedMediaType(filePathModel);

        final var expected = new MimeTypeInformationModel();
        expected.setType("application/pdf");
        expected.setDescription("Portable Document Format");
        expected.setAcronym("PDF");

        assertThat(result, is(expected));
        Mockito
            .verify(this.documentStorageFileRepository, Mockito.times(0))
            .deleteFile(Mockito.any(String.class), Mockito.any(Integer.class));
        Mockito.reset(this.documentStorageFileRepository);

        file = this.getClass().getClassLoader().getResourceAsStream("svg_for_test.svg");
        Mockito
            .when(this.documentStorageFileRepository.getFileInputStream("pathToFile/svg_for_test.svg", 5))
            .thenReturn(file);

        filePathModel = new FilepathModel();
        filePathModel.setPathToFile("pathToFile/svg_for_test.svg");

        try {
            this.mimeTypeService.extractMediaTypeInformationForAllowedMediaType(filePathModel);
            Assertions.fail();
        } catch (final MimeTypeNotAllowedException exception) {
            assertThat(
                "Das Hochladen der Datei svg_for_test.svg des Typs SVG ist nicht erlaubt.",
                is(exception.getMessage())
            );
        }
        Mockito
            .verify(this.documentStorageFileRepository, Mockito.times(1))
            .deleteFile(Mockito.any(String.class), Mockito.any(Integer.class));
    }

    @Test
    void extractMediaTypeInformation()
        throws DocumentStorageException, PropertyNotSetException, DocumentStorageClientErrorException, DocumentStorageServerErrorException, FileHandlingWithS3FailedException, FileHandlingFailedException, MimeTypeExtractionFailedException {
        final InputStream file = this.getClass().getClassLoader().getResourceAsStream("pdf_for_test.pdf");
        Mockito
            .when(this.documentStorageFileRepository.getFileInputStream("pathToFile/pdf_for_test.pdf", 5))
            .thenReturn(file);

        final var filePathModel = new FilepathModel();
        filePathModel.setPathToFile("pathToFile/pdf_for_test.pdf");

        final var result = this.mimeTypeService.extractMediaTypeInformation(filePathModel);

        final var expected = new MimeTypeInformationModel();
        expected.setType("application/pdf");
        expected.setDescription("Portable Document Format");
        expected.setAcronym("PDF");

        assertThat(result, is(expected));

        Mockito
            .verify(this.documentStorageFileRepository, Mockito.times(1))
            .getFileInputStream("pathToFile/pdf_for_test.pdf", 5);

        // Prüfung ob InputStream geschlossen.
        try {
            file.readAllBytes();
            Assertions.fail();
        } catch (final IOException exception) {
            assertThat(exception.getMessage(), is("Stream closed"));
        }
    }

    @Test
    void extractMediaTypeInformationException()
        throws DocumentStorageException, PropertyNotSetException, DocumentStorageClientErrorException, DocumentStorageServerErrorException, FileHandlingWithS3FailedException, FileHandlingFailedException, MimeTypeExtractionFailedException {
        final var filePathModel = new FilepathModel();
        filePathModel.setPathToFile("pathToFile/pdf_for_test.pdf");

        Mockito
            .when(this.documentStorageFileRepository.getFileInputStream("pathToFile/pdf_for_test.pdf", 5))
            .thenThrow(new DocumentStorageException("outermessage", new Exception("innermessage")));
        Assertions.assertThrows(
            FileHandlingFailedException.class,
            () -> this.mimeTypeService.extractMediaTypeInformation(filePathModel)
        );
        Mockito
            .verify(this.documentStorageFileRepository, Mockito.times(1))
            .getFileInputStream("pathToFile/pdf_for_test.pdf", 5);
        Mockito.reset(this.documentStorageFileRepository);

        Mockito
            .when(this.documentStorageFileRepository.getFileInputStream("pathToFile/pdf_for_test.pdf", 5))
            .thenThrow(new PropertyNotSetException("outermessage"));
        Assertions.assertThrows(
            FileHandlingFailedException.class,
            () -> this.mimeTypeService.extractMediaTypeInformation(filePathModel)
        );
        Mockito
            .verify(this.documentStorageFileRepository, Mockito.times(1))
            .getFileInputStream("pathToFile/pdf_for_test.pdf", 5);
        Mockito.reset(this.documentStorageFileRepository);

        Mockito
            .when(this.documentStorageFileRepository.getFileInputStream("pathToFile/pdf_for_test.pdf", 5))
            .thenThrow(
                new DocumentStorageClientErrorException(
                    "outermessage",
                    new HttpClientErrorException(HttpStatus.BAD_REQUEST)
                )
            );
        Assertions.assertThrows(
            FileHandlingWithS3FailedException.class,
            () -> this.mimeTypeService.extractMediaTypeInformation(filePathModel)
        );
        Mockito
            .verify(this.documentStorageFileRepository, Mockito.times(1))
            .getFileInputStream("pathToFile/pdf_for_test.pdf", 5);
        Mockito.reset(this.documentStorageFileRepository);

        Mockito
            .when(this.documentStorageFileRepository.getFileInputStream("pathToFile/pdf_for_test.pdf", 5))
            .thenThrow(
                new DocumentStorageServerErrorException(
                    "outermessage",
                    new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)
                )
            );
        Assertions.assertThrows(
            FileHandlingWithS3FailedException.class,
            () -> this.mimeTypeService.extractMediaTypeInformation(filePathModel)
        );
        Mockito
            .verify(this.documentStorageFileRepository, Mockito.times(1))
            .getFileInputStream("pathToFile/pdf_for_test.pdf", 5);
        Mockito.reset(this.documentStorageFileRepository);
    }

    @Test
    void getInputStream()
        throws DocumentStorageException, PropertyNotSetException, DocumentStorageClientErrorException, DocumentStorageServerErrorException, FileHandlingWithS3FailedException, FileHandlingFailedException, IOException {
        final InputStream file = this.getClass().getClassLoader().getResourceAsStream("pdf_for_test.pdf");
        Mockito
            .when(this.documentStorageFileRepository.getFileInputStream("pathToFile/pdf_for_test.pdf", 5))
            .thenReturn(file);

        final var filePathModel = new FilepathModel();
        filePathModel.setPathToFile("pathToFile/pdf_for_test.pdf");
        final var result = this.mimeTypeService.getInputStream(filePathModel);

        assertThat(file, is(file));

        Mockito
            .verify(this.documentStorageFileRepository, Mockito.times(1))
            .getFileInputStream("pathToFile/pdf_for_test.pdf", 5);

        file.close();
    }

    @Test
    void getInputStreamException()
        throws DocumentStorageException, PropertyNotSetException, DocumentStorageClientErrorException, DocumentStorageServerErrorException {
        final var filePathModel = new FilepathModel();
        filePathModel.setPathToFile("pathToFile/pdf_for_test.pdf");

        Mockito
            .when(this.documentStorageFileRepository.getFileInputStream("pathToFile/pdf_for_test.pdf", 5))
            .thenThrow(new DocumentStorageException("outermessage", new Exception("innermessage")));
        Assertions.assertThrows(
            FileHandlingFailedException.class,
            () -> this.mimeTypeService.getInputStream(filePathModel)
        );
        Mockito
            .verify(this.documentStorageFileRepository, Mockito.times(1))
            .getFileInputStream("pathToFile/pdf_for_test.pdf", 5);
        Mockito.reset(this.documentStorageFileRepository);

        Mockito
            .when(this.documentStorageFileRepository.getFileInputStream("pathToFile/pdf_for_test.pdf", 5))
            .thenThrow(new PropertyNotSetException("outermessage"));
        Assertions.assertThrows(
            FileHandlingFailedException.class,
            () -> this.mimeTypeService.getInputStream(filePathModel)
        );
        Mockito
            .verify(this.documentStorageFileRepository, Mockito.times(1))
            .getFileInputStream("pathToFile/pdf_for_test.pdf", 5);
        Mockito.reset(this.documentStorageFileRepository);

        Mockito
            .when(this.documentStorageFileRepository.getFileInputStream("pathToFile/pdf_for_test.pdf", 5))
            .thenThrow(
                new DocumentStorageClientErrorException(
                    "outermessage",
                    new HttpClientErrorException(HttpStatus.BAD_REQUEST)
                )
            );
        Assertions.assertThrows(
            FileHandlingWithS3FailedException.class,
            () -> this.mimeTypeService.getInputStream(filePathModel)
        );
        Mockito
            .verify(this.documentStorageFileRepository, Mockito.times(1))
            .getFileInputStream("pathToFile/pdf_for_test.pdf", 5);
        Mockito.reset(this.documentStorageFileRepository);

        Mockito
            .when(this.documentStorageFileRepository.getFileInputStream("pathToFile/pdf_for_test.pdf", 5))
            .thenThrow(
                new DocumentStorageServerErrorException(
                    "outermessage",
                    new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)
                )
            );
        Assertions.assertThrows(
            FileHandlingWithS3FailedException.class,
            () -> this.mimeTypeService.getInputStream(filePathModel)
        );
        Mockito
            .verify(this.documentStorageFileRepository, Mockito.times(1))
            .getFileInputStream("pathToFile/pdf_for_test.pdf", 5);
        Mockito.reset(this.documentStorageFileRepository);
    }

    @Test
    void deleteFile()
        throws DocumentStorageException, PropertyNotSetException, DocumentStorageClientErrorException, DocumentStorageServerErrorException, FileHandlingWithS3FailedException, FileHandlingFailedException, IOException {
        final var filePathModel = new FilepathModel();
        filePathModel.setPathToFile("pathToFile/pdf_for_test.pdf");
        this.mimeTypeService.deleteFile(filePathModel);

        Mockito
            .verify(this.documentStorageFileRepository, Mockito.times(1))
            .deleteFile("pathToFile/pdf_for_test.pdf", 5);
    }

    @Test
    void deleteFileException()
        throws DocumentStorageException, PropertyNotSetException, DocumentStorageClientErrorException, DocumentStorageServerErrorException {
        final var filePathModel = new FilepathModel();
        filePathModel.setPathToFile("pathToFile/pdf_for_test.pdf");

        Mockito
            .doThrow(new DocumentStorageException("outermessage", new Exception("innermessage")))
            .when(this.documentStorageFileRepository)
            .deleteFile("pathToFile/pdf_for_test.pdf", 5);
        Assertions.assertThrows(
            FileHandlingFailedException.class,
            () -> this.mimeTypeService.deleteFile(filePathModel)
        );
        Mockito
            .verify(this.documentStorageFileRepository, Mockito.times(1))
            .deleteFile("pathToFile/pdf_for_test.pdf", 5);
        Mockito.reset(this.documentStorageFileRepository);

        Mockito
            .doThrow(new PropertyNotSetException("outermessage"))
            .when(this.documentStorageFileRepository)
            .deleteFile("pathToFile/pdf_for_test.pdf", 5);
        Assertions.assertThrows(
            FileHandlingFailedException.class,
            () -> this.mimeTypeService.deleteFile(filePathModel)
        );
        Mockito
            .verify(this.documentStorageFileRepository, Mockito.times(1))
            .deleteFile("pathToFile/pdf_for_test.pdf", 5);
        Mockito.reset(this.documentStorageFileRepository);

        Mockito
            .doThrow(
                new DocumentStorageClientErrorException(
                    "outermessage",
                    new HttpClientErrorException(HttpStatus.BAD_REQUEST)
                )
            )
            .when(this.documentStorageFileRepository)
            .deleteFile("pathToFile/pdf_for_test.pdf", 5);
        Assertions.assertThrows(
            FileHandlingWithS3FailedException.class,
            () -> this.mimeTypeService.deleteFile(filePathModel)
        );
        Mockito
            .verify(this.documentStorageFileRepository, Mockito.times(1))
            .deleteFile("pathToFile/pdf_for_test.pdf", 5);
        Mockito.reset(this.documentStorageFileRepository);

        Mockito
            .doThrow(
                new DocumentStorageServerErrorException(
                    "outermessage",
                    new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)
                )
            )
            .when(this.documentStorageFileRepository)
            .deleteFile("pathToFile/pdf_for_test.pdf", 5);
        Assertions.assertThrows(
            FileHandlingWithS3FailedException.class,
            () -> this.mimeTypeService.deleteFile(filePathModel)
        );
        Mockito
            .verify(this.documentStorageFileRepository, Mockito.times(1))
            .deleteFile("pathToFile/pdf_for_test.pdf", 5);
        Mockito.reset(this.documentStorageFileRepository);
    }

    @Test
    void extractMediaTypeInformationOfFileAndCloseStream() throws MimeTypeExtractionFailedException {
        InputStream file = this.getClass().getClassLoader().getResourceAsStream("pdf_for_test.pdf");
        var result = this.mimeTypeService.extractMediaTypeInformationOfFileAndCloseStream(file);

        var expected = new MimeTypeInformationModel();
        expected.setType("application/pdf");
        expected.setDescription("Portable Document Format");
        expected.setAcronym("PDF");

        assertThat(result, is(expected));

        file = this.getClass().getClassLoader().getResourceAsStream("svg_for_test.svg");
        result = this.mimeTypeService.extractMediaTypeInformationOfFileAndCloseStream(file);

        expected = new MimeTypeInformationModel();
        expected.setType("image/svg+xml");
        expected.setDescription("Scalable Vector Graphics");
        expected.setAcronym("SVG");

        assertThat(result, is(expected));

        // Prüfung ob InputStream geschlossen.
        try {
            file.readAllBytes();
            Assertions.fail();
        } catch (final IOException exception) {
            assertThat(exception.getMessage(), is("Stream closed"));
        }
    }

    @Test
    void getAcronymOrDescriptionWhenAcronymEmptyOrTypeWhenDescriptionEmpty() {
        var mimeTypeInformationModel = new MimeTypeInformationModel();
        mimeTypeInformationModel.setAcronym("Acronym");
        String result =
            this.mimeTypeService.getAcronymOrDescriptionWhenAcronymEmptyOrTypeWhenDescriptionEmpty(
                    mimeTypeInformationModel
                );
        assertThat(result, is("Acronym"));

        mimeTypeInformationModel = new MimeTypeInformationModel();
        mimeTypeInformationModel.setAcronym("");
        mimeTypeInformationModel.setDescription("Description");
        result =
        this.mimeTypeService.getAcronymOrDescriptionWhenAcronymEmptyOrTypeWhenDescriptionEmpty(
                mimeTypeInformationModel
            );
        assertThat(result, is("Description"));

        mimeTypeInformationModel = new MimeTypeInformationModel();
        mimeTypeInformationModel.setAcronym(null);
        mimeTypeInformationModel.setDescription("Description");
        result =
        this.mimeTypeService.getAcronymOrDescriptionWhenAcronymEmptyOrTypeWhenDescriptionEmpty(
                mimeTypeInformationModel
            );
        assertThat(result, is("Description"));

        mimeTypeInformationModel = new MimeTypeInformationModel();
        mimeTypeInformationModel.setAcronym("");
        mimeTypeInformationModel.setDescription("");
        mimeTypeInformationModel.setType("Type");
        result =
        this.mimeTypeService.getAcronymOrDescriptionWhenAcronymEmptyOrTypeWhenDescriptionEmpty(
                mimeTypeInformationModel
            );
        assertThat(result, is("Type"));

        mimeTypeInformationModel = new MimeTypeInformationModel();
        mimeTypeInformationModel.setAcronym("");
        mimeTypeInformationModel.setDescription(null);
        mimeTypeInformationModel.setType("Type");
        result =
        this.mimeTypeService.getAcronymOrDescriptionWhenAcronymEmptyOrTypeWhenDescriptionEmpty(
                mimeTypeInformationModel
            );
        assertThat(result, is("Type"));

        mimeTypeInformationModel = new MimeTypeInformationModel();
        mimeTypeInformationModel.setAcronym(null);
        mimeTypeInformationModel.setDescription("");
        mimeTypeInformationModel.setType("Type");
        result =
        this.mimeTypeService.getAcronymOrDescriptionWhenAcronymEmptyOrTypeWhenDescriptionEmpty(
                mimeTypeInformationModel
            );
        assertThat(result, is("Type"));

        mimeTypeInformationModel = new MimeTypeInformationModel();
        mimeTypeInformationModel.setAcronym(null);
        mimeTypeInformationModel.setDescription(null);
        mimeTypeInformationModel.setType("Type");
        result =
        this.mimeTypeService.getAcronymOrDescriptionWhenAcronymEmptyOrTypeWhenDescriptionEmpty(
                mimeTypeInformationModel
            );
        assertThat(result, is("Type"));
    }
}
