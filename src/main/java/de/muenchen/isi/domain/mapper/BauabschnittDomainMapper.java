/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.infrastructure.entity.Bauabschnitt;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface BauabschnittDomainMapper {
    BauabschnittModel entity2Model(final Bauabschnitt entity);

    Bauabschnitt model2Entity(final BauabschnittModel entity);
}
