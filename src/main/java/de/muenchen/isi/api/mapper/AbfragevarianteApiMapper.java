/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.AbfragevarianteDto;
import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfragevarianteAngelegtDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.AbfragevarianteModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfragevarianteAngelegtModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfiguration.class, uses = { BauabschnittApiMapper.class })
public interface AbfragevarianteApiMapper {
    @Mapping(target = "abfragevariantenNrDisplay", ignore = true)
    AbfragevarianteDto model2Dto(final AbfragevarianteModel model);

    AbfragevarianteModel dto2Model(final AbfragevarianteDto dto);

    AbfragevarianteAngelegtModel dto2Model(final AbfragevarianteAngelegtDto dto);
}
