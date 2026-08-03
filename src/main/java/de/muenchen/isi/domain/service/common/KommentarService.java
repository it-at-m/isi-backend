package de.muenchen.isi.domain.service.common;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.mapper.KommentarBauvorhabenDomainMapper;
import de.muenchen.isi.domain.mapper.KommentarInfrastruktureinrichtungDomainMapper;
import de.muenchen.isi.domain.model.common.KommentarBauvorhabenModel;
import de.muenchen.isi.domain.model.common.KommentarInfrastruktureinrichtungModel;
import de.muenchen.isi.domain.service.email.SendKommentarBauvorhabenNotificationService;
import de.muenchen.isi.domain.service.filehandling.DokumentService;
import de.muenchen.isi.infrastructure.entity.common.Kommentar;
import de.muenchen.isi.infrastructure.repository.common.KommentarRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KommentarService {

    private final KommentarRepository kommentarRepository;

    private final KommentarBauvorhabenDomainMapper kommentarBauvorhabenMapper;

    private final KommentarInfrastruktureinrichtungDomainMapper kommentarInfrastruktureinrichtungMapper;

    private final DokumentService dokumentService;

    private final SendKommentarBauvorhabenNotificationService sendKommentarBauvorhabenNotificationService;

    /**
     * Die Methode holt alle für ein Bauvorhaben hinterlegten Kommentare.
     *
     * @param bauvorhabenId als ID des Bauvorhabens.
     * @return die Kommentare sortiert nach absteigenden Erstellungsdatum.
     */
    public List<KommentarBauvorhabenModel> getKommentareForBauvorhaben(final UUID bauvorhabenId) {
        return kommentarRepository
            .findAllByBauvorhabenIdOrderByCreatedDateTimeDesc(bauvorhabenId)
            .map(kommentarBauvorhabenMapper::entity2Model)
            .collect(Collectors.toList());
    }

    /**
     * Die Methode holt alle für eine Infrastruktureinrichtung hinterlegten Kommentare.
     *
     * @param infrastruktureinrichtungId als ID der Infrastruktureinrichtung.
     * @return die Kommentare sortiert nach absteigenden Erstellungsdatum.
     */
    public List<KommentarInfrastruktureinrichtungModel> getKommentareForInfrastruktureinrichtung(
        final UUID infrastruktureinrichtungId
    ) {
        return kommentarRepository
            .findAllByInfrastruktureinrichtungIdOrderByCreatedDateTimeDesc(infrastruktureinrichtungId)
            .map(kommentarInfrastruktureinrichtungMapper::entity2Model)
            .collect(Collectors.toList());
    }

    /**
     * Holt den Kommentar eines Bauvorhabens identifiziert durch die ID des Kommentars.
     *
     * @param id des Kommentars.
     * @return den Kommentar.
     * @throws EntityNotFoundException falls kein Kommentar mit der ID existiert.
     */
    public KommentarBauvorhabenModel getKommentarForBauvorhabenById(final UUID id) throws EntityNotFoundException {
        return kommentarBauvorhabenMapper.entity2Model(this.getKommentarById(id));
    }

    /**
     * Holt den Kommentar identifiziert durch die ID des Kommentars.
     *
     * @param id des Kommentars.
     * @return den Kommentar.
     * @throws EntityNotFoundException falls kein Kommentar mit der ID existiert.
     */
    public KommentarInfrastruktureinrichtungModel getKommentarForInfrastruktureinrichtungById(final UUID id)
        throws EntityNotFoundException {
        return kommentarInfrastruktureinrichtungMapper.entity2Model(this.getKommentarById(id));
    }

    private Kommentar getKommentarById(final UUID id) throws EntityNotFoundException {
        return kommentarRepository
            .findById(id)
            .orElseThrow(() -> {
                final var message = "Kommentar nicht gefunden.";
                log.error(message);
                return new EntityNotFoundException(message);
            });
    }

    /**
     * Speichert den Kommentar eines Bauvorhabens.
     *
     * @param kommentar zum Speichern.
     * @return den gespeicherten Kommentar.
     * @throws EntityNotFoundException falls kein referenzierbares Bauvorhaben bzw. keine referenzierbare Infrastruktureinrichtung existiert.
     * @throws OptimisticLockingException falls der Kommentar in einer neueren Version gespeichert ist.
     */
    public KommentarBauvorhabenModel saveKommentarForBauvorhaben(final KommentarBauvorhabenModel kommentar)
        throws OptimisticLockingException, EntityNotFoundException {
        final var isNew = kommentar.getId() == null;
        var entity = kommentarBauvorhabenMapper.model2Entity(kommentar);
        final var savedEntity = this.saveKommentar(entity);
        if (savedEntity.getBauvorhaben() != null) {
            sendKommentarBauvorhabenNotificationService.sendKommentarBauvorhabenNotificationAsync(
                savedEntity.getBauvorhaben().getId(),
                savedEntity.getBauvorhaben().getNameVorhaben(),
                savedEntity.getText(),
                savedEntity.getErstellungsdatum(),
                isNew
            );
        }
        return kommentarBauvorhabenMapper.entity2Model(savedEntity);
    }

    /**
     * Speichert den Kommentar einer Infrastruktureinrichtung.
     *
     * @param kommentar zum Speichern.
     * @return den gespeicherten Kommentar.
     * @throws EntityNotFoundException falls kein referenzierbares Bauvorhaben bzw. keine referenzierbare Infrastruktureinrichtung existiert.
     * @throws OptimisticLockingException falls der Kommentar in einer neueren Version gespeichert ist.
     */
    public KommentarInfrastruktureinrichtungModel saveKommentarForInfrastruktureinrichtung(
        final KommentarInfrastruktureinrichtungModel kommentar
    ) throws OptimisticLockingException, EntityNotFoundException {
        var entity = kommentarInfrastruktureinrichtungMapper.model2Entity(kommentar);
        return kommentarInfrastruktureinrichtungMapper.entity2Model(this.saveKommentar(entity));
    }

    private Kommentar saveKommentar(final Kommentar entity) throws OptimisticLockingException {
        try {
            return kommentarRepository.saveAndFlush(entity);
        } catch (final ObjectOptimisticLockingFailureException exception) {
            final var message =
                "Der Kommentar wurde in der Zwischenzeit geändert. Bitte öffnen Sie die Kommentare neu!";
            throw new OptimisticLockingException(message, exception);
        }
    }

    /**
     * Aktualisiert den bereits gespeicherten Kommentar eines Bauvorhabens.
     *
     * @param kommentar zum Aktualisieren.
     * @return den aktualisierten Kommentar.
     * @throws EntityNotFoundException falls kein Kommentar mit der ID existiert oder kein referenzierbares Bauvorhaben bzw. keine referenzierbare Infrastruktureinrichtung existiert.
     * @throws OptimisticLockingException falls der Kommentar in einer neueren Version gespeichert ist.
     */
    public KommentarBauvorhabenModel updateKommentarForBauvorhaben(final KommentarBauvorhabenModel kommentar)
        throws EntityNotFoundException, OptimisticLockingException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        this.getKommentarForBauvorhabenById(kommentar.getId());
        final var orignalKommentar = this.getKommentarForBauvorhabenById(kommentar.getId());
        dokumentService.deleteDokumenteFromOriginalDokumentenListWhichAreMissingInParameterAdaptedDokumentenListe(
            kommentar.getDokumente(),
            orignalKommentar.getDokumente()
        );
        return this.saveKommentarForBauvorhaben(kommentar);
    }

    /**
     * Aktualisiert den bereits gespeicherten Kommentar einer Infrastruktureinrichtung.
     *
     * @param kommentar zum Aktualisieren.
     * @return den aktualisierten Kommentar.
     * @throws EntityNotFoundException falls kein Kommentar mit der ID existiert oder kein referenzierbares Bauvorhaben bzw. keine referenzierbare Infrastruktureinrichtung existiert.
     * @throws OptimisticLockingException falls der Kommentar in einer neueren Version gespeichert ist.
     */
    public KommentarInfrastruktureinrichtungModel updateKommentarForInfrastruktureinrichtung(
        final KommentarInfrastruktureinrichtungModel kommentar
    )
        throws EntityNotFoundException, OptimisticLockingException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        this.getKommentarForInfrastruktureinrichtungById(kommentar.getId());
        final var orignalKommentar = this.getKommentarForInfrastruktureinrichtungById(kommentar.getId());
        dokumentService.deleteDokumenteFromOriginalDokumentenListWhichAreMissingInParameterAdaptedDokumentenListe(
            kommentar.getDokumente(),
            orignalKommentar.getDokumente()
        );
        return this.saveKommentarForInfrastruktureinrichtung(kommentar);
    }

    /**
     * Löscht den Kommentar identifiziert mit der im Parameter gegebenen Kommentar ID.
     * @param id des zu löschenden Kommentars.
     */
    public void deleteKommentarById(final UUID id) {
        kommentarRepository.deleteById(id);
    }
}
