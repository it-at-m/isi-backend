package de.muenchen.isi.infrastructure.repository.stammdaten;

import de.muenchen.isi.infrastructure.entity.stammdaten.MimeTypeInformation;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MimeTypeRepositoryTest {

    private MimeTypeRepository mimeTypeRepository;

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private Resource resource;

    @BeforeEach
    public void beforeEach() throws IOException {
        Mockito.when(this.resourceLoader.getResource("classpath:mime.types")).thenReturn(this.resource);
        final var inputStream = IOUtils.toInputStream(this.getFileContent(), Charset.defaultCharset());
        Mockito.when(this.resource.getInputStream()).thenReturn(inputStream);
        this.mimeTypeRepository = new MimeTypeRepository(this.resourceLoader);
        Mockito.reset(this.resourceLoader, this.resource);
    }

    @Test
    void init() {
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
    void parseLineToMimeTypeInfos() {
        var lineOfFile = "image/vnd.ms-modi\t\t\t\tmdi";
        var result = this.mimeTypeRepository.parseLineToMimeTypeInfos(lineOfFile);
        var expected = new MimeTypeInformation();
        expected.setMimeType("image/vnd.ms-modi");
        expected.setFileExtensions(List.of(".mdi"));
        assertThat(
                result,
                is(expected)
        );

        lineOfFile = "image/vnd.ms-modi\t        \t\tmdi";
        result = this.mimeTypeRepository.parseLineToMimeTypeInfos(lineOfFile);
        expected = new MimeTypeInformation();
        expected.setMimeType("image/vnd.ms-modi");
        expected.setFileExtensions(List.of(".mdi"));
        assertThat(
                result,
                is(expected)
        );

        lineOfFile = "image/vnd.ms-modi mdi";
        result = this.mimeTypeRepository.parseLineToMimeTypeInfos(lineOfFile);
        expected = new MimeTypeInformation();
        expected.setMimeType("image/vnd.ms-modi");
        expected.setFileExtensions(List.of(".mdi"));
        assertThat(
                result,
                is(expected)
        );

        lineOfFile = "image/vnd.ms-modi            mdi";
        result = this.mimeTypeRepository.parseLineToMimeTypeInfos(lineOfFile);
        expected = new MimeTypeInformation();
        expected.setMimeType("image/vnd.ms-modi");
        expected.setFileExtensions(List.of(".mdi"));
        assertThat(
                result,
                is(expected)
        );


        lineOfFile = "image/jpeg\t\t\t\t\tjpeg jpg \t jpe";
        result = this.mimeTypeRepository.parseLineToMimeTypeInfos(lineOfFile);
        expected = new MimeTypeInformation();
        expected.setMimeType("image/jpeg");
        expected.setFileExtensions(List.of(".jpeg", ".jpg", ".jpe"));
        assertThat(
                result,
                is(expected)
        );

        Assertions.assertThrows(NullPointerException.class, () -> this.mimeTypeRepository.parseLineToMimeTypeInfos(null));
    }

    private String getFileContent() {
        return "# This file maps Internet media types to unique file extension(s).\n" +
                "# Although created for httpd, this file is used by many software systems\n" +
                "# and has been placed in the public domain for unlimited redisribution.\n" +
                "#\n" +
                "# The table below contains both registered and (common) unregistered types.\n" +
                "# A type that has no unique extension can be ignored -- they are listed\n" +
                "# here to guide configurations toward known types and to make it easier to\n" +
                "# identify \"new\" types.  File extensions are also commonly used to indicate\n" +
                "# content languages and encodings, so choose them carefully.\n" +
                "#\n" +
                "# Internet media types should be registered as described in RFC 4288.\n" +
                "# The registry is at <http://www.iana.org/assignments/media-types/>.\n" +
                "#\n" +
                "# MIME type (lowercased)\t\t\tExtensions\n" +
                "# ============================================\t==========\n" +
                "# application/1d-interleaved-parityfec\n" +
                "# application/3gpdash-qoe-report+xml\n" +
                "# application/3gpp-ims+xml\n" +
                "# application/a2l\n" +
                "# application/activemessage\n" +
                "# application/alto-costmap+json\n" +
                "# application/alto-costmapfilter+json\n" +
                "# application/alto-directory+json\n" +
                "# application/alto-endpointcost+json\n" +
                "# application/alto-endpointcostparams+json\n" +
                "# application/alto-endpointprop+json\n" +
                "# application/alto-endpointpropparams+json\n" +
                "# application/alto-error+json\n" +
                "# application/alto-networkmap+json\n" +
                "# application/alto-networkmapfilter+json\n" +
                "# application/aml\n" +
                "application/andrew-inset\t\t\tez\n" +
                "# application/applefile\n" +
                "application/applixware\t\t\t\taw\n" +
                "# application/atf\n" +
                "# application/atfx\n" +
                "application/atom+xml\t\t\t\tatom\n" +
                "application/atomcat+xml\t\t\t\tatomcat\n" +
                "application/mathematica\t\t\t\tma nb mb" +
                "\n" +
                "";
    }

}
