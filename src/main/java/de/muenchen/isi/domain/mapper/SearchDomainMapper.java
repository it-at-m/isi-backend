package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.enums.SearchResultType;
import de.muenchen.isi.domain.model.search.response.AbfrageSearchResultModel;
import de.muenchen.isi.domain.model.search.response.BauvorhabenSearchResultModel;
import de.muenchen.isi.domain.model.search.response.InfrastruktureinrichtungSearchResultModel;
import de.muenchen.isi.domain.model.search.response.SearchResultModel;
import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.Baugenehmigungsverfahren;
import de.muenchen.isi.infrastructure.entity.Bauleitplanverfahren;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import java.util.UUID;
import org.apache.commons.lang3.ObjectUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.SubclassMapping;

@Mapper(
    config = MapstructConfiguration.class,
    uses = { AbfrageDomainMapper.class, InfrastruktureinrichtungDomainMapper.class, BauvorhabenDomainMapper.class }
)
public interface SearchDomainMapper {
    @Mapping(target = "type", ignore = true)
    @SubclassMapping(source = Infrastruktureinrichtung.class, target = InfrastruktureinrichtungSearchResultModel.class)
    @SubclassMapping(source = Bauvorhaben.class, target = BauvorhabenSearchResultModel.class)
    @SubclassMapping(source = Bauleitplanverfahren.class, target = AbfrageSearchResultModel.class)
    @SubclassMapping(source = Baugenehmigungsverfahren.class, target = AbfrageSearchResultModel.class)
    SearchResultModel entity2SearchResultModel(final BaseEntity entity);

    @Mappings(
        {
            @Mapping(target = "type", constant = SearchResultType.Values.BAUVORHABEN),
            @Mapping(source = "verortung.stadtbezirke", target = "stadtbezirke"),
        }
    )
    BauvorhabenSearchResultModel entity2SearchResultModel(final Bauvorhaben entity);

    @Mappings(
        {
            @Mapping(target = "type", constant = SearchResultType.Values.ABFRAGE),
            @Mapping(source = "verortung.stadtbezirke", target = "stadtbezirke"),
        }
    )
    AbfrageSearchResultModel entity2SearchResultModel(final Bauleitplanverfahren entity);

    @Mappings(
        {
            @Mapping(target = "type", constant = SearchResultType.Values.ABFRAGE),
            @Mapping(source = "verortung.stadtbezirke", target = "stadtbezirke"),
        }
    )
    AbfrageSearchResultModel entity2SearchResultModel(final Baugenehmigungsverfahren entity);

    @Mappings({ @Mapping(target = "type", constant = SearchResultType.Values.INFRASTRUKTUREINRICHTUNG) })
    InfrastruktureinrichtungSearchResultModel entity2SearchResultModel(final Infrastruktureinrichtung entity);

    default UUID map(final Bauvorhaben bauvorhaben) {
        return ObjectUtils.isEmpty(bauvorhaben) ? null : bauvorhaben.getId();
    }
}
