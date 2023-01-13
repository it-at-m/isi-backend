/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
import org.mapstruct.Mapper;

@Mapper(
        config = MapstructConfiguration.class,
        uses = {
                AbfragevarianteDomainMapper.class,
                DokumentDomainMapper.class
        }
)
public interface AbfrageDomainMapper {

    AbfrageModel entity2Model(final Abfrage entity);

    Abfrage model2Entity(final AbfrageModel model);

    InfrastrukturabfrageModel entity2Model(final Infrastrukturabfrage entity);

    Infrastrukturabfrage model2entity(final InfrastrukturabfrageModel model);

}
