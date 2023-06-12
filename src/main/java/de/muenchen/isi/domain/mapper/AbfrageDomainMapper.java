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
import de.muenchen.isi.domain.model.abfrageSachbearbeitungInBearbeitungSachbearbeitung.InfrastrukturabfrageInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
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

    /**
     * Mapping Methode welche die Attribute des im Parameter gegebenen {@link AbfrageerstellungInfrastrukturabfrageAngelegtModel}
     * auf das ebenfalls im Parameter gegebene {@link InfrastrukturabfrageModel} mapped.
     * <p>
     * Die Abfragevarianten werden ignoriert da diese in der AfterMapping-Methode
     * {@link AbfrageDomainMapper#setAbfragevarianteOnInfrastrukturabfrage} verarbeitet werden.
     *
     * @param request  das Request-Model welches gemapped werden soll
     * @param response das {@link InfrastrukturabfrageModel} zu dem es gemapped wird
     * @return gemappte {@link InfrastrukturabfrageModel}
     */
    @Mappings(
        {
            @Mapping(target = "abfrage.statusAbfrage", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdDateTime", ignore = true),
            @Mapping(target = "lastModifiedDateTime", ignore = true),
            @Mapping(target = "abfragevarianten", ignore = true),
            @Mapping(target = "abfragevariantenSachbearbeitung", ignore = true),
        }
    )
    public abstract InfrastrukturabfrageModel request2Model(
        final AbfrageerstellungInfrastrukturabfrageAngelegtModel request,
        @MappingTarget InfrastrukturabfrageModel response
    );

    /**
     * Führt das Mapping der Abfragevarianten für die im Parameter gegebenen Klassen durch.
     *
     * @param request  das Request-Objekt welches gemapped werden soll
     * @param response das {@link InfrastrukturabfrageModel} zu dem es gemapped wird
     */
    @AfterMapping
    void setAbfragevarianteOnInfrastrukturabfrage(
        final AbfrageerstellungInfrastrukturabfrageAngelegtModel request,
        final @MappingTarget InfrastrukturabfrageModel response
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

    @BeanMapping(ignoreByDefault = true)
    @Mappings({ @Mapping(target = "version", ignore = false) })
    public abstract InfrastrukturabfrageModel request2Model(
        final InfrastrukturabfrageInBearbeitungSachbearbeitungModel request,
        @MappingTarget InfrastrukturabfrageModel response
    );

    @AfterMapping
    void setAbfragevarianteSachbearbeitungOnInfrastrukturabfrage(
        final InfrastrukturabfrageInBearbeitungSachbearbeitungModel request,
        final @MappingTarget InfrastrukturabfrageModel response
    ) {
        // Mapping der zusätzlichen durch die Sachbearbeitung pflegbaren Attribute der Abfragevarianten
        final List<AbfragevarianteModel> mappedAbfragevarianten = new ArrayList<>();
        request
            .getAbfragevarianten()
            .forEach(abfragevariante -> {
                response
                    .getAbfragevarianten()
                    .stream()
                    .filter(abfragevarianteModel -> abfragevarianteModel.getId().equals(abfragevariante.getId()))
                    .findFirst()
                    .ifPresent(abfragevarianteModel ->
                        mappedAbfragevarianten.add(
                            abfragevarianteDomainMapper.request2Model(abfragevariante, abfragevarianteModel)
                        )
                    );
            });
        response.setAbfragevarianten(mappedAbfragevarianten);
        // Mapping der Abfragevarianten welche ausschlielich durch die Sachbearbeitung gemappt werden.
        final List<AbfragevarianteModel> mappedAbfragevariantenSachbearbeitung = new ArrayList<>();
        request
            .getAbfragevariantenSachbearbeitung()
            .forEach(abfragevariante -> {
                if (abfragevariante.getId() == null) {
                    mappedAbfragevariantenSachbearbeitung.add(
                        abfragevarianteDomainMapper.request2Model(abfragevariante, new AbfragevarianteModel())
                    );
                } else {
                    response
                        .getAbfragevariantenSachbearbeitung()
                        .stream()
                        .filter(abfragevarianteModel -> abfragevarianteModel.getId().equals(abfragevariante.getId()))
                        .findFirst()
                        .ifPresent(model ->
                            mappedAbfragevariantenSachbearbeitung.add(
                                abfragevarianteDomainMapper.request2Model(abfragevariante, model)
                            )
                        );
                }
            });
        response.setAbfragevariantenSachbearbeitung(mappedAbfragevariantenSachbearbeitung);
    }
}
