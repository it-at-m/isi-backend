/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.GrundschuleModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.GsNachmittagBetreuungModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.HausFuerKinderModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.KindergartenModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.KinderkrippeModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.MittelschuleModel;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Grundschule;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.GsNachmittagBetreuung;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.HausFuerKinder;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kindergarten;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kinderkrippe;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Mittelschule;
import de.muenchen.isi.infrastructure.repository.BauvorhabenRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.SubclassMapping;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Mapper(config = MapstructConfiguration.class, uses = { BauvorhabenDomainMapper.class })
public abstract class InfrastruktureinrichtungDomainMapper {

    @Autowired
    private BauvorhabenRepository bauvorhabenRepository;

    @Mapping(source = "bauvorhaben.id", target = "bauvorhaben")
    @SubclassMapping(source = Grundschule.class, target = GrundschuleModel.class)
    @SubclassMapping(source = GsNachmittagBetreuung.class, target = GsNachmittagBetreuungModel.class)
    @SubclassMapping(source = HausFuerKinder.class, target = HausFuerKinderModel.class)
    @SubclassMapping(source = Kindergarten.class, target = KindergartenModel.class)
    @SubclassMapping(source = Kinderkrippe.class, target = KinderkrippeModel.class)
    @SubclassMapping(source = Mittelschule.class, target = MittelschuleModel.class)
    public abstract InfrastruktureinrichtungModel entity2Model(final Infrastruktureinrichtung entity);

    @Mapping(target = "bauvorhaben", ignore = true)
    @SubclassMapping(source = GrundschuleModel.class, target = Grundschule.class)
    @SubclassMapping(source = GsNachmittagBetreuungModel.class, target = GsNachmittagBetreuung.class)
    @SubclassMapping(source = HausFuerKinderModel.class, target = HausFuerKinder.class)
    @SubclassMapping(source = KindergartenModel.class, target = Kindergarten.class)
    @SubclassMapping(source = KinderkrippeModel.class, target = Kinderkrippe.class)
    @SubclassMapping(source = MittelschuleModel.class, target = Mittelschule.class)
    public abstract Infrastruktureinrichtung model2Entity(final InfrastruktureinrichtungModel model)
        throws EntityNotFoundException;

    @AfterMapping
    void model2EntityAfterMapping(
        final InfrastruktureinrichtungModel model,
        @MappingTarget Infrastruktureinrichtung entity
    ) throws EntityNotFoundException {
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
}
