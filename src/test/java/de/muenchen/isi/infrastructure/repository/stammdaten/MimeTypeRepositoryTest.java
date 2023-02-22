package de.muenchen.isi.infrastructure.repository.stammdaten;

import de.muenchen.isi.infrastructure.entity.stammdaten.MimeTypeInformation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MimeTypeRepositoryTest {

    private final MimeTypeRepository mimeTypeRepository = new MimeTypeRepository();

    @Test
    void findByMimeType() {
        var result = this.mimeTypeRepository.findByMimeType("application/andrew-inset");
        var expected = new MimeTypeInformation();
        expected.setMimeType("application/andrew-inset");
        expected.setFileExtensions(List.of(".ez"));
        assertThat(
                result,
                is(Optional.of(expected))
        );

        result = this.mimeTypeRepository.findByMimeType("application/applixware");
        expected = new MimeTypeInformation();
        expected.setMimeType("application/applixware");
        expected.setFileExtensions(List.of(".aw"));
        assertThat(
                result,
                is(Optional.of(expected))
        );

        result = this.mimeTypeRepository.findByMimeType("application/atom+xml");
        expected = new MimeTypeInformation();
        expected.setMimeType("application/atom+xml");
        expected.setFileExtensions(List.of(".atom"));
        assertThat(
                result,
                is(Optional.of(expected))
        );

        result = this.mimeTypeRepository.findByMimeType("application/atomcat+xml");
        expected = new MimeTypeInformation();
        expected.setMimeType("application/atomcat+xml");
        expected.setFileExtensions(List.of(".atomcat"));
        assertThat(
                result,
                is(Optional.of(expected))
        );

        result = this.mimeTypeRepository.findByMimeType("application/mathematica");
        expected = new MimeTypeInformation();
        expected.setMimeType("application/mathematica");
        expected.setFileExtensions(List.of(".ma", ".nb", ".mb"));
        assertThat(
                result,
                is(Optional.of(expected))
        );

        result = this.mimeTypeRepository.findByMimeType("no/mimetype");
        assertThat(
                result,
                is(Optional.empty())
        );
    }

    @Test
    void findAllByMimeTypes() {
        final var result = this.mimeTypeRepository.findAllByMimeTypes(
                List.of(
                        "application/andrew-inset",
                        "application/atomcat+xml",
                        "no/mimetype",
                        "application/mathematica"
                )
        );

        final var expectedSet = new HashSet<MimeTypeInformation>();
        var mimeTypeInformation = new MimeTypeInformation();
        mimeTypeInformation.setMimeType("application/andrew-inset");
        mimeTypeInformation.setFileExtensions(List.of(".ez"));
        expectedSet.add(mimeTypeInformation);
        mimeTypeInformation = new MimeTypeInformation();
        mimeTypeInformation.setMimeType("application/atomcat+xml");
        mimeTypeInformation.setFileExtensions(List.of(".atomcat"));
        expectedSet.add(mimeTypeInformation);
        mimeTypeInformation = new MimeTypeInformation();
        mimeTypeInformation.setMimeType("application/mathematica");
        mimeTypeInformation.setFileExtensions(List.of(".ma", ".nb", ".mb"));
        expectedSet.add(mimeTypeInformation);

        assertThat(
                result,
                is(expectedSet)
        );
    }

}
