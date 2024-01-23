package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.calculation.InfrastrukturbedarfProJahrDto;
import de.muenchen.isi.api.dto.calculation.LangfristigerPlanungsursaechlicherBedarfDto;
import de.muenchen.isi.api.dto.calculation.PersonenProJahrDto;
import de.muenchen.isi.api.dto.calculation.WohneinheitenProFoerderartProJahrDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.calculation.InfrastrukturbedarfProJahrModel;
import de.muenchen.isi.domain.model.calculation.LangfristigerBedarfModel;
import de.muenchen.isi.domain.model.calculation.PersonenProJahrModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenProFoerderartProJahrModel;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface LangfristigerPlanungsursaechlicherBedarfApiMapper {
    WohneinheitenProFoerderartProJahrDto model2Dto(final WohneinheitenProFoerderartProJahrModel model);

    InfrastrukturbedarfProJahrDto model2Dto(final InfrastrukturbedarfProJahrModel model);

    PersonenProJahrDto model2Dto(final PersonenProJahrModel model);

    LangfristigerPlanungsursaechlicherBedarfDto model2Dto(final LangfristigerBedarfModel model);
}
