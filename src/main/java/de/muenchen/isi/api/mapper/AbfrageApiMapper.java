/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.InfrastrukturabfrageDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapstructConfiguration.class, uses = { AbfragevarianteApiMapper.class, DokumentApiMapper.class })
public interface AbfrageApiMapper {
    @Mapping(target = "abfrage.bauvorhaben", ignore = true)
    InfrastrukturabfrageDto model2Dto(final InfrastrukturabfrageModel model);

    @AfterMapping
    default void setBauvorhabenIdOnInfrastrukturabfrageDto(
        final InfrastrukturabfrageModel model,
        @MappingTarget final InfrastrukturabfrageDto dto
    ) {
        final BauvorhabenModel bauvorhabenModel = model.getAbfrage().getBauvorhaben();
        dto.getAbfrage().setBauvorhaben(bauvorhabenModel != null ? bauvorhabenModel.getId() : null);
    }

    @Mapping(target = "abfrage.bauvorhaben", ignore = true)
    @Mapping(target = "displayName", ignore = true)
    InfrastrukturabfrageModel dto2Model(final InfrastrukturabfrageDto dto);
}
