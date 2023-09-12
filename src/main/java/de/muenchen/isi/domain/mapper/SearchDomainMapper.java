package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.BaseEntityModel;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.domain.model.search.response.AbfrageSearchResultModel;
import de.muenchen.isi.domain.model.search.response.BauvorhabenSearchResultModel;
import de.muenchen.isi.domain.model.search.response.InfrastruktureinrichtungSearchResultModel;
import de.muenchen.isi.domain.model.search.response.SearchResultModel;
import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.SubclassMapping;

@Mapper(
    config = MapstructConfiguration.class,
    uses = { AbfrageDomainMapper.class, InfrastruktureinrichtungDomainMapper.class, BauvorhabenDomainMapper.class }
)
public interface SearchDomainMapper {
    @SubclassMapping(source = Infrastruktureinrichtung.class, target = InfrastruktureinrichtungModel.class)
    @SubclassMapping(source = Bauvorhaben.class, target = BauvorhabenModel.class)
    @SubclassMapping(source = Infrastrukturabfrage.class, target = InfrastrukturabfrageModel.class)
    BaseEntityModel entity2Model(final BaseEntity entity);

    @Mapping(target = "type", ignore = true)
    @SubclassMapping(
        source = InfrastruktureinrichtungModel.class,
        target = InfrastruktureinrichtungSearchResultModel.class
    )
    @SubclassMapping(source = BauvorhabenModel.class, target = BauvorhabenSearchResultModel.class)
    @SubclassMapping(source = InfrastrukturabfrageModel.class, target = AbfrageSearchResultModel.class)
    SearchResultModel model2SearchResultModel(final BaseEntityModel entity);
}
