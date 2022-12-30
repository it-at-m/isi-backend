/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.BaugebietDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.BaugebietModel;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface BaugebietApiMapper {

    BaugebietDto model2Dto(final BaugebietModel model);

    BaugebietModel dto2Model(final BaugebietDto dto);

}
