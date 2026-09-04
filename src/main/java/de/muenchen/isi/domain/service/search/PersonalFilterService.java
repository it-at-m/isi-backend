package de.muenchen.isi.domain.service.search;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.domain.mapper.PersonalFilterDomainMapper;
import de.muenchen.isi.domain.model.search.filter.PersonalFilterRequestModel;
import de.muenchen.isi.domain.model.search.filter.PersonalFilterResponseModel;
import de.muenchen.isi.infrastructure.repository.search.PersonalFilterRepository;
import de.muenchen.isi.security.AuthenticationUtils;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonalFilterService {

    private final PersonalFilterDomainMapper personalFilterDomainMapper;

    private final AuthenticationUtils authenticationUtils;

    private final PersonalFilterRepository personalFilterRepository;

    /**
     * Gibt alle eigenen persönlichen Filter zurück.
     *
     * @return alle von diesem Nutzer existierenden persönlichen Filter als Liste (exklusive der userID)
     * @throws UserRoleNotAllowedException falls der Nutzer den Fallback-Sub aus AuthenticationUtils zugewiesen hat
     */
    public List<PersonalFilterResponseModel> getPersonalFilters() throws UserRoleNotAllowedException {
        var entities = personalFilterRepository.findByPersonalID(getSubFromAuthenticatedUser());
        return personalFilterDomainMapper.entities2Models(entities);
    }

    /**
     * Gibt einen spezifisch angefragten persönlichen Filter zurück.
     *
     * @param filterId des zu lesenden persönlichen Filters
     * @return den persönlichen Filter mit dieser ID (exklusive der userID)
     * @throws EntityNotFoundException falls es keinen persönlichen Filter mit dieser ID gibt
     * @throws UserRoleNotAllowedException falls der Nutzer den Fallback-Sub aus AuthenticationUtils zugewiesen hat
     */
    public PersonalFilterResponseModel getByFilterID(UUID filterId)
        throws EntityNotFoundException, UserRoleNotAllowedException {
        String userSub = getSubFromAuthenticatedUser();
        var entity = personalFilterRepository.findByIdAndPersonalID(filterId, userSub);
        if (entity == null) {
            if (personalFilterRepository.findById(filterId).isPresent()) {
                throw new UserRoleNotAllowedException("Sie sind nicht der Ersteller dieses persönlichen Filters.");
            }
            throw new EntityNotFoundException("PersonalFilter nicht gefunden.");
        }
        return personalFilterDomainMapper.entity2Model(entity);
    }

    /**
     * Aktualisiert einen spezifizierten persönlichen Filter.
     *
     * @param personalFilterRequestModel entspricht neuen persönlichen Filtereinstellungen, die bestehende Filtereinstellungen überschreiben sollen
     * @return den aktualisierten persönlichen Filter (exklusive der userID)
     * @throws EntityNotFoundException falls es keinen persönlichen Filter mit dieser ID gibt
     * @throws OptimisticLockingException falls es bereits eine neuere Version der Entität in der Datenbank gibt
     * @throws UserRoleNotAllowedException falls der Nutzer den Fallback-Sub aus AuthenticationUtils zugewiesen hat
     */
    public PersonalFilterResponseModel update(PersonalFilterRequestModel personalFilterRequestModel)
        throws EntityNotFoundException, OptimisticLockingException, UserRoleNotAllowedException {
        personalFilterRequestModel.setPersonalID(getSubFromAuthenticatedUser());
        var entity = personalFilterRepository.findByIdAndPersonalID(
            personalFilterRequestModel.getId(),
            personalFilterRequestModel.getPersonalID()
        );
        if (entity == null) {
            UUID id = personalFilterRequestModel.getId();
            if (id != null && personalFilterRepository.findById(id).isPresent()) {
                throw new UserRoleNotAllowedException("Sie sind nicht der Ersteller dieses persönlichen Filters.");
            }
            throw new EntityNotFoundException("PersonalFilter nicht gefunden.");
        }
        personalFilterDomainMapper.updateEntityFromModel(personalFilterRequestModel, entity);
        try {
            entity = personalFilterRepository.saveAndFlush(entity);
        } catch (final ObjectOptimisticLockingFailureException exception) {
            final var message = "Die Daten wurden in der Zwischenzeit geändert. Bitte laden Sie die Seite neu!";
            throw new OptimisticLockingException(message, exception);
        }
        return personalFilterDomainMapper.entity2Model(entity);
    }

    /**
     * Speichert einen persönlichen Filter und gibt diesen inkl. FilterId zurück.
     *
     * @param personalFilterRequestModel entspricht dem persönlichen Filter, der gespeichert werden soll
     * @return den gespeicherten persönlichen Filter (exklusive der userID)
     * @throws OptimisticLockingException falls es bereits eine neuere Version der Entität in der Datenbank gibt
     * @throws UserRoleNotAllowedException falls der Nutzer den Fallback-Sub aus AuthenticationUtils zugewiesen hat
     */
    public PersonalFilterResponseModel save(PersonalFilterRequestModel personalFilterRequestModel)
        throws OptimisticLockingException, UserRoleNotAllowedException {
        if (personalFilterRequestModel.getId() != null) {
            personalFilterRequestModel.setId(null);
        }
        personalFilterRequestModel.setPersonalID(getSubFromAuthenticatedUser());
        var entity = personalFilterDomainMapper.model2Entity(personalFilterRequestModel);
        try {
            entity = this.personalFilterRepository.saveAndFlush(entity);
        } catch (final ObjectOptimisticLockingFailureException exception) {
            final var message = "Die Daten wurden in der Zwischenzeit geändert. Bitte laden Sie die Seite neu!";
            throw new OptimisticLockingException(message, exception);
        }
        return personalFilterDomainMapper.entity2Model(entity);
    }

    /**
     * Löscht einen spezifisch angegebenen Filter.
     *
     * @param filterId des zu löschenden persönlichen Filters
     * @throws EntityNotFoundException falls es keinen persönlichen Filter mit dieser ID gibt
     * @throws UserRoleNotAllowedException falls der Nutzer den Fallback-Sub aus AuthenticationUtils zugewiesen hat
     */
    public void delete(UUID filterId) throws EntityNotFoundException, UserRoleNotAllowedException {
        var verifyEntity = personalFilterRepository.findByIdAndPersonalID(filterId, getSubFromAuthenticatedUser());
        if (verifyEntity == null) {
            if (personalFilterRepository.findById(filterId).isPresent()) {
                throw new UserRoleNotAllowedException("Sie sind nicht der Ersteller dieses persönlichen Filters.");
            }
            throw new EntityNotFoundException("PersonalFilter nicht gefunden.");
        }
        this.personalFilterRepository.deleteById(filterId);
    }

    /**
     * Gibt den userSub zurück sofern es kein Fallback-Wert ist.
     *
     * @return den userSub aus authenticationUtils
     * @throws UserRoleNotAllowedException falls der Nutzer den Fallback-Sub aus AuthenticationUtils zugewiesen hat
     */
    private String getSubFromAuthenticatedUser() throws UserRoleNotAllowedException {
        final String userSub = authenticationUtils.getUserSub();
        if (authenticationUtils.isSubFromUnauthenticatedUser(userSub)) {
            throw new UserRoleNotAllowedException(
                "Sie müssen authentifiziert sein, um mit persönlichen Filtern interagieren zu können"
            );
        }
        return userSub;
    }
}
