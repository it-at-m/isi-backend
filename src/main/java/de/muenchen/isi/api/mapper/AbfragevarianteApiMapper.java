/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.AbfragevarianteResponseDto;
import de.muenchen.isi.api.dto.abfrageAngelegt.AbfragevarianteRequestDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.AbfragevarianteResponseModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfragevarianteRequestModel;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class, uses = {BauabschnittApiMapper.class})
public interface AbfragevarianteApiMapper {
    AbfragevarianteResponseDto model2Dto(final AbfragevarianteResponseModel model);

    AbfragevarianteResponseModel dto2Model(final AbfragevarianteResponseDto dto);

    AbfragevarianteRequestModel dto2Model(final AbfragevarianteRequestDto dto);

}
