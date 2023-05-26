/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.AbfragevarianteDto;
import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfrageerstellungAbfragevarianteAngelegtDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.AbfragevarianteModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfrageerstellungAbfragevarianteAngelegtModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = MapstructConfiguration.class, uses = { BauabschnittApiMapper.class })
public interface AbfragevarianteApiMapper {
    AbfragevarianteDto model2Dto(final AbfragevarianteModel model);

    AbfragevarianteModel dto2Model(final AbfragevarianteDto dto);

    AbfrageerstellungAbfragevarianteAngelegtModel dto2Model(final AbfrageerstellungAbfragevarianteAngelegtDto dto);

    @Mappings(
        {
            @Mapping(target = "relevant", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdDateTime", ignore = true),
            @Mapping(target = "lastModifiedDateTime", ignore = true),
        }
    )
    AbfragevarianteDto request2Response(final AbfrageerstellungAbfragevarianteAngelegtDto dto);
}
