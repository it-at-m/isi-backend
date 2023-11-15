package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.calculation.PlanungsursaechlicherBedarfDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.calculation.PlanungsursaechlicherBedarfModel;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface PlanungsursaechlicherBedarfApiMapper {
    PlanungsursaechlicherBedarfDto model2Dto(final PlanungsursaechlicherBedarfModel model);
}
