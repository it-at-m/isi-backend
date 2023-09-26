package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.common.KommentarDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.common.KommentarModel;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface KommentarApiMapper {
    KommentarDto model2Dto(final KommentarModel model);

    KommentarModel dto2Model(final KommentarDto dto) throws EntityNotFoundException;
}
