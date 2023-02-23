package de.muenchen.isi.domain.service.filehandling;

import de.muenchen.isi.api.validation.IsFilepathWithoutLeadingPathdividerValidator;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.MimeTypeExtractionFailedException;
import de.muenchen.isi.domain.exception.MimeTypeNotAllowedException;
import de.muenchen.isi.domain.model.filehandling.FilepathModel;
import de.muenchen.isi.domain.model.filehandling.MimeTypeInformationModel;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageClientErrorException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageServerErrorException;
import io.muenchendigital.digiwf.s3.integration.client.exception.PropertyNotSetException;
import io.muenchendigital.digiwf.s3.integration.client.repository.DocumentStorageFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Slf4j
@Service
public class MimeTypeService {

    private final DocumentStorageFileRepository documentStorageFileRepository;

    private final Integer fileExpirationTime;

    private final Set<String> allowedMimeTypes;

    public MimeTypeService(final DocumentStorageFileRepository documentStorageFileRepository,
                           @Value("${io.muenchendigital.digiwf.s3.client.file-expiration-time}") final Integer fileExpirationTime,
                           @Value("#{'${file.mimetypes.allowed}'.split(',')}") final List<String> allowedMimeTypes) {
        this.documentStorageFileRepository = documentStorageFileRepository;
        this.fileExpirationTime = fileExpirationTime;
        this.allowedMimeTypes = new HashSet<>(allowedMimeTypes);
    }

    public MimeTypeInformationModel extractMediaTypeInformationForAllowedMediaType(final FilepathModel filepath) throws FileHandlingWithS3FailedException, FileHandlingFailedException, MimeTypeExtractionFailedException, MimeTypeNotAllowedException {
        final MimeTypeInformationModel mimeTypeInformationModel = this.extractMediaTypeInformation(filepath);
        if (!this.allowedMimeTypes.contains(mimeTypeInformationModel.getType())) {
            this.deleteFile(filepath);
            final var fileName = StringUtils.substringAfterLast(
                    filepath.getPathToFile(),
                    IsFilepathWithoutLeadingPathdividerValidator.PATH_SEPARATOR
            );
            final String type = this.getAcronymOrDescriptionWhenAcronymEmptyOrTypeWhenDescriptionEmpty(mimeTypeInformationModel);
            final var message = String.format("Das Hochladen der Datei %s des Typs %s ist nicht erlaubt.", fileName, type);
            throw new MimeTypeNotAllowedException(message);
        }
        return mimeTypeInformationModel;
    }

    protected MimeTypeInformationModel extractMediaTypeInformation(final FilepathModel filepath) throws FileHandlingWithS3FailedException, FileHandlingFailedException, MimeTypeExtractionFailedException {
        final var fileInputStream = this.getInputStream(filepath);
        return this.extractMediaTypeInformationOfFileAndCloseStream(fileInputStream);
    }

    protected InputStream getInputStream(final FilepathModel filepath) throws FileHandlingWithS3FailedException, FileHandlingFailedException {
        try {
            return this.documentStorageFileRepository.getFileInputStream(filepath.getPathToFile(), this.fileExpirationTime);
        } catch (final DocumentStorageClientErrorException | DocumentStorageServerErrorException |
                       DocumentStorageException | PropertyNotSetException exception) {
            final var message = "Beim Herunterladen zur Dateiprüfung vom ISI-Dokumentenverwaltungssystem ist ein Fehler aufgetreten.";
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

    protected void deleteFile(final FilepathModel filepath) throws FileHandlingWithS3FailedException, FileHandlingFailedException {
        try {
            this.documentStorageFileRepository.deleteFile(filepath.getPathToFile(), this.fileExpirationTime);
        } catch (final DocumentStorageClientErrorException | DocumentStorageServerErrorException |
                       DocumentStorageException | PropertyNotSetException exception) {
            final var message = "Beim Herunterladen zur Dateiprüfung vom ISI-Dokumentenverwaltungssystem ist ein Fehler aufgetreten.";
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

    protected MimeTypeInformationModel extractMediaTypeInformationOfFileAndCloseStream(final InputStream file) throws MimeTypeExtractionFailedException {
        final TikaConfig config = TikaConfig.getDefaultConfig();
        final Detector detector = config.getDetector();
        try (final InputStream fileInputStream = file;
             final TikaInputStream tikaInputStream = TikaInputStream.get(fileInputStream)) {
            final MediaType mediaType = detector.detect(tikaInputStream, new Metadata());
            final MimeType mimeType = config.getMimeRepository().forName(mediaType.toString());
            final var mimeTypeInformation = new MimeTypeInformationModel();
            mimeTypeInformation.setType(mimeType.getType().toString());
            mimeTypeInformation.setDescription(mimeType.getDescription());
            mimeTypeInformation.setAcronym(mimeType.getAcronym());
            return mimeTypeInformation;
        } catch (final IOException | MimeTypeException exception) {
            final var message = "Bei der Ermittlung des Dateitypen ist ein Fehler aufgetreten.";
            log.error(message);
            throw new MimeTypeExtractionFailedException(message, exception);
        }
    }

    protected String getAcronymOrDescriptionWhenAcronymEmptyOrTypeWhenDescriptionEmpty(final MimeTypeInformationModel mimeTypeInformation) {
        final String type;
        if (StringUtils.isEmpty(mimeTypeInformation.getAcronym())) {
            if (StringUtils.isEmpty(mimeTypeInformation.getDescription())) {
                type = mimeTypeInformation.getType();
            } else {
                type = mimeTypeInformation.getDescription();
            }
        } else {
            type = mimeTypeInformation.getAcronym();
        }
        return type;
    }

}
