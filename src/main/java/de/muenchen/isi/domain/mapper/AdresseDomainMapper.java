package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.common.AdresseModel;
import de.muenchen.isi.domain.service.KoordinatenService;
import de.muenchen.isi.infrastructure.entity.common.Adresse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Mapper(config = MapstructConfiguration.class, uses = { KoordinatenDomainMapper.class })
public abstract class AdresseDomainMapper {

    @Autowired
    private KoordinatenService koordinatenService;

    @Autowired
    private KoordinatenDomainMapper koordinatenDomainMapper;

    public abstract Adresse model2Entity(final AdresseModel model);

    @AfterMapping
    public void afterMappingModel2Entity(@MappingTarget final Adresse entity) {
        try {
            if (ObjectUtils.isNotEmpty(entity.getCoordinate())) {
                final var wgs84 = entity.getCoordinate();
                final var wgs84Model = koordinatenDomainMapper.entity2Model(wgs84);
                final var utmModel = koordinatenService.wgs84ToUtm32(wgs84Model);
                final var utm = koordinatenDomainMapper.model2Entity(utmModel);
                entity.setCoordinateUtm(utm);
            }
        } catch (final Exception exception) {
            entity.setCoordinateUtm(null);
            log.error("Für die Adresskoordinate konnte keine UTM-Transformation durchgeführt werden.", exception);
        }
    }

    public abstract AdresseModel entity2Model(final Adresse entity);
}
