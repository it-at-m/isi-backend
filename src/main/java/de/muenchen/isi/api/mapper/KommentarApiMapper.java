package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.common.KommentarBauvorhabenDto;
import de.muenchen.isi.api.dto.common.KommentarInfrastruktureinrichtungDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.common.KommentarBauvorhabenModel;
import de.muenchen.isi.domain.model.common.KommentarInfrastruktureinrichtungModel;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface KommentarApiMapper {
    KommentarBauvorhabenDto model2Dto(final KommentarBauvorhabenModel model);

    KommentarBauvorhabenModel dto2Model(final KommentarBauvorhabenDto dto) throws EntityNotFoundException;

    KommentarInfrastruktureinrichtungDto model2Dto(final KommentarInfrastruktureinrichtungModel model);

    KommentarInfrastruktureinrichtungModel dto2Model(final KommentarInfrastruktureinrichtungDto dto)
        throws EntityNotFoundException;
}
