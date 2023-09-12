package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.search.response.BauvorhabenSearchResultModel;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = MapstructConfiguration.class, uses = { DokumentDomainMapper.class })
public interface BauvorhabenDomainMapper {
    BauvorhabenModel entity2Model(final Bauvorhaben bauvorhaben);

    Bauvorhaben model2Entity(final BauvorhabenModel bauvorhabenModel);

    @Mappings(
        {
            @Mapping(target = "type", constant = "BAUVORHABEN"),
            @Mapping(source = "verortung.stadtbezirke", target = "stadtbezirke"),
        }
    )
    BauvorhabenSearchResultModel model2ListElementModel(final BauvorhabenModel model);
}
