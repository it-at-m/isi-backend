package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.common.TransitionDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.common.TransitionModel;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface TransitionApiMapper {
    TransitionDto model2Dto(TransitionModel model);
}
