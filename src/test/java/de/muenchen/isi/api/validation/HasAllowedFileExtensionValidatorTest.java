package de.muenchen.isi.api.validation;

import de.muenchen.isi.domain.service.stammdaten.FileInformationStammService;
import de.muenchen.isi.infrastructure.repository.stammdaten.MimeTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class HasAllowedFileExtensionValidatorTest {

    private HasAllowedFileExtensionValidator hasAllowedFileExtensionValidator;

    @Mock
    private MimeTypeRepository mimeTypeRepository;

    @BeforeEach
    public void beforeEach() {
        final var fileEndings = List.of(".jpg", ".pdf", " .tif", ".shp.xml");
        final var mimeTypes = List.of("application/pdf");
        final var fileInformationStammService = new FileInformationStammService(fileEndings, mimeTypes, 1024L, 30L, this.mimeTypeRepository);
        this.hasAllowedFileExtensionValidator = new HasAllowedFileExtensionValidator(fileInformationStammService);
    }

    @Test
    void isValid() {
        var pathToFile = "folder/subfolder/theFile.tif";
        assertThat(
                this.hasAllowedFileExtensionValidator.isValid(pathToFile, null),
                is(true)
        );

        pathToFile = "/folder/subfolder/theFile.tif";
        assertThat(
                this.hasAllowedFileExtensionValidator.isValid(pathToFile, null),
                is(true)
        );

        pathToFile = "/folder/subfolder/theFile.TIF";
        assertThat(
                this.hasAllowedFileExtensionValidator.isValid(pathToFile, null),
                is(true)
        );

        pathToFile = "/folder/subfolder/theFile.TiF";
        assertThat(
                this.hasAllowedFileExtensionValidator.isValid(pathToFile, null),
                is(true)
        );

        pathToFile = "/folder/subfolder/theFile.shp.xml";
        assertThat(
                this.hasAllowedFileExtensionValidator.isValid(pathToFile, null),
                is(true)
        );

        pathToFile = "/folder/subfolder/theFile.wrong";
        assertThat(
                this.hasAllowedFileExtensionValidator.isValid(pathToFile, null),
                is(false)
        );

        pathToFile = "/folder/subfolder/theFile";
        assertThat(
                this.hasAllowedFileExtensionValidator.isValid(pathToFile, null),
                is(false)
        );

        pathToFile = "";
        assertThat(
                this.hasAllowedFileExtensionValidator.isValid(pathToFile, null),
                is(false)
        );

        assertThat(
                this.hasAllowedFileExtensionValidator.isValid(null, null),
                is(true)
        );
    }

}
