package de.muenchen.isi.domain.service.stammdaten;

import de.muenchen.isi.domain.model.stammdaten.FileInformationModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FileInformationStammServiceTest {

    private FileInformationStammService fileInformationStammService;

    @BeforeEach
    public void beforeEach() {
        this.fileInformationStammService = new FileInformationStammService(1024L, 30L);
    }

    @Test
    void getAllowedFileExtensions() {
        final var expected = new FileInformationModel();
        expected.setMaxFileSizeBytes(1024L);
        expected.setMaxNumberOfFiles(30L);

        assertThat(
                this.fileInformationStammService.getFileInformation(),
                is(expected)
        );
    }

}
