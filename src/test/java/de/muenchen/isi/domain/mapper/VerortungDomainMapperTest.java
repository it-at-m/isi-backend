package de.muenchen.isi.domain.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.model.common.MultiPolygonGeometryModel;
import de.muenchen.isi.domain.model.common.PointGeometryModel;
import de.muenchen.isi.domain.model.common.VerortungMultiPolygonModel;
import de.muenchen.isi.domain.model.common.VerortungPointModel;
import de.muenchen.isi.domain.service.KoordinatenService;
import de.muenchen.isi.infrastructure.entity.common.MultiPolygonGeometry;
import de.muenchen.isi.infrastructure.entity.common.PointGeometry;
import de.muenchen.isi.infrastructure.entity.common.Utm;
import de.muenchen.isi.infrastructure.entity.common.VerortungMultiPolygon;
import de.muenchen.isi.infrastructure.entity.common.VerortungPoint;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class VerortungDomainMapperTest {

    private final VerortungDomainMapper verortungDomainMapper = new VerortungDomainMapperImpl(
        new KoordinatenDomainMapperImpl()
    );

    @BeforeEach
    public void beforeEach() throws NoSuchFieldException, IllegalAccessException {
        Field field = verortungDomainMapper.getClass().getSuperclass().getDeclaredField("koordinatenService");
        field.setAccessible(true);
        field.set(verortungDomainMapper, new KoordinatenService());
        field = verortungDomainMapper.getClass().getSuperclass().getDeclaredField("koordinatenDomainMapper");
        field.setAccessible(true);
        field.set(verortungDomainMapper, new KoordinatenDomainMapperImpl());
    }

    @Test
    void model2EntityVerortungMultiPolygon() {
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
        final var multiPolygonGeometryModel = new MultiPolygonGeometryModel();
        multiPolygonGeometryModel.setType("MultiPolygon");
        multiPolygonGeometryModel.setCoordinates(polygon);
        final var model = new VerortungMultiPolygonModel();
        model.setMultiPolygon(multiPolygonGeometryModel);

        final var result = verortungDomainMapper.model2Entity(model);

        final var expected = new VerortungMultiPolygon();

        final var multiPolygonGeometry = new MultiPolygonGeometry();
        multiPolygonGeometry.setType("MultiPolygon");
        multiPolygonGeometry.setCoordinates(polygon);
        expected.setMultiPolygon(multiPolygonGeometry);

        final var centroidPointGeometry = new PointGeometry();
        centroidPointGeometry.setType("Point");
        centroidPointGeometry.setCoordinates(
            List.of(BigDecimal.valueOf(11.542026984158708), BigDecimal.valueOf(48.108341907595324))
        );
        expected.setCentroid(centroidPointGeometry);

        final var utm = new Utm();
        utm.setZone("32U");
        utm.setEast(689219.7547272056);
        utm.setNorth(5331467.745840158);
        expected.setCentroidUtm(utm);

        assertThat(result, is(expected));
    }

    @Test
    void model2EntityVerortungMultiPolygonEmpty() {
        final var model = new VerortungMultiPolygonModel();

        final var result = verortungDomainMapper.model2Entity(model);

        final var expected = new VerortungMultiPolygon();

        assertThat(result, is(expected));
    }

    @Test
    void model2EntityVerortungMultiPolygonException() {
        final var polygon = List.of(
            List.of(
                List.of(
                    List.of(BigDecimal.valueOf(999999999), BigDecimal.valueOf(999999999)),
                    List.of(BigDecimal.valueOf(11.54322343181365), BigDecimal.valueOf(48.110148558353664)),
                    List.of(BigDecimal.valueOf(11.543523839223319), BigDecimal.valueOf(48.10668123409517)),
                    List.of(BigDecimal.valueOf(11.54090600322478), BigDecimal.valueOf(48.10648063793053)),
                    List.of(BigDecimal.valueOf(11.5404768497824), BigDecimal.valueOf(48.11000528512523))
                )
            )
        );
        final var multiPolygonGeometryModel = new MultiPolygonGeometryModel();
        multiPolygonGeometryModel.setType("MultiPolygon");
        multiPolygonGeometryModel.setCoordinates(polygon);
        final var model = new VerortungMultiPolygonModel();
        model.setMultiPolygon(multiPolygonGeometryModel);

        final var result = verortungDomainMapper.model2Entity(model);

        final var expected = new VerortungMultiPolygon();

        final var multiPolygonGeometry = new MultiPolygonGeometry();
        multiPolygonGeometry.setType("MultiPolygon");
        multiPolygonGeometry.setCoordinates(polygon);
        expected.setMultiPolygon(multiPolygonGeometry);

        assertThat(result, is(expected));
    }

    @Test
    void model2EntityVerortungPoint() {
        final var model = new VerortungPointModel();
        final var pointGeometryModel = new PointGeometryModel();
        pointGeometryModel.setType("Point");
        pointGeometryModel.setCoordinates(
            List.of(BigDecimal.valueOf(11.542026984158708), BigDecimal.valueOf(48.108341907595324))
        );
        model.setPoint(pointGeometryModel);

        final var result = verortungDomainMapper.model2Entity(model);

        final var expected = new VerortungPoint();

        final var pointGeometry = new PointGeometry();
        pointGeometry.setType("Point");
        pointGeometry.setCoordinates(
            List.of(BigDecimal.valueOf(11.542026984158708), BigDecimal.valueOf(48.108341907595324))
        );
        expected.setPoint(pointGeometry);

        final var utm = new Utm();
        utm.setZone("32U");
        utm.setEast(689219.7547272056);
        utm.setNorth(5331467.745840158);
        expected.setPointUtm(utm);

        assertThat(result, is(expected));
    }

    @Test
    void model2EntityVerortungPointEmpty() {
        final var model = new VerortungPointModel();

        final var result = verortungDomainMapper.model2Entity(model);

        final var expected = new VerortungPoint();

        assertThat(result, is(expected));
    }

    @Test
    void model2EntityVerortungPointException() {
        final var model = new VerortungPointModel();
        final var pointGeometryModel = new PointGeometryModel();
        pointGeometryModel.setType("Point");
        pointGeometryModel.setCoordinates(List.of(BigDecimal.valueOf(999999999), BigDecimal.valueOf(999999999)));
        model.setPoint(pointGeometryModel);

        final var result = verortungDomainMapper.model2Entity(model);

        final var expected = new VerortungPoint();

        final var pointGeometry = new PointGeometry();
        pointGeometry.setType("Point");
        pointGeometry.setCoordinates(List.of(BigDecimal.valueOf(999999999), BigDecimal.valueOf(999999999)));
        expected.setPoint(pointGeometry);

        assertThat(result, is(expected));
    }
}
