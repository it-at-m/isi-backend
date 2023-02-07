package de.muenchen.isi.domain.service.infrastruktureinrichtung;

import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.mapper.InfrastruktureinrichtungDomainMapper;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.KindergartenModel;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kindergarten;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.KindergartenRepository;
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
public class KindergartenService {

    private final InfrastruktureinrichtungDomainMapper infrastruktureinrichtungDomainMapper;

    private final KindergartenRepository kindergartenRepository;

    /**
     * Die Methode gibt alle {@link KindergartenModel} als Liste zurück.
     *
     * @return Liste an {@link KindergartenModel}.
     */
    public List<KindergartenModel> getKindergaerten() {
        return this.kindergartenRepository.findAllByOrderByInfrastruktureinrichtungNameEinrichtungAsc()
                .map(this.infrastruktureinrichtungDomainMapper::entity2Model)
                .collect(Collectors.toList());
    }

    /**
     * Die Methode gibt ein {@link KindergartenModel} identifiziert durch die ID zurück.
     *
     * @param id zum Identifizieren des {@link KindergartenModel}.
     * @return {@link KindergartenModel}.
     * @throws EntityNotFoundException falls der Kindergarten identifiziert durch die {@link KindergartenModel#getId()} nicht gefunden wird.
     */
    public KindergartenModel getKindergartenById(final UUID id) throws EntityNotFoundException {
        final var optEntity = this.kindergartenRepository.findById(id);
        final var entity = optEntity.orElseThrow(() -> {
            final var message = "Kindergarten nicht gefunden.";
            log.error(message);
            return new EntityNotFoundException(message);
        });
        return this.infrastruktureinrichtungDomainMapper.entity2Model(entity);
    }

    /**
     * Diese Methode speichert ein {@link KindergartenModel}.
     *
     * @param kindergarten zum Speichern.
     * @return das gespeicherte {@link KindergartenModel}.
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist.
     */
    public KindergartenModel saveKindergarten(final KindergartenModel kindergarten) throws OptimisticLockingException {
        Kindergarten kindergartenEntity = this.infrastruktureinrichtungDomainMapper.model2Entity(kindergarten);
        try {
            kindergartenEntity = this.kindergartenRepository.saveAndFlush(kindergartenEntity);
        } catch (final ObjectOptimisticLockingFailureException exception) {
            final var message = "Die Daten sind nicht mehr aktuell. Es wurden bereits aktuellere Daten gespeichert.";
            throw new OptimisticLockingException(message, exception);
        }
        return this.infrastruktureinrichtungDomainMapper.entity2Model(kindergartenEntity);
    }

    /**
     * Diese Methode updated ein {@link KindergartenModel}.
     *
     * @param kindergarten zum Updaten.
     * @return das geupdatete {@link KindergartenModel}.
     * @throws EntityNotFoundException    falls der Kindergarten identifiziert durch die {@link KindergartenModel#getId()} nicht gefunden wird.
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist.
     */
    public KindergartenModel updateKindergarten(final KindergartenModel kindergarten) throws EntityNotFoundException, OptimisticLockingException {
        this.getKindergartenById(kindergarten.getId());
        return this.saveKindergarten(kindergarten);
    }

    /**
     * Diese Methode löscht ein {@link KindergartenModel}.
     *
     * @param id zum Identifizieren des {@link KindergartenModel}.
     * @throws EntityNotFoundException     falls der Kindergarten identifiziert durch die {@link KindergartenModel#getId()} nicht gefunden wird.
     * @throws EntityIsReferencedException falls ein {@link BauvorhabenModel} in dem Kindergarten referenziert wird.
     */
    public void deleteKindergartenById(final UUID id) throws EntityNotFoundException, EntityIsReferencedException {
        final var kindergarten = this.getKindergartenById(id);
        this.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(kindergarten.getInfrastruktureinrichtung());
        this.kindergartenRepository.deleteById(id);
    }

    /**
     * Enthält das im Parameter gegebene {@link KindergartenModel} ein {@link BauvorhabenModel},
     * wird eine {@link EntityIsReferencedException} geworfen.
     *
     * @param infrastruktureinrichtung zum Prüfen.
     * @throws EntityIsReferencedException falls das {@link KindergartenModel} ein {@link BauvorhabenModel} referenziert.
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
