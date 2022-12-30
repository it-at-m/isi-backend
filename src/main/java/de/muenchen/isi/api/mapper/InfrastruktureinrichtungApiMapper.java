/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.infrastruktureinrichtung.GrundschuleDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.GsNachmittagBetreuungDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.HausFuerKinderDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.KindergartenDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.KinderkrippeDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.MittelschuleDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.BaugebietModel;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.GrundschuleModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.GsNachmittagBetreuungModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.HausFuerKinderModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.KindergartenModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.KinderkrippeModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.MittelschuleModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapstructConfiguration.class)
public interface InfrastruktureinrichtungApiMapper {

    /*
     * Mapping für Kinderkrippe
     */
    @Mapping(target = "infrastruktureinrichtung.bauvorhaben", ignore = true)
    @Mapping(target = "infrastruktureinrichtung.zugeordnetesBaugebiet", ignore = true)
    KinderkrippeDto model2Dto(final KinderkrippeModel model);

    @AfterMapping
    default void setBauvorhabenIdOnKinderkrippeDto(final KinderkrippeModel model, @MappingTarget final KinderkrippeDto dto) {
        final BauvorhabenModel bauvorhabenModel = model.getInfrastruktureinrichtung().getBauvorhaben();
        dto.getInfrastruktureinrichtung().setBauvorhaben(bauvorhabenModel != null ? bauvorhabenModel.getId() : null);
    }

    @AfterMapping
    default void setBaugebietIdOnKinderkrippeDto(final KinderkrippeModel model, @MappingTarget final KinderkrippeDto dto) {
        final BaugebietModel baugebietModel = model.getInfrastruktureinrichtung().getZugeordnetesBaugebiet();
        dto.getInfrastruktureinrichtung().setZugeordnetesBaugebiet(baugebietModel != null ? baugebietModel.getId() : null);
    }

    @Mapping(target = "infrastruktureinrichtung.bauvorhaben", ignore = true)
    @Mapping(target = "infrastruktureinrichtung.zugeordnetesBaugebiet", ignore = true)
    KinderkrippeModel dto2Model(final KinderkrippeDto dto);

    /*
     * Mapping für Kindergarten
     */
    @Mapping(target = "infrastruktureinrichtung.bauvorhaben", ignore = true)
    @Mapping(target = "infrastruktureinrichtung.zugeordnetesBaugebiet", ignore = true)
    KindergartenDto model2Dto(final KindergartenModel model);

    @AfterMapping
    default void setBauvorhabenIdOnKindergartenDto(final KindergartenModel model, @MappingTarget final KindergartenDto dto) {
        final BauvorhabenModel bauvorhabenModel = model.getInfrastruktureinrichtung().getBauvorhaben();
        dto.getInfrastruktureinrichtung().setBauvorhaben(bauvorhabenModel != null ? bauvorhabenModel.getId() : null);
    }

    @AfterMapping
    default void setBaugebietIdOnKindergartenDto(final KindergartenModel model, @MappingTarget final KindergartenDto dto) {
        final BaugebietModel baugebietModel = model.getInfrastruktureinrichtung().getZugeordnetesBaugebiet();
        dto.getInfrastruktureinrichtung().setZugeordnetesBaugebiet(baugebietModel != null ? baugebietModel.getId() : null);
    }

    @Mapping(target = "infrastruktureinrichtung.bauvorhaben", ignore = true)
    @Mapping(target = "infrastruktureinrichtung.zugeordnetesBaugebiet", ignore = true)
    KindergartenModel dto2Model(final KindergartenDto dto);

    /*
     * Mapping für Haus für Kinder
     */
    @Mapping(target = "infrastruktureinrichtung.bauvorhaben", ignore = true)
    @Mapping(target = "infrastruktureinrichtung.zugeordnetesBaugebiet", ignore = true)
    HausFuerKinderDto model2Dto(final HausFuerKinderModel model);

    @AfterMapping
    default void setBauvorhabenIdOnHausFuerKinderDto(final HausFuerKinderModel model, @MappingTarget final HausFuerKinderDto dto) {
        final BauvorhabenModel bauvorhabenModel = model.getInfrastruktureinrichtung().getBauvorhaben();
        dto.getInfrastruktureinrichtung().setBauvorhaben(bauvorhabenModel != null ? bauvorhabenModel.getId() : null);
    }

    @AfterMapping
    default void setBaugebietIdOnHausFuerKinderDto(final HausFuerKinderModel model, @MappingTarget final HausFuerKinderDto dto) {
        final BaugebietModel baugebietModel = model.getInfrastruktureinrichtung().getZugeordnetesBaugebiet();
        dto.getInfrastruktureinrichtung().setZugeordnetesBaugebiet(baugebietModel != null ? baugebietModel.getId() : null);
    }

