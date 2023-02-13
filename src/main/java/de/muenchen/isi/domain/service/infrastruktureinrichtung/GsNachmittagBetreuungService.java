package de.muenchen.isi.domain.service.infrastruktureinrichtung;

import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.mapper.InfrastruktureinrichtungDomainMapper;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.GsNachmittagBetreuungModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.GsNachmittagBetreuung;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.GsNachmittagBetreuungRepository;
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
public class GsNachmittagBetreuungService {

    private final InfrastruktureinrichtungDomainMapper infrastruktureinrichtungDomainMapper;

    private final GsNachmittagBetreuungRepository gsNachmittagBetreuungRepository;

    /**
     * Die Methode gibt alle {@link GsNachmittagBetreuungModel} als Liste zurück.
     *
     * @return Liste an {@link GsNachmittagBetreuungModel}.
     */
    public List<GsNachmittagBetreuungModel> getGsNachmittagBetreuungen() {
        return this.gsNachmittagBetreuungRepository.findAllByOrderByInfrastruktureinrichtungNameEinrichtungAsc()
                .map(this.infrastruktureinrichtungDomainMapper::entity2Model)
                .collect(Collectors.toList());
    }

    /**
     * Die Methode gibt ein {@link GsNachmittagBetreuungModel} identifiziert durch die ID zurück.
     *
     * @param id zum Identifizieren des {@link GsNachmittagBetreuungModel}.
     * @return {@link GsNachmittagBetreuungModel}.
     * @throws EntityNotFoundException falls die Nachmittagsbetreuung für Grundschulkinder identifiziert durch die {@link GsNachmittagBetreuungModel#getId()} nicht gefunden wird.
     */
    public GsNachmittagBetreuungModel getGsNachmittagBetreuungById(final UUID id) throws EntityNotFoundException {
        final var optEntity = this.gsNachmittagBetreuungRepository.findById(id);
        final var entity = optEntity.orElseThrow(() -> {
            final var message = "Nachmittagsbetreuung für Grundschulkinder nicht gefunden.";
            log.error(message);
            return new EntityNotFoundException(message);
        });
        return this.infrastruktureinrichtungDomainMapper.entity2Model(entity);
    }

    /**
     * Diese Methode speichert ein {@link GsNachmittagBetreuungModel}.
     *
     * @param gsNachmittagBetreuung zum Speichern.
     * @return das gespeicherte {@link GsNachmittagBetreuungModel}.
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist.
     */
    public GsNachmittagBetreuungModel saveGsNachmittagBetreuung(final GsNachmittagBetreuungModel gsNachmittagBetreuung) throws OptimisticLockingException {
        GsNachmittagBetreuung gsNachmittagBetreuungEntity = this.infrastruktureinrichtungDomainMapper.model2Entity(gsNachmittagBetreuung);
        try {
            gsNachmittagBetreuungEntity = this.gsNachmittagBetreuungRepository.saveAndFlush(gsNachmittagBetreuungEntity);
        } catch (final ObjectOptimisticLockingFailureException exception) {
            final var message = "Die Daten wurden in der Zwischenzeit geändert. Bitte laden Sie die Seite neu!";
            throw new OptimisticLockingException(message, exception);
        }
        return this.infrastruktureinrichtungDomainMapper.entity2Model(gsNachmittagBetreuungEntity);
    }

    /**
     * Diese Methode updated ein {@link GsNachmittagBetreuungModel}.
     *
     * @param gsNachmittagBetreuung zum Updaten.
     * @return das geupdatete {@link GsNachmittagBetreuungModel}.
     * @throws EntityNotFoundException    falls die Nachmittagsbetreuung für Grundschulkinder identifiziert durch die {@link GsNachmittagBetreuungModel#getId()} nicht gefunden wird.
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist.
     */
    public GsNachmittagBetreuungModel updateGsNachmittagBetreuung(final GsNachmittagBetreuungModel gsNachmittagBetreuung) throws EntityNotFoundException, OptimisticLockingException {
        this.getGsNachmittagBetreuungById(gsNachmittagBetreuung.getId());
        return this.saveGsNachmittagBetreuung(gsNachmittagBetreuung);
    }

    /**
     * Diese Methode löscht ein {@link GsNachmittagBetreuungModel}.
     *
     * @param id zum Identifizieren des {@link GsNachmittagBetreuungModel}.
     * @throws EntityNotFoundException     falls die Nachmittagsbetreuung für Grundschulkinder identifiziert durch die {@link GsNachmittagBetreuungModel#getId()} nicht gefunden wird.
     * @throws EntityIsReferencedException falls ein {@link BauvorhabenModel} in der ganztägigen Betreuung von Grundschulkinder referenziert wird.
     */
    public void deleteGsNachmittagBetreuungById(final UUID id) throws EntityNotFoundException, EntityIsReferencedException {
        final var gsNachmittagBetreuung = this.getGsNachmittagBetreuungById(id);
        this.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(gsNachmittagBetreuung.getInfrastruktureinrichtung());
        this.gsNachmittagBetreuungRepository.deleteById(id);
    }

    /**
     * Enthält das im Parameter gegebene {@link GsNachmittagBetreuungModel} ein {@link BauvorhabenModel},
     * wird eine {@link EntityIsReferencedException} geworfen.
     *
     * @param infrastruktureinrichtung zum Prüfen.
     * @throws EntityIsReferencedException falls das {@link GsNachmittagBetreuungModel} ein {@link BauvorhabenModel} referenziert.
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
