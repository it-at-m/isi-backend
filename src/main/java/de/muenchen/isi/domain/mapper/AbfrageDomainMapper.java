/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.AbfragevarianteBaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.AbfragevarianteBauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.AbfragevarianteWeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.BaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.WeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfrageAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.BaugenehmigungsverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.BauleitplanverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.WeiteresVerfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.BaugenehmigungsverfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.BauleitplanverfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.WeiteresVerfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.BaugenehmigungsverfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.BauleitplanverfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.WeiteresVerfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.entity.Baugenehmigungsverfahren;
import de.muenchen.isi.infrastructure.entity.Bauleitplanverfahren;
import de.muenchen.isi.infrastructure.entity.WeiteresVerfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.repository.BauvorhabenRepository;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.SubclassMapping;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Mapper(config = MapstructConfiguration.class, uses = { AbfragevarianteDomainMapper.class, DokumentDomainMapper.class })
public abstract class AbfrageDomainMapper {

    @Autowired
    private BauvorhabenRepository bauvorhabenRepository;

    @Autowired
    private AbfragevarianteDomainMapper abfragevarianteDomainMapper;

    @SubclassMapping(source = Bauleitplanverfahren.class, target = BauleitplanverfahrenModel.class)
    @SubclassMapping(source = Baugenehmigungsverfahren.class, target = BaugenehmigungsverfahrenModel.class)
    @SubclassMapping(source = WeiteresVerfahren.class, target = WeiteresVerfahrenModel.class)
    @Mapping(source = "bauvorhaben.id", target = "bauvorhaben")
    public abstract AbfrageModel entity2Model(final Abfrage entity);

    @SubclassMapping(source = BauleitplanverfahrenModel.class, target = Bauleitplanverfahren.class)
    @SubclassMapping(source = BaugenehmigungsverfahrenModel.class, target = Baugenehmigungsverfahren.class)
    @SubclassMapping(source = WeiteresVerfahrenModel.class, target = WeiteresVerfahren.class)
    @Mapping(target = "bauvorhaben", ignore = true)
    public abstract Abfrage model2Entity(final AbfrageModel model) throws EntityNotFoundException;

    @AfterMapping
    public void afterMappingModel2Entity(final AbfrageModel model, @MappingTarget final Abfrage entity)
        throws EntityNotFoundException {
        if (ObjectUtils.isNotEmpty(model.getBauvorhaben())) {
            final var bauvorhaben = bauvorhabenRepository
                .findById(model.getBauvorhaben())
                .orElseThrow(() -> {
                    final var message = "Bauvorhaben nicht gefunden";
                    log.error(message);
                    return new EntityNotFoundException(message);
                });
            entity.setBauvorhaben(bauvorhaben);
        }
    }

    public AbfrageModel request2NewModel(final AbfrageAngelegtModel request) throws EntityNotFoundException {
        if (ArtAbfrage.BAULEITPLANVERFAHREN.equals(request.getArtAbfrage())) {
            return this.request2Model((BauleitplanverfahrenAngelegtModel) request, new BauleitplanverfahrenModel());
        } else if (ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN.equals(request.getArtAbfrage())) {
            return this.request2Model(
                    (BaugenehmigungsverfahrenAngelegtModel) request,
                    new BaugenehmigungsverfahrenModel()
                );
        } else if (ArtAbfrage.WEITERES_VERFAHREN.equals(request.getArtAbfrage())) {
            return this.request2Model((WeiteresVerfahrenAngelegtModel) request, new WeiteresVerfahrenModel());
        } else {
            final var message = "Die Art der Abfrage wird nicht unterstützt.";
            log.error(message);
            throw new EntityNotFoundException(message);
        }
    }

