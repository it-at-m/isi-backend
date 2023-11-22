package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.calculation.LangfristigerPlanungsursaechlicherBedarfDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.calculation.LangfristigerPlanungsursaechlicherBedarfModel;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface PlanungsursaechlicherBedarfApiMapper {
    LangfristigerPlanungsursaechlicherBedarfDto model2Dto(final LangfristigerPlanungsursaechlicherBedarfModel model);
}
