package de.muenchen.isi.domain.service.infrastruktureinrichtung;

import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.mapper.InfrastruktureinrichtungDomainMapper;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.KinderkrippeModel;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kinderkrippe;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.KinderkrippeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class KinderkrippeService {

    private final InfrastruktureinrichtungDomainMapper infrastruktureinrichtungDomainMapper;

    private final KinderkrippeRepository kinderkrippeRepository;

    /**
     * Die Methode gibt alle {@link KinderkrippeModel} als Liste zurück.
     *
     * @return Liste an {@link KinderkrippeModel}.
     */
    public List<KinderkrippeModel> getKinderkrippen() {
        return this.kinderkrippeRepository.findAllByOrderByInfrastruktureinrichtungNameEinrichtungAsc()
                .map(this.infrastruktureinrichtungDomainMapper::entity2Model)
                .collect(Collectors.toList());
    }

    /**
     * Die Methode gibt ein {@link KinderkrippeModel} identifiziert durch die ID zurück.
     *
     * @param id zum Identifizieren des {@link KinderkrippeModel}.
     * @return {@link KinderkrippeModel}.
     * @throws EntityNotFoundException falls die Kinderkrippe identifiziert durch die {@link KinderkrippeModel#getId()} nicht gefunden wird.
     */
    public KinderkrippeModel getKinderkrippeById(final UUID id) throws EntityNotFoundException {
        final var optEntity = this.kinderkrippeRepository.findById(id);
        final var entity = optEntity.orElseThrow(() -> {
            final var message = "Kinderkrippe nicht gefunden.";
            log.error(message);
            return new EntityNotFoundException(message);
        });
        return this.infrastruktureinrichtungDomainMapper.entity2Model(entity);
    }

    /**
     * Diese Methode speichert ein {@link KinderkrippeModel}.
     *
     * @param kinderkrippe zum Speichern.
     * @return das gespeicherte {@link KinderkrippeModel}.
     */
    public KinderkrippeModel saveKinderkrippe(final KinderkrippeModel kinderkrippe) {
        Kinderkrippe kinderkrippeEntity = this.infrastruktureinrichtungDomainMapper.model2Entity(kinderkrippe);
        kinderkrippeEntity = this.kinderkrippeRepository.saveAndFlush(kinderkrippeEntity);
        return this.infrastruktureinrichtungDomainMapper.entity2Model(kinderkrippeEntity);
    }

    /**
     * Diese Methode updated ein {@link KinderkrippeModel}.
     *
     * @param kinderkrippe zum Updaten.
     * @return das geupdatete {@link KinderkrippeModel}.
     * @throws EntityNotFoundException falls die Kinderkrippe identifiziert durch die {@link KinderkrippeModel#getId()} nicht gefunden wird.
     */
    public KinderkrippeModel updateKinderkrippe(final KinderkrippeModel kinderkrippe) throws EntityNotFoundException {
        this.getKinderkrippeById(kinderkrippe.getId());
        return this.saveKinderkrippe(kinderkrippe);
    }

    /**
     * Diese Methode löscht ein {@link KinderkrippeModel}.
     *
     * @param id zum Identifizieren des {@link KinderkrippeModel}.
     * @throws EntityNotFoundException     falls die Kinderkrippe identifiziert durch die {@link KinderkrippeModel#getId()} nicht gefunden wird.
     * @throws EntityIsReferencedException falls ein {@link BauvorhabenModel} in der Kinderkrippe referenziert wird.
     */
    public void deleteKinderkrippeById(final UUID id) throws EntityNotFoundException, EntityIsReferencedException {
        final var kinderkrippe = this.getKinderkrippeById(id);
        this.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(kinderkrippe.getInfrastruktureinrichtung());
        this.kinderkrippeRepository.deleteById(id);
    }

    /**
     * Enthält das im Parameter gegebene {@link KinderkrippeModel} ein {@link BauvorhabenModel},
     * wird eine {@link EntityIsReferencedException} geworfen.
     *
     * @param infrastruktureinrichtung zum Prüfen.
     * @throws EntityIsReferencedException falls das {@link KinderkrippeModel} ein {@link BauvorhabenModel} referenziert.
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
