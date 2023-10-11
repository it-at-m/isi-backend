/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.AbfragevarianteBauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.BauleitplanverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.BauleitplanverfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.BauleitplanverfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.entity.Bauleitplanverfahren;
import de.muenchen.isi.infrastructure.repository.BauvorhabenRepository;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.SubclassMapping;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Mapper(
    config = MapstructConfiguration.class,
    uses = { AbfragevarianteBauleitplanverfahrenDomainMapper.class, DokumentDomainMapper.class }
)
public abstract class AbfrageDomainMapper {

    @Autowired
    private BauvorhabenRepository bauvorhabenRepository;

    @Autowired
    private AbfragevarianteBauleitplanverfahrenDomainMapper abfragevarianteBauleitplanverfahrenDomainMapper;

    @SubclassMapping(source = Bauleitplanverfahren.class, target = BauleitplanverfahrenModel.class)
    @Mapping(source = "bauvorhaben.id", target = "bauvorhaben")
    public abstract AbfrageModel entity2Model(final Abfrage entity);

    @SubclassMapping(source = BauleitplanverfahrenModel.class, target = Bauleitplanverfahren.class)
    @Mapping(target = "bauvorhaben", ignore = true)
    public abstract Abfrage model2Entity(final AbfrageModel model) throws EntityNotFoundException;

    @AfterMapping
    public void afterMappingModel2Entity(final AbfrageModel model, @MappingTarget final Abfrage entity)
        throws EntityNotFoundException {
        final var bauvorhaben = bauvorhabenRepository
            .findById(model.getBauvorhaben())
            .orElseThrow(() -> {
                final var message = "Bauvorhaben nicht gefunden";
                log.error(message);
                return new EntityNotFoundException(message);
            });
        entity.setBauvorhaben(bauvorhaben);
    }

