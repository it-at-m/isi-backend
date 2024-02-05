package de.muenchen.isi.domain.service.filehandling;

import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.mapper.DokumentDomainMapper;
import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import de.muenchen.isi.domain.model.filehandling.DokumenteModel;
import de.muenchen.isi.domain.model.filehandling.FilepathModel;
import de.muenchen.isi.infrastructure.repository.filehandling.DokumentRepository;
import de.muenchen.oss.digiwf.s3.integration.client.exception.DocumentStorageClientErrorException;
import de.muenchen.oss.digiwf.s3.integration.client.exception.DocumentStorageException;
import de.muenchen.oss.digiwf.s3.integration.client.exception.DocumentStorageServerErrorException;
import de.muenchen.oss.digiwf.s3.integration.client.exception.PropertyNotSetException;
import de.muenchen.oss.digiwf.s3.integration.client.repository.DocumentStorageFileRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.reactive.function.client.WebClientException;

@Slf4j
@Service
public class DokumentService {

    private final DokumentRepository dokumentRepository;

    private final DokumentDomainMapper dokumentDomainMapper;

    private final DocumentStorageFileRepository documentStorageFileRepository;

    private final Integer fileExpirationTime;

    public DokumentService(
        final DokumentRepository dokumentRepository,
        final DokumentDomainMapper dokumentDomainMapper,
        final DocumentStorageFileRepository documentStorageFileRepository,
        @Value("${io.muenchendigital.digiwf.s3.client.file-expiration-time}") final Integer fileExpirationTime
    ) {
        this.dokumentRepository = dokumentRepository;
        this.dokumentDomainMapper = dokumentDomainMapper;
        this.documentStorageFileRepository = documentStorageFileRepository;
        this.fileExpirationTime = fileExpirationTime;
    }

    /**
     * Die Methode extrahiert seitenweise alle in der Datenbank persistierten Dokumente.
     *
     * @param pageNumber für die Seite die abgerufen werden soll
     * @param pageSize   für die Anzahl der Dokumente je Seite
     * @return Die Dokumente der gewählten Seite
     */
    public DokumenteModel getDokumente(final Integer pageNumber, final Integer pageSize) {
        final var foundPage = this.dokumentRepository.findAll(PageRequest.of(pageNumber, pageSize));
        final var dokumentEntities = foundPage.getContent();
        final var dokumentModels = this.dokumentDomainMapper.entity2Model(dokumentEntities);
        final var dokumenteModel = new DokumenteModel();
        dokumenteModel.setDokumente(dokumentModels);
        dokumenteModel.setPageNumber(foundPage.getNumber());
        dokumenteModel.setPageSize(foundPage.getSize());
        dokumenteModel.setTotalElements(foundPage.getTotalElements());
        dokumenteModel.setTotalPages(foundPage.getTotalPages());
        dokumenteModel.setLast(foundPage.isLast());
        return dokumenteModel;
    }

    /**
     * Ermittelt und löscht die Dokumente in der originalen Dokumentenliste welche in der adaptierten Dokumentenliste nicht mehr vorhanden sind.
     * <p>
     * Bei der Ermittlung werden nicht persistierte Dokumente (id == null) ignoriert.
     *
     * @param adaptedDokumentenListe  darf auch null sein
     * @param originalDokumentenListe darf auch null sein
     * @throws FileHandlingFailedException
     * @throws FileHandlingWithS3FailedException
     */
    public void deleteDokumenteFromOriginalDokumentenListWhichAreMissingInParameterAdaptedDokumentenListe(
        final List<DokumentModel> adaptedDokumentenListe,
        final List<DokumentModel> originalDokumentenListe
    ) throws FileHandlingFailedException, FileHandlingWithS3FailedException {
        final List<DokumentModel> dokumenteToDelete =
            this.getDokumenteInOriginalDokumentenListWhichAreMissingInAdaptedDokumentenListe(
                    adaptedDokumentenListe == null ? new ArrayList<>() : adaptedDokumentenListe,
                    originalDokumentenListe == null ? new ArrayList<>() : originalDokumentenListe
                );
        this.deleteDokumente(dokumenteToDelete);
    }

    /**
     * Ermittelt die Dokumente in der originalen Dokumentenliste welche in der adaptierten Dokumentenliste nicht mehr vorhanden sind.
     * <p>
     * Bei der Ermittlung werden nicht persistierte Dokumente (id == null) ignoriert.
     *
     * @param adaptedDokumentenListe
     * @param originalDokumentenListe
     * @return die Dokumente welche im Vergleich zur adaptierten Liste nicht mehr in der originalen Liste vorhanden sind
     */
    protected List<DokumentModel> getDokumenteInOriginalDokumentenListWhichAreMissingInAdaptedDokumentenListe(
        final List<DokumentModel> adaptedDokumentenListe,
        final List<DokumentModel> originalDokumentenListe
    ) {
        final Map<FilepathModel, DokumentModel> alreadyPersistedAdaptedDokumentenMap = adaptedDokumentenListe
            .stream()
            // Entfernen aller nicht persistierten Dokumente
            .filter(dokumentModel -> ObjectUtils.isNotEmpty(dokumentModel.getId()))
            .collect(Collectors.toMap(DokumentModel::getFilePath, Function.identity()));

        final Map<FilepathModel, DokumentModel> originalDokumentenMap = originalDokumentenListe
            .stream()
            // Entfernen aller nicht persistierten Dokumente
            .filter(dokumentModel -> ObjectUtils.isNotEmpty(dokumentModel.getId()))
            .collect(Collectors.toMap(DokumentModel::getFilePath, Function.identity()));

        /**
         * Entfernen der in der adaptierten Map vorhandenen Dokumente aus der Map der original Dokumente.
         * In der Map der original Dokumente bleiben die in der adaptierten Map nicht vorhandenen Dokumente übrig.
         */
        originalDokumentenMap.keySet().removeAll(alreadyPersistedAdaptedDokumentenMap.keySet());
        return new ArrayList<>(originalDokumentenMap.values());
    }

    /**
     * Löscht die im Parameter gegebenen Dokumente mittels einer Presigned-URL.
     *
     * @param dokumenteToDelete
     * @throws FileHandlingFailedException
     * @throws FileHandlingWithS3FailedException
     */
    protected void deleteDokumente(final List<DokumentModel> dokumenteToDelete)
        throws FileHandlingFailedException, FileHandlingWithS3FailedException {
        for (final var dokument : dokumenteToDelete) {
            try {
                documentStorageFileRepository.deleteFile(dokument.getFilePath().getPathToFile(), fileExpirationTime);
            } catch (
                final DocumentStorageClientErrorException
                | DocumentStorageServerErrorException
                | DocumentStorageException
                | PropertyNotSetException
                | WebClientException exception
            ) {
                final var message =
                    "Beim Löschen der Datei im ISI-Dokumentenverwaltungssystem ist ein Fehler aufgetreten.";
                this.exceptionLogging(exception, message);
                final var clazz = exception.getClass();
                if (
                    clazz.equals(DocumentStorageClientErrorException.class) ||
                    clazz.equals(DocumentStorageServerErrorException.class)
                ) {
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
    }

    private void exceptionLogging(final Exception exception, final String errorMessage) {
        log.error(exception.getMessage());
        if (exception.getCause() != null) {
            log.error(exception.getCause().getMessage());
        }
        log.error(errorMessage);
    }
}
