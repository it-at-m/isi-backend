/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.AbfragevarianteModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfragevarianteAngelegtModel;
import de.muenchen.isi.domain.model.abfrageBedarfsmeldungInBearbeitungFachreferate.AbfragevarianteInBearbeitungFachreferateModel;
import de.muenchen.isi.domain.model.abfrageSachbearbeitungInBearbeitungSachbearbeitung.AbfragevarianteInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageSachbearbeitungInBearbeitungSachbearbeitung.AbfragevarianteSachbearbeitungInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.infrastructure.entity.Abfragevariante;
import org.mapstruct.BeanMapping;
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
            @Mapping(target = "abfragevarianteSachbearbeitung", ignore = true),
        }
    )
    AbfragevarianteModel request2Model(
        final AbfragevarianteAngelegtModel request,
        final @MappingTarget AbfragevarianteModel model
    );

    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        {
            @Mapping(target = "version", ignore = false),
            @Mapping(target = "abfragevarianteSachbearbeitung", ignore = false),
        }
    )
    AbfragevarianteModel request2Model(
        final AbfragevarianteSachbearbeitungInBearbeitungSachbearbeitungModel request,
        final @MappingTarget AbfragevarianteModel model
    );

    @Mappings(
        {
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "lastModifiedDateTime", ignore = true),
            @Mapping(target = "createdDateTime", ignore = true),
        }
    )
    AbfragevarianteModel request2Model(
        final AbfragevarianteInBearbeitungSachbearbeitungModel request,
        final @MappingTarget AbfragevarianteModel model
    );

    @BeanMapping(ignoreByDefault = true)
    @Mappings({ @Mapping(target = "version", ignore = false) })
    AbfragevarianteModel request2Model(
        final AbfragevarianteInBearbeitungFachreferateModel request,
        final @MappingTarget AbfragevarianteModel model
    );
}
