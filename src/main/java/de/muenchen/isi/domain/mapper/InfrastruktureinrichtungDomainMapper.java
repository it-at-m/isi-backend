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
import de.muenchen.isi.infrastructure.entity.Abfragevariante;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Grundschule;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.GsNachmittagBetreuung;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.HausFuerKinder;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kindergarten;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kinderkrippe;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Mittelschule;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassMapping;

@Mapper(config = MapstructConfiguration.class, uses = { BauvorhabenDomainMapper.class })
public interface InfrastruktureinrichtungDomainMapper {
    @SubclassMapping(source = Grundschule.class, target = GrundschuleModel.class)
    @SubclassMapping(source = GsNachmittagBetreuung.class, target = GsNachmittagBetreuungModel.class)
    @SubclassMapping(source = HausFuerKinder.class, target = HausFuerKinderModel.class)
    @SubclassMapping(source = Kindergarten.class, target = KindergartenModel.class)
    @SubclassMapping(source = Kinderkrippe.class, target = KinderkrippeModel.class)
    @SubclassMapping(source = Mittelschule.class, target = MittelschuleModel.class)
    InfrastruktureinrichtungModel entity2Model(final Infrastruktureinrichtung entity);

    @SubclassMapping(source = GrundschuleModel.class, target = Grundschule.class)
    @SubclassMapping(source = GsNachmittagBetreuungModel.class, target = GsNachmittagBetreuung.class)
    @SubclassMapping(source = HausFuerKinderModel.class, target = HausFuerKinder.class)
    @SubclassMapping(source = KindergartenModel.class, target = Kindergarten.class)
    @SubclassMapping(source = KinderkrippeModel.class, target = Kinderkrippe.class)
    @SubclassMapping(source = MittelschuleModel.class, target = Mittelschule.class)
    Infrastruktureinrichtung model2Entity(final InfrastruktureinrichtungModel model) throws EntityNotFoundException;

    default UUID map(final Abfragevariante abfragevariante) {
        return abfragevariante.getId();
    }
}
