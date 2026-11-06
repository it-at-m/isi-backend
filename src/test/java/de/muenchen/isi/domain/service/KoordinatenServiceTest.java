package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.exception.GeometryOperationFailedException;
import de.muenchen.isi.domain.exception.KoordinatenException;
import de.muenchen.isi.domain.model.common.MultiPolygonGeometryModel;
import de.muenchen.isi.domain.model.common.UtmModel;
import de.muenchen.isi.domain.model.common.Wgs84Model;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class KoordinatenServiceTest {

    private static final double TOLERANZ_METER = 0.01;

    /** Entspricht rund 1 cm. */
    private static final double TOLERANZ_GRAD = 0.0000001;

    private KoordinatenService koordinatenService;

    @BeforeEach
    public void beforeEach() {
        this.koordinatenService = new KoordinatenService();
    }

    @Test
    public void wgs84ToUtm32Test() throws KoordinatenException {
        final Wgs84Model wgs84Model = new Wgs84Model();
        wgs84Model.setLatitude(48.137237);
        wgs84Model.setLongitude(11.575524);

        final UtmModel utmModel = this.koordinatenService.wgs84ToUtm32(wgs84Model);

        // Sollwerte aus PostGIS: ST_Transform(ST_SetSRID(ST_MakePoint(11.575524, 48.137237), 4326), 25832).
        // GeoTools rechnet über das Ellipsoid und weicht um wenige Millimeter ab, die Transformation ist auf 1 m genau.
        assertThat(utmModel.getNorth(), is(closeTo(5334761.97536709, TOLERANZ_METER)));
        assertThat(utmModel.getEast(), is(closeTo(691605.424190945, TOLERANZ_METER)));
    }

    @Test
    public void utm32ToWgs84Test() throws KoordinatenException {
        final UtmModel utmModel = new UtmModel();
        utmModel.setNorth(5334530.596);
        utmModel.setEast(689878.435);
        final Wgs84Model wgs84model = this.koordinatenService.utm32ToWgs84(utmModel);

        // Sollwerte aus PostGIS: ST_Transform(ST_SetSRID(ST_MakePoint(689878.435, 5334530.596), 25832), 4326).
        assertThat(wgs84model.getLatitude(), is(closeTo(48.13567500209103, TOLERANZ_GRAD)));
        assertThat(wgs84model.getLongitude(), is(closeTo(11.552230995001413, TOLERANZ_GRAD)));
    }

    @Test
    void createMultiPolygon() throws GeometryOperationFailedException {
        final var polygon = List.of(
            List.of(
                List.of(
                    List.of(BigDecimal.valueOf(11.5404768497824), BigDecimal.valueOf(48.11000528512523)),
                    List.of(BigDecimal.valueOf(11.54322343181365), BigDecimal.valueOf(48.110148558353664)),
                    List.of(BigDecimal.valueOf(11.543523839223319), BigDecimal.valueOf(48.10668123409517)),
                    List.of(BigDecimal.valueOf(11.54090600322478), BigDecimal.valueOf(48.10648063793053)),
                    List.of(BigDecimal.valueOf(11.5404768497824), BigDecimal.valueOf(48.11000528512523))
                )
            )
        );
        final MultiPolygonGeometryModel multiPolygon = new MultiPolygonGeometryModel();
        multiPolygon.setType("MultiPolygon");
        multiPolygon.setCoordinates(polygon);

        final var result = this.koordinatenService.createMultiPolygon(multiPolygon);

        final var coordiantes = polygon
            .get(0)
            .get(0)
            .stream()
            .map(this::createCoordinate)
            .collect(Collectors.toList())
            .toArray(new Coordinate[polygon.size()]);
        final var coordinateSequence = JTSFactoryFinder.getGeometryFactory()
            .getCoordinateSequenceFactory()
            .create(coordiantes);
        final var linearRing = new LinearRing(coordinateSequence, JTSFactoryFinder.getGeometryFactory());
        final var expectedPolygon = new Polygon(linearRing, null, JTSFactoryFinder.getGeometryFactory());
        final var expected = new MultiPolygon(new Polygon[] { expectedPolygon }, JTSFactoryFinder.getGeometryFactory());

        assertThat(result, is(expected));
        assertThat(result.getSRID(), is(4326));
    }

    @Test
    void createMultiPolygonModelBehaeltLoecherUndTeilflaechen() throws GeometryOperationFailedException {
        final var model = new MultiPolygonGeometryModel();
        model.setType("MultiPolygon");
        model.setCoordinates(
            List.of(
                List.of(
                    ring(11.0, 48.0, 11.1, 48.1),
                    // Loch in der ersten Teilfläche
                    ring(11.02, 48.02, 11.04, 48.04)
                ),
                List.of(ring(11.2, 48.2, 11.3, 48.3))
            )
        );

        final var result = this.koordinatenService.createMultiPolygonModel(
            this.koordinatenService.createMultiPolygon(model)
        );

        assertThat(result.getType(), is("MultiPolygon"));
        assertThat(result.getCoordinates().size(), is(2));
        assertThat(result.getCoordinates().get(0).size(), is(2));
        assertThat(result.getCoordinates().get(1).size(), is(1));
        assertThat(result.getCoordinates().get(0).get(1).get(0).get(0).doubleValue(), is(11.02));
        assertThat(result.getCoordinates().get(1).get(0).get(2).get(1).doubleValue(), is(48.3));
    }

    @Test
    void getMultiPolygonPointOnSurfaceLiegtBeiRingformInDerFlaeche() throws GeometryOperationFailedException {
        // Bei einem Ring läge der geometrische Schwerpunkt im Loch und damit außerhalb der Fläche.
        final var model = new MultiPolygonGeometryModel();
        model.setType("MultiPolygon");
        model.setCoordinates(List.of(List.of(ring(11.0, 48.0, 11.1, 48.1), ring(11.01, 48.01, 11.09, 48.09))));
        final var flaeche = this.koordinatenService.createMultiPolygon(model);

        final var result = this.koordinatenService.getMultiPolygonPointOnSurface(model);

        final var punkt = flaeche.getFactory().createPoint(new Coordinate(result.getLongitude(), result.getLatitude()));
        assertThat(flaeche.contains(punkt), is(true));
        assertThat(flaeche.contains(flaeche.getCentroid()), is(false));
    }

    private List<List<BigDecimal>> ring(final double x1, final double y1, final double x2, final double y2) {
        return List.of(
            List.of(BigDecimal.valueOf(x1), BigDecimal.valueOf(y1)),
            List.of(BigDecimal.valueOf(x2), BigDecimal.valueOf(y1)),
            List.of(BigDecimal.valueOf(x2), BigDecimal.valueOf(y2)),
            List.of(BigDecimal.valueOf(x1), BigDecimal.valueOf(y2)),
            List.of(BigDecimal.valueOf(x1), BigDecimal.valueOf(y1))
        );
    }

    private Coordinate createCoordinate(final List<BigDecimal> coordinate) {
        return new Coordinate(coordinate.get(0).doubleValue(), coordinate.get(1).doubleValue());
    }
}
