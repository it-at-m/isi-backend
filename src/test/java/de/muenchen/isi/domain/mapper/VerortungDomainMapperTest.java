package de.muenchen.isi.domain.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import de.muenchen.isi.domain.exception.GeometryOperationFailedException;
import de.muenchen.isi.domain.model.common.KitaplanungsbereichModel;
import de.muenchen.isi.domain.model.common.MultiPolygonGeometryModel;
import de.muenchen.isi.domain.model.common.PointGeometryModel;
import de.muenchen.isi.domain.model.common.UtmModel;
import de.muenchen.isi.domain.model.common.VerortungMultiPolygonModel;
import de.muenchen.isi.domain.model.common.VerortungPointModel;
import de.muenchen.isi.domain.service.KoordinatenService;
import de.muenchen.isi.infrastructure.entity.common.PointGeometry;
import de.muenchen.isi.infrastructure.entity.common.Stadtbezirk;
import de.muenchen.isi.infrastructure.entity.common.Utm;
import de.muenchen.isi.infrastructure.entity.common.VerortungMultiPolygon;
import de.muenchen.isi.infrastructure.entity.common.VerortungPoint;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
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
    void model2EntityVerortungMultiPolygon() throws GeometryOperationFailedException {
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

        // Der Schwerpunkt liegt in der Fläche, der Umgriff selbst gehört nicht in die Spalte verortung.
        final var koordinaten = result.getCentroid().getCoordinates();
        assertThat(result.getCentroid().getType(), is("Point"));
        final var flaeche = new KoordinatenService().createMultiPolygon(multiPolygonGeometryModel);
        final var punkt = flaeche
            .getFactory()
            .createPoint(new Coordinate(koordinaten.get(0).doubleValue(), koordinaten.get(1).doubleValue()));
        assertThat(flaeche.contains(punkt), is(true));
        assertThat(result.getCentroidUtm().getZone(), is("32U"));
        assertThat(result.getCentroidUtm().getEast(), is(closeTo(689220, 300)));
        assertThat(result.getCentroidUtm().getNorth(), is(closeTo(5331468, 300)));
    }

    @Test
    void entity2ModelVerortungMultiPolygonOhneGeometrie() {
        final var stadtbezirk = new Stadtbezirk();
        stadtbezirk.setNummer("08");
        final var entity = new VerortungMultiPolygon();
        entity.setStadtbezirke(Set.of(stadtbezirk));

        final var result = verortungDomainMapper.entity2Model(entity);

        assertThat(result.getMultiPolygon(), is(nullValue()));
        assertThat(result.getStadtbezirke().iterator().next().getNummer(), is("08"));
        assertThat(result.getStadtbezirke().iterator().next().getMultiPolygon(), is(nullValue()));
    }

    @Test
    void model2EntitySortiertKitaplanungsbereiche() {
        final var zweiter = new KitaplanungsbereichModel();
        zweiter.setKitaPlb(200L);
        final var erster = new KitaplanungsbereichModel();
        erster.setKitaPlb(100L);
        final var model = new VerortungPointModel();
        model.setKitaplanungsbereiche(Set.of(zweiter, erster));

        final var result = verortungDomainMapper.model2Entity(model);

        assertThat(result.getKitaplanungsbereiche().iterator().next().getKitaPlb(), is(100L));
    }

    @Test
    void model2EntityVerortungMultiPolygonEmpty() {
        final var model = new VerortungMultiPolygonModel();

        final var result = verortungDomainMapper.model2Entity(model);

        final var expected = new VerortungMultiPolygon();

        assertThat(result, is(expected));
    }

    @Test
    void model2EntityVerortungMultiPolygonOhneGeometrieBehaeltSchwerpunkt() {
        final var centroidModel = new PointGeometryModel();
        centroidModel.setType("Point");
        centroidModel.setCoordinates(
            List.of(BigDecimal.valueOf(11.542026984158708), BigDecimal.valueOf(48.108341907595324))
        );
        final var centroidUtmModel = new UtmModel();
        centroidUtmModel.setZone("32U");
        centroidUtmModel.setEast(689219.7547272056);
        centroidUtmModel.setNorth(5331467.745840158);
        final var model = new VerortungMultiPolygonModel();
        model.setMultiPolygon(null);
        model.setCentroid(centroidModel);
        model.setCentroidUtm(centroidUtmModel);

        final var result = verortungDomainMapper.model2Entity(model);

        assertThat(result.getCentroid().getCoordinates(), is(centroidModel.getCoordinates()));
        assertThat(result.getCentroidUtm().getEast(), is(689219.7547272056));
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

        assertThat(result, is(new VerortungMultiPolygon()));
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
        utm.setEast(689219.7545742918);
        utm.setNorth(5331467.750397166);
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
