/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.AbfragevarianteDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.AbfragevarianteModel;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class, uses = { BauabschnittApiMapper.class })
public interface AbfragevarianteApiMapper {
    AbfragevarianteDto model2Dto(final AbfragevarianteModel model);

    AbfragevarianteModel dto2Model(final AbfragevarianteDto dto);
}
