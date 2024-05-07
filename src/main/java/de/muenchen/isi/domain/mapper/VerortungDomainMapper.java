package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.common.VerortungMultiPolygonModel;
import de.muenchen.isi.domain.model.common.VerortungPointModel;
import de.muenchen.isi.domain.service.KoordinatenService;
import de.muenchen.isi.infrastructure.entity.common.PointGeometry;
import de.muenchen.isi.infrastructure.entity.common.VerortungMultiPolygon;
import de.muenchen.isi.infrastructure.entity.common.VerortungPoint;
import de.muenchen.isi.infrastructure.entity.common.Wgs84;
import java.math.BigDecimal;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Mapper(config = MapstructConfiguration.class, uses = { KoordinatenDomainMapper.class })
public abstract class VerortungDomainMapper {

    public static final String TYPE_POINT_GEOMETRY = "Point";

    @Autowired
    private KoordinatenService koordinatenService;

    @Autowired
    private KoordinatenDomainMapper koordinatenDomainMapper;

    public abstract VerortungMultiPolygon model2Entity(final VerortungMultiPolygonModel model);

    @AfterMapping
    public void afterMappingModel2Entity(
        final VerortungMultiPolygonModel model,
        @MappingTarget final VerortungMultiPolygon entity
    ) {
        try {
            if (ObjectUtils.isNotEmpty(entity.getMultiPolygon())) {
                final var centroidWgs84 = koordinatenService.getMultiPolygonCentroid(entity.getMultiPolygon());
                final var centroidWgs84Model = koordinatenDomainMapper.entity2Model(centroidWgs84);
                final var centroidPointGeometry = new PointGeometry();
                centroidPointGeometry.setType(TYPE_POINT_GEOMETRY);
                centroidPointGeometry.setCoordinates(
                    List.of(
                        BigDecimal.valueOf(centroidWgs84.getLongitude()),
                        BigDecimal.valueOf(centroidWgs84.getLatitude())
                    )
                );
                entity.setCentroid(centroidPointGeometry);

                final var centroidUtmModel = koordinatenService.wgs84ToUtm32(centroidWgs84Model);
                final var centroidUtm = koordinatenDomainMapper.model2Entity(centroidUtmModel);
                entity.setCentroidUtm(centroidUtm);
            }
        } catch (final Exception exception) {
            entity.setCentroid(null);
            entity.setCentroidUtm(null);
            log.error("Für die Verortung eines Multipolygons konnte kein Schwerpunkt ermittelt werden.", exception);
        }
    }

    public abstract VerortungMultiPolygonModel entity2Model(final VerortungMultiPolygon entity);

    public abstract VerortungPoint model2Entity(final VerortungPointModel model);

    @AfterMapping
    public void afterMappingModel2Entity(final VerortungPointModel model, @MappingTarget final VerortungPoint entity) {
        try {
            if (ObjectUtils.isNotEmpty(entity.getPoint())) {
                final var longitude = entity.getPoint().getCoordinates().get(0).doubleValue();
                final var latitude = entity.getPoint().getCoordinates().get(1).doubleValue();
                final var pointWgs84 = new Wgs84();
                pointWgs84.setLongitude(longitude);
                pointWgs84.setLatitude(latitude);
                final var pointWgs84Model = koordinatenDomainMapper.entity2Model(pointWgs84);
                final var pointUtmModel = koordinatenService.wgs84ToUtm32(pointWgs84Model);
                final var pointUtm = koordinatenDomainMapper.model2Entity(pointUtmModel);
                entity.setPointUtm(pointUtm);
            }
        } catch (final Exception exception) {
            entity.setPointUtm(null);
            log.error(
                "Für die Verortung einer Punktkoordinate konnte keine UTM-Transformation durchgeführt werden.",
                exception
            );
        }
    }

    public abstract VerortungPointModel entity2Model(final VerortungPoint entity);
}
