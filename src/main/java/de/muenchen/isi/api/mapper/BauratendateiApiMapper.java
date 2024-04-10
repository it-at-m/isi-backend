package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.bauratendatei.BauratendateiInputDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.bauratendatei.BauratendateiInputModel;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface BauratendateiApiMapper {
    BauratendateiInputModel dto2Model(final BauratendateiInputDto dto);

    List<BauratendateiInputModel> dto2Model(final List<BauratendateiInputDto> dto);
}