    @Mappings(
        {
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "statusAbfrage", ignore = true),
            @Mapping(target = "sub", ignore = true),
            @Mapping(target = "createdDateTime", ignore = true),
            @Mapping(target = "lastModifiedDateTime", ignore = true),
            @Mapping(target = "abfragevariantenBauleitplanverfahren", ignore = true),
            @Mapping(target = "abfragevariantenSachbearbeitungBauleitplanverfahren", ignore = true),
        }
    )
    public abstract BauleitplanverfahrenModel request2Model(
        final BauleitplanverfahrenAngelegtModel request,
        @MappingTarget final BauleitplanverfahrenModel model
    );

    /**
     * Führt das Mapping der Abfragevarianten für die im Parameter gegebenen Klassen durch.
     *
     * @param request das Request-Objekt welches gemapped werden soll
     * @param model   das {@link BauleitplanverfahrenModel} zu dem es gemapped wird
     */
    @AfterMapping
    void afterMappingRequest2Model(
        final BauleitplanverfahrenAngelegtModel request,
        @MappingTarget final BauleitplanverfahrenModel model
    ) {
        final var abfragevarianten = new ArrayList<AbfragevarianteBauleitplanverfahrenModel>();
        CollectionUtils
            .emptyIfNull(request.getAbfragevariantenBauleitplanverfahren())
            .forEach(abfragevariante -> {
                if (abfragevariante.getId() == null) {
                    final var mappedModel = abfragevarianteDomainMapper.request2Model(
                        abfragevariante,
                        new AbfragevarianteBauleitplanverfahrenModel()
                    );
                    abfragevarianten.add(mappedModel);
                } else {
                    CollectionUtils
                        .emptyIfNull(model.getAbfragevariantenBauleitplanverfahren())
                        .stream()
                        .filter(abfragevarianteModel -> abfragevarianteModel.getId().equals(abfragevariante.getId()))
                        .findFirst()
                        .ifPresent(abfragevarianteModel ->
                            abfragevarianten.add(
                                abfragevarianteDomainMapper.request2Model(abfragevariante, abfragevarianteModel)
                            )
                        );
                }
            });
        model.setAbfragevariantenBauleitplanverfahren(abfragevarianten);
    }

    @Mappings(
        {
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "statusAbfrage", ignore = true),
            @Mapping(target = "sub", ignore = true),
            @Mapping(target = "createdDateTime", ignore = true),
            @Mapping(target = "lastModifiedDateTime", ignore = true),
            @Mapping(target = "abfragevariantenBaugenehmigungsverfahren", ignore = true),
            @Mapping(target = "abfragevariantenSachbearbeitungBaugenehmigungsverfahren", ignore = true),
        }
    )
    public abstract BaugenehmigungsverfahrenModel request2Model(
        final BaugenehmigungsverfahrenAngelegtModel request,
        @MappingTarget final BaugenehmigungsverfahrenModel model
    );

    /**
     * Führt das Mapping der Abfragevarianten für die im Parameter gegebenen Klassen durch.
     *
     * @param request  das Request-Objekt welches gemapped werden soll
     * @param model das {@link BaugenehmigungsverfahrenModel} zu dem es gemapped wird
     */
    @AfterMapping
    void afterMappingRequest2Model(
        final BaugenehmigungsverfahrenAngelegtModel request,
        @MappingTarget final BaugenehmigungsverfahrenModel model
    ) {
        final var abfragevarianten = new ArrayList<AbfragevarianteBaugenehmigungsverfahrenModel>();
        CollectionUtils
            .emptyIfNull(request.getAbfragevariantenBaugenehmigungsverfahren())
            .forEach(abfragevariante -> {
                if (abfragevariante.getId() == null) {
                    final var mappedModel = abfragevarianteDomainMapper.request2Model(
                        abfragevariante,
                        new AbfragevarianteBaugenehmigungsverfahrenModel()
                    );
                    abfragevarianten.add(mappedModel);
                } else {
                    CollectionUtils
                        .emptyIfNull(model.getAbfragevariantenBaugenehmigungsverfahren())
                        .stream()
                        .filter(abfragevarianteModel -> abfragevarianteModel.getId().equals(abfragevariante.getId()))
                        .findFirst()
                        .ifPresent(abfragevarianteModel ->
                            abfragevarianten.add(
                                abfragevarianteDomainMapper.request2Model(abfragevariante, abfragevarianteModel)
                            )
                        );
                }
            });
        model.setAbfragevariantenBaugenehmigungsverfahren(abfragevarianten);
    }

    @Mappings(
        {
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "statusAbfrage", ignore = true),
            @Mapping(target = "sub", ignore = true),
            @Mapping(target = "createdDateTime", ignore = true),
            @Mapping(target = "lastModifiedDateTime", ignore = true),
            @Mapping(target = "abfragevariantenWeiteresVerfahren", ignore = true),
            @Mapping(target = "abfragevariantenSachbearbeitungWeiteresVerfahren", ignore = true),
        }
    )
    public abstract WeiteresVerfahrenModel request2Model(
        final WeiteresVerfahrenAngelegtModel request,
        @MappingTarget final WeiteresVerfahrenModel model
    );

    /**
     * Führt das Mapping der Abfragevarianten für die im Parameter gegebenen Klassen durch.
     *
     * @param request  das Request-Objekt welches gemapped werden soll
     * @param model das {@link WeiteresVerfahrenModel} zu dem es gemapped wird
     */
    @AfterMapping
    void afterMappingRequest2Model(
        final WeiteresVerfahrenAngelegtModel request,
        @MappingTarget final WeiteresVerfahrenModel model
    ) {
        final var abfragevarianten = new ArrayList<AbfragevarianteWeiteresVerfahrenModel>();
        CollectionUtils
            .emptyIfNull(request.getAbfragevariantenWeiteresVerfahren())
            .forEach(abfragevariante -> {
                if (abfragevariante.getId() == null) {
                    final var mappedModel = abfragevarianteDomainMapper.request2Model(
                        abfragevariante,
                        new AbfragevarianteWeiteresVerfahrenModel()
                    );
                    abfragevarianten.add(mappedModel);
                } else {
                    CollectionUtils
                        .emptyIfNull(model.getAbfragevariantenWeiteresVerfahren())
                        .stream()
                        .filter(abfragevarianteModel -> abfragevarianteModel.getId().equals(abfragevariante.getId()))
                        .findFirst()
                        .ifPresent(abfragevarianteModel ->
                            abfragevarianten.add(
                                abfragevarianteDomainMapper.request2Model(abfragevariante, abfragevarianteModel)
                            )
                        );
                }
            });
        model.setAbfragevariantenWeiteresVerfahren(abfragevarianten);
    }

    @BeanMapping(ignoreByDefault = true)
    @Mappings({ @Mapping(target = "version", ignore = false), @Mapping(target = "verortung", ignore = false) })
    public abstract BauleitplanverfahrenModel request2Model(
        final BauleitplanverfahrenInBearbeitungSachbearbeitungModel request,
        @MappingTarget final BauleitplanverfahrenModel response
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
            .emptyIfNull(request.getAbfragevariantenBauleitplanverfahren())
            .forEach(abfragevariante -> {
                CollectionUtils
                    .emptyIfNull(response.getAbfragevariantenBauleitplanverfahren())
                    .stream()
                    .filter(abfragevarianteModel -> abfragevarianteModel.getId().equals(abfragevariante.getId()))
                    .findFirst()
                    .ifPresent(abfragevarianteModel ->
                        mappedAbfragevarianten.add(
                            abfragevarianteDomainMapper.request2Model(abfragevariante, abfragevarianteModel)
                        )
                    );
            });
        response.setAbfragevariantenBauleitplanverfahren(mappedAbfragevarianten);
        // Mapping der Abfragevarianten welche ausschließlich durch die Sachbearbeitung gemappt werden.
        final var mappedAbfragevariantenSachbearbeitung = new ArrayList<AbfragevarianteBauleitplanverfahrenModel>();
        CollectionUtils
            .emptyIfNull(request.getAbfragevariantenSachbearbeitungBauleitplanverfahren())
            .forEach(abfragevariante -> {
                if (abfragevariante.getId() == null) {
                    final var mappedModel = abfragevarianteDomainMapper.request2Model(
                        abfragevariante,
                        new AbfragevarianteBauleitplanverfahrenModel()
                    );
                    mappedAbfragevariantenSachbearbeitung.add(mappedModel);
                } else {
                    CollectionUtils
                        .emptyIfNull(response.getAbfragevariantenSachbearbeitungBauleitplanverfahren())
                        .stream()
                        .filter(abfragevarianteModel -> abfragevarianteModel.getId().equals(abfragevariante.getId()))
                        .findFirst()
                        .ifPresent(abfragevarianteModel ->
                            mappedAbfragevariantenSachbearbeitung.add(
                                abfragevarianteDomainMapper.request2Model(abfragevariante, abfragevarianteModel)
                            )
                        );
                }
            });
        response.setAbfragevariantenSachbearbeitungBauleitplanverfahren(mappedAbfragevariantenSachbearbeitung);
    }

    @BeanMapping(ignoreByDefault = true)
    @Mappings({ @Mapping(target = "version", ignore = false), @Mapping(target = "verortung", ignore = false) })
    public abstract BaugenehmigungsverfahrenModel request2Model(
        final BaugenehmigungsverfahrenInBearbeitungSachbearbeitungModel request,
        @MappingTarget final BaugenehmigungsverfahrenModel response
    );

    /**
     * Führt das Mapping der Abfragevarianten für die im Parameter gegebenen Klassen durch.
     *
     * @param request  das Request-Objekt welches gemapped werden soll
     * @param response das {@link BaugenehmigungsverfahrenModel} zu dem es gemapped wird
     */
    @AfterMapping
    void afterMappingRequest2Model(
        final BaugenehmigungsverfahrenInBearbeitungSachbearbeitungModel request,
        final @MappingTarget BaugenehmigungsverfahrenModel response
    ) {
        // Mapping der zusätzlichen durch die Sachbearbeitung pflegbaren Attribute der Abfragevarianten
        final var mappedAbfragevarianten = new ArrayList<AbfragevarianteBaugenehmigungsverfahrenModel>();
        CollectionUtils
            .emptyIfNull(request.getAbfragevariantenBaugenehmigungsverfahren())
            .forEach(abfragevariante -> {
                CollectionUtils
                    .emptyIfNull(response.getAbfragevariantenBaugenehmigungsverfahren())
                    .stream()
                    .filter(abfragevarianteModel -> abfragevarianteModel.getId().equals(abfragevariante.getId()))
                    .findFirst()
                    .ifPresent(abfragevarianteModel ->
                        mappedAbfragevarianten.add(
                            abfragevarianteDomainMapper.request2Model(abfragevariante, abfragevarianteModel)
                        )
                    );
            });
        response.setAbfragevariantenBaugenehmigungsverfahren(mappedAbfragevarianten);
        // Mapping der Abfragevarianten welche ausschließlich durch die Sachbearbeitung gemappt werden.
        final var mappedAbfragevariantenSachbearbeitung = new ArrayList<AbfragevarianteBaugenehmigungsverfahrenModel>();
        CollectionUtils
            .emptyIfNull(request.getAbfragevariantenSachbearbeitungBaugenehmigungsverfahren())
            .forEach(abfragevariante -> {
                if (abfragevariante.getId() == null) {
                    final var mappedModel = abfragevarianteDomainMapper.request2Model(
                        abfragevariante,
                        new AbfragevarianteBaugenehmigungsverfahrenModel()
                    );
                    mappedAbfragevariantenSachbearbeitung.add(mappedModel);
                } else {
                    CollectionUtils
                        .emptyIfNull(response.getAbfragevariantenSachbearbeitungBaugenehmigungsverfahren())
                        .stream()
                        .filter(abfragevarianteModel -> abfragevarianteModel.getId().equals(abfragevariante.getId()))
                        .findFirst()
                        .ifPresent(abfragevarianteModel ->
                            mappedAbfragevariantenSachbearbeitung.add(
                                abfragevarianteDomainMapper.request2Model(abfragevariante, abfragevarianteModel)
                            )
                        );
                }
            });
        response.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(mappedAbfragevariantenSachbearbeitung);
    }

    @BeanMapping(ignoreByDefault = true)
    @Mappings({ @Mapping(target = "version", ignore = false), @Mapping(target = "verortung", ignore = false) })
    public abstract WeiteresVerfahrenModel request2Model(
        final WeiteresVerfahrenInBearbeitungSachbearbeitungModel request,
        @MappingTarget final WeiteresVerfahrenModel response
    );

    /**
     * Führt das Mapping der Abfragevarianten für die im Parameter gegebenen Klassen durch.
     *
     * @param request  das Request-Objekt welches gemapped werden soll
     * @param response das {@link WeiteresVerfahrenModel} zu dem es gemapped wird
     */
    @AfterMapping
    void afterMappingRequest2Model(
        final WeiteresVerfahrenInBearbeitungSachbearbeitungModel request,
        final @MappingTarget WeiteresVerfahrenModel response
    ) {
        // Mapping der zusätzlichen durch die Sachbearbeitung pflegbaren Attribute der Abfragevarianten
        final var mappedAbfragevarianten = new ArrayList<AbfragevarianteWeiteresVerfahrenModel>();
        CollectionUtils
            .emptyIfNull(request.getAbfragevariantenWeiteresVerfahren())
            .forEach(abfragevariante -> {
                CollectionUtils
                    .emptyIfNull(response.getAbfragevariantenWeiteresVerfahren())
                    .stream()
                    .filter(abfragevarianteModel -> abfragevarianteModel.getId().equals(abfragevariante.getId()))
                    .findFirst()
                    .ifPresent(abfragevarianteModel ->
                        mappedAbfragevarianten.add(
                            abfragevarianteDomainMapper.request2Model(abfragevariante, abfragevarianteModel)
                        )
                    );
            });
        response.setAbfragevariantenWeiteresVerfahren(mappedAbfragevarianten);
        // Mapping der Abfragevarianten welche ausschließlich durch die Sachbearbeitung gemappt werden.
        final var mappedAbfragevariantenSachbearbeitung = new ArrayList<AbfragevarianteWeiteresVerfahrenModel>();
        CollectionUtils
            .emptyIfNull(request.getAbfragevariantenSachbearbeitungWeiteresVerfahren())
            .forEach(abfragevariante -> {
                if (abfragevariante.getId() == null) {
                    final var mappedModel = abfragevarianteDomainMapper.request2Model(
                        abfragevariante,
                        new AbfragevarianteWeiteresVerfahrenModel()
                    );
                    mappedAbfragevariantenSachbearbeitung.add(mappedModel);
                } else {
                    CollectionUtils
                        .emptyIfNull(response.getAbfragevariantenSachbearbeitungWeiteresVerfahren())
                        .stream()
                        .filter(abfragevarianteModel -> abfragevarianteModel.getId().equals(abfragevariante.getId()))
                        .findFirst()
                        .ifPresent(abfragevarianteModel ->
                            mappedAbfragevariantenSachbearbeitung.add(
                                abfragevarianteDomainMapper.request2Model(abfragevariante, abfragevarianteModel)
                            )
                        );
                }
            });
        response.setAbfragevariantenSachbearbeitungWeiteresVerfahren(mappedAbfragevariantenSachbearbeitung);
    }

    @BeanMapping(ignoreByDefault = true)
    @Mappings({ @Mapping(target = "version", ignore = false) })
    public abstract BauleitplanverfahrenModel request2Model(
        final BauleitplanverfahrenInBearbeitungFachreferatModel request,
        @MappingTarget final BauleitplanverfahrenModel response
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
        @MappingTarget final BauleitplanverfahrenModel response
    ) {
        // Mapping der Bedarfsmeldungen durch die Fachabteilungen der Abfragevarianten
        final var mappedAbfragevarianten = new ArrayList<AbfragevarianteBauleitplanverfahrenModel>();
        CollectionUtils
            .emptyIfNull(request.getAbfragevariantenBauleitplanverfahren())
            .forEach(abfragevariante -> {
                CollectionUtils
                    .emptyIfNull(response.getAbfragevariantenBauleitplanverfahren())
                    .stream()
                    .filter(abfragevarianteModel -> abfragevarianteModel.getId().equals(abfragevariante.getId()))
                    .findFirst()
                    .ifPresent(abfragevarianteModel ->
                        mappedAbfragevarianten.add(
                            abfragevarianteDomainMapper.request2Model(abfragevariante, abfragevarianteModel)
                        )
                    );
            });
        response.setAbfragevariantenBauleitplanverfahren(mappedAbfragevarianten);
        // Mapping der Abfragevarianten welche ausschließlich durch die Sachbearbeitung gemappt werden.
        final var mappedAbfragevariantenSachbearbeitung = new ArrayList<AbfragevarianteBauleitplanverfahrenModel>();
        CollectionUtils
            .emptyIfNull(request.getAbfragevariantenSachbearbeitungBauleitplanverfahren())
            .forEach(abfragevariante -> {
                if (abfragevariante.getId() == null) {
                    final var mappedModel = abfragevarianteDomainMapper.request2Model(
                        abfragevariante,
                        new AbfragevarianteBauleitplanverfahrenModel()
                    );
                    mappedAbfragevariantenSachbearbeitung.add(mappedModel);
                } else {
                    CollectionUtils
                        .emptyIfNull(response.getAbfragevariantenSachbearbeitungBauleitplanverfahren())
                        .stream()
                        .filter(abfragevarianteModel -> abfragevarianteModel.getId().equals(abfragevariante.getId()))
                        .findFirst()
                        .ifPresent(abfragevarianteModel ->
                            mappedAbfragevariantenSachbearbeitung.add(
                                abfragevarianteDomainMapper.request2Model(abfragevariante, abfragevarianteModel)
                            )
                        );
                }
            });
        response.setAbfragevariantenSachbearbeitungBauleitplanverfahren(mappedAbfragevariantenSachbearbeitung);
    }

    @BeanMapping(ignoreByDefault = true)
    @Mappings({ @Mapping(target = "version", ignore = false) })
    public abstract BaugenehmigungsverfahrenModel request2Model(
        final BaugenehmigungsverfahrenInBearbeitungFachreferatModel request,
        @MappingTarget final BaugenehmigungsverfahrenModel response
    );

    /**
     * Führt das Mapping der Abfragevarianten für die im Parameter gegebenen Klassen durch.
     *
     * @param request  das Request-Objekt welches gemapped werden soll
     * @param response das {@link BaugenehmigungsverfahrenModel} zu dem es gemapped wird
     */
    @AfterMapping
    void afterMappingRequest2Model(
        final BaugenehmigungsverfahrenInBearbeitungFachreferatModel request,
        @MappingTarget final BaugenehmigungsverfahrenModel response
    ) {
        // Mapping der Bedarfsmeldungen durch die Fachabteilungen der Abfragevarianten
        final var mappedAbfragevarianten = new ArrayList<AbfragevarianteBaugenehmigungsverfahrenModel>();
        CollectionUtils
            .emptyIfNull(request.getAbfragevariantenBaugenehmigungsverfahren())
            .forEach(abfragevariante -> {
                CollectionUtils
                    .emptyIfNull(response.getAbfragevariantenBaugenehmigungsverfahren())
                    .stream()
                    .filter(abfragevarianteModel -> abfragevarianteModel.getId().equals(abfragevariante.getId()))
                    .findFirst()
                    .ifPresent(abfragevarianteModel ->
                        mappedAbfragevarianten.add(
                            abfragevarianteDomainMapper.request2Model(abfragevariante, abfragevarianteModel)
                        )
                    );
            });
        response.setAbfragevariantenBaugenehmigungsverfahren(mappedAbfragevarianten);
        // Mapping der Abfragevarianten welche ausschließlich durch die Sachbearbeitung gemappt werden.
        final var mappedAbfragevariantenSachbearbeitung = new ArrayList<AbfragevarianteBaugenehmigungsverfahrenModel>();
        CollectionUtils
            .emptyIfNull(request.getAbfragevariantenSachbearbeitungBaugenehmigungsverfahren())
            .forEach(abfragevariante -> {
                if (abfragevariante.getId() == null) {
                    final var mappedModel = abfragevarianteDomainMapper.request2Model(
                        abfragevariante,
                        new AbfragevarianteBaugenehmigungsverfahrenModel()
                    );
                    mappedAbfragevariantenSachbearbeitung.add(mappedModel);
                } else {
                    CollectionUtils
                        .emptyIfNull(response.getAbfragevariantenSachbearbeitungBaugenehmigungsverfahren())
                        .stream()
                        .filter(abfragevarianteModel -> abfragevarianteModel.getId().equals(abfragevariante.getId()))
                        .findFirst()
                        .ifPresent(abfragevarianteModel ->
                            mappedAbfragevariantenSachbearbeitung.add(
                                abfragevarianteDomainMapper.request2Model(abfragevariante, abfragevarianteModel)
                            )
                        );
                }
            });
        response.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(mappedAbfragevariantenSachbearbeitung);
    }

    @BeanMapping(ignoreByDefault = true)
    @Mappings({ @Mapping(target = "version", ignore = false) })
    public abstract WeiteresVerfahrenModel request2Model(
        final WeiteresVerfahrenInBearbeitungFachreferatModel request,
        @MappingTarget final WeiteresVerfahrenModel response
    );

    /**
     * Führt das Mapping der Abfragevarianten für die im Parameter gegebenen Klassen durch.
     *
     * @param request  das Request-Objekt welches gemapped werden soll
     * @param response das {@link WeiteresVerfahrenModel} zu dem es gemapped wird
     */
    @AfterMapping
    void afterMappingRequest2Model(
        final WeiteresVerfahrenInBearbeitungFachreferatModel request,
        @MappingTarget final WeiteresVerfahrenModel response
    ) {
        // Mapping der Bedarfsmeldungen durch die Fachabteilungen der Abfragevarianten
        final var mappedAbfragevarianten = new ArrayList<AbfragevarianteWeiteresVerfahrenModel>();
        CollectionUtils
            .emptyIfNull(request.getAbfragevariantenWeiteresVerfahren())
            .forEach(abfragevariante -> {
                CollectionUtils
                    .emptyIfNull(response.getAbfragevariantenWeiteresVerfahren())
                    .stream()
                    .filter(abfragevarianteModel -> abfragevarianteModel.getId().equals(abfragevariante.getId()))
                    .findFirst()
                    .ifPresent(abfragevarianteModel ->
                        mappedAbfragevarianten.add(
                            abfragevarianteDomainMapper.request2Model(abfragevariante, abfragevarianteModel)
                        )
                    );
            });
        response.setAbfragevariantenWeiteresVerfahren(mappedAbfragevarianten);
        // Mapping der Abfragevarianten welche ausschließlich durch die Sachbearbeitung gemappt werden.
        final var mappedAbfragevariantenSachbearbeitung = new ArrayList<AbfragevarianteWeiteresVerfahrenModel>();
        CollectionUtils
            .emptyIfNull(request.getAbfragevariantenSachbearbeitungWeiteresVerfahren())
            .forEach(abfragevariante -> {
                if (abfragevariante.getId() == null) {
                    final var mappedModel = abfragevarianteDomainMapper.request2Model(
                        abfragevariante,
                        new AbfragevarianteWeiteresVerfahrenModel()
                    );
                    mappedAbfragevariantenSachbearbeitung.add(mappedModel);
                } else {
                    CollectionUtils
                        .emptyIfNull(response.getAbfragevariantenSachbearbeitungWeiteresVerfahren())
                        .stream()
                        .filter(abfragevarianteModel -> abfragevarianteModel.getId().equals(abfragevariante.getId()))
                        .findFirst()
                        .ifPresent(abfragevarianteModel ->
                            mappedAbfragevariantenSachbearbeitung.add(
                                abfragevarianteDomainMapper.request2Model(abfragevariante, abfragevarianteModel)
                            )
                        );
                }
            });
        response.setAbfragevariantenSachbearbeitungWeiteresVerfahren(mappedAbfragevariantenSachbearbeitung);
    }
}
