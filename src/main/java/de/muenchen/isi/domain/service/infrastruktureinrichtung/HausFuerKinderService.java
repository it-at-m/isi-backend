package de.muenchen.isi.domain.service.infrastruktureinrichtung;

import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.mapper.InfrastruktureinrichtungDomainMapper;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.HausFuerKinderModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.HausFuerKinder;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.HausFuerKinderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HausFuerKinderService {

    private final InfrastruktureinrichtungDomainMapper infrastruktureinrichtungDomainMapper;

    private final HausFuerKinderRepository hausFuerKinderRepository;

    /**
     * Die Methode gibt alle {@link HausFuerKinderModel} als Liste zurück.
     *
     * @return Liste an {@link HausFuerKinderModel}.
     */
    public List<HausFuerKinderModel> getHaeuserFuerKinder() {
        return this.hausFuerKinderRepository.findAllByOrderByInfrastruktureinrichtungNameEinrichtungAsc()
                .map(this.infrastruktureinrichtungDomainMapper::entity2Model)
                .collect(Collectors.toList());
    }

    /**
     * Die Methode gibt ein {@link HausFuerKinderModel} identifiziert durch die ID zurück.
     *
     * @param id zum Identifizieren des {@link HausFuerKinderModel}.
     * @return {@link HausFuerKinderModel}.
     * @throws EntityNotFoundException falls das Haus für Kinder identifiziert durch die {@link HausFuerKinderModel#getId()} nicht gefunden wird.
     */
    public HausFuerKinderModel getHausFuerKinderById(final UUID id) throws EntityNotFoundException {
        final var optEntity = this.hausFuerKinderRepository.findById(id);
        final var entity = optEntity.orElseThrow(() -> {
            final var message = "Haus für Kinder nicht gefunden.";
            log.error(message);
            return new EntityNotFoundException(message);
        });
        return this.infrastruktureinrichtungDomainMapper.entity2Model(entity);
    }

    /**
     * Diese Methode speichert ein {@link HausFuerKinderModel}.
     *
     * @param hausFuerKinder zum Speichern
     * @return das gespeicherte {@link HausFuerKinderModel}
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     */
    public HausFuerKinderModel saveHausFuerKinder(final HausFuerKinderModel hausFuerKinder) throws OptimisticLockingException {
        HausFuerKinder hausFuerKinderEntity = this.infrastruktureinrichtungDomainMapper.model2Entity(hausFuerKinder);
        try {
            hausFuerKinderEntity = this.hausFuerKinderRepository.saveAndFlush(hausFuerKinderEntity);
        } catch (final ObjectOptimisticLockingFailureException exception) {
            final var message = "Die Daten wurden in der Zwischenzeit geändert. Bitte laden Sie die Seite neu!";
            throw new OptimisticLockingException(message, exception);
        }
        return this.infrastruktureinrichtungDomainMapper.entity2Model(hausFuerKinderEntity);
    }

    /**
     * Diese Methode updated ein {@link HausFuerKinderModel}.
     *
     * @param hausFuerKinder zum Updaten
     * @return das geupdatete {@link HausFuerKinderModel}
     * @throws EntityNotFoundException    falls das Haus für Kinder identifiziert durch die {@link HausFuerKinderModel#getId()} nicht gefunden wird.
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist.
     */
    public HausFuerKinderModel updateHausFuerKinder(final HausFuerKinderModel hausFuerKinder) throws EntityNotFoundException, OptimisticLockingException {
        this.getHausFuerKinderById(hausFuerKinder.getId());
        return this.saveHausFuerKinder(hausFuerKinder);
    }

    /**
     * Diese Methode löscht ein {@link HausFuerKinderModel}.
     *
     * @param id zum Identifizieren des {@link HausFuerKinderModel}.
     * @throws EntityNotFoundException     falls das Haus für Kinder identifiziert durch die {@link HausFuerKinderModel#getId()} nicht gefunden wird.
     * @throws EntityIsReferencedException falls ein {@link BauvorhabenModel} in dem Haus für Kinder referenziert wird.
     */
    public void deleteHausFuerKinderById(final UUID id) throws EntityNotFoundException, EntityIsReferencedException {
        final var hausFuerKinder = this.getHausFuerKinderById(id);
        this.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(hausFuerKinder.getInfrastruktureinrichtung());
        this.hausFuerKinderRepository.deleteById(id);
    }

    /**
     * Enthält das im Parameter gegebene {@link HausFuerKinderModel} ein {@link BauvorhabenModel},
     * wird eine {@link EntityIsReferencedException} geworfen.
     *
     * @param infrastruktureinrichtung zum Prüfen.
     * @throws EntityIsReferencedException falls das {@link HausFuerKinderModel} ein {@link BauvorhabenModel} referenziert.
     */
    protected void throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(final InfrastruktureinrichtungModel infrastruktureinrichtung) throws EntityIsReferencedException {
        final var bauvorhaben = infrastruktureinrichtung.getBauvorhaben();
        if (ObjectUtils.isNotEmpty(bauvorhaben)) {
            final var message = "Die Infrastruktureinrichtung " + infrastruktureinrichtung.getNameEinrichtung() + " referenziert das Bauvorhaben " + bauvorhaben.getNameVorhaben() + ".";
            log.error(message);
            throw new EntityIsReferencedException(message);
        }
    }

}
