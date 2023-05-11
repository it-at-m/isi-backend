/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.AbfrageResponseModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageResponseModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.InfrastrukturabfrageRequestModel;
import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapstructConfiguration.class, uses = {AbfragevarianteDomainMapper.class, DokumentDomainMapper.class})
public interface AbfrageDomainMapper {
    AbfrageResponseModel entity2Model(final Abfrage entity);

    Abfrage model2Entity(final AbfrageResponseModel model);

    InfrastrukturabfrageResponseModel entity2Model(final Infrastrukturabfrage entity);

    Infrastrukturabfrage model2entity(final InfrastrukturabfrageResponseModel model);

    @Mapping(target = "abfrage.statusAbfrage", ignore = true)
    InfrastrukturabfrageResponseModel request2Reponse(InfrastrukturabfrageRequestModel request, @MappingTarget InfrastrukturabfrageResponseModel response);
}
