package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.StringLengthExceededException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.domain.mapper.AbfrageAltDomainMapper;
import de.muenchen.isi.domain.model.AbfrageAltModel;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.InfrastrukturabfrageAngelegtModel;
import de.muenchen.isi.domain.model.abfrageBedarfsmeldungInBearbeitungFachreferate.InfrastrukturabfrageInBearbeitungFachreferateModel;
import de.muenchen.isi.domain.model.abfrageSachbearbeitungInBearbeitungSachbearbeitung.InfrastrukturabfrageInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.service.filehandling.DokumentService;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.repository.InfrastrukturabfrageRepository;
import de.muenchen.isi.security.AuthenticationUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbfrageAltService {

    private final AbfrageAltDomainMapper abfrageDomainMapper;

    private final InfrastrukturabfrageRepository infrastrukturabfrageRepository;

    private final DokumentService dokumentService;

    private final AuthenticationUtils authenticationUtils;

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
            abfrage.setSub(authenticationUtils.getUserSub());
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
     * Diese Methode führt für eine Abfrage, die durch die {@link InfrastrukturabfrageModel#getId()} identifiziert ist, eine Statusänderung durch.
     *
     * @param id            {@link InfrastrukturabfrageModel#getId()} der Abfrage zum Updaten
     * @param statusAbfrage neuer {@link StatusAbfrage}
     * @return das geupdatete {@link InfrastrukturabfrageModel}
     * @throws EntityNotFoundException       falls die Abfrage identifiziert durch die {@link InfrastrukturabfrageModel#getId()} nicht gefunden wird
     * @throws UniqueViolationException      falls der Name der Abfrage {@link InfrastrukturabfrageModel#getAbfrage().getNameAbfrage} ()} bereits vorhanden ist
     * @throws OptimisticLockingException    falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     * @throws StringLengthExceededException wenn die Anmerkung zur Statusänderung die max. Länge überschreitet
     */
    public InfrastrukturabfrageModel changeStatusAbfrage(
        final UUID id,
        final StatusAbfrage statusAbfrage,
        final String anmerkung
    )
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        var originalAbfrageDb = this.getInfrastrukturabfrageById(id);
        originalAbfrageDb.getAbfrage().setStatusAbfrage(statusAbfrage);
        originalAbfrageDb = this.addAbfrageAnmerkung(originalAbfrageDb, anmerkung);
        return this.saveInfrastrukturabfrage(originalAbfrageDb);
    }

    /**
     * Fügt der Abfrage eine Anmerkung hinzu oder aktualisiert sie.
     *
     * @param infrastrukturabfrageModel Das InfrastrukturabfrageModel, zu dem die Anmerkung hinzugefügt wird.
     * @param anmerkung                 Die Anmerkung, die hinzugefügt oder angehängt wird.
     * @return Das aktualisierte InfrastrukturabfrageModel mit der hinzugefügten oder aktualisierten Anmerkung.
     */
    public InfrastrukturabfrageModel addAbfrageAnmerkung(
        final InfrastrukturabfrageModel infrastrukturabfrageModel,
        final String anmerkung
    ) {
        if (StringUtils.isNotEmpty(anmerkung)) {
            if (infrastrukturabfrageModel.getAbfrage().getAnmerkung() == null) {
                infrastrukturabfrageModel.getAbfrage().setAnmerkung(anmerkung);
            } else {
                infrastrukturabfrageModel
                    .getAbfrage()
                    .setAnmerkung(infrastrukturabfrageModel.getAbfrage().getAnmerkung().concat("\n").concat(anmerkung));
            }
        }
        return infrastrukturabfrageModel;
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
        this.throwUserRoleNotAllowedOrAbfrageStatusNotAlloweExceptionWhenDeleteAbfrage(abfrage);
        this.throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(abfrage.getAbfrage());
        this.infrastrukturabfrageRepository.deleteById(id);
    }

    /**
     * Diese Methode überprüft ob der Nutzer die richtige Rolle hat und die Abfrage im richtigen Status, um sie zu löschen.
     * Dabei wird auch geprüft, ob der Nutzer der Abfrage zugeordnet ist per sub Id
     *
     * @param abfrage zum Identifizieren des Status.
     * @throws UserRoleNotAllowedException      falls der Nutzer nicht die richtige Rolle hat.
     * @throws AbfrageStatusNotAllowedException falls die Abfrage den falschen Status hat oder der Sub des Nutzers nicht mit dem Sub der Abfrage übereinstimmt
     */
    public void throwUserRoleNotAllowedOrAbfrageStatusNotAlloweExceptionWhenDeleteAbfrage(
        InfrastrukturabfrageModel abfrage
    ) throws UserRoleNotAllowedException, AbfrageStatusNotAllowedException {
        var roles = authenticationUtils.getUserRoles();
        if (!roles.contains(AuthenticationUtils.ROLE_ADMIN)) {
            if (!roles.contains(AuthenticationUtils.ROLE_ABFRAGEERSTELLUNG)) {
                throw new UserRoleNotAllowedException("Keine Berechtigung zum Löschen der Abfrage");
            } else if (!abfrage.getSub().equals(authenticationUtils.getUserSub())) {
                log.error(
                    "User {} hat versucht, die Abfrage {} von User {} zu löschen.",
                    authenticationUtils.getUserSub(),
                    abfrage.getId(),
                    abfrage.getSub()
                );
                throw new UserRoleNotAllowedException("Keine Berechtigung zum Löschen der Abfrage");
            } else if (abfrage.getAbfrage().getStatusAbfrage() != StatusAbfrage.ANGELEGT) {
                throw new AbfrageStatusNotAllowedException(
                    "Die Abfrage kann nur im Status 'angelegt' gelöscht werden."
                );
            }
        }
    }

    /**
     * Enthält das im Parameter gegebene {@link AbfrageAltModel} ein {@link BauvorhabenModel},
     * wird eine {@link EntityIsReferencedException} geworfen.
     *
     * @param abfrage zum Prüfen.
     * @throws EntityIsReferencedException falls das {@link AbfrageAltModel} ein {@link BauvorhabenModel} referenziert.
     */
    protected void throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(final AbfrageAltModel abfrage)
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
     * Enthält das im Parameter gegebene {@link AbfrageAltModel} einen ungültigen Status {@link StatusAbfrage},
     * wird eine {@link AbfrageStatusNotAllowedException} geworfen.
     *
     * @param abfrage       zum Prüfen.
     * @param statusAbfrage gültiger Status.
     * @throws AbfrageStatusNotAllowedException falls das {@link AbfrageAltModel} einen unzulässigen Status hat
     */
    protected void throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
        final AbfrageAltModel abfrage,
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
}
