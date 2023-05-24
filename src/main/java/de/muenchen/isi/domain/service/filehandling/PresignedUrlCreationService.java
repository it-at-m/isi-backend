package de.muenchen.isi.domain.service.filehandling;

import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.model.filehandling.FilepathModel;
import de.muenchen.isi.domain.model.filehandling.PresignedUrlModel;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageClientErrorException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageServerErrorException;
import io.muenchendigital.digiwf.s3.integration.client.exception.PropertyNotSetException;
import io.muenchendigital.digiwf.s3.integration.client.repository.presignedurl.PresignedUrlRepository;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.reactive.function.client.WebClientException;

@Service
@Slf4j
public class PresignedUrlCreationService {

    private final PresignedUrlRepository presignedUrlRepository;

    private final Integer fileExpirationTime;

    public PresignedUrlCreationService(
        final PresignedUrlRepository presignedUrlRepository,
        @Value("${io.muenchendigital.digiwf.s3.client.file-expiration-time}") final Integer fileExpirationTime
    ) {
        this.presignedUrlRepository = presignedUrlRepository;
        this.fileExpirationTime = fileExpirationTime;
    }

    /**
     * Die Methode erstellt über den ISI-document-storage eine Presigned-Url zum Herunterladen der im Parameter angegebenen Datei.
     *
     * @param filepath für die Datei welche heruntergeladen werden soll.
     * @return die Presigned-Url zum direkten Herunterladen der Datei direkt vom S3-Storage.
     * @throws FileHandlingWithS3FailedException
     * @throws FileHandlingFailedException
     */
    public PresignedUrlModel getFile(final FilepathModel filepath)
        throws FileHandlingWithS3FailedException, FileHandlingFailedException {
        try {
            final var presignedUrl =
                this.presignedUrlRepository.getPresignedUrlGetFile(filepath.getPathToFile(), this.fileExpirationTime);
            log.debug("Presigned-URL get file: {}", presignedUrl);
            return new PresignedUrlModel(HttpMethod.GET.name(), presignedUrl.block());
        } catch (
            final DocumentStorageClientErrorException
            | DocumentStorageServerErrorException
            | DocumentStorageException
            | PropertyNotSetException
            | WebClientException exception
        ) {
            final var message =
                "Beim Herunterladen der Datei vom ISI-Dokumentenverwaltungssystem ist ein Fehler aufgetreten.";
            this.exceptionLogging(exception, message);
            final var clazz = exception.getClass();
            if (
                clazz.equals(DocumentStorageClientErrorException.class) ||
                clazz.equals(DocumentStorageServerErrorException.class)
            ) {
                throw new FileHandlingWithS3FailedException(
                    message,
                    this.getStatusCode((HttpStatusCodeException) exception.getCause()),
                    exception
                );
            } else {
                throw new FileHandlingFailedException(message, exception);
            }
        }
    }

    /**
     * Die Methode erstellt über den ISI-document-storage eine Presigned-Url zum initialen Speichern der im Parameter angegebenen Datei.
     *
     * @param filepath für die Datei welche initial gespeichert werden soll.
     * @return die Presigned-Url zum initialen Speichern der Datei direkt im S3-Storage.
     * @throws FileHandlingWithS3FailedException
     * @throws FileHandlingFailedException
     */
    public PresignedUrlModel saveFile(final FilepathModel filepath)
        throws FileHandlingWithS3FailedException, FileHandlingFailedException {
        try {
            final var presignedUrl =
                this.presignedUrlRepository.getPresignedUrlSaveFile(
                        filepath.getPathToFile(),
                        this.fileExpirationTime,
                        LocalDate.now().plusYears(999)
                    );
            log.debug("Presigned-URL save file: {}", presignedUrl);
            return new PresignedUrlModel(HttpMethod.PUT.name(), presignedUrl);
        } catch (
            final DocumentStorageClientErrorException
            | DocumentStorageServerErrorException
            | DocumentStorageException
            | PropertyNotSetException
            | WebClientException exception
        ) {
            final var message =
                "Beim Speichern der Datei im ISI-Dokumentenverwaltungssystem ist ein Fehler aufgetreten.";
            this.exceptionLogging(exception, message);
            final var clazz = exception.getClass();
            if (
                clazz.equals(DocumentStorageClientErrorException.class) ||
                clazz.equals(DocumentStorageServerErrorException.class)
            ) {
                throw new FileHandlingWithS3FailedException(
                    message,
                    this.getStatusCode((HttpStatusCodeException) exception.getCause()),
                    exception
                );
            } else {
                throw new FileHandlingFailedException(message, exception);
            }
        }
    }

    /**
     * Die Methode erstellt über den ISI-document-storage eine Presigned-Url zum Löschen der im Parameter angegebenen Datei.
     *
     * @param filepath für die Datei welche gelöscht werden soll.
     * @return die Presigned-Url zum Löschen der Datei direkt im S3-Storage.
     * @throws FileHandlingWithS3FailedException
     * @throws FileHandlingFailedException
     */
    public PresignedUrlModel deleteFile(final FilepathModel filepath)
        throws FileHandlingWithS3FailedException, FileHandlingFailedException {
        try {
            final var presignedUrl =
                this.presignedUrlRepository.getPresignedUrlDeleteFile(
                        filepath.getPathToFile(),
                        this.fileExpirationTime
                    );
            log.debug("Presigned-URL delete file: {}", presignedUrl);
            return new PresignedUrlModel(HttpMethod.DELETE.name(), presignedUrl);
        } catch (
            final DocumentStorageClientErrorException
            | DocumentStorageServerErrorException
            | DocumentStorageException
            | PropertyNotSetException
            | WebClientException exception
        ) {
            final var message = "Beim Löschen der Datei im ISI-Dokumentenverwaltungssystem ist ein Fehler aufgetreten.";
            this.exceptionLogging(exception, message);
            final var clazz = exception.getClass();
            if (
                clazz.equals(DocumentStorageClientErrorException.class) ||
                clazz.equals(DocumentStorageServerErrorException.class)
            ) {
                throw new FileHandlingWithS3FailedException(
                    message,
                    this.getStatusCode((HttpStatusCodeException) exception.getCause()),
                    exception
                );
            } else {
                throw new FileHandlingFailedException(message, exception);
            }
        }
    }

    private void exceptionLogging(final Exception exception, final String errorMessage) {
        log.error(exception.getMessage());
        if (exception.getCause() != null) {
            log.error(exception.getCause().getMessage());
        }
        log.error(errorMessage);
    }

    private HttpStatus getStatusCode(final HttpStatusCodeException exception) {
        return exception.getStatusCode();
    }
}
