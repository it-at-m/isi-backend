package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.muenchen.isi.api.dto.common.UtmDto;
import de.muenchen.isi.api.dto.common.Wgs84Dto;
import de.muenchen.isi.domain.exception.GeometryOperationFailedException;
import de.muenchen.isi.domain.exception.KoordinatenException;
import de.muenchen.isi.infrastructure.entity.common.MultiPolygonGeometry;
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

    private KoordinatenService koordinatenService;

    @BeforeEach
    public void beforeEach() {
        this.koordinatenService = new KoordinatenService();
    }

    @Test
    public void wgs84ToUtm32Test() throws KoordinatenException {
        final Wgs84Dto wgs84Dto = new Wgs84Dto();
        wgs84Dto.setLatitude(48.137237);
        wgs84Dto.setLongitude(11.575524);

        final UtmDto utmDto = this.koordinatenService.wgs84ToUtm32(wgs84Dto);

        assertEquals(utmDto.getNorth(), 5334761.975490341);
        assertEquals(utmDto.getEast(), 691605.42419006);
    }

    @Test
    public void utm32ToWgs84Test() throws KoordinatenException {
        final UtmDto utmDto = new UtmDto();
        utmDto.setNorth(5334530.596);
        utmDto.setEast(689878.435);
        final Wgs84Dto wgs84Dto = this.koordinatenService.utm32ToWgs84(utmDto);

        assertEquals(wgs84Dto.getLatitude(), 48.13567500095738);
        assertEquals(wgs84Dto.getLongitude(), 11.552230994968712);
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
        final MultiPolygonGeometry multiPolygon = new MultiPolygonGeometry();
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
        final var coordinateSequence = JTSFactoryFinder
            .getGeometryFactory()
            .getCoordinateSequenceFactory()
            .create(coordiantes);
        final var linearRing = new LinearRing(coordinateSequence, JTSFactoryFinder.getGeometryFactory());
        final var expectedPolygon = new Polygon(linearRing, null, JTSFactoryFinder.getGeometryFactory());
        final var expected = new MultiPolygon(new Polygon[] { expectedPolygon }, JTSFactoryFinder.getGeometryFactory());

        assertThat(result, is(expected));
    }

    @Test
    void testGetMultiPolygonCentroid() throws GeometryOperationFailedException {
        // Beispiel-MultiPolygon für den Test
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
        final MultiPolygonGeometry multiPolygon = new MultiPolygonGeometry();
        multiPolygon.setType("MultiPolygon");
        multiPolygon.setCoordinates(polygon);

        // Erwartete Ergebnisse für den Test
        final var expectedLatitude = BigDecimal.valueOf(48.108341907595324);
        final var expectedLongitude = BigDecimal.valueOf(11.542363635517965);

        // Testausführung
        final var result = this.koordinatenService.getMultiPolygonCentroid(multiPolygon);

        // Vergleich der Ergebnisse mit den Erwartungen
        assertThat(result.getLatitude(), is(expectedLatitude));
        assertThat(result.getLongitude(), is(expectedLongitude));
    }

    private Coordinate createCoordinate(final List<BigDecimal> coordinate) {
        return new Coordinate(coordinate.get(0).doubleValue(), coordinate.get(1).doubleValue());
    }
}
