package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.ReportingException;
import de.muenchen.isi.domain.mapper.InfrastruktureinrichtungDomainMapper;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.domain.service.etlInterface.EtlInterfaceService;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import de.muenchen.isi.infrastructure.repository.BauvorhabenRepository;
import de.muenchen.isi.infrastructure.repository.InfrastruktureinrichtungRepository;
import de.muenchen.isi.infrastructure.repository.common.KommentarRepository;
import de.muenchen.isi.reporting.client.model.EtlTriggerJobDto;
import de.muenchen.isi.reporting.client.model.PairStringString;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
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

    private final KommentarRepository kommentarRepository;

    private final BauvorhabenRepository bauvorhabenRepository;

    private final EtlInterfaceService etlInterfaceService;

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
     * @throws ReportingException         falls bei der Übermittlung an die Reportingschnittstelle ein Fehler auftritt.
     */
    public InfrastruktureinrichtungModel saveInfrastruktureinrichtung(
        final InfrastruktureinrichtungModel infrastruktureinrichtung
    ) throws OptimisticLockingException, EntityNotFoundException, ReportingException {
        final var listParameter = new ArrayList<PairStringString>();
        Infrastruktureinrichtung entity =
            this.infrastruktureinrichtungDomainMapper.model2Entity(infrastruktureinrichtung);
        try {
            entity = this.infrastruktureinrichtungRepository.saveAndFlush(entity);
            EtlTriggerJobDto etlTriggerJobDto = new EtlTriggerJobDto();
            etlTriggerJobDto.setJobname(
                "importFromBackend/infrastruktureinrichtung/Job_Import_Infrastruktureinrichtung.kjb"
            );
            final var idParameter = new PairStringString();
            idParameter.setFirst("id");
            idParameter.setSecond(entity.getId().toString());
            listParameter.add(idParameter);
            etlTriggerJobDto.setParameters(listParameter);
            etlInterfaceService.etlInterfaceTriggerJob(etlTriggerJobDto);
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
     * @throws EntityNotFoundException    falls die Infrastruktureinrichtung identifiziert durch die {@link InfrastruktureinrichtungModel#getId()} nicht gefunden wird
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     * @throws ReportingException         falls bei der Übermittlung an die Reportingschnittstelle ein Fehler auftritt.
     */
    public InfrastruktureinrichtungModel updateInfrastruktureinrichtung(
        final InfrastruktureinrichtungModel infrastruktureinrichtung
    ) throws EntityNotFoundException, OptimisticLockingException, ReportingException {
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
        this.kommentarRepository.deleteAllByInfrastruktureinrichtungId(id);
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
        Optional<Bauvorhaben> bauvorhaben = Optional.empty();
        if (ObjectUtils.isNotEmpty(infrastruktureinrichtung.getBauvorhaben())) {
            bauvorhaben = bauvorhabenRepository.findById(infrastruktureinrichtung.getBauvorhaben());
        }
        if (bauvorhaben.isPresent()) {
            final var message =
                "Die Infrastruktureinrichtung " +
                infrastruktureinrichtung.getNameEinrichtung() +
                " referenziert das Bauvorhaben " +
                bauvorhaben.get().getNameVorhaben() +
                ".";
            log.error(message);
            throw new EntityIsReferencedException(message);
        }
    }
}
