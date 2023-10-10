package de.muenchen.isi.domain.service.common;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.mapper.KommentarDomainMapper;
import de.muenchen.isi.domain.model.common.KommentarModel;
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

    private final KommentarDomainMapper kommentarMapper;

    /**
     * Die Methode holt alle für ein Bauvorhaben hinterlegten Kommentare.
     *
     * @param bauvorhabenId als ID des Bauvorhabens.
     * @return die Kommentare sortiert nach absteigenden Erstellungsdatum.
     */
    public List<KommentarModel> getKommentareForBauvorhaben(final UUID bauvorhabenId) {
        return kommentarRepository
            .findAllByBauvorhabenIdOrderByCreatedDateTimeDesc(bauvorhabenId)
            .map(kommentarMapper::entity2Model)
            .collect(Collectors.toList());
    }

    /**
     * Die Methode holt alle für eine Infrastruktureinrichtung hinterlegten Kommentare.
     *
     * @param infrastruktureinrichtungId als ID der Infrastruktureinrichtung.
     * @return die Kommentare sortiert nach absteigenden Erstellungsdatum.
     */
    public List<KommentarModel> getKommentareForInfrastruktureinrichtung(final UUID infrastruktureinrichtungId) {
        return kommentarRepository
            .findAllByInfrastruktureinrichtungIdOrderByCreatedDateTimeDesc(infrastruktureinrichtungId)
            .map(kommentarMapper::entity2Model)
            .collect(Collectors.toList());
    }

    /**
     * Holt den Kommentar identifiziert durch die ID des Kommentars.
     *
     * @param id des Kommentars.
     * @return den Kommentar.
     * @throws EntityNotFoundException falls kein Kommentar mit der ID existiert.
     */
    public KommentarModel getKommentarById(final UUID id) throws EntityNotFoundException {
        final var entity = kommentarRepository
            .findById(id)
            .orElseThrow(() -> {
                final var message = "Kommentar nicht gefunden.";
                log.error(message);
                return new EntityNotFoundException(message);
            });
        return kommentarMapper.entity2Model(entity);
    }

    /**
     * Speichert den Kommentar.
     *
     * @param kommentar zum Speichern.
     * @return den gespeicherten Kommentar.
     * @throws EntityNotFoundException falls kein referenzierbares Bauvorhaben bzw. keine referenzierbare Infrastruktureinrichtung existiert.
     * @throws OptimisticLockingException falls der Kommentar in einer neueren Version gespeichert ist.
     */
    public KommentarModel saveKommentar(final KommentarModel kommentar)
        throws OptimisticLockingException, EntityNotFoundException {
        var entity = kommentarMapper.model2Entity(kommentar);
        try {
            entity = kommentarRepository.saveAndFlush(entity);
        } catch (final ObjectOptimisticLockingFailureException exception) {
            final var message =
                "Der Kommentar wurde in der Zwischenzeit geändert. Bitte öffnen Sie die Kommentare neu!";
            throw new OptimisticLockingException(message, exception);
        }
        return kommentarMapper.entity2Model(entity);
    }

    /**
     * Aktualisiert den bereits gespeicherten Kommentar.
     *
     * @param kommentar zum Aktualisieren.
     * @return den aktualisierten Kommentar.
     * @throws EntityNotFoundException falls kein Kommentar mit der ID existiert oder kein referenzierbares Bauvorhaben bzw. keine referenzierbare Infrastruktureinrichtung existiert.
     * @throws OptimisticLockingException falls der Kommentar in einer neueren Version gespeichert ist.
     */
    public KommentarModel updateKommentar(final KommentarModel kommentar)
        throws EntityNotFoundException, OptimisticLockingException {
        this.getKommentarById(kommentar.getId());
        return this.saveKommentar(kommentar);
    }

    /**
     * Löscht den Kommentar identifiziert mit der im Parameter gegebenen Kommentar ID.
     * @param id des zu löschenden Kommentars.
     */
    public void deleteKommentarById(final UUID id) {
        kommentarRepository.deleteById(id);
    }
}
