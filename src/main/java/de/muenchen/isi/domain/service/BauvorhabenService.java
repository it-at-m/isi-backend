package de.muenchen.isi.domain.service;

import de.muenchen.isi.api.dto.AbfrageDto;
import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.mapper.AbfrageDomainMapper;
import de.muenchen.isi.domain.mapper.BauvorhabenDomainMapper;
import de.muenchen.isi.domain.mapper.InfrastruktureinrichtungDomainMapper;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfrageAngelegtModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.domain.model.list.AbfrageListElementModel;
import de.muenchen.isi.domain.model.list.InfrastruktureinrichtungListElementModel;
import de.muenchen.isi.domain.service.filehandling.DokumentService;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import de.muenchen.isi.infrastructure.repository.BauvorhabenRepository;
import de.muenchen.isi.infrastructure.repository.InfrastrukturabfrageRepository;
import de.muenchen.isi.infrastructure.repository.InfrastruktureinrichtungRepository;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.lang.Nullable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BauvorhabenService {

    private final BauvorhabenDomainMapper bauvorhabenDomainMapper;

    private final InfrastruktureinrichtungDomainMapper infrastruktureinrichtungDomainMapper;

    private final AbfrageDomainMapper abfrageDomainMapper;

    private final BauvorhabenRepository bauvorhabenRepository;

    private final InfrastrukturabfrageRepository infrastrukturabfrageRepository;

    private final InfrastruktureinrichtungRepository infrastruktureinrichtungRepository;

    private final DokumentService dokumentService;

    /**
     * Die Methode gibt alle {@link BauvorhabenModel} als Liste zurück.
     *
     * @return Liste an {@link BauvorhabenModel}.
     */
    public List<BauvorhabenModel> getBauvorhaben() {
        return this.bauvorhabenRepository.findAllByOrderByGrundstuecksgroesseDesc()
            .map(this.bauvorhabenDomainMapper::entity2Model)
            .collect(Collectors.toList());
    }

    /**
     * Die Methode gibt ein {@link BauvorhabenModel} identifiziert durch die ID zurück.
     *
     * @param id zum Identifizieren des {@link BauvorhabenModel}.
     * @return {@link BauvorhabenModel}.
     */
    public BauvorhabenModel getBauvorhabenById(final UUID id) throws EntityNotFoundException {
        final var optEntity = this.bauvorhabenRepository.findById(id);
        final var entity = optEntity.orElseThrow(() -> {
            final var message = "Bauvorhaben nicht gefunden";
            log.error(message);
            return new EntityNotFoundException(message);
        });
        return this.bauvorhabenDomainMapper.entity2Model(entity);
    }

    /**
     * Diese Methode speichert ein {@link BauvorhabenModel}.
     *
     * @param bauvorhaben zum Speichern
     * @return das gespeicherte {@link BauvorhabenModel}
     * @throws UniqueViolationException   falls der Name des Bauvorhabens {@link BauvorhabenModel#getNameVorhaben()} bereits vorhanden ist
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     */
    public BauvorhabenModel saveBauvorhaben(final BauvorhabenModel bauvorhaben)
        throws UniqueViolationException, OptimisticLockingException {
        var bauvorhabenEntity = this.bauvorhabenDomainMapper.model2Entity(bauvorhaben);
        final var saved = this.bauvorhabenRepository.findByNameVorhabenIgnoreCase(bauvorhabenEntity.getNameVorhaben());
        if ((saved.isPresent() && saved.get().getId().equals(bauvorhabenEntity.getId())) || saved.isEmpty()) {
            try {
                bauvorhabenEntity = this.bauvorhabenRepository.saveAndFlush(bauvorhabenEntity);
            } catch (final ObjectOptimisticLockingFailureException exception) {
                final var message = "Die Daten wurden in der Zwischenzeit geändert. Bitte laden Sie die Seite neu!";
                throw new OptimisticLockingException(message, exception);
            }
            return this.bauvorhabenDomainMapper.entity2Model(bauvorhabenEntity);
        } else {
            throw new UniqueViolationException(
                "Der angegebene Name des Bauvorhabens ist schon vorhanden, bitte wählen Sie daher einen anderen Namen und speichern Sie die Abfrage erneut."
            );
        }
    }

    /**
     * Diese Methode updated ein {@link BauvorhabenModel}.
     *
     * @param bauvorhaben zum Updaten
     * @return das geupdatete {@link BauvorhabenModel}
     * @throws EntityNotFoundException           falls das Bauvorhaben identifiziert durch die {@link BauvorhabenModel#getId()} nicht gefunden wird
     * @throws UniqueViolationException          falls der Name des Bauvorhabens {@link BauvorhabenModel#getNameVorhaben()} bereits vorhanden ist
     * @throws OptimisticLockingException        falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     * @throws FileHandlingFailedException
     * @throws FileHandlingWithS3FailedException
     */
    public BauvorhabenModel updateBauvorhaben(final BauvorhabenModel bauvorhaben)
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        final var originalBauvorhabenDb = this.getBauvorhabenById(bauvorhaben.getId());
        dokumentService.deleteDokumenteFromOriginalDokumentenListWhichAreMissingInParameterAdaptedDokumentenListe(
            bauvorhaben.getDokumente(),
            originalBauvorhabenDb.getDokumente()
        );
        return this.saveBauvorhaben(bauvorhaben);
    }

    /**
     * Diese Methode löscht ein {@link BauvorhabenModel}.
     *
     * @param id zum Identifizieren des {@link BauvorhabenModel}.
     * @throws EntityNotFoundException     falls das Bauvorhaben identifiziert durch die {@link BauvorhabenModel#getId()} nicht gefunden wird.
     * @throws EntityIsReferencedException falls das {@link BauvorhabenModel} in einer Abfrage referenziert wird.
     */
    public void deleteBauvorhaben(final UUID id) throws EntityNotFoundException, EntityIsReferencedException {
        final var bauvorhaben = this.getBauvorhabenById(id);
        this.throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(bauvorhaben);
        this.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(bauvorhaben);
        this.bauvorhabenRepository.deleteById(id);
    }

    /**
     * Setzt in einem {@link AbfrageModel} den Wert des Feldes 'bauvorhaben' auf das {@link BauvorhabenModel}, welches über die gegebene ID gefunden wurde.
     * Da im {@link AbfrageDto} das Bauvorhaben über eine ID und im {@link AbfrageModel} als ein {@link BauvorhabenModel} gespeichert wird, wird bei einem Mapping von Dto nach Model das Feld 'bauvorhaben' auf null gesetzt.
     * Diese Methode soll dann verwendet werden, um die beim Mapping verloren gegangene Information zum Bauvorhaben wieder in die Abfrage einzusetzen.
     * Der Parameter 'bauvorhabenId' darf null sein. In diesem Fall passiert nichts.
     *
     * @param bauvorhabenId id des {@link BauvorhabenModel}s. Darf null sein.
     * @param abfrage       zum Speichern.
     * @return Die (möglicherweise) geänderte Abfrage.
     * @throws EntityNotFoundException falls das Bauvorhaben mit der gegebenen ID nicht gefunden wurde.
     */
    public AbfrageAngelegtModel assignBauvorhabenToAbfrage(
        @Nullable final UUID bauvorhabenId,
        final AbfrageAngelegtModel abfrage
    ) throws EntityNotFoundException {
        if (bauvorhabenId != null) {
            final var model = this.getBauvorhabenById(bauvorhabenId);
            abfrage.setBauvorhaben(model);
        }
        return abfrage;
    }

    /**
     * Setzt in einem {@link InfrastruktureinrichtungModel} den Wert des Feldes 'bauvorhaben' auf das {@link BauvorhabenModel}, welches über die gegebene ID gefunden wurde.
     * Da im {@link InfrastruktureinrichtungModel} das Bauvorhaben über eine ID und im {@link InfrastruktureinrichtungModel} als ein {@link BauvorhabenModel} gespeichert wird, wird bei einem Mapping von Dto nach Model das Feld 'bauvorhaben' auf null gesetzt.
     * Diese Methode soll dann verwendet werden, um die beim Mapping verloren gegangene Information zum Bauvorhaben wieder in der Infrastruktureinrichung einzusetzen.
     * Der Parameter 'bauvorhabenId' darf null sein. In diesem Fall passiert nichts.
     *
     * @param bauvorhabenId            id des {@link BauvorhabenModel}s
     * @param infrastruktureinrichtung zum Speichern
     * @return Die (möglicherweise) geänderte Infrastruktureinrichtung
     * @throws EntityNotFoundException falls das Bauvorhaben mit der gegebenen ID nicht gefunden wurde
     */
    public InfrastruktureinrichtungModel assignBauvorhabenToInfrastruktureinrichtung(
        @Nullable final UUID bauvorhabenId,
        final InfrastruktureinrichtungModel infrastruktureinrichtung
    ) throws EntityNotFoundException {
        if (bauvorhabenId != null) {
            final var model = this.getBauvorhabenById(bauvorhabenId);
            infrastruktureinrichtung.setBauvorhaben(model);
        }
        return infrastruktureinrichtung;
    }

    /**
     * Die Methode gibt alle {@link InfrastruktureinrichtungListElementModel} als Liste zurück sortiert nach InfrastrukturTyp und innerhalb
     * des InfrastrukturTyps alphabetisch aufsteigend welche einem Bauvorhaben zugeordnet sind.
     *
     * @param bauvorhabenId zum Identifizieren des {@link BauvorhabenModel}
     * @return Liste von {@link InfrastruktureinrichtungListElementModel} welche einem Bauvorhaben zugeordent sind
     */
    public List<InfrastruktureinrichtungListElementModel> getReferencedInfrastruktureinrichtungen(
        final UUID bauvorhabenId
    ) {
        return this.infrastruktureinrichtungRepository.findAllByBauvorhabenId(bauvorhabenId)
            .map(this.infrastruktureinrichtungDomainMapper::entity2ListElementModel)
            .sorted(
                Comparator
                    .comparing(InfrastruktureinrichtungListElementModel::getInfrastruktureinrichtungTyp)
                    .thenComparing(InfrastruktureinrichtungListElementModel::getNameEinrichtung)
            )
            .collect(Collectors.toList());
    }

    /**
     * Die Methode gibt alle {@link AbfrageListElementModel} als Liste zurück sortiert nach Erstellungsdatum aufsteigend
     * welche einem Bauvorhaben zugeordnet sind.
     *
     * @param bauvorhabenId zum Identifizieren des {@link BauvorhabenModel}
     * @return Liste von {@link AbfrageListElementModel} welche einem Bauvorhaben zugeordent sind
     */
    public List<AbfrageListElementModel> getReferencedInfrastrukturabfragen(final UUID bauvorhabenId) {
        return this.infrastrukturabfrageRepository.findAllByAbfrageBauvorhabenIdOrderByCreatedDateTimeAsc(bauvorhabenId)
            .map(this.abfrageDomainMapper::entity2Model)
            .map(this.abfrageDomainMapper::model2ListElementModel)
            .collect(Collectors.toList());
    }

    /**
     * Wird das im Parameter gegebene {@link BauvorhabenModel} durch ein {@link AbfrageModel} referenziert,
     * wird eine {@link EntityIsReferencedException} geworfen.
     *
     * @param bauvorhaben zum Prüfen.
     * @throws EntityIsReferencedException falls das {@link BauvorhabenModel} durch ein {@link AbfrageModel} referenziert wird.
     */
    protected void throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(
        final BauvorhabenModel bauvorhaben
    ) throws EntityIsReferencedException {
        final List<String> nameAbfragen =
            this.infrastrukturabfrageRepository.findAllByAbfrageBauvorhabenId(bauvorhaben.getId())
                .map(abfrage -> abfrage.getAbfrage().getNameAbfrage())
                .collect(Collectors.toList());
        if (!nameAbfragen.isEmpty()) {
            final var commaSeparatedNames = String.join(", ", nameAbfragen);
            final var message =
                "Das Bauvorhaben " +
                bauvorhaben.getNameVorhaben() +
                " wird durch die Abfragen " +
                commaSeparatedNames +
                " referenziert.";
            log.error(message);
            throw new EntityIsReferencedException(message);
        }
    }

    /**
     * Wird das im Parameter gegebene {@link BauvorhabenModel} durch ein {@link InfrastruktureinrichtungModel} referenziert,
     * wird eine {@link EntityIsReferencedException} geworfen.
     *
     * @param bauvorhaben zum Prüfen.
     * @throws EntityIsReferencedException falls das {@link BauvorhabenModel} durch ein {@link InfrastruktureinrichtungModel} referenziert wird.
     */
    protected void throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(
        final BauvorhabenModel bauvorhaben
    ) throws EntityIsReferencedException {
        final List<String> namenInfrastruktureinrichtung =
            this.infrastruktureinrichtungRepository.findAllByBauvorhabenId(bauvorhaben.getId())
                .map(Infrastruktureinrichtung::getNameEinrichtung)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(namenInfrastruktureinrichtung)) {
            final var commaSeparatedNames = String.join(", ", namenInfrastruktureinrichtung);
            final var message =
                "Das Bauvorhaben " +
                bauvorhaben.getNameVorhaben() +
                " wird durch die Infrastruktureinrichtungen " +
                commaSeparatedNames +
                " referenziert.";
            log.error(message);
            throw new EntityIsReferencedException(message);
        }
    }
}
