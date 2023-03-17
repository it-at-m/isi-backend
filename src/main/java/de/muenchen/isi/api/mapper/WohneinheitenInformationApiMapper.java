/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.calculation.WohneinheitenInformationDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.calculation.WohneinheitenInformationModel;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface WohneinheitenInformationApiMapper {
    WohneinheitenInformationDto model2Dto(final WohneinheitenInformationModel model);

    WohneinheitenInformationModel dto2Model(final WohneinheitenInformationDto dto);
}
