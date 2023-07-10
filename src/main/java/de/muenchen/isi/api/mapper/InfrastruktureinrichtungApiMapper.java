/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.infrastruktureinrichtung.GrundschuleDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.GsNachmittagBetreuungDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.HausFuerKinderDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.InfrastruktureinrichtungDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.KindergartenDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.KinderkrippeDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.MittelschuleDto;
import de.muenchen.isi.api.dto.list.InfrastruktureinrichtungListElementsDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.BaugebietModel;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.GrundschuleModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.GsNachmittagBetreuungModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.HausFuerKinderModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.KindergartenModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.KinderkrippeModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.MittelschuleModel;
import de.muenchen.isi.domain.model.list.InfrastruktureinrichtungListElementsModel;
import org.apache.commons.lang3.ObjectUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.SubclassMapping;

@Mapper(config = MapstructConfiguration.class)
public interface InfrastruktureinrichtungApiMapper {
    @Mapping(target = "zugeordnetesBaugebiet", ignore = true)
    @Mapping(target = "bauvorhaben", ignore = true)
    @SubclassMapping(source = GrundschuleDto.class, target = GrundschuleModel.class)
    @SubclassMapping(source = GsNachmittagBetreuungDto.class, target = GsNachmittagBetreuungModel.class)
    @SubclassMapping(source = HausFuerKinderDto.class, target = HausFuerKinderModel.class)
    @SubclassMapping(source = KindergartenDto.class, target = KindergartenModel.class)
    @SubclassMapping(source = KinderkrippeDto.class, target = KinderkrippeModel.class)
    @SubclassMapping(source = MittelschuleDto.class, target = MittelschuleModel.class)
    InfrastruktureinrichtungModel dto2Model(final InfrastruktureinrichtungDto dto);

    @Mapping(target = "zugeordnetesBaugebiet", ignore = true)
    @Mapping(target = "bauvorhaben", ignore = true)
    @SubclassMapping(source = GrundschuleModel.class, target = GrundschuleDto.class)
    @SubclassMapping(source = GsNachmittagBetreuungModel.class, target = GsNachmittagBetreuungDto.class)
    @SubclassMapping(source = HausFuerKinderModel.class, target = HausFuerKinderDto.class)
    @SubclassMapping(source = KindergartenModel.class, target = KindergartenDto.class)
    @SubclassMapping(source = KinderkrippeModel.class, target = KinderkrippeDto.class)
    @SubclassMapping(source = MittelschuleModel.class, target = MittelschuleDto.class)
    InfrastruktureinrichtungDto model2Dto(final InfrastruktureinrichtungModel model);

    @AfterMapping
    default void setBauvorhabenIdOnInfrastruktureinrichtungDto(
        final InfrastruktureinrichtungModel model,
        @MappingTarget final InfrastruktureinrichtungDto dto
    ) {
        final BauvorhabenModel bauvorhabenModel = model.getBauvorhaben();
        dto.setBauvorhaben(ObjectUtils.isNotEmpty(bauvorhabenModel) ? bauvorhabenModel.getId() : null);
    }

    @AfterMapping
    default void setBaugebietIdOnInfrastruktureinrichtungDto(
        final InfrastruktureinrichtungModel model,
        @MappingTarget final InfrastruktureinrichtungDto dto
    ) {
        final BaugebietModel baugebietModel = model.getZugeordnetesBaugebiet();
        dto.setZugeordnetesBaugebiet(ObjectUtils.isNotEmpty(baugebietModel) ? baugebietModel.getId() : null);
    }

    InfrastruktureinrichtungListElementsDto model2Dto(final InfrastruktureinrichtungListElementsModel model);
}
