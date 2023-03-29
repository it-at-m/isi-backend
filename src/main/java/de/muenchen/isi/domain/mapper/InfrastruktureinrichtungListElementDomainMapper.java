/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.enums.InfrastruktureinrichtungTyp;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.GrundschuleModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.GsNachmittagBetreuungModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.HausFuerKinderModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.KindergartenModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.KinderkrippeModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.MittelschuleModel;
import de.muenchen.isi.domain.model.list.InfrastruktureinrichtungListElementModel;
import org.mapstruct.*;

@Mapper(config = MapstructConfiguration.class)
public interface InfrastruktureinrichtungListElementDomainMapper {
    /*
     * Kinderkrippe Mapping
     */
    @Mappings(
        {
            @Mapping(target = "infrastruktureinrichtungTyp", ignore = true),
            @Mapping(source = "infrastruktureinrichtung.nameEinrichtung", target = "nameEinrichtung"),
        }
    )
    InfrastruktureinrichtungListElementModel kinderkrippeModel2InfrastruktureinrichtungListElementModel(
        final KinderkrippeModel model
    );

    @AfterMapping
    default void kinderkrippeModel2InfrastruktureinrichtungListElementModelAfterMapping(
        @MappingTarget final InfrastruktureinrichtungListElementModel infrastruktureinrichtungListElementModel,
        final KinderkrippeModel model
    ) {
        InfrastruktureinrichtungTyp
            .findByClazz(model.getClass())
            .ifPresent(infrastruktureinrichtungListElementModel::setInfrastruktureinrichtungTyp);
    }

    /*
     * Kindergarten Mapping
     */
    @Mappings(
        {
            @Mapping(target = "infrastruktureinrichtungTyp", ignore = true),
            @Mapping(source = "infrastruktureinrichtung.nameEinrichtung", target = "nameEinrichtung"),
        }
    )
    InfrastruktureinrichtungListElementModel kindergartenModel2InfrastruktureinrichtungListElementModel(
        final KindergartenModel model
    );

    @AfterMapping
    default void kindergartenModel2InfrastruktureinrichtungListElementModelAfterMapping(
        @MappingTarget final InfrastruktureinrichtungListElementModel infrastruktureinrichtungListElementModel,
        final KindergartenModel model
    ) {
        InfrastruktureinrichtungTyp
            .findByClazz(model.getClass())
            .ifPresent(infrastruktureinrichtungListElementModel::setInfrastruktureinrichtungTyp);
    }

    /*
     * Haus für Kinder Mapping
     */
    @Mappings(
        {
            @Mapping(target = "infrastruktureinrichtungTyp", ignore = true),
            @Mapping(source = "infrastruktureinrichtung.nameEinrichtung", target = "nameEinrichtung"),
        }
    )
    InfrastruktureinrichtungListElementModel hausFuerKinderModel2InfrastruktureinrichtungListElementModel(
        final HausFuerKinderModel model
    );

    @AfterMapping
    default void hausFuerKinderModel2InfrastruktureinrichtungListElementModelAfterMapping(
        @MappingTarget final InfrastruktureinrichtungListElementModel infrastruktureinrichtungListElementModel,
        final HausFuerKinderModel model
    ) {
        InfrastruktureinrichtungTyp
            .findByClazz(model.getClass())
            .ifPresent(infrastruktureinrichtungListElementModel::setInfrastruktureinrichtungTyp);
    }

    /*
     * Nachmittagsbetreuung für Grundschulkinder Mapping
     */
    @Mappings(
        {
            @Mapping(target = "infrastruktureinrichtungTyp", ignore = true),
            @Mapping(source = "infrastruktureinrichtung.nameEinrichtung", target = "nameEinrichtung"),
        }
    )
    InfrastruktureinrichtungListElementModel gsNachmittagBetreuungModel2InfrastruktureinrichtungListElementModel(
        final GsNachmittagBetreuungModel model
    );

    @AfterMapping
    default void gsNachmittagBetreuungModel2InfrastruktureinrichtungListElementModelAfterMapping(
        @MappingTarget final InfrastruktureinrichtungListElementModel infrastruktureinrichtungListElementModel,
        final GsNachmittagBetreuungModel model
    ) {
        InfrastruktureinrichtungTyp
            .findByClazz(model.getClass())
            .ifPresent(infrastruktureinrichtungListElementModel::setInfrastruktureinrichtungTyp);
    }

    /*
     * Grundschule Mapping
     */
    @Mappings(
        {
            @Mapping(target = "infrastruktureinrichtungTyp", ignore = true),
            @Mapping(source = "infrastruktureinrichtung.nameEinrichtung", target = "nameEinrichtung"),
        }
    )
    InfrastruktureinrichtungListElementModel grundschuleModel2InfrastruktureinrichtungListElementModel(
        final GrundschuleModel model
    );

    @AfterMapping
    default void grundschuleModel2InfrastruktureinrichtungListElementModelAfterMapping(
        @MappingTarget final InfrastruktureinrichtungListElementModel infrastruktureinrichtungListElementModel,
        final GrundschuleModel model
    ) {
        InfrastruktureinrichtungTyp
            .findByClazz(model.getClass())
            .ifPresent(infrastruktureinrichtungListElementModel::setInfrastruktureinrichtungTyp);
    }

    /*
     * Mittelschule Mapping
     */
    @Mappings(
        {
            @Mapping(target = "infrastruktureinrichtungTyp", ignore = true),
            @Mapping(source = "infrastruktureinrichtung.nameEinrichtung", target = "nameEinrichtung"),
        }
    )
    InfrastruktureinrichtungListElementModel mittelschuleModel2InfrastruktureinrichtungListElementModel(
        final MittelschuleModel model
    );

    @AfterMapping
    default void mittelschuleModel2InfrastruktureinrichtungListElementModelAfterMapping(
        @MappingTarget final InfrastruktureinrichtungListElementModel infrastruktureinrichtungListElementModel,
        final MittelschuleModel model
    ) {
        InfrastruktureinrichtungTyp
            .findByClazz(model.getClass())
            .ifPresent(infrastruktureinrichtungListElementModel::setInfrastruktureinrichtungTyp);
    }
}
