package de.muenchen.isi.domain.service.search;

import de.muenchen.isi.domain.mapper.PersonalFilterDomainMapper;
import de.muenchen.isi.domain.model.search.filter.PersonalFilterRequestModel;
import de.muenchen.isi.domain.model.search.filter.PersonalFilterResponseModel;
import de.muenchen.isi.infrastructure.repository.search.PersonalFilterRepository;
import de.muenchen.isi.security.AuthenticationUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public PersonalFilterResponseModel getByFilterID(PersonalFilterRequestModel personalFilterRequestModel) {
        String userSub = authenticationUtils.getUserSub();
        var entity = personalFilterRepository.findByIdAndPersonalID(personalFilterRequestModel.getId(), userSub);
        return personalFilterDomainMapper.entity2Model(entity);
    }

    public PersonalFilterResponseModel update(PersonalFilterRequestModel personalFilterRequestModel) {
        personalFilterRequestModel.setPersonalID(authenticationUtils.getUserSub());
        var verifyEntity = personalFilterRepository.findByIdAndPersonalID(
            personalFilterRequestModel.getId(),
            personalFilterRequestModel.getPersonalID()
        );
        if (verifyEntity == null) {
            return null;
        }
        var entity = personalFilterDomainMapper.model2Entity(personalFilterRequestModel);
        entity.setVersion(verifyEntity.getVersion());
        entity.setCreatedDateTime(verifyEntity.getCreatedDateTime());
        entity.setLastModifiedDateTime(verifyEntity.getLastModifiedDateTime());
        entity = personalFilterRepository.saveAndFlush(entity);
        return personalFilterDomainMapper.entity2Model(entity);
    }

    public PersonalFilterResponseModel save(PersonalFilterRequestModel personalFilterRequestModel) {
        personalFilterRequestModel.setPersonalID(authenticationUtils.getUserSub());
        var entity = personalFilterDomainMapper.model2Entity(personalFilterRequestModel);
        entity = this.personalFilterRepository.saveAndFlush(entity);
        return personalFilterDomainMapper.entity2Model(entity);
    }

    public void delete(PersonalFilterRequestModel personalFilterRequestModel) {
        personalFilterRequestModel.setPersonalID(authenticationUtils.getUserSub());
        var verifyEntity = personalFilterRepository.findByIdAndPersonalID(
            personalFilterRequestModel.getId(),
            personalFilterRequestModel.getPersonalID()
        );
        if (verifyEntity == null) {
            return;
        }
        this.personalFilterRepository.deleteById(personalFilterRequestModel.getId());
    }
}
