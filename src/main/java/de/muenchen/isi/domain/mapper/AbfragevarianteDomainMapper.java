/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.AbfragevarianteModel;
import de.muenchen.isi.domain.model.AbfragevarianteSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfrageerstellungAbfragevarianteAngelegtModel;
import de.muenchen.isi.infrastructure.entity.Abfragevariante;
import de.muenchen.isi.infrastructure.entity.AbfragevarianteSachbearbeitung;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(config = MapstructConfiguration.class, uses = { BauabschnittDomainMapper.class })
public interface AbfragevarianteDomainMapper {
    AbfragevarianteModel entity2Model(final Abfragevariante entity);

    Abfragevariante model2entity(final AbfragevarianteModel model);

    @Mappings(
        {
            @Mapping(target = "lastModifiedDateTime", ignore = true),
            @Mapping(target = "createdDateTime", ignore = true),
            @Mapping(target = "relevant", ignore = true),
            @Mapping(target = "abfragevarianteSachbearbeitung", ignore = true),
        }
    )
    AbfragevarianteModel request2Model(
        final AbfrageerstellungAbfragevarianteAngelegtModel request,
        @MappingTarget AbfragevarianteModel model
    );
}
