/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.AbfragevarianteBauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfragevarianteBauleitplanverfahrenAngelegtModel;
import de.muenchen.isi.infrastructure.entity.AbfragevarianteBauleitplanverfahren;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(config = MapstructConfiguration.class, uses = { BauabschnittDomainMapper.class })
public interface AbfragevarianteBauleitplanverfahrenDomainMapper {
    AbfragevarianteBauleitplanverfahrenModel entity2Model(final AbfragevarianteBauleitplanverfahren entity);

    AbfragevarianteBauleitplanverfahren model2entity(final AbfragevarianteBauleitplanverfahrenModel model);

    @Mappings(
        {
            @Mapping(target = "lastModifiedDateTime", ignore = true),
            @Mapping(target = "createdDateTime", ignore = true),
            @Mapping(target = "abfragevarianteSachbearbeitung", ignore = true),
            @Mapping(target = "abfragevarianteFachreferat", ignore = true),
        }
    )
    AbfragevarianteBauleitplanverfahrenModel request2Model(
        final AbfragevarianteBauleitplanverfahrenAngelegtModel request,
        final @MappingTarget AbfragevarianteBauleitplanverfahrenModel model
    );
}
