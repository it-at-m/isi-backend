package de.muenchen.isi.domain.service.infrastruktureinrichtung;

import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.mapper.InfrastruktureinrichtungDomainMapper;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.MittelschuleModel;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Mittelschule;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.MittelschuleRepository;
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
public class MittelschuleService {

    private final InfrastruktureinrichtungDomainMapper infrastruktureinrichtungDomainMapper;

    private final MittelschuleRepository mittelschuleRepository;

    /**
     * Die Methode gibt alle {@link MittelschuleModel} als Liste zurück.
     *
     * @return Liste an {@link MittelschuleModel}.
     */
    public List<MittelschuleModel> getMittelschulen() {
        return this.mittelschuleRepository.findAllByOrderByInfrastruktureinrichtungNameEinrichtungAsc()
            .map(this.infrastruktureinrichtungDomainMapper::entity2Model)
            .collect(Collectors.toList());
    }

    /**
     * Die Methode gibt ein {@link MittelschuleModel} identifiziert durch die ID zurück.
     *
     * @param id zum Identifizieren des {@link MittelschuleModel}.
     * @return {@link MittelschuleModel}.
     * @throws EntityNotFoundException falls die Mittelschule identifiziert durch die {@link MittelschuleModel#getId()} nicht gefunden wird.
     */
    public MittelschuleModel getMittelschuleById(final UUID id) throws EntityNotFoundException {
        final var optEntity = this.mittelschuleRepository.findById(id);
        final var entity = optEntity.orElseThrow(() -> {
            final var message = "Mittelschule nicht gefunden.";
            log.error(message);
            return new EntityNotFoundException(message);
        });
        return this.infrastruktureinrichtungDomainMapper.entity2Model(entity);
    }

    /**
     * Diese Methode speichert ein {@link MittelschuleModel}.
     *
     * @param mittelschule zum Speichern
     * @return das gespeicherte {@link MittelschuleModel}
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     */
    public MittelschuleModel saveMittelschule(final MittelschuleModel mittelschule) throws OptimisticLockingException {
        Mittelschule mittelschuleEntity = this.infrastruktureinrichtungDomainMapper.model2Entity(mittelschule);
        try {
            mittelschuleEntity = this.mittelschuleRepository.saveAndFlush(mittelschuleEntity);
        } catch (final ObjectOptimisticLockingFailureException exception) {
            final var message = "Die Daten wurden in der Zwischenzeit geändert. Bitte laden Sie die Seite neu!";
            throw new OptimisticLockingException(message, exception);
        }
        return this.infrastruktureinrichtungDomainMapper.entity2Model(mittelschuleEntity);
    }

    /**
     * Diese Methode updated ein {@link MittelschuleModel}.
     *
     * @param mittelschule zum Updaten
     * @return das geupdatete {@link MittelschuleModel}
     * @throws EntityNotFoundException falls die Mittelschule identifiziert durch die {@link MittelschuleModel#getId()} nicht gefunden wird
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     */
    public MittelschuleModel updateMittelschule(final MittelschuleModel mittelschule)
        throws EntityNotFoundException, OptimisticLockingException {
        this.getMittelschuleById(mittelschule.getId());
        return this.saveMittelschule(mittelschule);
    }

    /**
     * Diese Methode löscht ein {@link MittelschuleModel}.
     *
     * @param id zum Identifizieren des {@link MittelschuleModel}.
     * @throws EntityNotFoundException     falls die Mittelschule identifiziert durch die {@link MittelschuleModel#getId()} nicht gefunden wird.
     * @throws EntityIsReferencedException falls ein {@link BauvorhabenModel} in der Mittelschule referenziert wird.
     */
    public void deleteMittelschuleById(final UUID id) throws EntityNotFoundException, EntityIsReferencedException {
        final var mittelschule = this.getMittelschuleById(id);
        this.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(
                mittelschule.getInfrastruktureinrichtung()
            );
        this.mittelschuleRepository.deleteById(id);
    }

    /**
     * Enthält das im Parameter gegebene {@link MittelschuleModel} ein {@link BauvorhabenModel},
     * wird eine {@link EntityIsReferencedException} geworfen.
     *
     * @param infrastruktureinrichtung zum Prüfen.
     * @throws EntityIsReferencedException falls das {@link MittelschuleModel} ein {@link BauvorhabenModel} referenziert.
     */
    protected void throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(
        final InfrastruktureinrichtungModel infrastruktureinrichtung
    ) throws EntityIsReferencedException {
        final var bauvorhaben = infrastruktureinrichtung.getBauvorhaben();
        if (ObjectUtils.isNotEmpty(bauvorhaben)) {
            final var message =
                "Die Infrastruktureinrichtung " +
                infrastruktureinrichtung.getNameEinrichtung() +
                " referenziert das Bauvorhaben " +
                bauvorhaben.getNameVorhaben() +
                ".";
            log.error(message);
            throw new EntityIsReferencedException(message);
        }
    }
}
