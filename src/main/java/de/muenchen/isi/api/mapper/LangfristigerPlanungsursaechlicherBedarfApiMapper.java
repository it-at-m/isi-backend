package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.calculation.InfrastrukturbedarfProJahrDto;
import de.muenchen.isi.api.dto.calculation.LangfristigerPlanungsursaechlicherBedarfDto;
import de.muenchen.isi.api.dto.calculation.PersonenProJahrDto;
import de.muenchen.isi.api.dto.calculation.WohneinheitenProFoerderartProJahrDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.calculation.InfrastrukturbedarfProJahrModel;
import de.muenchen.isi.domain.model.calculation.LangfristigerPlanungsursaechlicherBedarfModel;
import de.muenchen.isi.domain.model.calculation.PersonenProJahrModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenProFoerderartProJahrModel;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapstructConfiguration.class)
public interface LangfristigerPlanungsursaechlicherBedarfApiMapper {
    WohneinheitenProFoerderartProJahrDto model2Dto(final WohneinheitenProFoerderartProJahrModel model);

    InfrastrukturbedarfProJahrDto model2Dto(final InfrastrukturbedarfProJahrModel model);

    PersonenProJahrDto model2Dto(final PersonenProJahrModel model);

    LangfristigerPlanungsursaechlicherBedarfDto model2Dto(final LangfristigerPlanungsursaechlicherBedarfModel model);

    @AfterMapping
    default void model2DtoAfterMapping(
        final LangfristigerPlanungsursaechlicherBedarfModel model,
        @MappingTarget final LangfristigerPlanungsursaechlicherBedarfDto dto
    ) {
        // Hier Wohneinheiten
        dto
            .getBedarfKinderkrippe()
            .addAll(
                List.of(
                    model2Dto(model.getBedarfKinderkrippeMittelwert10()),
                    model2Dto(model.getBedarfKinderkrippeMittelwert15()),
                    model2Dto(model.getBedarfKinderkrippeMittelwert20())
                )
            );
        dto
            .getBedarfKindergarten()
            .addAll(
                List.of(
                    model2Dto(model.getBedarfKindergartenMittelwert10()),
                    model2Dto(model.getBedarfKindergartenMittelwert15()),
                    model2Dto(model.getBedarfKindergartenMittelwert20())
                )
            );
        dto
            .getAlleEinwohner()
            .addAll(
                List.of(
                    model2Dto(model.getAlleEinwohnerMittelwert10()),
                    model2Dto(model.getAlleEinwohnerMittelwert15()),
                    model2Dto(model.getAlleEinwohnerMittelwert20())
                )
            );
    }
}
