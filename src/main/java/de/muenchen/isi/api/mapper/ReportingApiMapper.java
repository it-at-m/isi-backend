package de.muenchen.isi.api.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.calculation.LangfristigerPlanungsursaechlicherBedarfModel;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface ReportingApiMapper {
    org.openapitools.client.model.LangfristigerPlanungsursaechlicherBedarfDto model2Dto(
        final LangfristigerPlanungsursaechlicherBedarfModel model
    );
}
