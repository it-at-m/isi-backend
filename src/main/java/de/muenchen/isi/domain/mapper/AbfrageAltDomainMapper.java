/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.AbfrageAltModel;
import de.muenchen.isi.domain.model.AbfragevarianteAltModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.InfrastrukturabfrageAngelegtModel;
import de.muenchen.isi.domain.model.abfrageBedarfsmeldungInBearbeitungFachreferate.InfrastrukturabfrageInBearbeitungFachreferateModel;
import de.muenchen.isi.domain.model.abfrageSachbearbeitungInBearbeitungSachbearbeitung.InfrastrukturabfrageInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.infrastructure.entity.AbfrageAlt;
import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
    config = MapstructConfiguration.class,
    uses = { AbfragevarianteAltDomainMapper.class, DokumentDomainMapper.class }
)
public abstract class AbfrageAltDomainMapper {

    @Autowired
    private AbfragevarianteAltDomainMapper abfragevarianteDomainMapper;

    public abstract AbfrageAltModel entity2Model(final AbfrageAlt entity);

    public abstract AbfrageAlt model2Entity(final AbfrageAltModel model);

    public abstract InfrastrukturabfrageModel entity2Model(final Infrastrukturabfrage entity);

    public abstract Infrastrukturabfrage model2entity(final InfrastrukturabfrageModel model);

    /**
     * Mapping Methode welche die Attribute des im Parameter gegebenen {@link InfrastrukturabfrageAngelegtModel}
     * auf das ebenfalls im Parameter gegebene {@link InfrastrukturabfrageModel} mapped.
     * <p>
     * Die Abfragevarianten werden ignoriert da diese in der AfterMapping-Methode
     * {@link AbfrageAltDomainMapper#afterMappingRequest2Model} verarbeitet werden.
     *
     * @param request  das Request-Model welches gemapped werden soll
     * @param response das {@link InfrastrukturabfrageModel} zu dem es gemapped wird
     * @return gemappte {@link InfrastrukturabfrageModel}
     */
    @Mappings(
        {
            @Mapping(target = "abfrage.statusAbfrage", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "sub", ignore = true),
            @Mapping(target = "createdDateTime", ignore = true),
            @Mapping(target = "lastModifiedDateTime", ignore = true),
            @Mapping(target = "abfragevarianten", ignore = true),
            @Mapping(target = "abfragevariantenSachbearbeitung", ignore = true),
        }
    )
    public abstract InfrastrukturabfrageModel request2Model(
        final InfrastrukturabfrageAngelegtModel request,
        @MappingTarget InfrastrukturabfrageModel response
    );

    /**
     * Führt das Mapping der Abfragevarianten für die im Parameter gegebenen Klassen durch.
     *
     * @param request  das Request-Objekt welches gemapped werden soll
     * @param response das {@link InfrastrukturabfrageModel} zu dem es gemapped wird
     */
    @AfterMapping
    void afterMappingRequest2Model(
        final InfrastrukturabfrageAngelegtModel request,
        final @MappingTarget InfrastrukturabfrageModel response
    ) {
        final List<AbfragevarianteAltModel> abfragevarianten = new ArrayList<>();
        CollectionUtils
            .emptyIfNull(request.getAbfragevarianten())
            .forEach(abfragevariante -> {
                if (abfragevariante.getId() == null) {
                    abfragevarianten.add(
                        abfragevarianteDomainMapper.request2Model(abfragevariante, new AbfragevarianteAltModel())
                    );
                } else {
                    CollectionUtils
                        .emptyIfNull(response.getAbfragevarianten())
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

    /**
     * Führt das Mapping der Abfragevarianten für die im Parameter gegebenen Klassen durch.
     *
     * @param request  das Request-Objekt welches gemapped werden soll
     * @param response das {@link InfrastrukturabfrageModel} zu dem es gemapped wird
     */
    @AfterMapping
    void afterMappingRequest2Model(
        final InfrastrukturabfrageInBearbeitungSachbearbeitungModel request,
        final @MappingTarget InfrastrukturabfrageModel response
    ) {
        // Mapping der zusätzlichen durch die Sachbearbeitung pflegbaren Attribute der Abfragevarianten
        final List<AbfragevarianteAltModel> mappedAbfragevarianten = new ArrayList<>();
        CollectionUtils
            .emptyIfNull(request.getAbfragevarianten())
            .forEach(abfragevariante -> {
                CollectionUtils
                    .emptyIfNull(response.getAbfragevarianten())
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
        // Mapping der Abfragevarianten welche ausschließlich durch die Sachbearbeitung gemappt werden.
        final List<AbfragevarianteAltModel> mappedAbfragevariantenSachbearbeitung = new ArrayList<>();
        CollectionUtils
            .emptyIfNull(request.getAbfragevariantenSachbearbeitung())
            .forEach(abfragevariante -> {
                if (abfragevariante.getId() == null) {
                    mappedAbfragevariantenSachbearbeitung.add(
                        abfragevarianteDomainMapper.request2Model(abfragevariante, new AbfragevarianteAltModel())
                    );
                } else {
                    CollectionUtils
                        .emptyIfNull(response.getAbfragevariantenSachbearbeitung())
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

    @BeanMapping(ignoreByDefault = true)
    @Mappings({ @Mapping(target = "version", ignore = false) })
    public abstract InfrastrukturabfrageModel request2Model(
        final InfrastrukturabfrageInBearbeitungFachreferateModel request,
        @MappingTarget InfrastrukturabfrageModel response
    );

    /**
     * Führt das Mapping der Abfragevarianten für die im Parameter gegebenen Klassen durch.
     *
     * @param request  das Request-Objekt welches gemapped werden soll
     * @param response das {@link InfrastrukturabfrageModel} zu dem es gemapped wird
     */
    @AfterMapping
    void afterMappingRequest2Model(
        final InfrastrukturabfrageInBearbeitungFachreferateModel request,
        final @MappingTarget InfrastrukturabfrageModel response
    ) {
        // Mapping der Bedarfsmeldungen durch die Fachabteilungen der Abfragevarianten
        final List<AbfragevarianteAltModel> mappedAbfragevarianten = new ArrayList<>();
        CollectionUtils
            .emptyIfNull(request.getAbfragevarianten())
            .forEach(abfragevariante -> {
                CollectionUtils
                    .emptyIfNull(response.getAbfragevarianten())
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
        // Mapping der Abfragevarianten welche ausschließlich durch die Sachbearbeitung gemappt werden.
        final List<AbfragevarianteAltModel> mappedAbfragevariantenSachbearbeitung = new ArrayList<>();
        CollectionUtils
            .emptyIfNull(request.getAbfragevariantenSachbearbeitung())
            .forEach(abfragevariante -> {
                if (abfragevariante.getId() == null) {
                    mappedAbfragevariantenSachbearbeitung.add(
                        abfragevarianteDomainMapper.request2Model(abfragevariante, new AbfragevarianteAltModel())
                    );
                } else {
                    CollectionUtils
                        .emptyIfNull(response.getAbfragevariantenSachbearbeitung())
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
