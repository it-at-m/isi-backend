package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.search.SuchwortModel;
import de.muenchen.isi.infrastructure.entity.search.Suchwort;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class, uses = { DokumentDomainMapper.class })
public interface SearchDomainMapper {
    SuchwortModel entity2Model(Suchwort entity);
}
