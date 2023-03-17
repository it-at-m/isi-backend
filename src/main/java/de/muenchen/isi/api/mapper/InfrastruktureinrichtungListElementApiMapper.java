/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.list.InfrastruktureinrichtungListElementsDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.list.InfrastruktureinrichtungListElementsModel;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface InfrastruktureinrichtungListElementApiMapper {
    InfrastruktureinrichtungListElementsDto model2Dto(final InfrastruktureinrichtungListElementsModel model);
}
