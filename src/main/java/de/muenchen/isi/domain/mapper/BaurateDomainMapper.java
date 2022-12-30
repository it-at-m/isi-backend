/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.BaurateModel;
import de.muenchen.isi.infrastructure.entity.Baurate;
import org.mapstruct.Mapper;

@Mapper(
        config = MapstructConfiguration.class,
        uses = {AbfragevarianteDomainMapper.class}
)
public interface BaurateDomainMapper {

    BaurateModel entity2Model(final Baurate entity);

    Baurate model2entity(final BaurateModel model);

}
