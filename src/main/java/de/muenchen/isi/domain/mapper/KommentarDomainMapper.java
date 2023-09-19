package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.common.KommentarModel;
import de.muenchen.isi.infrastructure.entity.common.Kommentar;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class, uses = { InfrastruktureinrichtungDomainMapper.class })
public interface KommentarDomainMapper {
    KommentarModel entity2Model(final Kommentar entity);

    Kommentar model2Entity(final KommentarModel model);
}
