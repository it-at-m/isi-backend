package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.BaseEntityModel;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.domain.model.search.SuchwortModel;
import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.search.Suchwort;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassMapping;

@Mapper(
    config = MapstructConfiguration.class,
    uses = { AbfrageDomainMapper.class, InfrastruktureinrichtungDomainMapper.class, BauvorhabenDomainMapper.class }
)
public interface SearchDomainMapper {
    SuchwortModel entity2Model(Suchwort entity);

    @SubclassMapping(source = Infrastruktureinrichtung.class, target = InfrastruktureinrichtungModel.class)
    @SubclassMapping(source = Bauvorhaben.class, target = BauvorhabenModel.class)
    @SubclassMapping(source = Infrastrukturabfrage.class, target = InfrastrukturabfrageModel.class)
    BaseEntityModel entity2Model(BaseEntity entity);
}
