package de.muenchen.isi.domain.service.filehandling;

import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.model.filehandling.FilepathModel;
import de.muenchen.isi.domain.model.filehandling.PresignedUrlModel;
import de.muenchen.oss.refarch.integration.s3.application.port.out.S3OutPort;
import de.muenchen.oss.refarch.integration.s3.domain.exception.S3Exception;
import de.muenchen.oss.refarch.integration.s3.domain.model.FileReference;
import de.muenchen.oss.refarch.integration.s3.domain.model.PresignedUrl;
import java.net.MalformedURLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PresignedUrlCreationService {

    private final String bucket;

    private final S3OutPort s3OutPort;

    private final Integer fileExpirationTime;

    public PresignedUrlCreationService(
        @Value("${refarch.s3.bucket-name}") final String bucket,
        @Value("${refarch.s3.file-expiration-time}") final Integer fileExpirationTime,
        final S3OutPort s3OutPort
    ) {
        this.bucket = bucket;
        this.fileExpirationTime = fileExpirationTime;
        this.s3OutPort = s3OutPort;
    }

    /**
     * Die Methode erstellt über den ISI-document-storage eine Presigned-Url zum Herunterladen der im Parameter angegebenen Datei.
     *
     * @param filepath für die Datei welche heruntergeladen werden soll.
     * @return die Presigned-Url zum direkten Herunterladen der Datei direkt vom S3-Storage.
     * @throws FileHandlingFailedException
     */
    public PresignedUrlModel getFile(final FilepathModel filepath) throws FileHandlingFailedException {
        try {
            final FileReference fileReference = new FileReference(this.bucket, filepath.getPathToFile());
            final PresignedUrl presignedUrl = this.s3OutPort.getPresignedUrl(
                fileReference,
                PresignedUrl.Action.GET,
                java.time.Duration.ofMinutes(this.fileExpirationTime)
            );
            log.debug("Presigned-URL get file: {}", presignedUrl.url());
            return new PresignedUrlModel(HttpMethod.GET.name(), presignedUrl.url().toExternalForm());
        } catch (final S3Exception exception) {
            final var message =
                "Beim Herunterladen der Datei vom ISI-Dokumentenverwaltungssystem ist ein Fehler aufgetreten.";
            this.exceptionLogging(exception, message);
            throw new FileHandlingFailedException(message, exception);
        }
    }

    /**
     * Die Methode erstellt über den ISI-document-storage eine Presigned-Url zum initialen Speichern der im Parameter angegebenen Datei.
     *
     * @param filepath für die Datei welche initial gespeichert werden soll.
     * @return die Presigned-Url zum initialen Speichern der Datei direkt im S3-Storage.
     * @throws FileHandlingFailedException
     */
    public PresignedUrlModel saveFile(final FilepathModel filepath) throws FileHandlingFailedException {
        try {
            final FileReference fileReference = new FileReference(this.bucket, filepath.getPathToFile());
            final PresignedUrl presignedUrl = this.s3OutPort.getPresignedUrl(
                fileReference,
                PresignedUrl.Action.PUT,
                java.time.Duration.ofMinutes(this.fileExpirationTime)
            );
            log.debug("Presigned-URL save file: {}", presignedUrl.url().toExternalForm());
            return new PresignedUrlModel(HttpMethod.PUT.name(), presignedUrl.url().toExternalForm());
        } catch (final S3Exception exception) {
            final var message =
                "Beim Speichern der Datei im ISI-Dokumentenverwaltungssystem ist ein Fehler aufgetreten.";
            this.exceptionLogging(exception, message);
            throw new FileHandlingFailedException(message, exception);
        }
    }

    /**
     * Die Methode erstellt über den ISI-document-storage eine Presigned-Url zum Löschen der im Parameter angegebenen Datei.
     *
     * @param filepath für die Datei welche gelöscht werden soll.
     * @return die Presigned-Url zum Löschen der Datei direkt im S3-Storage.
     * @throws FileHandlingFailedException
     */
    public PresignedUrlModel deleteFile(final FilepathModel filepath) throws FileHandlingFailedException {
        try {
            final FileReference fileReference = new FileReference(this.bucket, filepath.getPathToFile());
            final PresignedUrl presignedUrl = this.s3OutPort.getPresignedUrl(
                fileReference,
                PresignedUrl.Action.DELETE,
                java.time.Duration.ofMinutes(this.fileExpirationTime)
            );
            log.debug("Presigned-URL delete file: {}", presignedUrl);
            return new PresignedUrlModel(HttpMethod.DELETE.name(), presignedUrl.url().toExternalForm());
        } catch (final S3Exception exception) {
            final var message = "Beim Löschen der Datei im ISI-Dokumentenverwaltungssystem ist ein Fehler aufgetreten.";
            this.exceptionLogging(exception, message);
            throw new FileHandlingFailedException(message, exception);
        }
    }

    private void exceptionLogging(final Exception exception, final String errorMessage) {
        log.error(exception.getMessage());
        if (exception.getCause() != null) {
            log.error(exception.getCause().getMessage());
        }
        log.error(errorMessage);
    }
}