    @Mappings(
        {
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "statusAbfrage", ignore = true),
            @Mapping(target = "sub", ignore = true),
            @Mapping(target = "createdDateTime", ignore = true),
            @Mapping(target = "lastModifiedDateTime", ignore = true),
            @Mapping(target = "abfragevarianten", ignore = true),
            @Mapping(target = "abfragevariantenSachbearbeitung", ignore = true),
        }
    )
    public abstract BauleitplanverfahrenModel request2Model(
        final BauleitplanverfahrenAngelegtModel request,
        @MappingTarget final BauleitplanverfahrenModel model
    );

    /**
     * Führt das Mapping der Abfragevarianten für die im Parameter gegebenen Klassen durch.
     *
     * @param request  das Request-Objekt welches gemapped werden soll
     * @param model das {@link BauleitplanverfahrenModel} zu dem es gemapped wird
     */
    @AfterMapping
    void afterMappingRequest2Model(
        final BauleitplanverfahrenAngelegtModel request,
        final @MappingTarget BauleitplanverfahrenModel model
    ) {
        final var abfragevarianten = new ArrayList<AbfragevarianteBauleitplanverfahrenModel>();
        CollectionUtils
            .emptyIfNull(request.getAbfragevarianten())
            .forEach(abfragevariante -> {
                if (abfragevariante.getId() == null) {
                    final var mappedModel = abfragevarianteBauleitplanverfahrenDomainMapper.request2Model(
                        abfragevariante,
                        new AbfragevarianteBauleitplanverfahrenModel()
                    );
                    abfragevarianten.add(mappedModel);
                } else {
                    CollectionUtils
                        .emptyIfNull(model.getAbfragevarianten())
                        .stream()
                        .filter(abfragevarianteModel -> abfragevarianteModel.getId().equals(abfragevariante.getId()))
                        .findFirst()
                        .ifPresent(abfragevarianteModel ->
                            abfragevarianten.add(
                                abfragevarianteBauleitplanverfahrenDomainMapper.request2Model(
                                    abfragevariante,
                                    abfragevarianteModel
                                )
                            )
                        );
                }
            });
        model.setAbfragevarianten(abfragevarianten);
    }

    @BeanMapping(ignoreByDefault = true)
    @Mappings({ @Mapping(target = "version", ignore = false) })
    public abstract BauleitplanverfahrenModel request2Model(
        final BauleitplanverfahrenInBearbeitungSachbearbeitungModel request,
        @MappingTarget BauleitplanverfahrenModel response
    );

    /**
     * Führt das Mapping der Abfragevarianten für die im Parameter gegebenen Klassen durch.
     *
     * @param request  das Request-Objekt welches gemapped werden soll
     * @param response das {@link BauleitplanverfahrenModel} zu dem es gemapped wird
     */
    @AfterMapping
    void afterMappingRequest2Model(
        final BauleitplanverfahrenInBearbeitungSachbearbeitungModel request,
        final @MappingTarget BauleitplanverfahrenModel response
    ) {
        // Mapping der zusätzlichen durch die Sachbearbeitung pflegbaren Attribute der Abfragevarianten
        final var mappedAbfragevarianten = new ArrayList<AbfragevarianteBauleitplanverfahrenModel>();
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
                            abfragevarianteBauleitplanverfahrenDomainMapper.request2Model(
                                abfragevariante,
                                abfragevarianteModel
                            )
                        )
                    );
            });
        response.setAbfragevarianten(mappedAbfragevarianten);
        // Mapping der Abfragevarianten welche ausschließlich durch die Sachbearbeitung gemappt werden.
        final var mappedAbfragevariantenSachbearbeitung = new ArrayList<AbfragevarianteBauleitplanverfahrenModel>();
        CollectionUtils
            .emptyIfNull(request.getAbfragevariantenSachbearbeitung())
            .forEach(abfragevariante -> {
                if (abfragevariante.getId() == null) {
                    final var mappedModel = abfragevarianteBauleitplanverfahrenDomainMapper.request2Model(
                        abfragevariante,
                        new AbfragevarianteBauleitplanverfahrenModel()
                    );
                    mappedAbfragevariantenSachbearbeitung.add(mappedModel);
                } else {
                    CollectionUtils
                        .emptyIfNull(response.getAbfragevariantenSachbearbeitung())
                        .stream()
                        .filter(abfragevarianteModel -> abfragevarianteModel.getId().equals(abfragevariante.getId()))
                        .findFirst()
                        .ifPresent(abfragevarianteModel ->
                            mappedAbfragevariantenSachbearbeitung.add(
                                abfragevarianteBauleitplanverfahrenDomainMapper.request2Model(
                                    abfragevariante,
                                    abfragevarianteModel
                                )
                            )
                        );
                }
            });
        response.setAbfragevariantenSachbearbeitung(mappedAbfragevariantenSachbearbeitung);
    }

    @BeanMapping(ignoreByDefault = true)
    @Mappings({ @Mapping(target = "version", ignore = false) })
    public abstract BauleitplanverfahrenModel request2Model(
        final BauleitplanverfahrenInBearbeitungFachreferatModel request,
        @MappingTarget BauleitplanverfahrenModel response
    );

    /**
     * Führt das Mapping der Abfragevarianten für die im Parameter gegebenen Klassen durch.
     *
     * @param request  das Request-Objekt welches gemapped werden soll
     * @param response das {@link BauleitplanverfahrenModel} zu dem es gemapped wird
     */
    @AfterMapping
    void afterMappingRequest2Model(
        final BauleitplanverfahrenInBearbeitungFachreferatModel request,
        final @MappingTarget BauleitplanverfahrenModel response
    ) {
        // Mapping der Bedarfsmeldungen durch die Fachabteilungen der Abfragevarianten
        final var mappedAbfragevarianten = new ArrayList<AbfragevarianteBauleitplanverfahrenModel>();
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
                            abfragevarianteBauleitplanverfahrenDomainMapper.request2Model(
                                abfragevariante,
                                abfragevarianteModel
                            )
                        )
                    );
            });
        response.setAbfragevarianten(mappedAbfragevarianten);
        // Mapping der Abfragevarianten welche ausschließlich durch die Sachbearbeitung gemappt werden.
        final var mappedAbfragevariantenSachbearbeitung = new ArrayList<AbfragevarianteBauleitplanverfahrenModel>();
        CollectionUtils
            .emptyIfNull(request.getAbfragevariantenSachbearbeitung())
            .forEach(abfragevariante -> {
                if (abfragevariante.getId() == null) {
                    final var mappedModel = abfragevarianteBauleitplanverfahrenDomainMapper.request2Model(
                        abfragevariante,
                        new AbfragevarianteBauleitplanverfahrenModel()
                    );
                    mappedAbfragevariantenSachbearbeitung.add(mappedModel);
                } else {
                    CollectionUtils
                        .emptyIfNull(response.getAbfragevariantenSachbearbeitung())
                        .stream()
                        .filter(abfragevarianteModel -> abfragevarianteModel.getId().equals(abfragevariante.getId()))
                        .findFirst()
                        .ifPresent(abfragevarianteModel ->
                            mappedAbfragevariantenSachbearbeitung.add(
                                abfragevarianteBauleitplanverfahrenDomainMapper.request2Model(
                                    abfragevariante,
                                    abfragevarianteModel
                                )
                            )
                        );
                }
            });
        response.setAbfragevariantenSachbearbeitung(mappedAbfragevariantenSachbearbeitung);
    }
}
