package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.mapper.AbfrageDomainMapper;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.repository.InfrastrukturabfrageRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbfrageService {

    private final AbfrageDomainMapper abfrageDomainMapper;

    private final InfrastrukturabfrageRepository infrastrukturabfrageRepository;

    /**
     * Die Methode gibt alle {@link InfrastrukturabfrageModel} als Liste zurück.
     *
     * @return Liste an {@link InfrastrukturabfrageModel}.
     */
    public List<InfrastrukturabfrageModel> getInfrastrukturabfragen() {
        return this.infrastrukturabfrageRepository.findAllByOrderByAbfrageFristStellungnahmeDesc()
            .map(this.abfrageDomainMapper::entity2Model)
            .collect(Collectors.toList());
    }

    /**
     * Die Methode gibt ein {@link InfrastrukturabfrageModel} identifiziert durch die ID zurück.
     *
     * @param id zum Identifizieren des {@link InfrastrukturabfrageModel}.
     * @return {@link InfrastrukturabfrageModel}.
     * @throws EntityNotFoundException falls die Abfrage identifiziert durch die {@link InfrastrukturabfrageModel#getId()} nicht gefunden wird.
     */
    public InfrastrukturabfrageModel getInfrastrukturabfrageById(final UUID id) throws EntityNotFoundException {
        final var optEntity = this.infrastrukturabfrageRepository.findById(id);
        final var entity = optEntity.orElseThrow(() -> {
            final var message = "Infrastrukturabfrage nicht gefunden.";
            log.error(message);
            return new EntityNotFoundException(message);
        });
        return this.abfrageDomainMapper.entity2Model(entity);
    }

    /**
     * Diese Methode speichert ein {@link InfrastrukturabfrageModel}.
     *
     * @param abfrage zum Speichern
     * @return das gespeicherte {@link InfrastrukturabfrageModel}
     * @throws UniqueViolationException falls der Name der Abfrage {@link InfrastrukturabfrageModel#getAbfrage().getNameAbfrage} ()} bereits vorhanden ist
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     */
    public InfrastrukturabfrageModel saveInfrastrukturabfrage(final InfrastrukturabfrageModel abfrage)
        throws UniqueViolationException, OptimisticLockingException {
        if (abfrage.getId() == null) {
            abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        }
        var abfrageEntity = this.abfrageDomainMapper.model2entity(abfrage);
        final var saved =
            this.infrastrukturabfrageRepository.findByAbfrage_NameAbfrageIgnoreCase(
                    abfrageEntity.getAbfrage().getNameAbfrage()
                );
        if ((saved.isPresent() && saved.get().getId().equals(abfrageEntity.getId())) || saved.isEmpty()) {
            try {
                abfrageEntity = this.infrastrukturabfrageRepository.saveAndFlush(abfrageEntity);
            } catch (final ObjectOptimisticLockingFailureException exception) {
                final var message = "Die Daten wurden in der Zwischenzeit geändert. Bitte laden Sie die Seite neu!";
                throw new OptimisticLockingException(message, exception);
            }
            return this.abfrageDomainMapper.entity2Model(abfrageEntity);
        } else {
            throw new UniqueViolationException(
                "Der angegebene Name der Abfrage ist schon vorhanden, bitte wählen Sie daher einen anderen Namen und speichern Sie die Abfrage erneut."
            );
        }
    }

    /**
     * Diese Methode updated ein {@link InfrastrukturabfrageModel} ausgenommen vom Abfrage Status.
     *
     * @param abfrage zum Updaten
     * @return das geupdatete {@link InfrastrukturabfrageModel}
     * @throws EntityNotFoundException falls die Abfrage identifiziert durch die {@link InfrastrukturabfrageModel#getId()} nicht gefunden wird
     * @throws UniqueViolationException falls der Name der Abfrage {@link InfrastrukturabfrageModel#getAbfrage().getNameAbfrage} ()} bereits vorhanden ist
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     */
    public InfrastrukturabfrageModel updateInfrastrukturabfrageWithoutStatus(final InfrastrukturabfrageModel abfrage)
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException {
        final InfrastrukturabfrageModel abfrageDb = this.getInfrastrukturabfrageById(abfrage.getId());
        abfrage.getAbfrage().setStatusAbfrage(abfrageDb.getAbfrage().getStatusAbfrage());
        return this.saveInfrastrukturabfrage(abfrage);
    }

    /**
     * Diese Methode updated ein {@link InfrastrukturabfrageModel} eingenommen mit dem Abfrage Status.
     *
     * @param abfrage zum Updaten
     * @return das geupdatete {@link InfrastrukturabfrageModel}
     * @throws EntityNotFoundException falls die Abfrage identifiziert durch die {@link InfrastrukturabfrageModel#getId()} nicht gefunden wird
     * @throws UniqueViolationException falls der Name der Abfrage {@link InfrastrukturabfrageModel#getAbfrage().getNameAbfrage} ()} bereits vorhanden ist
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     */
    public InfrastrukturabfrageModel updateInfrastrukturabfrageWithStatus(final InfrastrukturabfrageModel abfrage)
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException {
        this.getInfrastrukturabfrageById(abfrage.getId());
        return this.saveInfrastrukturabfrage(abfrage);
    }

    /**
     * Diese Methode löscht ein {@link InfrastrukturabfrageModel}.
     *
     * @param id zum Identifizieren des {@link InfrastrukturabfrageModel}.
     * @throws EntityNotFoundException     falls die Abfrage identifiziert durch die {@link InfrastrukturabfrageModel#getId()} nicht gefunden wird.
     * @throws EntityIsReferencedException falls ein {@link BauvorhabenModel} in der Abfrage referenziert wird.
     */
    public void deleteInfrasturkturabfrageById(final UUID id)
        throws EntityNotFoundException, EntityIsReferencedException {
        final var abfrage = this.getInfrastrukturabfrageById(id);
        this.throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(abfrage.getAbfrage());
        this.infrastrukturabfrageRepository.deleteById(id);
    }

    /**
     * Enthält das im Parameter gegebene {@link AbfrageModel} ein {@link BauvorhabenModel},
     * wird eine {@link EntityIsReferencedException} geworfen.
     *
     * @param abfrage zum Prüfen.
     * @throws EntityIsReferencedException falls das {@link AbfrageModel} ein {@link BauvorhabenModel} referenziert.
     */
    protected void throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(final AbfrageModel abfrage)
        throws EntityIsReferencedException {
        final var bauvorhaben = abfrage.getBauvorhaben();
        if (ObjectUtils.isNotEmpty(bauvorhaben)) {
            final var message =
                "Die Abfrage " +
                abfrage.getNameAbfrage() +
                " referenziert das Bauvorhaben " +
                bauvorhaben.getNameVorhaben() +
                ".";
            log.error(message);
            throw new EntityIsReferencedException(message);
        }
    }
}
