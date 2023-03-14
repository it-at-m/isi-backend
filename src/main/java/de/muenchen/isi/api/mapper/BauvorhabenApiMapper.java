package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.BauvorhabenDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class, uses = { DokumentApiMapper.class })
public interface BauvorhabenApiMapper {
    BauvorhabenDto model2Dto(final BauvorhabenModel model);

    BauvorhabenModel dto2Model(final BauvorhabenDto dto);
}
