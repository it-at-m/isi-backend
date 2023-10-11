package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.domain.mapper.AbfrageDomainMapper;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.domain.service.filehandling.DokumentService;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.repository.AbfrageRepository;
import de.muenchen.isi.security.AuthenticationUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbfrageService {

    private final AbfrageRepository abfrageRepository;

    private final AbfrageDomainMapper abfrageDomainMapper;

    private final BauvorhabenService bauvorhabenService;

    private final DokumentService dokumentService;

    private final AuthenticationUtils authenticationUtils;

    /**
     * Die Methode gibt ein {@link AbfrageModel} identifiziert durch die ID zurück.
     *
     * @param id zum Identifizieren des {@link AbfrageModel}.
     * @return {@link AbfrageModel}.
     * @throws EntityNotFoundException falls die Abfrage identifiziert durch die {@link AbfrageModel#getId()} nicht gefunden wird.
     */
    public AbfrageModel getAbfrageById(final UUID id) throws EntityNotFoundException {
        final var optAbfrage = this.abfrageRepository.findById(id);
        final var abfrage = optAbfrage.orElseThrow(() -> {
            final var message = "Abfrage nicht gefunden.";
            log.error(message);
            return new EntityNotFoundException(message);
        });
        return this.abfrageDomainMapper.entity2Model(abfrage);
    }

    /**
     * Diese Methode speichert ein {@link AbfrageModel}.
     *
     * @param abfrage zum Speichern
     * @return das gespeicherte {@link AbfrageModel}
     * @throws UniqueViolationException   falls der Name der Abfrage oder der Abfragevariante bereits vorhanden ist
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     * @throws EntityNotFoundException falls das referenzierte Bauvorhaben nicht existiert.
     */
    public AbfrageModel saveAbfrage(final AbfrageModel abfrage)
        throws EntityNotFoundException, OptimisticLockingException, UniqueViolationException {
        if (abfrage.getId() == null) {
            abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
            abfrage.setSub(authenticationUtils.getUserSub());
        }
        var entity = this.abfrageDomainMapper.model2Entity(abfrage);
        final var saved = this.abfrageRepository.findByNameIgnoreCase(abfrage.getName());
        if ((saved.isPresent() && saved.get().getId().equals(entity.getId())) || saved.isEmpty()) {
            try {
                entity = this.abfrageRepository.saveAndFlush(entity);
            } catch (final ObjectOptimisticLockingFailureException exception) {
                final var message = "Die Daten wurden in der Zwischenzeit geändert. Bitte laden Sie die Seite neu!";
                throw new OptimisticLockingException(message, exception);
            } catch (final DataIntegrityViolationException exception) {
                final var message =
                    "Der angegebene Name der Abfragevariante ist schon vorhanden, bitte wählen Sie daher einen anderen Namen und speichern Sie die Abfrage erneut.";
                throw new UniqueViolationException(message);
            }
            return this.abfrageDomainMapper.entity2Model(entity);
        } else {
            throw new UniqueViolationException(
                "Der angegebene Name der Abfrage ist schon vorhanden, bitte wählen Sie daher einen anderen Namen und speichern Sie die Abfrage erneut."
            );
        }
    }

    /**
     * Diese Methode löscht ein {@link AbfrageModel}.
     *
     * @param id zum Identifizieren des {@link AbfrageModel}.
     * @throws EntityNotFoundException          falls die Abfrage identifiziert durch die {@link InfrastrukturabfrageModel#getId()} nicht gefunden wird.
     * @throws EntityIsReferencedException      falls ein {@link BauvorhabenModel} in der Abfrage referenziert wird.
     * @throws UserRoleNotAllowedException      falls der Nutzer nicht die richtige Rolle hat.
     * @throws AbfrageStatusNotAllowedException falls die Abfrage den falschen Status hat..
     */
    public void deleteInfrasturkturabfrageById(final UUID id)
        throws EntityNotFoundException, EntityIsReferencedException, UserRoleNotAllowedException, AbfrageStatusNotAllowedException {
        final var abfrage = this.getAbfrageById(id);
        this.throwUserRoleNotAllowedOrAbfrageStatusNotAlloweExceptionWhenDeleteAbfrage(abfrage);
        this.throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(abfrage);
        this.abfrageRepository.deleteById(id);
    }

    /**
     * Diese Methode überprüft ob der Nutzer die richtige Rolle hat und die Abfrage im richtigen Status, um sie zu löschen.
     * Dabei wird auch geprüft, ob der Nutzer der Abfrage zugeordnet ist per sub Id
     *
     * @param abfrage zum Identifizieren des Status.
     * @throws UserRoleNotAllowedException      falls der Nutzer nicht die richtige Rolle hat.
     * @throws AbfrageStatusNotAllowedException falls die Abfrage den falschen Status hat oder der Sub des Nutzers nicht mit dem Sub der Abfrage übereinstimmt
     */
    public void throwUserRoleNotAllowedOrAbfrageStatusNotAlloweExceptionWhenDeleteAbfrage(final AbfrageModel abfrage)
        throws UserRoleNotAllowedException, AbfrageStatusNotAllowedException {
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
            } else if (abfrage.getStatusAbfrage() != StatusAbfrage.ANGELEGT) {
                throw new AbfrageStatusNotAllowedException(
                    "Die Abfrage kann nur im Status 'angelegt' gelöscht werden."
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
     * @throws EntityNotFoundException falls das referenzierte Bauvorhaben nicht existiert.
     */
    protected void throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(final AbfrageModel abfrage)
        throws EntityIsReferencedException, EntityNotFoundException {
        final var bauvorhaben = bauvorhabenService.getBauvorhabenById(abfrage.getBauvorhaben());
        if (ObjectUtils.isNotEmpty(bauvorhaben)) {
            final var message =
                "Die Abfrage " +
                abfrage.getName() +
                " referenziert das Bauvorhaben " +
                bauvorhaben.getNameVorhaben() +
                ".";
            log.error(message);
            throw new EntityIsReferencedException(message);
        }
    }
}
