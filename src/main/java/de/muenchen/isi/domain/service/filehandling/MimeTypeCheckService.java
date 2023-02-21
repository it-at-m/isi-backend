package de.muenchen.isi.domain.service.filehandling;

import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.MimeTypeExtractionFailedException;
import de.muenchen.isi.domain.model.filehandling.FilepathModel;
import de.muenchen.isi.domain.model.filehandling.MediaTypeInformationModel;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageClientErrorException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageServerErrorException;
import io.muenchendigital.digiwf.s3.integration.client.exception.PropertyNotSetException;
import io.muenchendigital.digiwf.s3.integration.client.repository.DocumentStorageFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;
import java.io.InputStream;


@Slf4j
@Service
public class MimeTypeCheckService {

    private final DocumentStorageFileRepository documentStorageFileRepository;

    private final Integer fileExpirationTime;

    public MimeTypeCheckService(final DocumentStorageFileRepository documentStorageFileRepository,
                                @Value("${io.muenchendigital.digiwf.s3.client.file-expiration-time}") final Integer fileExpirationTime) {
        this.documentStorageFileRepository = documentStorageFileRepository;
        this.fileExpirationTime = fileExpirationTime;
    }

    public MediaTypeInformationModel extractMediaTypeInformation(final FilepathModel filepath) throws FileHandlingWithS3FailedException, FileHandlingFailedException, MimeTypeExtractionFailedException {
        final var fileInputStream = this.getInputStream(filepath);
        return this.extractMediaTypeInformationOfFileAndCloseStream(fileInputStream);
    }

    protected InputStream getInputStream(final FilepathModel filepath) throws FileHandlingWithS3FailedException, FileHandlingFailedException {
        try {
            return this.documentStorageFileRepository.getFileInputStream(filepath.getPathToFile(), this.fileExpirationTime);
        } catch (final DocumentStorageClientErrorException | DocumentStorageServerErrorException |
                       DocumentStorageException | PropertyNotSetException exception) {
            final var message = "Beim Herunterladen der Datei vom ISI-Dokumentenverwaltungssystem ist ein Fehler aufgetreten.";
            log.error(message);
            final var clazz = exception.getClass();
            if (clazz.equals(DocumentStorageClientErrorException.class) || clazz.equals(DocumentStorageServerErrorException.class)) {
                throw new FileHandlingWithS3FailedException(
                        message,
                        ((HttpStatusCodeException) exception.getCause()).getStatusCode(),
                        exception
                );
            } else {
                throw new FileHandlingFailedException(message, exception);
            }
        }
    }

    protected MediaTypeInformationModel extractMediaTypeInformationOfFileAndCloseStream(final InputStream file) throws MimeTypeExtractionFailedException {
        final TikaConfig config = TikaConfig.getDefaultConfig();
        final Detector detector = config.getDetector();
        try (final InputStream fileInputStream = file;
             final TikaInputStream tikaInputStream = TikaInputStream.get(fileInputStream)) {
            final MediaType mediaType = detector.detect(tikaInputStream, new Metadata());
            final MimeType mimeType = config.getMimeRepository().forName(mediaType.toString());
            final var mediaTypeInformation = new MediaTypeInformationModel();
            mediaTypeInformation.setType(mimeType.getType().toString());
            mediaTypeInformation.setDescription(mimeType.getDescription());
            mediaTypeInformation.setAcronym(mimeType.getAcronym());
            return mediaTypeInformation;
        } catch (final IOException | MimeTypeException exception) {
            final var message = "Bei der Ermittlung des Dateitypen ist ein Fehler aufgetreten.";
            log.error(message);
            throw new MimeTypeExtractionFailedException(message, exception);
        }
    }

}
