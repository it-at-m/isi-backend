package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.BauvorhabenNotReferencedException;
import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.domain.mapper.AbfrageDomainMapper;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.AbfragevarianteModel;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.InfrastrukturabfrageAngelegtModel;
import de.muenchen.isi.domain.model.abfrageBedarfsmeldungInBearbeitungFachreferate.InfrastrukturabfrageInBearbeitungFachreferateModel;
import de.muenchen.isi.domain.model.abfrageSachbearbeitungInBearbeitungSachbearbeitung.InfrastrukturabfrageInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.service.filehandling.DokumentService;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.repository.InfrastrukturabfrageRepository;
import de.muenchen.isi.security.AuthenticationUtils;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbfrageService {

    private final AbfrageDomainMapper abfrageDomainMapper;

    private final InfrastrukturabfrageRepository infrastrukturabfrageRepository;

    private final DokumentService dokumentService;

    private final AuthenticationUtils authenticationUtils;

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
     * @throws UniqueViolationException   falls der Name der Abfrage oder der Abfragevariante bereits vorhanden ist
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
            } catch (final DataIntegrityViolationException exception) {
                final var message =
                    "Der angegebene Name der Abfragevariante ist schon vorhanden, bitte wählen Sie daher einen anderen Namen und speichern Sie die Abfrage erneut.";
                throw new UniqueViolationException(message);
            }
            return this.abfrageDomainMapper.entity2Model(abfrageEntity);
        } else {
            throw new UniqueViolationException(
                "Der angegebene Name der Abfrage ist schon vorhanden, bitte wählen Sie daher einen anderen Namen und speichern Sie die Abfrage erneut."
            );
        }
    }

    /**
     * Die Methode führt ein Update das in der Datenbank befindlichen {@link InfrastrukturabfrageModel} identifiziert durch den Parameter id durch.
     * Dieses muss sich im Status {@link StatusAbfrage#ANGELEGT} befinden.
     *
     * @param abfrage zum Updaten
     * @return das geupdatete {@link InfrastrukturabfrageModel}
     * @throws EntityNotFoundException           falls die Abfrage identifiziert durch die {@link InfrastrukturabfrageModel#getId()} nicht gefunden wird
     * @throws UniqueViolationException          falls der Name der Abfrage {@link InfrastrukturabfrageModel#getAbfrage().getNameAbfrage} ()} bereits vorhanden ist
     * @throws OptimisticLockingException        falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     * @throws AbfrageStatusNotAllowedException  falls sich die Abfrage nicht in einem zulässigen Status befindet
     * @throws FileHandlingFailedException
     * @throws FileHandlingWithS3FailedException
     */
    @Transactional
    public InfrastrukturabfrageModel patchAbfrageAngelegt(
        final InfrastrukturabfrageAngelegtModel abfrage,
        final UUID id
    )
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException, AbfrageStatusNotAllowedException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        final var originalAbfrageDb = this.getInfrastrukturabfrageById(id);
        this.throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                originalAbfrageDb.getAbfrage(),
                StatusAbfrage.ANGELEGT
            );
        dokumentService.deleteDokumenteFromOriginalDokumentenListWhichAreMissingInParameterAdaptedDokumentenListe(
            abfrage.getAbfrage().getDokumente(),
            originalAbfrageDb.getAbfrage().getDokumente()
        );
        final var abfrageToSave = this.abfrageDomainMapper.request2Model(abfrage, originalAbfrageDb);
        return this.saveInfrastrukturabfrage(abfrageToSave);
    }

    /**
     * Die Methode führt ein Update das in der Datenbank befindlichen {@link InfrastrukturabfrageModel} identifiziert durch den Parameter id durch.
     * Dieses muss sich im Status {@link StatusAbfrage#IN_BEARBEITUNG_SACHBEARBEITUNG} befinden.
     *
     * @param abfrage zum zum Updaten
     * @return das geupdatete {@link InfrastrukturabfrageModel}
     * @throws EntityNotFoundException    falls die Abfrage identifiziert durch die {@link InfrastrukturabfrageModel#getId()} nicht gefunden wird
     * @throws UniqueViolationException   falls der Name der Abfrage {@link InfrastrukturabfrageModel#getAbfrage().getNameAbfrage} ()} bereits vorhanden ist
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     */
    public InfrastrukturabfrageModel patchAbfrageInBearbeitungSachbearbeitung(
        final InfrastrukturabfrageInBearbeitungSachbearbeitungModel abfrage,
        final UUID id
    )
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException {
        final var originalAbfrageDb = this.getInfrastrukturabfrageById(id);
        this.throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                originalAbfrageDb.getAbfrage(),
                StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG
            );
        final var abfrageToSave = this.abfrageDomainMapper.request2Model(abfrage, originalAbfrageDb);
        return this.saveInfrastrukturabfrage(abfrageToSave);
    }

    /**
     * Die Methode führt ein Update das in der Datenbank befindlichen {@link InfrastrukturabfrageModel} identifiziert durch den Parameter id durch.
     * Dieses muss sich im Status {@link StatusAbfrage#IN_BEARBEITUNG_FACHREFERATE} befinden.
     *
     * @param abfrage zum zum Updaten
     * @return das geupdatete {@link InfrastrukturabfrageModel}
     * @throws EntityNotFoundException    falls die Abfrage identifiziert durch die {@link InfrastrukturabfrageModel#getId()} nicht gefunden wird
     * @throws UniqueViolationException   falls der Name der Abfrage {@link InfrastrukturabfrageModel#getAbfrage().getNameAbfrage} ()} bereits vorhanden ist
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     */
    public InfrastrukturabfrageModel patchAbfrageInBearbeitungFachreferate(
        final InfrastrukturabfrageInBearbeitungFachreferateModel abfrage,
        final UUID id
    )
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException {
        final var originalAbfrageDb = this.getInfrastrukturabfrageById(id);
        this.throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                originalAbfrageDb.getAbfrage(),
                StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE
            );
        final var abfrageToSave = this.abfrageDomainMapper.request2Model(abfrage, originalAbfrageDb);
        return this.saveInfrastrukturabfrage(abfrageToSave);
    }

    /**
     * Diese Methode markiert ein {@link AbfragevarianteModel} als relevant, falls diese noch nicht relevant ist.
     * Ist die Abfragevariante bereits als relevant markiert, wird der Status auf nicht relevant gesetzt.
     * Die Abfragevariante muss sich im Status {@link StatusAbfrage#IN_BEARBEITUNG_SACHBEARBEITUNG} befinden.
     *
     * @param abfrageId         die Abfrage zum Ändern der Relevanz.
     * @param abfragevarianteId die Abfragevariante welche man relevant oder nicht relevant setzten möchte
     * @return das geupdatete {@link InfrastrukturabfrageModel}
     * @throws EntityNotFoundException           falls die Abfrage oder Abfragevariante nicht gefunden wurde
     * @throws UniqueViolationException          falls es schon eine Abfragevariante relevant ist
     * @throws OptimisticLockingException        falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     * @throws AbfrageStatusNotAllowedException  fall die Abfrage den falschen Status hat
     * @throws BauvorhabenNotReferencedException falls die Abfrage zu keinem Bauvorhaben dazugehört
     */
    public InfrastrukturabfrageModel changeAbfragevarianteRelevant(final UUID abfrageId, final UUID abfragevarianteId)
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException, AbfrageStatusNotAllowedException, BauvorhabenNotReferencedException {
        final var abfrage = this.getInfrastrukturabfrageById(abfrageId);
        this.throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                abfrage.getAbfrage(),
                StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG
            );
        final var abfragevariante = Stream
            .concat(
                CollectionUtils.emptyIfNull(abfrage.getAbfragevarianten()).stream(),
                CollectionUtils.emptyIfNull(abfrage.getAbfragevariantenSachbearbeitung()).stream()
            )
            .filter(abfragevarianteModel -> abfragevarianteModel.getId().equals(abfragevarianteId))
            .findFirst()
            .orElseThrow(() -> {
                final var message = "Abfragevariante wurde nicht gefunden";
                log.error(message);
                return new EntityNotFoundException(message);
            });
        if (abfragevariante.isRelevant()) {
            abfragevariante.setRelevant(false);
        } else {
            this.throwsBauvorhabenNotReferencedExceptionOrUniqueViolationExceptionIfRelevant(abfrage);
            abfragevariante.setRelevant(true);
        }
        return this.saveInfrastrukturabfrage(abfrage);
    }

    /**
     * Diese Methode führt für eine Abfrage, die durch die {@link InfrastrukturabfrageModel#getId()} identifiziert ist, eine Statusänderung durch.
     *
     * @param id            {@link InfrastrukturabfrageModel#getId()} der Abfrage zum Updaten
     * @param statusAbfrage neuer {@link StatusAbfrage}
     * @return das geupdatete {@link InfrastrukturabfrageModel}
     * @throws EntityNotFoundException    falls die Abfrage identifiziert durch die {@link InfrastrukturabfrageModel#getId()} nicht gefunden wird
     * @throws UniqueViolationException   falls der Name der Abfrage {@link InfrastrukturabfrageModel#getAbfrage().getNameAbfrage} ()} bereits vorhanden ist
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     */
    public InfrastrukturabfrageModel changeStatusAbfrage(final UUID id, final StatusAbfrage statusAbfrage)
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException {
        final var originalAbfrageDb = this.getInfrastrukturabfrageById(id);
        originalAbfrageDb.getAbfrage().setStatusAbfrage(statusAbfrage);
        return this.saveInfrastrukturabfrage(originalAbfrageDb);
    }

    /**
     * Diese Methode löscht ein {@link InfrastrukturabfrageModel}.
     *
     * @param id zum Identifizieren des {@link InfrastrukturabfrageModel}.
     * @throws EntityNotFoundException          falls die Abfrage identifiziert durch die {@link InfrastrukturabfrageModel#getId()} nicht gefunden wird.
     * @throws EntityIsReferencedException      falls ein {@link BauvorhabenModel} in der Abfrage referenziert wird.
     * @throws UserRoleNotAllowedException      falls der Nutzer nicht die richtige Rolle hat.
     * @throws AbfrageStatusNotAllowedException falls die Abfrage den falschen Status hat..
     */
    public void deleteInfrasturkturabfrageById(final UUID id)
        throws EntityNotFoundException, EntityIsReferencedException, UserRoleNotAllowedException, AbfrageStatusNotAllowedException {
        final var abfrage = this.getInfrastrukturabfrageById(id);
        this.throwUserRoleNotAllowedOrAbfrageStatusNotAlloweExceptionWhenDeleteAbfrage(abfrage.getAbfrage());
        this.throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(abfrage.getAbfrage());
        this.infrastrukturabfrageRepository.deleteById(id);
    }

    /**
     * Diese Methode überprüft ob der Nutzer die richtige Rolle hat und die Abfrage im richtigen Status, um sie zu löschen.
     *
     * @param abfrage zum Identifizieren des Status.
     * @throws UserRoleNotAllowedException      falls der Nutzer nicht die richtige Rolle hat.
     * @throws AbfrageStatusNotAllowedException falls die Abfrage den falschen Status hat..
     */
    public void throwUserRoleNotAllowedOrAbfrageStatusNotAlloweExceptionWhenDeleteAbfrage(AbfrageModel abfrage)
        throws UserRoleNotAllowedException, AbfrageStatusNotAllowedException {
        var roles = authenticationUtils.getUserRoles();
        if (!roles.contains("admin")) {
            if (!roles.contains("abfrageerstellung")) {
                throw new UserRoleNotAllowedException("Keine Berechtigung zum Löschen der Abfrage");
            } else if (abfrage.getStatusAbfrage() != StatusAbfrage.ANGELEGT) {
                throw new AbfrageStatusNotAllowedException(
                    "Die Abfrage kann im nur im Status 'angelegt' gelöscht werden."
                );
            }
        }
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

    /**
     * Enthält das im Parameter gegebene {@link AbfrageModel} einen ungültigen Status {@link StatusAbfrage},
     * wird eine {@link AbfrageStatusNotAllowedException} geworfen.
     *
     * @param abfrage       zum Prüfen.
     * @param statusAbfrage gültiger Status.
     * @throws AbfrageStatusNotAllowedException falls das {@link AbfrageModel} einen unzulässigen Status hat
     */
    protected void throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
        final AbfrageModel abfrage,
        final StatusAbfrage statusAbfrage
    ) throws AbfrageStatusNotAllowedException {
        if (abfrage.getStatusAbfrage() != statusAbfrage) {
            final var message =
                "Die Abfrage " +
                abfrage.getNameAbfrage() +
                " ist im Status " +
                abfrage.getStatusAbfrage().toString() +
                ". Der gültige Status wäre " +
                statusAbfrage.toString() +
                ".";
            log.error(message);
            throw new AbfrageStatusNotAllowedException(message);
        }
    }

    /**
     * Überprüft für das der Abfrage zugeordnete Bauvorhaben, dass in keiner anderen Abfrage eine Abfragevariante als relevant markiert ist.
     *
     * @param abfrage Abfrage in welcher die Relevantsetzung geschieht.
     * @throws UniqueViolationException          falls schon eine Relevante Abfragevariante exisitiert
     * @throws BauvorhabenNotReferencedException falls die Abfrage keinem Bauvorhaben zugeordnet ist
     */

    private void throwsBauvorhabenNotReferencedExceptionOrUniqueViolationExceptionIfRelevant(
        final InfrastrukturabfrageModel abfrage
    ) throws UniqueViolationException, BauvorhabenNotReferencedException {
        if (abfrage.getAbfrage().getBauvorhaben() == null) {
            String message =
                "Die Abfrage ist keinem Bauvorhaben zugeordnet. Somit kann keine Abfragevariante als relevant markiert werden.";
            log.error(message);
            throw new BauvorhabenNotReferencedException(message);
        }
        final var relevanteAbfrage = new AtomicBoolean(false);
        final var nameRelevantAbfrage = new StringBuilder();
        final var nameRelevantAbfragevariante = new StringBuilder();
        this.infrastrukturabfrageRepository.findAllByAbfrageBauvorhabenId(abfrage.getAbfrage().getBauvorhaben().getId())
            .forEach(infrastrukturabfrage -> {
                Stream
                    .concat(
                        CollectionUtils.emptyIfNull(infrastrukturabfrage.getAbfragevarianten()).stream(),
                        CollectionUtils.emptyIfNull(infrastrukturabfrage.getAbfragevariantenSachbearbeitung()).stream()
                    )
                    .forEach(abfragevariante -> {
                        if (abfragevariante.isRelevant()) {
                            relevanteAbfrage.set(true);
                            nameRelevantAbfrage.append(infrastrukturabfrage.getAbfrage().getNameAbfrage());
                            nameRelevantAbfragevariante.append(abfragevariante.getAbfragevariantenName());
                        }
                    });
            });

        if (relevanteAbfrage.get()) {
            var errorMessage =
                "Die Abfragevariante " +
                nameRelevantAbfragevariante +
                " in Abfrage " +
                nameRelevantAbfrage +
                " ist bereits als relevant markiert.";
            log.error(errorMessage);
            throw new UniqueViolationException(errorMessage);
        }
    }
}
