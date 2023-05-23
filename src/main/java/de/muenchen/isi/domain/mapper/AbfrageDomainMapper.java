/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfrageerstellungInfrastrukturabfrageAngelegtModel;
import de.muenchen.isi.domain.model.abfrageSachbearbeitungOffenInBearbeitung.SachbearbeitungInfrastrukturabfrageOffenInBearbeitungModel;
import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
    config = MapstructConfiguration.class,
    uses = { AbfragevarianteDomainMapper.class, DokumentDomainMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AbfrageDomainMapper {
    AbfrageModel entity2Model(final Abfrage entity);

    Abfrage model2Entity(final AbfrageModel model);

    InfrastrukturabfrageModel entity2Model(final Infrastrukturabfrage entity);

    Infrastrukturabfrage model2entity(final InfrastrukturabfrageModel model);

    @Mapping(target = "abfrage.statusAbfrage", ignore = true)
    InfrastrukturabfrageModel request2Model(
        final AbfrageerstellungInfrastrukturabfrageAngelegtModel request,
        @MappingTarget InfrastrukturabfrageModel response
    );

    @Mapping(target = "abfrage.statusAbfrage", ignore = true)
    InfrastrukturabfrageModel request2Model(
        final SachbearbeitungInfrastrukturabfrageOffenInBearbeitungModel request,
        @MappingTarget InfrastrukturabfrageModel response
    );
}
