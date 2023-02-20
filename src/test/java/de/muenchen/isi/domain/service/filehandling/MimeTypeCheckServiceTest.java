package de.muenchen.isi.domain.service.filehandling;

import org.apache.tika.mime.MimeTypeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MimeTypeCheckServiceTest {

    private final MimeTypeCheckService mimeTypeCheckService = new MimeTypeCheckService();


    @Test
    void getMediaTypeOfFileAndCloseStream() throws IOException, MimeTypeException {
        final InputStream file = this.getClass().getClassLoader().getResourceAsStream("dummy_for_test.pdf");
        final var result = this.mimeTypeCheckService.getMediaTypeInformationOfFileAndCloseStream(file);

        final var expected = new MimeTypeCheckService.MediaTypeInformation();
        expected.setType("application/pdf");
        expected.setDescription("Portable Document Format");
        expected.setAcronym("PDF");

        assertThat(
                result,
                is(expected)
        );

    }


}