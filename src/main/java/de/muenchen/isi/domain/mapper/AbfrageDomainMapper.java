/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.AbfragevarianteModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfrageerstellungInfrastrukturabfrageAngelegtModel;
import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(config = MapstructConfiguration.class, uses = { AbfragevarianteDomainMapper.class, DokumentDomainMapper.class })
public abstract class AbfrageDomainMapper {

    @Autowired
    private AbfragevarianteDomainMapper abfragevarianteDomainMapper;

    public abstract AbfrageModel entity2Model(final Abfrage entity);

    public abstract Abfrage model2Entity(final AbfrageModel model);

    public abstract InfrastrukturabfrageModel entity2Model(final Infrastrukturabfrage entity);

    public abstract Infrastrukturabfrage model2entity(final InfrastrukturabfrageModel model);

    @Mappings(
        {
            @Mapping(target = "abfrage.statusAbfrage", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdDateTime", ignore = true),
            @Mapping(target = "lastModifiedDateTime", ignore = true),
            @Mapping(target = "abfragevarianten", ignore = true),
        }
    )
    public abstract InfrastrukturabfrageModel request2Model(
        final AbfrageerstellungInfrastrukturabfrageAngelegtModel request,
        @MappingTarget InfrastrukturabfrageModel response
    );

    @AfterMapping
    void setAbfragevarianteOnInfrastrukturabfrage(
        final AbfrageerstellungInfrastrukturabfrageAngelegtModel request,
        @MappingTarget InfrastrukturabfrageModel response
    ) {
        final List<AbfragevarianteModel> abfragevarianten = new ArrayList<>();
        request
            .getAbfragevarianten()
            .forEach(abfragevariante -> {
                if (abfragevariante.getId() == null) {
                    abfragevarianten.add(
                        abfragevarianteDomainMapper.request2Model(abfragevariante, new AbfragevarianteModel())
                    );
                } else {
                    response
                        .getAbfragevarianten()
                        .stream()
                        .filter(abfragevarianteModel -> abfragevarianteModel.getId().equals(abfragevariante.getId()))
                        .findFirst()
                        .ifPresent(model ->
                            abfragevarianten.add(abfragevarianteDomainMapper.request2Model(abfragevariante, model))
                        );
                }
            });
        response.setAbfragevarianten(abfragevarianten);
    }
}
