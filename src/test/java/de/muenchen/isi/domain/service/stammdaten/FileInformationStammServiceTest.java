package de.muenchen.isi.domain.service.stammdaten;

import de.muenchen.isi.domain.model.stammdaten.FileInformationModel;
import de.muenchen.isi.infrastructure.entity.stammdaten.MimeTypeInformation;
import de.muenchen.isi.infrastructure.repository.stammdaten.MimeTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FileInformationStammServiceTest {

    private FileInformationStammService fileInformationStammService;

    @Mock
    private MimeTypeRepository mimeTypeRepository;

    @BeforeEach
    public void beforeEach() {
        final var fileEndings = List.of(".jpg", " .tif ", " ", "");
        final var mimeTypes = List.of("application/pdf");
        this.fileInformationStammService = new FileInformationStammService(fileEndings, mimeTypes, 1024L, 30L, this.mimeTypeRepository);
        Mockito.reset(this.mimeTypeRepository);
    }

    @Test
    void getAllowedFileExtensions() {
        final var expected = new FileInformationModel();
        expected.setMaxFileSizeBytes(1024L);
        expected.setMaxNumberOfFiles(30L);
        expected.setAllowedFileExtensions(List.of(".jpg", ".tif", ".pdf"));
        expected.setAllowedMimeTypes(List.of("application/pdf"));

        final var mimeTypeInfos = new MimeTypeInformation();
        mimeTypeInfos.setMimeType("application/pdf");
        mimeTypeInfos.setFileExtensions(List.of(".pdf"));

        Mockito.when(this.mimeTypeRepository.findAllByMimeTypes(List.of("application/pdf"))).thenReturn(List.of(mimeTypeInfos));

        assertThat(
                this.fileInformationStammService.getFileInformation(),
                is(expected)
        );
    }

    @Test
    void getFileExtensionsFromMimeTypes() {
        final var mimeTypeInfos = new MimeTypeInformation();
        mimeTypeInfos.setMimeType("application/pdf");
        mimeTypeInfos.setFileExtensions(List.of(".pdf"));

        Mockito.when(this.mimeTypeRepository.findAllByMimeTypes(List.of("application/pdf"))).thenReturn(List.of(mimeTypeInfos));

        final Stream<String> fileExtensions = this.fileInformationStammService.getFileExtensionsFromMimeTypes(
                List.of("application/pdf")
        );

        assertThat(
                fileExtensions.collect(Collectors.toList()),
                is(List.of(".pdf"))
        );
    }

}
