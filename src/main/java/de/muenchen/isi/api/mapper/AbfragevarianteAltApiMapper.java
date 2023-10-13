/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.AbfragevarianteAltDto;
import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfragevarianteAngelegtDto;
import de.muenchen.isi.api.dto.abfrageBedarfsmeldungInBearbeitungFachreferate.AbfragevarianteInBearbeitungFachreferateDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.AbfragevarianteAltModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfragevarianteAngelegtModel;
import de.muenchen.isi.domain.model.abfrageBedarfsmeldungInBearbeitungFachreferate.AbfragevarianteInBearbeitungFachreferateModel;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class, uses = { BauabschnittApiMapper.class })
public interface AbfragevarianteAltApiMapper {
    AbfragevarianteAltDto model2Dto(final AbfragevarianteAltModel model);

    AbfragevarianteAltModel dto2Model(final AbfragevarianteAltDto dto);

    AbfragevarianteAngelegtModel dto2Model(final AbfragevarianteAngelegtDto dto);

    AbfragevarianteInBearbeitungFachreferateModel dto2Model(final AbfragevarianteInBearbeitungFachreferateDto dto);
}
