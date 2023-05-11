/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.InfrastrukturabfrageResponseDto;
import de.muenchen.isi.api.dto.abfrageAngelegt.AbfrageRequestDto;
import de.muenchen.isi.api.dto.abfrageAngelegt.InfrastrukturabfrageRequestDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageResponseModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfrageRequestModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapstructConfiguration.class, uses = {AbfragevarianteApiMapper.class, DokumentApiMapper.class})
public interface AbfrageApiMapper {
    @Mapping(target = "abfrage.bauvorhaben", ignore = true)
    InfrastrukturabfrageResponseDto model2Dto(final InfrastrukturabfrageResponseModel model);

    @AfterMapping
    default void setBauvorhabenIdOnInfrastrukturabfrageDto(
            final InfrastrukturabfrageResponseModel model,
            @MappingTarget final InfrastrukturabfrageResponseDto dto
    ) {
        final BauvorhabenModel bauvorhabenModel = model.getAbfrage().getBauvorhaben();
        dto.getAbfrage().setBauvorhaben(bauvorhabenModel != null ? bauvorhabenModel.getId() : null);
    }

    @Mapping(target = "abfrage.bauvorhaben", ignore = true)
    @Mapping(target = "displayName", ignore = true)
    InfrastrukturabfrageResponseModel dto2Model(final InfrastrukturabfrageResponseDto dto);

    de.muenchen.isi.domain.model.abfrageAngelegt.InfrastrukturabfrageRequestModel dto2Model(final InfrastrukturabfrageRequestDto dto);

    AbfrageRequestModel dto2Model(final AbfrageRequestDto dto);

}
