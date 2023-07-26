package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.BauvorhabenDto;
import de.muenchen.isi.api.dto.list.BauvorhabenReferencedElementsDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.list.BauvorhabenReferencedElementsModel;
import org.mapstruct.Mapper;

@Mapper(
    config = MapstructConfiguration.class,
    uses = { DokumentApiMapper.class, AbfrageApiMapper.class, InfrastruktureinrichtungApiMapper.class }
)
public interface BauvorhabenApiMapper {
    BauvorhabenDto model2Dto(final BauvorhabenModel model);

    BauvorhabenModel dto2Model(final BauvorhabenDto dto);

    BauvorhabenReferencedElementsDto model2Dto(final BauvorhabenReferencedElementsModel model);
}
