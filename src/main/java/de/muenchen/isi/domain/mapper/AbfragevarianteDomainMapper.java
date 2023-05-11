/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.AbfragevarianteResponseModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfragevarianteRequestModel;
import de.muenchen.isi.infrastructure.entity.Abfragevariante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapstructConfiguration.class, uses = {BauabschnittDomainMapper.class})
public interface AbfragevarianteDomainMapper {
    AbfragevarianteResponseModel entity2Model(final Abfragevariante entity);

    Abfragevariante model2entity(final AbfragevarianteResponseModel model);

    @Mapping(target = "isRelevant", ignore = true)
    AbfragevarianteResponseModel request2Response(final AbfragevarianteRequestModel request, @MappingTarget AbfragevarianteResponseModel response);
}
