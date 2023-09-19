package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.common.KommentarDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.common.KommentarModel;
import de.muenchen.isi.domain.service.BauvorhabenService;
import de.muenchen.isi.domain.service.InfrastruktureinrichtungService;
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

    @Autowired
    private InfrastruktureinrichtungService infrastruktureinrichtungService;

    @Mapping(source = "bauvorhaben.id", target = "bauvorhaben")
    @Mapping(source = "infrastruktureinrichtung.id", target = "infrastruktureinrichtung")
    public abstract KommentarDto model2Dto(final KommentarModel model);

    @Mapping(target = "bauvorhaben", ignore = true)
    @Mapping(target = "infrastruktureinrichtung", ignore = true)
    public abstract KommentarModel dto2Model(final KommentarDto dto) throws EntityNotFoundException;

    @AfterMapping
    void afterMappingDto2Model(final KommentarDto dto, final @MappingTarget KommentarModel model)
        throws EntityNotFoundException {
        if (ObjectUtils.isNotEmpty(dto.getBauvorhaben())) {
            final var bauvorhaben = bauvorhabenService.getBauvorhabenById(dto.getBauvorhaben());
            model.setBauvorhaben(bauvorhaben);
        } else if (ObjectUtils.isNotEmpty(dto.getInfrastruktureinrichtung())) {
            final var infrastruktureinrichtung = infrastruktureinrichtungService.getInfrastruktureinrichtungById(
                dto.getInfrastruktureinrichtung()
            );
            model.setInfrastruktureinrichtung(infrastruktureinrichtung);
        }
    }
}
