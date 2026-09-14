package de.muenchen.isi.domain.cronjob;

import de.muenchen.isi.domain.exception.CronJobFailedException;
import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import de.muenchen.isi.domain.model.filehandling.DokumenteModel;
import de.muenchen.isi.domain.model.filehandling.FilepathModel;
import de.muenchen.isi.domain.service.filehandling.DokumentService;
import de.muenchen.oss.refarch.integration.s3.application.port.out.S3OutPort;
import de.muenchen.oss.refarch.integration.s3.domain.exception.S3Exception;
import de.muenchen.oss.refarch.integration.s3.domain.model.FileMetadata;
import de.muenchen.oss.refarch.integration.s3.domain.model.FileReference;
import de.muenchen.oss.refarch.integration.s3.domain.model.ListResult;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class OrphanFileCleanupService {

    private static final String S3_ROOT_FOLDER = StringUtils.EMPTY;

    private static final Integer PAGE_SIZE = 100;

    private final DokumentService dokumentService;

    private final S3OutPort s3OutPort;

    private final String bucket;

    public OrphanFileCleanupService(
        @Value("${refarch.s3.bucket-name}") final String bucket,
        final S3OutPort s3OutPort,
        DokumentService dokumentService
    ) {
        this.bucket = bucket;
        this.s3OutPort = s3OutPort;
        this.dokumentService = dokumentService;
    }

    /**
     * Alle im S3-Storage befindlichen Dateien welche nicht durch ein Dokument im Backend referenziert sind,
     * werden gelöscht.
     *
     * @throws CronJobFailedException falls innerhalb der Methode ein Fehler auftritt.
     */
    @Scheduled(cron = "${de.muenchen.isi.cronjob.s3.cleanup.orphan-files}")
    @SchedulerLock(
        name = "orphanFileCleanupCron",
        lockAtMostFor = "${de.muenchen.isi.cronjob.shedlock}",
        lockAtLeastFor = "${de.muenchen.isi.cronjob.shedlock}"
    )
    public void cleanUp() throws CronJobFailedException {
        log.info("Bereinigung verwaister Dateien gestartet.");
        final Set<String> filePathsToDelete = this.determineFilePathsInS3WhichAreNotReferencedByBackendDokuments();
        filePathsToDelete.forEach(this::deleteFile);
        log.info("Bereinigung verwaister Dateien beendet.");
    }

    /**
     * In dieser Methode werden alle Dateipfade im S3-Storage ermittelt, welche nicht durch ein Dokument im Backend
     * referenziert werden.
     *
     * @return die Auflistung der Dateipfade im S3-Storage welche nicht durch Dokumente im Backend referenziert werden.
     * @throws CronJobFailedException sobald beim Holen der Dateipfade vom S3-Storage oder der Dokumente von Backend ein Fehler auftritt.
     */
    protected Set<String> determineFilePathsInS3WhichAreNotReferencedByBackendDokuments()
        throws CronJobFailedException {
        try {
            // Extrahieren der im S3-Storage gespeicherten Dateien.
            final ListResult filesInS3 = this.s3OutPort.getFilesWithPrefix(this.bucket, S3_ROOT_FOLDER, true);
            final Set<String> filePathsInS3 = filesInS3
                .files()
                .stream()
                .map(FileMetadata::path)
                .collect(Collectors.toSet());
            int nextPageNumber = 0;
            boolean isLastPage = false;

            // Entfernen der referenzierten Dateien von den gelisteten S3-Dateien
            while (!isLastPage) {
                final DokumenteModel dokumenteModel = this.getDokumentPage(nextPageNumber);
                final Set<String> filePathsInBackend = this.getFilePaths(dokumenteModel);
                filePathsInS3.removeAll(filePathsInBackend);
                isLastPage = BooleanUtils.toBooleanDefaultIfNull(dokumenteModel.getLast(), true);
                nextPageNumber = Math.incrementExact(dokumenteModel.getPageNumber());
            }

            // Zurückgeben der nicht referenzierten Dateien
            return filePathsInS3;
        } catch (final S3Exception exception) {
            final var message = "Es konnten keine Dateipfade aus dem S3-Storage extrahiert werden.";
            log.error(message, exception);
            throw new CronJobFailedException(message, exception);
        } catch (final Exception exception) {
            final var message = "Beim der Ermittlung der nicht referenzierten Dateien ist ein Fehler aufgetreten.";
            log.error(message, exception);
            throw new CronJobFailedException(message, exception);
        }
    }

    /**
     * Holt eine Seite an Dokumenten. Die Größe einer Seite wird durch die Konstante
     * {@link OrphanFileCleanupService#PAGE_SIZE} definiert.
     *
     * @param pageNumber welche die gewünschte Seite definiert.
     * @return das {@link DokumenteModel}.
     * @throws CronJobFailedException sobald keine Dokumente vom Backend geholt werden konnten.
     */
    protected DokumenteModel getDokumentPage(final Integer pageNumber) throws CronJobFailedException {
        try {
            return this.dokumentService.getDokumente(pageNumber, PAGE_SIZE);
        } catch (final Exception exception) {
            final var message = "Es konnten keine Dokumente vom Backend geholt werden.";
            log.error(message);
            throw new CronJobFailedException(message, exception);
        }
    }

    /**
     * Löscht die im Parameter referenzierte Datei von S3-Storage.
     *
     * @param pathToFile für die Datei zum Löschen.
     * @throws CronJobFailedException falls beim Löschen der Datei ein Fehler auftritt.
     */
    protected void deleteFile(final String pathToFile) throws CronJobFailedException {
        try {
            // Delete file on S3
            this.s3OutPort.deleteFile(new FileReference(this.bucket, pathToFile));
        } catch (final Exception exception) {
            final var message = String.format("Die Datei %s konnte nicht gelöscht werden.", pathToFile);
            log.error(message, exception);
            throw new CronJobFailedException(message, exception);
        }
    }

    protected Set<String> getFilePaths(final DokumenteModel dokumenteModel) {
        return CollectionUtils.emptyIfNull(dokumenteModel.getDokumente())
            .stream()
            .filter(ObjectUtils::isNotEmpty)
            .map(DokumentModel::getFilePath)
            .filter(ObjectUtils::isNotEmpty)
            .map(FilepathModel::getPathToFile)
            .filter(StringUtils::isNotEmpty)
            .collect(Collectors.toSet());
    }
}
