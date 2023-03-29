package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.common.UtmModel;
import de.muenchen.isi.domain.model.common.WGS84Model;
import de.muenchen.isi.infrastructure.entity.common.Utm;
import de.muenchen.isi.infrastructure.entity.common.Wgs84;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface KoordinatenDomainMapper {
    WGS84Model entity2Model(Wgs84 entity);

    Wgs84 model2Entity(WGS84Model model);

    UtmModel entity2Model(Utm entity);

    Utm model2Entity(UtmModel model);
}
