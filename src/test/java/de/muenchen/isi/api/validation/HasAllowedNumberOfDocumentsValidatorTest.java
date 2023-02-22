package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.filehandling.DokumentDto;
import de.muenchen.isi.domain.service.stammdaten.FileInformationStammService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class HasAllowedNumberOfDocumentsValidatorTest {

    private HasAllowedNumberOfDocumentsValidator hasAllowedFileExtensionValidator;

    @BeforeEach
    public void beforeEach() {
        final var fileInformationStammService = new FileInformationStammService(1024L, 30L, List.of());
        this.hasAllowedFileExtensionValidator = new HasAllowedNumberOfDocumentsValidator(fileInformationStammService);
    }

    @Test
    void isValid() {
        assertThat(
                this.hasAllowedFileExtensionValidator.isValid(this.erstelleDokumentenListe(30), null),
                is(true)
        );

        assertThat(
                this.hasAllowedFileExtensionValidator.isValid(this.erstelleDokumentenListe(29), null),
                is(true)
        );

        assertThat(
                this.hasAllowedFileExtensionValidator.isValid(this.erstelleDokumentenListe(31), null),
                is(false)
        );

        assertThat(
                this.hasAllowedFileExtensionValidator.isValid(this.erstelleDokumentenListe(0), null),
                is(true)
        );

        assertThat(
                this.hasAllowedFileExtensionValidator.isValid(null, null),
                is(true)
        );

    }

    public List<DokumentDto> erstelleDokumentenListe(final int sizeOfList) {
        return Collections.nCopies(sizeOfList, new DokumentDto());
    }

}