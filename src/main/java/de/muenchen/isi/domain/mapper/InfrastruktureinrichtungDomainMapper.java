/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.GrundschuleModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.GsNachmittagBetreuungModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.HausFuerKinderModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.KindergartenModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.KinderkrippeModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.MittelschuleModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.SchuleModel;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Grundschule;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.GsNachmittagBetreuung;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.HausFuerKinder;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kindergarten;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kinderkrippe;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Mittelschule;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Schule;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface InfrastruktureinrichtungDomainMapper {

    InfrastruktureinrichtungModel entity2Model(final Infrastruktureinrichtung entity);

    Infrastruktureinrichtung model2Entity(final InfrastruktureinrichtungModel model);

    SchuleModel entity2Model(final Schule entity);

    Schule model2Entity(final SchuleModel model);

    KinderkrippeModel entity2Model(final Kinderkrippe entity);

    Kinderkrippe model2Entity(final KinderkrippeModel model);

    KindergartenModel entity2Model(final Kindergarten entity);

    Kindergarten model2Entity(final KindergartenModel model);

    HausFuerKinderModel entity2Model(final HausFuerKinder entity);

    HausFuerKinder model2Entity(final HausFuerKinderModel model);

    GsNachmittagBetreuungModel entity2Model(final GsNachmittagBetreuung entity);

    GsNachmittagBetreuung model2Entity(final GsNachmittagBetreuungModel model);

    GrundschuleModel entity2Model(final Grundschule entity);

    Grundschule model2Entity(final GrundschuleModel model);

    MittelschuleModel entity2Model(final Mittelschule entity);

    Mittelschule model2Entity(final MittelschuleModel model);

}


