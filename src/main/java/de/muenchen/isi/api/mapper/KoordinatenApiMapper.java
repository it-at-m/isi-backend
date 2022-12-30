package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.common.UtmDto;
import de.muenchen.isi.api.dto.common.Wgs84Dto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.common.UtmModel;
import de.muenchen.isi.domain.model.common.WGS84Model;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface KoordinatenApiMapper {


    UtmDto model2Dto(UtmModel model);

    UtmModel dto2Model(UtmDto dto);

    Wgs84Dto model2Dto(WGS84Model model);

    WGS84Model dto2Model(Wgs84Dto dto);

}
