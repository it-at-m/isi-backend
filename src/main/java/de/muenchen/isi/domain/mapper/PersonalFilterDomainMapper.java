package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.search.filter.PersonalFilterRequestModel;
import de.muenchen.isi.domain.model.search.filter.PersonalFilterResponseModel;
import de.muenchen.isi.infrastructure.entity.search.filter.PersonalFilter;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapstructConfiguration.class)
public interface PersonalFilterDomainMapper {
    PersonalFilterResponseModel entity2Model(final PersonalFilter personalFilter);

    List<PersonalFilterResponseModel> entities2Models(final List<PersonalFilter> personalFilter);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDateTime", ignore = true)
    @Mapping(target = "lastModifiedDateTime", ignore = true)
    PersonalFilter model2Entity(final PersonalFilterRequestModel personalFilterRequestModel);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDateTime", ignore = true)
    @Mapping(target = "lastModifiedDateTime", ignore = true)
    void updateEntityFromModel(PersonalFilterRequestModel model, @MappingTarget PersonalFilter entity);
}
