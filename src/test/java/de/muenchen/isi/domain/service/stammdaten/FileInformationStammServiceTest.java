package de.muenchen.isi.domain.service.stammdaten;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.model.stammdaten.FileInformationModel;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FileInformationStammServiceTest {

    private FileInformationStammService fileInformationStammService;

    @BeforeEach
    public void beforeEach() {
        this.fileInformationStammService = new FileInformationStammService(1024L, 30L, List.of("application/pdf"));
    }

    @Test
    void getAllowedFileExtensions() {
        final var expected = new FileInformationModel();
        expected.setMaxFileSizeBytes(1024L);
        expected.setMaxNumberOfFiles(30L);
        expected.setAllowedMimeTypes(List.of("application/pdf"));
        assertThat(this.fileInformationStammService.getFileInformation(), is(expected));
    }
}