    @Mapping(target = "infrastruktureinrichtung.bauvorhaben", ignore = true)
    @Mapping(target = "infrastruktureinrichtung.zugeordnetesBaugebiet", ignore = true)
    HausFuerKinderModel dto2Model(final HausFuerKinderDto dto);

    /*
     * Mapping für Ganztägige Betreuung von Grundschulkindern (GsNachmittagBetreuung)
     */
    @Mapping(target = "infrastruktureinrichtung.bauvorhaben", ignore = true)
    @Mapping(target = "infrastruktureinrichtung.zugeordnetesBaugebiet", ignore = true)
    GsNachmittagBetreuungDto model2Dto(final GsNachmittagBetreuungModel model);

    @AfterMapping
    default void setBauvorhabenIdOnGsNachmittagBetreuungDto(final GsNachmittagBetreuungModel model, @MappingTarget final GsNachmittagBetreuungDto dto) {
        final BauvorhabenModel bauvorhabenModel = model.getInfrastruktureinrichtung().getBauvorhaben();
        dto.getInfrastruktureinrichtung().setBauvorhaben(bauvorhabenModel != null ? bauvorhabenModel.getId() : null);
    }

    @AfterMapping
    default void setBaugebietIdOnGsNachmittagBetreuungDto(final GsNachmittagBetreuungModel model, @MappingTarget final GsNachmittagBetreuungDto dto) {
        final BaugebietModel baugebietModel = model.getInfrastruktureinrichtung().getZugeordnetesBaugebiet();
        dto.getInfrastruktureinrichtung().setZugeordnetesBaugebiet(baugebietModel != null ? baugebietModel.getId() : null);
    }

    @Mapping(target = "infrastruktureinrichtung.bauvorhaben", ignore = true)
    @Mapping(target = "infrastruktureinrichtung.zugeordnetesBaugebiet", ignore = true)
    GsNachmittagBetreuungModel dto2Model(final GsNachmittagBetreuungDto dto);

    /*
     * Mapping für Grundschule
     */
    @Mapping(target = "infrastruktureinrichtung.bauvorhaben", ignore = true)
    @Mapping(target = "infrastruktureinrichtung.zugeordnetesBaugebiet", ignore = true)
    GrundschuleDto model2Dto(final GrundschuleModel model);

    @AfterMapping
    default void setBauvorhabenIdOnGrundschuleDto(final GrundschuleModel model, @MappingTarget final GrundschuleDto dto) {
        final BauvorhabenModel bauvorhabenModel = model.getInfrastruktureinrichtung().getBauvorhaben();
        dto.getInfrastruktureinrichtung().setBauvorhaben(bauvorhabenModel != null ? bauvorhabenModel.getId() : null);
    }

    @AfterMapping
    default void setBaugebietIdOnGrundschuleDto(final GrundschuleModel model, @MappingTarget final GrundschuleDto dto) {
        final BaugebietModel baugebietModel = model.getInfrastruktureinrichtung().getZugeordnetesBaugebiet();
        dto.getInfrastruktureinrichtung().setZugeordnetesBaugebiet(baugebietModel != null ? baugebietModel.getId() : null);
    }

    @Mapping(target = "infrastruktureinrichtung.bauvorhaben", ignore = true)
    @Mapping(target = "infrastruktureinrichtung.zugeordnetesBaugebiet", ignore = true)
    GrundschuleModel dto2Model(final GrundschuleDto dto);

    /*
     * Mapping für Mittelschule
     */
    @Mapping(target = "infrastruktureinrichtung.bauvorhaben", ignore = true)
    @Mapping(target = "infrastruktureinrichtung.zugeordnetesBaugebiet", ignore = true)
    MittelschuleDto model2Dto(final MittelschuleModel model);

    @AfterMapping
    default void setBauvorhabenIdOnMittelschuleDto(final MittelschuleModel model, @MappingTarget final MittelschuleDto dto) {
        final BauvorhabenModel bauvorhabenModel = model.getInfrastruktureinrichtung().getBauvorhaben();
        dto.getInfrastruktureinrichtung().setBauvorhaben(bauvorhabenModel != null ? bauvorhabenModel.getId() : null);
    }

    @AfterMapping
    default void setBaugebietIdOnMittelschuleDto(final MittelschuleModel model, @MappingTarget final MittelschuleDto dto) {
        final BaugebietModel baugebietModel = model.getInfrastruktureinrichtung().getZugeordnetesBaugebiet();
        dto.getInfrastruktureinrichtung().setZugeordnetesBaugebiet(baugebietModel != null ? baugebietModel.getId() : null);
    }

    @Mapping(target = "infrastruktureinrichtung.bauvorhaben", ignore = true)
    @Mapping(target = "infrastruktureinrichtung.zugeordnetesBaugebiet", ignore = true)
    MittelschuleModel dto2Model(final MittelschuleDto dto);

}
