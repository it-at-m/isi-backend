/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.AbfragevarianteBauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfragevarianteBauleitplanverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.AbfragevarianteBauleitplanverfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.AbfragevarianteBauleitplanverfahrenSachbearbeitungInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.infrastructure.entity.AbfragevarianteBauleitplanverfahren;
import org.mapstruct.BeanMapping;
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
            @Mapping(target = "gfWohnenPlanungsursaechlich", ignore = true),
            @Mapping(target = "sobonOrientierungswertJahr", ignore = true),
            @Mapping(target = "anmerkung", ignore = true),
            @Mapping(target = "bedarfsmeldungFachreferate", ignore = true),
        }
    )
    AbfragevarianteBauleitplanverfahrenModel request2Model(
        final AbfragevarianteBauleitplanverfahrenAngelegtModel request,
        final @MappingTarget AbfragevarianteBauleitplanverfahrenModel model
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
    AbfragevarianteBauleitplanverfahrenModel request2Model(
        final AbfragevarianteBauleitplanverfahrenSachbearbeitungInBearbeitungSachbearbeitungModel request,
        final @MappingTarget AbfragevarianteBauleitplanverfahrenModel model
    );

    @Mappings(
        {
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "lastModifiedDateTime", ignore = true),
            @Mapping(target = "createdDateTime", ignore = true),
            @Mapping(target = "bedarfsmeldungFachreferate", ignore = true),
        }
    )
    AbfragevarianteBauleitplanverfahrenModel request2Model(
        final AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungModel request,
        final @MappingTarget AbfragevarianteBauleitplanverfahrenModel model
    );

    @BeanMapping(ignoreByDefault = true)
    @Mappings({ @Mapping(target = "version", ignore = false) })
    AbfragevarianteBauleitplanverfahrenModel request2Model(
        final AbfragevarianteBauleitplanverfahrenInBearbeitungFachreferatModel request,
        final @MappingTarget AbfragevarianteBauleitplanverfahrenModel model
    );
}
