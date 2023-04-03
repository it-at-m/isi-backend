/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.BauabschnittDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.BauabschnittModel;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class, uses = { BaugebietApiMapper.class })
public interface BauabschnittApiMapper {
    BauabschnittDto model2Dto(final BauabschnittModel model);

    BauabschnittModel dto2Model(final BauabschnittDto dto);
}
