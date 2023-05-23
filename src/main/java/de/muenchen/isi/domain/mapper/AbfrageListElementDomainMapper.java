/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.domain.model.enums.AbfrageTyp;
import de.muenchen.isi.domain.model.list.AbfrageListElementModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(config = MapstructConfiguration.class)
public interface AbfrageListElementDomainMapper {
    @Mappings(
        {
            @Mapping(target = "type", ignore = true),
            @Mapping(source = "abfrage.nameAbfrage", target = "nameAbfrage"),
            @Mapping(source = "abfrage.standVorhaben", target = "standVorhaben"),
            @Mapping(source = "abfrage.statusAbfrage", target = "statusAbfrage"),
            @Mapping(source = "abfrage.fristStellungnahme", target = "fristStellungnahme"),
        }
    )
    AbfrageListElementModel infrastrukturabfrageModel2AbfrageListElementModel(final InfrastrukturabfrageModel model);

    @AfterMapping
    default void infrastrukturabfrageModel2AbfrageListElementModelAfterMapping(
        @MappingTarget final AbfrageListElementModel abfrageListElementModel,
        final InfrastrukturabfrageModel model
    ) {
        abfrageListElementModel.setType(AbfrageTyp.INFRASTRUKTURABFRAGE);
    }
}
