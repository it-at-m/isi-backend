package de.muenchen.isi.domain.service.filehandling;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.MimeTypeExtractionFailedException;
import de.muenchen.isi.domain.exception.MimeTypeNotAllowedException;
import de.muenchen.isi.domain.model.filehandling.FilepathModel;
import de.muenchen.isi.domain.model.filehandling.MimeTypeInformationModel;
import de.muenchen.oss.refarch.integration.s3.application.port.out.S3OutPort;
import de.muenchen.oss.refarch.integration.s3.domain.exception.S3Exception;
import de.muenchen.oss.refarch.integration.s3.domain.model.FileReference;
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

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MimeTypeServiceTest {

    @Mock
    private S3OutPort s3OutPort;

    static final String BUCKET = "isi-unittest-bucket";

    private MimeTypeService mimeTypeService;

    @BeforeEach
    public void beforeEach() {
        this.mimeTypeService = new MimeTypeService(BUCKET, s3OutPort, List.of("application/pdf"));
        Mockito.reset(this.s3OutPort);
    }

    @Test
    void extractMediaTypeInformationForAllowedMediaType()
        throws S3Exception, FileHandlingFailedException, MimeTypeExtractionFailedException, MimeTypeNotAllowedException {
        InputStream file = this.getClass().getClassLoader().getResourceAsStream("pdf_for_test.pdf");

        Mockito.when(
            this.s3OutPort.getFileContent(new FileReference(BUCKET, "pathToFile/pdf_for_test.pdf"))
        ).thenReturn(file);

        var filePathModel = new FilepathModel();
        filePathModel.setPathToFile("pathToFile/pdf_for_test.pdf");

        final var result = this.mimeTypeService.extractMediaTypeInformationForAllowedMediaType(filePathModel);

        final var expected = new MimeTypeInformationModel();
        expected.setType("application/pdf");
        expected.setDescription("Portable Document Format");
        expected.setAcronym("PDF");

        assertThat(result, is(expected));
        Mockito.verify(this.s3OutPort, Mockito.times(0)).deleteFile(Mockito.any(FileReference.class));
        Mockito.reset(this.s3OutPort);

        file = this.getClass().getClassLoader().getResourceAsStream("svg_for_test.svg");
        Mockito.when(
            this.s3OutPort.getFileContent(new FileReference(BUCKET, "pathToFile/svg_for_test.svg"))
        ).thenReturn(file);

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
        Mockito.verify(this.s3OutPort, Mockito.times(1)).deleteFile(Mockito.any(FileReference.class));
    }

    @Test
    void extractMediaTypeInformation()
        throws S3Exception, FileHandlingFailedException, MimeTypeExtractionFailedException {
        final InputStream file = this.getClass().getClassLoader().getResourceAsStream("pdf_for_test.pdf");

        Mockito.when(
            this.s3OutPort.getFileContent(new FileReference(BUCKET, "pathToFile/pdf_for_test.pdf"))
        ).thenReturn(file);

        final var filePathModel = new FilepathModel();
        filePathModel.setPathToFile("pathToFile/pdf_for_test.pdf");

        final var result = this.mimeTypeService.extractMediaTypeInformation(filePathModel);

        final var expected = new MimeTypeInformationModel();
        expected.setType("application/pdf");
        expected.setDescription("Portable Document Format");
        expected.setAcronym("PDF");

        assertThat(result, is(expected));

        Mockito.verify(this.s3OutPort, Mockito.times(1)).getFileContent(
            new FileReference(BUCKET, "pathToFile/pdf_for_test.pdf")
        );

        // Prüfung ob InputStream geschlossen.
        try {
            file.readAllBytes();
            Assertions.fail();
        } catch (final IOException exception) {
            assertThat(exception.getMessage(), is("Stream closed"));
        }
    }

    @Test
    void extractMediaTypeInformationException() throws S3Exception {
        final var filePathModel = new FilepathModel();
        filePathModel.setPathToFile("pathToFile/pdf_for_test.pdf");

        Mockito.when(this.s3OutPort.getFileContent(new FileReference(BUCKET, "pathToFile/pdf_for_test.pdf"))).thenThrow(
            new S3Exception("outermessage", new Exception("innermessage"))
        );
        Assertions.assertThrows(FileHandlingFailedException.class, () ->
            this.mimeTypeService.extractMediaTypeInformation(filePathModel)
        );
        Mockito.verify(this.s3OutPort, Mockito.times(1)).getFileContent(
            new FileReference(BUCKET, "pathToFile/pdf_for_test.pdf")
        );
        Mockito.reset(this.s3OutPort);
    }

    @Test
    void getInputStream() throws S3Exception, FileHandlingFailedException, IOException {
        final InputStream file = this.getClass().getClassLoader().getResourceAsStream("pdf_for_test.pdf");
        Mockito.when(
            this.s3OutPort.getFileContent(new FileReference(BUCKET, "pathToFile/pdf_for_test.pdf"))
        ).thenReturn(file);

        final var filePathModel = new FilepathModel();
        filePathModel.setPathToFile("pathToFile/pdf_for_test.pdf");
        final var result = this.mimeTypeService.getInputStream(filePathModel);

        assertThat(file, is(file));

        Mockito.verify(this.s3OutPort, Mockito.times(1)).getFileContent(
            new FileReference(BUCKET, "pathToFile/pdf_for_test.pdf")
        );

        file.close();
    }

    @Test
    void getInputStreamException() throws S3Exception {
        final var filePathModel = new FilepathModel();
        filePathModel.setPathToFile("pathToFile/pdf_for_test.pdf");

        Mockito.when(this.s3OutPort.getFileContent(new FileReference(BUCKET, "pathToFile/pdf_for_test.pdf"))).thenThrow(
            new S3Exception("outermessage", new Exception("innermessage"))
        );
        Assertions.assertThrows(FileHandlingFailedException.class, () ->
            this.mimeTypeService.getInputStream(filePathModel)
        );
        Mockito.verify(this.s3OutPort, Mockito.times(1)).getFileContent(
            new FileReference(BUCKET, "pathToFile/pdf_for_test.pdf")
        );
        Mockito.reset(this.s3OutPort);
    }

    @Test
    void deleteFile() throws S3Exception, FileHandlingFailedException {
        final var filePathModel = new FilepathModel();
        filePathModel.setPathToFile("pathToFile/pdf_for_test.pdf");
        this.mimeTypeService.deleteFile(filePathModel);

        Mockito.verify(this.s3OutPort, Mockito.times(1)).deleteFile(
            new FileReference(BUCKET, "pathToFile/pdf_for_test.pdf")
        );
    }

    @Test
    void deleteFileException() throws S3Exception {
        final var filePathModel = new FilepathModel();
        filePathModel.setPathToFile("pathToFile/pdf_for_test.pdf");

        Mockito.doThrow(new S3Exception("outermessage", new Exception("innermessage")))
            .when(this.s3OutPort)
            .deleteFile(new FileReference(BUCKET, "pathToFile/pdf_for_test.pdf"));
        Assertions.assertThrows(FileHandlingFailedException.class, () ->
            this.mimeTypeService.deleteFile(filePathModel)
        );
        Mockito.verify(this.s3OutPort, Mockito.times(1)).deleteFile(
            new FileReference(BUCKET, "pathToFile/pdf_for_test.pdf")
        );
        Mockito.reset(this.s3OutPort);
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
        String result = this.mimeTypeService.getAcronymOrDescriptionWhenAcronymEmptyOrTypeWhenDescriptionEmpty(
            mimeTypeInformationModel
        );
        assertThat(result, is("Acronym"));

        mimeTypeInformationModel = new MimeTypeInformationModel();
        mimeTypeInformationModel.setAcronym("");
        mimeTypeInformationModel.setDescription("Description");
        result = this.mimeTypeService.getAcronymOrDescriptionWhenAcronymEmptyOrTypeWhenDescriptionEmpty(
            mimeTypeInformationModel
        );
        assertThat(result, is("Description"));

        mimeTypeInformationModel = new MimeTypeInformationModel();
        mimeTypeInformationModel.setAcronym(null);
        mimeTypeInformationModel.setDescription("Description");
        result = this.mimeTypeService.getAcronymOrDescriptionWhenAcronymEmptyOrTypeWhenDescriptionEmpty(
            mimeTypeInformationModel
        );
        assertThat(result, is("Description"));

        mimeTypeInformationModel = new MimeTypeInformationModel();
        mimeTypeInformationModel.setAcronym("");
        mimeTypeInformationModel.setDescription("");
        mimeTypeInformationModel.setType("Type");
        result = this.mimeTypeService.getAcronymOrDescriptionWhenAcronymEmptyOrTypeWhenDescriptionEmpty(
            mimeTypeInformationModel
        );
        assertThat(result, is("Type"));

        mimeTypeInformationModel = new MimeTypeInformationModel();
        mimeTypeInformationModel.setAcronym("");
        mimeTypeInformationModel.setDescription(null);
        mimeTypeInformationModel.setType("Type");
        result = this.mimeTypeService.getAcronymOrDescriptionWhenAcronymEmptyOrTypeWhenDescriptionEmpty(
            mimeTypeInformationModel
        );
        assertThat(result, is("Type"));

        mimeTypeInformationModel = new MimeTypeInformationModel();
        mimeTypeInformationModel.setAcronym(null);
        mimeTypeInformationModel.setDescription("");
        mimeTypeInformationModel.setType("Type");
        result = this.mimeTypeService.getAcronymOrDescriptionWhenAcronymEmptyOrTypeWhenDescriptionEmpty(
            mimeTypeInformationModel
        );
        assertThat(result, is("Type"));

        mimeTypeInformationModel = new MimeTypeInformationModel();
        mimeTypeInformationModel.setAcronym(null);
        mimeTypeInformationModel.setDescription(null);
        mimeTypeInformationModel.setType("Type");
        result = this.mimeTypeService.getAcronymOrDescriptionWhenAcronymEmptyOrTypeWhenDescriptionEmpty(
            mimeTypeInformationModel
        );
        assertThat(result, is("Type"));
    }
}
