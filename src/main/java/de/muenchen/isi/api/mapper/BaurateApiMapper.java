/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.BaurateDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.BaurateModel;
import org.mapstruct.Mapper;

@Mapper(
        config = MapstructConfiguration.class,
        uses = {AbfragevarianteApiMapper.class}
)
public interface BaurateApiMapper {

    BaurateDto model2Dto(final BaurateModel model);

    BaurateModel dto2Model(final BaurateDto dto);

}
