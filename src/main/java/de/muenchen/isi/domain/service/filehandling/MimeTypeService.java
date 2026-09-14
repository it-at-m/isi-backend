package de.muenchen.isi.domain.service.filehandling;

import de.muenchen.isi.api.validation.IsFilepathWithoutLeadingPathdividerValidator;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.MimeTypeExtractionFailedException;
import de.muenchen.isi.domain.exception.MimeTypeNotAllowedException;
import de.muenchen.isi.domain.model.filehandling.FilepathModel;
import de.muenchen.isi.domain.model.filehandling.MimeTypeInformationModel;
import de.muenchen.oss.refarch.integration.s3.application.port.out.S3OutPort;
import de.muenchen.oss.refarch.integration.s3.domain.exception.S3Exception;
import de.muenchen.oss.refarch.integration.s3.domain.model.FileReference;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

@Slf4j
@Service
public class MimeTypeService {

    private final String bucket;

    private final S3OutPort s3OutPort;

    private final Set<String> allowedMimeTypes;

    public MimeTypeService(
        @Value("${refarch.s3.bucket-name}") final String bucket,
        final S3OutPort s3OutPort,
        @Value("#{'${file.mimetypes.allowed}'.split(',')}") final List<String> allowedMimeTypes
    ) {
        this.bucket = bucket;
        this.s3OutPort = s3OutPort;
        this.allowedMimeTypes = new HashSet<>(allowedMimeTypes);
    }

    /**
     * Ermittelt das {@link MimeTypeInformationModel} für die im S3-Storage referenzierte Datei.
     * Handelt es sich um einen zulässigen Dateitypen, so wird mit dem {@link MimeTypeInformationModel} geantwortet.
     * Handelt es sich um einen NICHT zulässigen Dateitypen, so wird eine {@link MimeTypeNotAllowedException} geworfen.
     * <p>
     * !!!Eine NICHT zulässige Datei wird durch diese Methode aus dem S3-Storage GELÖSCHT!!!
     *
     * @param filepath referenziert die im S3-Storage liegende Datei
     * @return {@link MimeTypeInformationModel}
     * @throws FileHandlingFailedException
     * @throws MimeTypeExtractionFailedException falls die Mime-Type-Ermittlung fehlgeschlagen ist
     * @throws MimeTypeNotAllowedException       falls der Mime-Type der referenzierten Datei nicht zulässig ist
     */
    public MimeTypeInformationModel extractMediaTypeInformationForAllowedMediaType(final FilepathModel filepath)
        throws FileHandlingFailedException, MimeTypeExtractionFailedException, MimeTypeNotAllowedException {
        final MimeTypeInformationModel mimeTypeInformationModel = this.extractMediaTypeInformation(filepath);
        if (!this.allowedMimeTypes.contains(mimeTypeInformationModel.getType())) {
            this.deleteFile(filepath);
            final var fileName = StringUtils.substringAfterLast(
                filepath.getPathToFile(),
                IsFilepathWithoutLeadingPathdividerValidator.PATH_SEPARATOR
            );
            final String type = this.getAcronymOrDescriptionWhenAcronymEmptyOrTypeWhenDescriptionEmpty(
                mimeTypeInformationModel
            );
            final var message = String.format(
                "Das Hochladen der Datei %s des Typs %s ist nicht erlaubt.",
                fileName,
                type
            );
            throw new MimeTypeNotAllowedException(message);
        }
        return mimeTypeInformationModel;
    }

    /**
     * Ermittelt das {@link MimeTypeInformationModel} für die im S3-Storage referenzierte Datei.
     *
     * @param filepath referenziert die im S3-Storage liegende Datei
     * @return {@link MimeTypeInformationModel}
     * @throws FileHandlingFailedException
     * @throws MimeTypeExtractionFailedException falls die Mime-Type-Ermittlung fehlgeschlagen ist
     */
    protected MimeTypeInformationModel extractMediaTypeInformation(final FilepathModel filepath)
        throws FileHandlingFailedException, MimeTypeExtractionFailedException {
        final var fileInputStream = this.getInputStream(filepath);
        return this.extractMediaTypeInformationOfFileAndCloseStream(fileInputStream);
    }

    protected InputStream getInputStream(final FilepathModel filepath) throws FileHandlingFailedException {
        try {
            final FileReference fileReference = new FileReference(this.bucket, filepath.getPathToFile());
            return this.s3OutPort.getFileContent(fileReference);
        } catch (final S3Exception exception) {
            final var message =
                "Beim Herunterladen zur Dateiprüfung vom ISI-Dokumentenverwaltungssystem ist ein Fehler aufgetreten.";
            throw new FileHandlingFailedException(message, exception);
        }
    }

    protected void deleteFile(final FilepathModel filepath) throws FileHandlingFailedException {
        try {
            final FileReference fileReference = new FileReference(this.bucket, filepath.getPathToFile());
            this.s3OutPort.deleteFile(fileReference);
        } catch (final S3Exception exception) {
            final var message =
                "Beim Herunterladen zur Dateiprüfung vom ISI-Dokumentenverwaltungssystem ist ein Fehler aufgetreten.";
            throw new FileHandlingFailedException(message, exception);
        }
    }

    protected MimeTypeInformationModel extractMediaTypeInformationOfFileAndCloseStream(final InputStream file)
        throws MimeTypeExtractionFailedException {
        final TikaConfig config = TikaConfig.getDefaultConfig();
        final Detector detector = config.getDetector();
        try (
            final InputStream fileInputStream = file;
            final TikaInputStream tikaInputStream = TikaInputStream.get(fileInputStream)
        ) {
            final MediaType mediaType = detector.detect(tikaInputStream, new Metadata());
            final MimeType mimeType = config.getMimeRepository().forName(mediaType.toString());
            final var mimeTypeInformation = new MimeTypeInformationModel();
            mimeTypeInformation.setType(mimeType.getType().toString());
            mimeTypeInformation.setDescription(mimeType.getDescription());
            mimeTypeInformation.setAcronym(mimeType.getAcronym());
            return mimeTypeInformation;
        } catch (final IOException | MimeTypeException exception) {
            final var message = "Bei der Ermittlung des Media-Type-Informationen ist ein Fehler aufgetreten.";
            log.error(message);
            throw new MimeTypeExtractionFailedException(message, exception);
        }
    }

    protected String getAcronymOrDescriptionWhenAcronymEmptyOrTypeWhenDescriptionEmpty(
        final MimeTypeInformationModel mimeTypeInformation
    ) {
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
