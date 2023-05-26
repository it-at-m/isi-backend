/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.AbfragevarianteModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfrageerstellungAbfragevarianteAngelegtModel;
import de.muenchen.isi.infrastructure.entity.Abfragevariante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(
    config = MapstructConfiguration.class,
    uses = { BauabschnittDomainMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AbfragevarianteDomainMapper {
    AbfragevarianteModel entity2Model(final Abfragevariante entity);

    Abfragevariante model2entity(final AbfragevarianteModel model);

    @Mappings({ @Mapping(target = "relevant", ignore = true) })
    AbfragevarianteModel request2Model(final AbfrageerstellungAbfragevarianteAngelegtModel request);
}
