package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.mapper.InfrastruktureinrichtungDomainMapper;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.domain.model.list.InfrastruktureinrichtungListElementModel;
import de.muenchen.isi.domain.model.list.InfrastruktureinrichtungListElementsModel;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import de.muenchen.isi.infrastructure.repository.InfrastruktureinrichtungRepository;
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
public class InfrastruktureinrichtungService {

    private final InfrastruktureinrichtungRepository infrastruktureinrichtungRepository;

    private final InfrastruktureinrichtungDomainMapper infrastruktureinrichtungDomainMapper;

    /**
     * Die Methode gibt {@link InfrastruktureinrichtungListElementModel}e sortiert aufsteigend nach Name der Einrichtung zurück.
     *
     * @return die {@link InfrastruktureinrichtungListElementModel}s sortiert in aufsteigender Reihenfolge.
     */
    public InfrastruktureinrichtungListElementsModel getInfrastruktureinrichtungListElements() {
        final var infrastruktureinrichtungListElementsModel = new InfrastruktureinrichtungListElementsModel();
        final List<InfrastruktureinrichtungListElementModel> listElements =
            this.infrastruktureinrichtungRepository.findAllByOrderByNameEinrichtungAsc()
                .map(this.infrastruktureinrichtungDomainMapper::entity2ListElementModel)
                .collect(Collectors.toList());
        infrastruktureinrichtungListElementsModel.setListElements(listElements);
        return infrastruktureinrichtungListElementsModel;
    }

    /**
     * Die Methode gibt ein {@link InfrastruktureinrichtungModel} identifiziert durch die ID zurück.
     *
     * @param id zum Identifizieren des {@link InfrastruktureinrichtungModel}.
     * @return {@link InfrastruktureinrichtungModel}.
     * @throws EntityNotFoundException falls die Infrastruktureinrichtung identifiziert durch die {@link InfrastruktureinrichtungModel#getId()} nicht gefunden wird.
     */
    public InfrastruktureinrichtungModel getInfrastruktureinrichtungById(final UUID id) throws EntityNotFoundException {
        final var optEntity = this.infrastruktureinrichtungRepository.findById(id);
        final var entity = optEntity.orElseThrow(() -> {
            final var message = "Grundschule nicht gefunden.";
            log.error(message);
            return new EntityNotFoundException(message);
        });
        return this.infrastruktureinrichtungDomainMapper.entity2Model(entity);
    }

    /**
     * Diese Methode speichert ein {@link InfrastruktureinrichtungModel}.
     *
     * @param infrastruktureinrichtung zum Speichern
     * @return das gespeicherte {@link InfrastruktureinrichtungModel}
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     */
    public InfrastruktureinrichtungModel saveInfrastruktureinrichtung(
        final InfrastruktureinrichtungModel infrastruktureinrichtung
    ) throws OptimisticLockingException {
        Infrastruktureinrichtung entity =
            this.infrastruktureinrichtungDomainMapper.model2Entity(infrastruktureinrichtung);
        try {
            entity = this.infrastruktureinrichtungRepository.saveAndFlush(entity);
        } catch (final ObjectOptimisticLockingFailureException exception) {
            final var message = "Die Daten wurden in der Zwischenzeit geändert. Bitte laden Sie die Seite neu!";
            throw new OptimisticLockingException(message, exception);
        }
        return this.infrastruktureinrichtungDomainMapper.entity2Model(entity);
    }

    /**
     * Diese Methode updated ein {@link InfrastruktureinrichtungModel}.
     *
     * @param infrastruktureinrichtung zum Updaten
     * @return das geupdatete {@link InfrastruktureinrichtungModel}
     * @throws EntityNotFoundException falls die Infrastruktureinrichtung identifiziert durch die {@link InfrastruktureinrichtungModel#getId()} nicht gefunden wird
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     */
    public InfrastruktureinrichtungModel updateInfrastruktureinrichtung(
        final InfrastruktureinrichtungModel infrastruktureinrichtung
    ) throws EntityNotFoundException, OptimisticLockingException {
        this.getInfrastruktureinrichtungById(infrastruktureinrichtung.getId());
        return this.saveInfrastruktureinrichtung(infrastruktureinrichtung);
    }

    /**
     * Diese Methode löscht ein {@link InfrastruktureinrichtungModel}.
     *
     * @param id zum Identifizieren des {@link InfrastruktureinrichtungModel}.
     * @throws EntityNotFoundException     falls die Grundschule identifiziert durch die {@link InfrastruktureinrichtungModel#getId()} nicht gefunden wird.
     * @throws EntityIsReferencedException falls ein {@link BauvorhabenModel} in der Infrastruktureinrichtung referenziert wird.
     */
    public void deleteInfrastruktureinrichtungById(final UUID id)
        throws EntityNotFoundException, EntityIsReferencedException {
        final var infrastruktureinrichtung = this.getInfrastruktureinrichtungById(id);
        this.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(
                infrastruktureinrichtung
            );
        this.infrastruktureinrichtungRepository.deleteById(id);
    }

    /**
     * Enthält das im Parameter gegebene {@link InfrastruktureinrichtungModel} ein {@link BauvorhabenModel},
     * wird eine {@link EntityIsReferencedException} geworfen.
     *
     * @param infrastruktureinrichtung zum Prüfen.
     * @throws EntityIsReferencedException falls das {@link InfrastruktureinrichtungModel} ein {@link BauvorhabenModel} referenziert.
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
