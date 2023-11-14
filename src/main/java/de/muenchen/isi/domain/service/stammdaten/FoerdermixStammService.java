package de.muenchen.isi.domain.service.stammdaten;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.mapper.StammdatenDomainMapper;
import de.muenchen.isi.domain.model.stammdaten.FoerdermixStammModel;
import de.muenchen.isi.infrastructure.repository.stammdaten.FoerdermixStammRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoerdermixStammService {

    private final StammdatenDomainMapper stammdatenDomainMapper;

    private final FoerdermixStammRepository foerdermixStammRepository;

    /**
     * Die Methode gibt alle {@link FoerdermixStammModel} als Liste zurück.
     *
     * @return Liste an {@link FoerdermixStammModel}
     */
    public List<FoerdermixStammModel> getFoerdermixStaemme() {
        return this.foerdermixStammRepository.findAllByOrderByFoerdermixBezeichnungAsc()
            .map(this.stammdatenDomainMapper::entity2Model)
            .collect(Collectors.toList());
    }

    /**
     * Die Methode gibt ein {@link FoerdermixStammModel} identifiziert durch die ID zurück.
     *
     * @param id zum Identifizieren des {@link FoerdermixStammModel}
     * @return {@link FoerdermixStammModel}
     */
    public FoerdermixStammModel getFoerdermixStammById(final UUID id) throws EntityNotFoundException {
        final var optEntity = this.foerdermixStammRepository.findById(id);
        final var entity = optEntity.orElseThrow(() -> {
            final var message = "FoerdermixStamm nicht gefunden.";
            log.error(message);
            return new EntityNotFoundException(message);
        });
        return this.stammdatenDomainMapper.entity2Model(entity);
    }

    /**
     * Diese Methode speichert ein {@link FoerdermixStammModel}.
     *
     * @param foerdermixstamm zum Speichern
     * @return das gespeicherte {@link FoerdermixStammModel}
     * @throws UniqueViolationException falls die Bezeichnung {@link FoerdermixStammModel#getFoerdermix()#ge} des Fördermixes bereits im gleichen Jahr {@link FoerdermixStammModel#getFoerdermix()#getBezeichnungJahr()} vorhanden ist
     */
    public FoerdermixStammModel saveFoerdermixStamm(final FoerdermixStammModel foerdermixstamm)
        throws UniqueViolationException, OptimisticLockingException {
        var entity = this.stammdatenDomainMapper.model2Entity(foerdermixstamm);

        final var saved =
            this.foerdermixStammRepository.findByFoerdermixBezeichnungJahrIgnoreCaseAndFoerdermixBezeichnungIgnoreCase(
                    foerdermixstamm.getFoerdermix().getBezeichnungJahr(),
                    foerdermixstamm.getFoerdermix().getBezeichnung()
                );
        if ((saved.isPresent() && saved.get().getId().equals(entity.getId())) || saved.isEmpty()) {
            try {
                entity = this.foerdermixStammRepository.saveAndFlush(entity);
            } catch (final ObjectOptimisticLockingFailureException exception) {
                final var message = "Die Daten wurden in der Zwischenzeit geändert. Bitte laden Sie die Seite neu!";
                throw new OptimisticLockingException(message, exception);
            }
            return this.stammdatenDomainMapper.entity2Model(entity);
        } else {
            throw new UniqueViolationException(
                "Die Bezeichnung exisitiert bereits unter dem angegebenen Jahr. Bitte wählen Sie daher eine andere Bezeichnung und speichern Sie den Fördermix erneut."
            );
        }
    }

    /**
     * Diese Methode updated ein {@link FoerdermixStammModel}.
     *
     * @param foerdermix zum Updaten
     * @return das geupdatete {@link FoerdermixStammModel}
     * @throws EntityNotFoundException    falls die Abfrage identifiziert durch die {@link FoerdermixStammModel#getId()} nicht gefunden wird
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     */
    public FoerdermixStammModel updateFoerdermixStamm(final FoerdermixStammModel foerdermix)
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException {
        this.getFoerdermixStammById(foerdermix.getId());
        return this.saveFoerdermixStamm(foerdermix);
    }

    /**
     * Diese Methode löscht ein {@link FoerdermixStammModel}.
     *
     * @param id zum Identifizieren des {@link FoerdermixStammModel}
     * @throws EntityNotFoundException falls die Abfrage identifiziert durch die {@link FoerdermixStammModel#getId()} nicht gefunden wird
     */
    public void deleteFoerdermixStammById(final UUID id) throws EntityNotFoundException {
        this.getFoerdermixStammById(id);
        this.foerdermixStammRepository.deleteById(id);
    }
}
