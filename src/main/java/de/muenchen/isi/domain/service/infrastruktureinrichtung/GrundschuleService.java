package de.muenchen.isi.domain.service.infrastruktureinrichtung;

import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.mapper.InfrastruktureinrichtungDomainMapper;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.GrundschuleModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Grundschule;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.GrundschuleRepository;
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
public class GrundschuleService {

    private final InfrastruktureinrichtungDomainMapper infrastruktureinrichtungDomainMapper;

    private final GrundschuleRepository grundschuleRepository;

    /**
     * Die Methode gibt alle {@link GrundschuleModel} als Liste zurück.
     *
     * @return Liste an {@link GrundschuleModel}.
     */
    public List<GrundschuleModel> getGrundschulen() {
        return this.grundschuleRepository.findAllByOrderByInfrastruktureinrichtungNameEinrichtungAsc()
                .map(this.infrastruktureinrichtungDomainMapper::entity2Model)
                .collect(Collectors.toList());
    }

    /**
     * Die Methode gibt ein {@link GrundschuleModel} identifiziert durch die ID zurück.
     *
     * @param id zum Identifizieren des {@link GrundschuleModel}.
     * @return {@link GrundschuleModel}.
     * @throws EntityNotFoundException falls die Grundschule identifiziert durch die {@link GrundschuleModel#getId()} nicht gefunden wird.
     */
    public GrundschuleModel getGrundschuleById(final UUID id) throws EntityNotFoundException {
        final var optEntity = this.grundschuleRepository.findById(id);
        final var entity = optEntity.orElseThrow(() -> {
            final var message = "Grundschule nicht gefunden.";
            log.error(message);
            return new EntityNotFoundException(message);
        });
        return this.infrastruktureinrichtungDomainMapper.entity2Model(entity);
    }

    /**
     * Diese Methode speichert ein {@link GrundschuleModel}.
     *
     * @param grundschule zum Speichern.
     * @return das gespeicherte {@link GrundschuleModel}.
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist.
     */
    public GrundschuleModel saveGrundschule(final GrundschuleModel grundschule) throws OptimisticLockingException {
        Grundschule grundschuleEntity = this.infrastruktureinrichtungDomainMapper.model2Entity(grundschule);
        try {
            grundschuleEntity = this.grundschuleRepository.saveAndFlush(grundschuleEntity);
        } catch (final ObjectOptimisticLockingFailureException exception) {
            final var message = "Die Daten sind nicht mehr aktuell. Es wurden bereits aktuellere Daten gespeichert.";
            throw new OptimisticLockingException(message, exception);
        }
        return this.infrastruktureinrichtungDomainMapper.entity2Model(grundschuleEntity);
    }

    /**
     * Diese Methode updated ein {@link GrundschuleModel}.
     *
     * @param grundschule zum Updaten.
     * @return das geupdatete {@link GrundschuleModel}.
     * @throws EntityNotFoundException    falls die Grundschule identifiziert durch die {@link GrundschuleModel#getId()} nicht gefunden wird.
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist.
     */
    public GrundschuleModel updateGrundschule(final GrundschuleModel grundschule) throws EntityNotFoundException, OptimisticLockingException {
        this.getGrundschuleById(grundschule.getId());
        return this.saveGrundschule(grundschule);
    }

    /**
     * Diese Methode löscht ein {@link GrundschuleModel}.
     *
     * @param id zum Identifizieren des {@link GrundschuleModel}.
     * @throws EntityNotFoundException     falls die Grundschule identifiziert durch die {@link GrundschuleModel#getId()} nicht gefunden wird.
     * @throws EntityIsReferencedException falls ein {@link BauvorhabenModel} in der Grundschule referenziert wird.
     */
    public void deleteGrundschuleById(final UUID id) throws EntityNotFoundException, EntityIsReferencedException {
        final var grundschule = this.getGrundschuleById(id);
        this.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(grundschule.getInfrastruktureinrichtung());
        this.grundschuleRepository.deleteById(id);
    }

    /**
     * Enthält das im Parameter gegebene {@link GrundschuleModel} ein {@link BauvorhabenModel},
     * wird eine {@link EntityIsReferencedException} geworfen.
     *
     * @param infrastruktureinrichtung zum Prüfen.
     * @throws EntityIsReferencedException falls das {@link GrundschuleModel} ein {@link BauvorhabenModel} referenziert.
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
