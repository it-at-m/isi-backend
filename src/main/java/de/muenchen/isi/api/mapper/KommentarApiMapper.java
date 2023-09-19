package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.common.KommentarDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.common.KommentarModel;
import de.muenchen.isi.domain.service.BauvorhabenService;
import org.apache.commons.lang3.ObjectUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(config = MapstructConfiguration.class)
public abstract class KommentarApiMapper {

    @Autowired
    private BauvorhabenService bauvorhabenService;

    @Mapping(source = "bauvorhaben.id", target = "bauvorhaben")
    public abstract KommentarDto model2Dto(final KommentarModel model);

    @Mapping(target = "bauvorhaben", ignore = true)
    public abstract KommentarModel dto2Model(final KommentarDto dto) throws EntityNotFoundException;

    @AfterMapping
    void afterMappingDto2Model(final KommentarDto dto, final @MappingTarget KommentarModel model)
        throws EntityNotFoundException {
        final var bauvorhabenId = dto.getBauvorhaben();
        if (ObjectUtils.isNotEmpty(bauvorhabenId)) {
            final var bauvorhaben = bauvorhabenService.getBauvorhabenById(bauvorhabenId);
            model.setBauvorhaben(bauvorhaben);
        }
    }
}
