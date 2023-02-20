package de.muenchen.isi.domain.service.filehandling;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;


@Slf4j
@Service
@RequiredArgsConstructor
public class MimeTypeCheckService {

    @Data
    public static class MediaTypeInformation {

        private String type;

        private String description;

        private String acronym;

    }

    public MediaTypeInformation getMediaTypeInformationOfFileAndCloseStream(final InputStream file) throws IOException, MimeTypeException {
        final TikaConfig config = TikaConfig.getDefaultConfig();
        final Detector detector = config.getDetector();
        try (final InputStream fileInputStream = file;
             final TikaInputStream tikaInputStream = TikaInputStream.get(fileInputStream)) {
            final Metadata metadata = new Metadata();
            final MediaType mediaType = detector.detect(tikaInputStream, metadata);
            final MimeType mimeType = config.getMimeRepository().forName(mediaType.toString());
            final var mediaTypeInformation = new MediaTypeInformation();
            mediaTypeInformation.setType(mimeType.getType().toString());
            mediaTypeInformation.setDescription(mimeType.getDescription());
            mediaTypeInformation.setAcronym(mimeType.getAcronym());
            return mediaTypeInformation;
        } catch (final IOException | MimeTypeException exception) {
            final var message = "Bei der Ermittlung des Dateitypen ist ein Fehler aufgetreten.";
            log.error(message);
            throw exception;
        }
    }

}
