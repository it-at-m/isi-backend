/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.AbfragevarianteModel;
import de.muenchen.isi.infrastructure.entity.Abfragevariante;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class, uses = { BauabschnittDomainMapper.class })
public interface AbfragevarianteDomainMapper {
    AbfragevarianteModel entity2Model(final Abfragevariante entity);

    Abfragevariante model2entity(final AbfragevarianteModel model);
}
