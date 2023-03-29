/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.list.AbfrageListElementsDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.list.AbfrageListElementsModel;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface AbfrageListElementApiMapper {
    AbfrageListElementsDto model2Dto(final AbfrageListElementsModel model);
}
