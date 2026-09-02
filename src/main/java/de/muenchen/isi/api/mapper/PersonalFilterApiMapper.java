package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.search.filter.PersonalFilterRequestDto;
import de.muenchen.isi.api.dto.search.filter.PersonalFilterResponseDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.search.filter.PersonalFilterRequestModel;
import de.muenchen.isi.domain.model.search.filter.PersonalFilterResponseModel;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface PersonalFilterApiMapper {
    PersonalFilterResponseDto model2Dto(final PersonalFilterResponseModel personalFilterResponseModel);

    List<PersonalFilterResponseDto> models2Dtos(final List<PersonalFilterResponseModel> personalFilterResponseModel);

    PersonalFilterRequestModel dto2Model(final PersonalFilterRequestDto personalFilterRequestDto);
}
