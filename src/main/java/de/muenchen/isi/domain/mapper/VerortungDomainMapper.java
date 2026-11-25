package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.common.BezirksteilModel;
import de.muenchen.isi.domain.model.common.FlurstueckModel;
import de.muenchen.isi.domain.model.common.GemarkungModel;
import de.muenchen.isi.domain.model.common.GrundschulsprengelModel;
import de.muenchen.isi.domain.model.common.KitaplanungsbereichModel;
import de.muenchen.isi.domain.model.common.MittelschulsprengelModel;
import de.muenchen.isi.domain.model.common.StadtbezirkModel;
import de.muenchen.isi.domain.model.common.VerortungMultiPolygonModel;
import de.muenchen.isi.domain.model.common.VerortungPointModel;
import de.muenchen.isi.domain.model.common.ViertelModel;
import de.muenchen.isi.domain.service.KoordinatenService;
import de.muenchen.isi.infrastructure.entity.common.Bezirksteil;
import de.muenchen.isi.infrastructure.entity.common.Flurstueck;
import de.muenchen.isi.infrastructure.entity.common.Gemarkung;
import de.muenchen.isi.infrastructure.entity.common.Grundschulsprengel;
import de.muenchen.isi.infrastructure.entity.common.Kitaplanungsbereich;
import de.muenchen.isi.infrastructure.entity.common.Mittelschulsprengel;
import de.muenchen.isi.infrastructure.entity.common.PointGeometry;
import de.muenchen.isi.infrastructure.entity.common.Stadtbezirk;
import de.muenchen.isi.infrastructure.entity.common.Verortung;
import de.muenchen.isi.infrastructure.entity.common.VerortungMultiPolygon;
import de.muenchen.isi.infrastructure.entity.common.VerortungPoint;
import de.muenchen.isi.infrastructure.entity.common.Viertel;
import de.muenchen.isi.infrastructure.entity.common.Wgs84;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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

    /**
     * Ohne Umgriff wurde die Geometrie nicht geladen, der Schwerpunkt aus dem Model bleibt dann gültig.
     */
    @AfterMapping
    public void afterMappingModel2Entity(
        final VerortungMultiPolygonModel model,
        @MappingTarget final VerortungMultiPolygon entity
    ) {
        sortiereKitaplanungsbereiche(entity);
        if (ObjectUtils.isEmpty(model.getMultiPolygon())) {
            return;
        }
        try {
            final var punktWgs84 = koordinatenService.getMultiPolygonPointOnSurface(model.getMultiPolygon());
            final var punktWgs84Model = koordinatenDomainMapper.entity2Model(punktWgs84);
            final var pointGeometry = new PointGeometry();
            pointGeometry.setType(TYPE_POINT_GEOMETRY);
            pointGeometry.setCoordinates(
                List.of(BigDecimal.valueOf(punktWgs84.getLongitude()), BigDecimal.valueOf(punktWgs84.getLatitude()))
            );
            entity.setCentroid(pointGeometry);

            final var utmModel = koordinatenService.wgs84ToUtm32(punktWgs84Model);
            entity.setCentroidUtm(koordinatenDomainMapper.model2Entity(utmModel));
        } catch (final Exception exception) {
            entity.setCentroid(null);
            entity.setCentroidUtm(null);
            log.error("Für die Verortung eines Multipolygons konnte kein Schwerpunkt ermittelt werden.", exception);
        }
    }

    /**
     * Der Report zu Kita-Plätzen liest den ersten Eintrag der jsonb-Liste. Die feste Reihenfolge macht ihn eindeutig.
     */
    private void sortiereKitaplanungsbereiche(final Verortung entity) {
        if (entity.getKitaplanungsbereiche() != null) {
            entity.setKitaplanungsbereiche(
                entity
                    .getKitaplanungsbereiche()
                    .stream()
                    .sorted(
                        Comparator.comparing(
                            Kitaplanungsbereich::getKitaPlb,
                            Comparator.nullsLast(Comparator.naturalOrder())
                        )
                    )
                    .collect(Collectors.toCollection(LinkedHashSet::new))
            );
        }
    }

    /**
     * Umgriff und Flurstück-Polygone ergänzt bei Bedarf der {@link de.muenchen.isi.domain.service.VerortungGeometrieService}.
     */
    @Mapping(target = "multiPolygon", ignore = true)
    public abstract VerortungMultiPolygonModel entity2Model(final VerortungMultiPolygon entity);

    @Mapping(target = "multiPolygon", ignore = true)
    public abstract StadtbezirkModel entity2Model(final Stadtbezirk entity);

    @Mapping(target = "multiPolygon", ignore = true)
    public abstract BezirksteilModel entity2Model(final Bezirksteil entity);

    @Mapping(target = "multiPolygon", ignore = true)
    public abstract ViertelModel entity2Model(final Viertel entity);

    @Mapping(target = "multiPolygon", ignore = true)
    public abstract GemarkungModel entity2Model(final Gemarkung entity);

    @Mapping(target = "multiPolygon", ignore = true)
    public abstract FlurstueckModel entity2Model(final Flurstueck entity);

    @Mapping(target = "multiPolygon", ignore = true)
    public abstract KitaplanungsbereichModel entity2Model(final Kitaplanungsbereich entity);

    @Mapping(target = "multiPolygon", ignore = true)
    public abstract GrundschulsprengelModel entity2Model(final Grundschulsprengel entity);

    @Mapping(target = "multiPolygon", ignore = true)
    public abstract MittelschulsprengelModel entity2Model(final Mittelschulsprengel entity);

    public abstract VerortungPoint model2Entity(final VerortungPointModel model);

    @AfterMapping
    public void afterMappingModel2Entity(@MappingTarget final VerortungPoint entity) {
        sortiereKitaplanungsbereiche(entity);
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
