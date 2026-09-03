package de.muenchen.isi.domain.service.search;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
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

    public List<PersonalFilterResponseModel> getPersonalFilters() {
        String userSub = authenticationUtils.getUserSub();
        var entities = personalFilterRepository.findByPersonalID(userSub);
        return personalFilterDomainMapper.entities2Models(entities);
    }

    public PersonalFilterResponseModel getByFilterID(UUID filter_id)
        throws EntityNotFoundException, IllegalAccessException {
        String userSub = authenticationUtils.getUserSub();
        var entity = personalFilterRepository.findByIdAndPersonalID(filter_id, userSub);
        if (entity == null) {
            if (personalFilterRepository.findById(filter_id).isPresent()) {
                throw new IllegalAccessException("Sie sind nicht der Ersteller dieses persönlichen Filters.");
            }
            throw new EntityNotFoundException("PersonalFilter nicht gefunden.");
        }
        return personalFilterDomainMapper.entity2Model(entity);
    }

    public PersonalFilterResponseModel update(PersonalFilterRequestModel personalFilterRequestModel)
        throws EntityNotFoundException, OptimisticLockingException, IllegalAccessException {
        personalFilterRequestModel.setPersonalID(authenticationUtils.getUserSub());
        var entity = personalFilterRepository.findByIdAndPersonalID(
            personalFilterRequestModel.getId(),
            personalFilterRequestModel.getPersonalID()
        );
        if (entity == null) {
            if (personalFilterRepository.findById(personalFilterRequestModel.getId()).isPresent()) {
                throw new IllegalAccessException("Sie sind nicht der Ersteller dieses persönlichen Filters.");
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

    public PersonalFilterResponseModel save(PersonalFilterRequestModel personalFilterRequestModel)
        throws OptimisticLockingException {
        personalFilterRequestModel.setPersonalID(authenticationUtils.getUserSub());
        var entity = personalFilterDomainMapper.model2Entity(personalFilterRequestModel);
        try {
            entity = this.personalFilterRepository.saveAndFlush(entity);
        } catch (final ObjectOptimisticLockingFailureException exception) {
            final var message = "Die Daten wurden in der Zwischenzeit geändert. Bitte laden Sie die Seite neu!";
            throw new OptimisticLockingException(message, exception);
        }
        return personalFilterDomainMapper.entity2Model(entity);
    }

    public void delete(UUID filterId) throws EntityNotFoundException, IllegalAccessException {
        var verifyEntity = personalFilterRepository.findByIdAndPersonalID(filterId, authenticationUtils.getUserSub());
        if (verifyEntity == null) {
            if (personalFilterRepository.findById(filterId).isPresent()) {
                throw new IllegalAccessException("Sie sind nicht der Ersteller dieses persönlichen Filters.");
            }
            throw new EntityNotFoundException("PersonalFilter nicht gefunden.");
        }
        this.personalFilterRepository.deleteById(filterId);
    }
}
