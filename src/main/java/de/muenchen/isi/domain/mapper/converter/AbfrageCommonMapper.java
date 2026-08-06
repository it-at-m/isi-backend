/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper.converter;

import de.muenchen.isi.domain.model.AbfrageModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Mapping für Felder von AbfrageModel/AbfrageDto.
 */
@Mapper(componentModel = "spring")
public interface AbfrageCommonMapper {
    void mapCommon(AbfrageModel source, @MappingTarget AbfrageModel target);
}
