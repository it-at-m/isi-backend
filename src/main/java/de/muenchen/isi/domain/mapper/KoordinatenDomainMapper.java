package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.common.UtmModel;
import de.muenchen.isi.domain.model.common.WGS84Model;
import de.muenchen.isi.infrastructure.entity.common.Utm;
import de.muenchen.isi.infrastructure.entity.common.Wgs84;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface KoordinatenDomainMapper {
    WGS84Model entity2Model(final Wgs84 entity);

    Wgs84 model2Entity(final WGS84Model model);

    UtmModel entity2Model(final Utm entity);

    Utm model2Entity(final UtmModel model);
}
