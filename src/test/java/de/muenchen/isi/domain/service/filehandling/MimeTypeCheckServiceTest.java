package de.muenchen.isi.domain.service.filehandling;

import de.muenchen.isi.domain.exception.MimeTypeExtractionFailedException;
import de.muenchen.isi.domain.model.filehandling.MediaTypeInformationModel;
import io.muenchendigital.digiwf.s3.integration.client.repository.DocumentStorageFileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MimeTypeCheckServiceTest {

    @Mock
    private DocumentStorageFileRepository documentStorageFileRepository;

    private final MimeTypeCheckService mimeTypeCheckService = new MimeTypeCheckService(this.documentStorageFileRepository, 5);

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

    }


}