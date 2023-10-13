/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.AbfragevarianteAltModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfragevarianteAngelegtModel;
import de.muenchen.isi.domain.model.abfrageBedarfsmeldungInBearbeitungFachreferate.AbfragevarianteInBearbeitungFachreferateModel;
import de.muenchen.isi.domain.model.abfrageSachbearbeitungInBearbeitungSachbearbeitung.AbfragevarianteInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageSachbearbeitungInBearbeitungSachbearbeitung.AbfragevarianteSachbearbeitungInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.infrastructure.entity.AbfragevarianteAlt;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(config = MapstructConfiguration.class, uses = { BauabschnittDomainMapper.class })
public interface AbfragevarianteAltDomainMapper {
    AbfragevarianteAltModel entity2Model(final AbfragevarianteAlt entity);

    AbfragevarianteAlt model2entity(final AbfragevarianteAltModel model);

    @Mappings(
        {
            @Mapping(target = "lastModifiedDateTime", ignore = true),
            @Mapping(target = "createdDateTime", ignore = true),
            @Mapping(target = "gfWohnenPlanungsursaechlich", ignore = true),
            @Mapping(target = "sobonOrientierungswertJahr", ignore = true),
            @Mapping(target = "anmerkung", ignore = true),
        }
    )
    AbfragevarianteAltModel request2Model(
        final AbfragevarianteAngelegtModel request,
        final @MappingTarget AbfragevarianteAltModel model
    );

    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        {
            @Mapping(target = "version", ignore = false),
            @Mapping(target = "gfWohnenPlanungsursaechlich", ignore = false),
            @Mapping(target = "sobonOrientierungswertJahr", ignore = false),
            @Mapping(target = "anmerkung", ignore = false),
        }
    )
    AbfragevarianteAltModel request2Model(
        final AbfragevarianteSachbearbeitungInBearbeitungSachbearbeitungModel request,
        final @MappingTarget AbfragevarianteAltModel model
    );

    @Mappings(
        {
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "lastModifiedDateTime", ignore = true),
            @Mapping(target = "createdDateTime", ignore = true),
        }
    )
    AbfragevarianteAltModel request2Model(
        final AbfragevarianteInBearbeitungSachbearbeitungModel request,
        final @MappingTarget AbfragevarianteAltModel model
    );

    @BeanMapping(ignoreByDefault = true)
    @Mappings({ @Mapping(target = "version", ignore = false) })
    AbfragevarianteAltModel request2Model(
        final AbfragevarianteInBearbeitungFachreferateModel request,
        final @MappingTarget AbfragevarianteAltModel model
    );
}
