package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.BauvorhabenDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    config = MapstructConfiguration.class,
    uses = { DokumentApiMapper.class, InfrastruktureinrichtungApiMapper.class }
)
public interface BauvorhabenApiMapper {
    BauvorhabenDto model2Dto(final BauvorhabenModel model);

    @Mapping(target = "sub", ignore = true)
    BauvorhabenModel dto2Model(final BauvorhabenDto dto);
}
