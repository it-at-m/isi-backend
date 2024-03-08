package de.muenchen.isi.domain.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import de.muenchen.isi.domain.exception.GeometryOperationFailedException;
import de.muenchen.isi.domain.model.common.MultiPolygonGeometryModel;
import de.muenchen.isi.domain.model.common.WGS84Model;
import de.muenchen.isi.domain.model.search.response.AbfrageSearchResultModel;
import de.muenchen.isi.domain.model.search.response.BauvorhabenSearchResultModel;
import de.muenchen.isi.domain.model.search.response.InfrastruktureinrichtungSearchResultModel;
import de.muenchen.isi.domain.service.KoordinatenService;
import de.muenchen.isi.infrastructure.entity.Bauleitplanverfahren;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.common.MultiPolygonGeometry;
import de.muenchen.isi.infrastructure.entity.common.PointGeometry;
import de.muenchen.isi.infrastructure.entity.common.VerortungMultiPolygon;
import de.muenchen.isi.infrastructure.entity.common.VerortungPoint;
import de.muenchen.isi.infrastructure.entity.common.Wgs84;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kinderkrippe;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class SearchDomainMapperTest {

    private final KoordinatenDomainMapper koordinatenDomainMapper = new KoordinatenDomainMapperImpl();

    private final SearchDomainMapper searchDomainMapper = new SearchDomainMapperImpl();

    @Mock
    private KoordinatenService koordinatenService;

    @BeforeEach
    public void beforeEach() throws NoSuchFieldException, IllegalAccessException {
        Field field = searchDomainMapper.getClass().getSuperclass().getDeclaredField("koordinatenDomainMapper");
        field.setAccessible(true);
        field.set(searchDomainMapper, koordinatenDomainMapper);
        field = searchDomainMapper.getClass().getSuperclass().getDeclaredField("koordinatenService");
        field.setAccessible(true);
        field.set(searchDomainMapper, koordinatenService);
        Mockito.reset(koordinatenService);
    }

    @Test
    void afterEntity2BauvorhabenSearchResultModelAdresse() throws GeometryOperationFailedException {
        Wgs84 mockCoordinate = new Wgs84();
        mockCoordinate.setLongitude(10.0);
        mockCoordinate.setLatitude(20.0);

        WGS84Model mockCoordinateModel = new WGS84Model();
        mockCoordinateModel.setLongitude(10.0);
        mockCoordinateModel.setLatitude(20.0);

        final var bauvorhaben = new Bauvorhaben();

        Adresse adresse = new Adresse();
        adresse.setCoordinate(mockCoordinate);
        bauvorhaben.setAdresse(adresse);

        BauvorhabenSearchResultModel model = new BauvorhabenSearchResultModel();

        BauvorhabenSearchResultModel expected = new BauvorhabenSearchResultModel();
        expected.setCoordinate(mockCoordinateModel);
        expected.setUmgriff(null);

        // Test der Methode
        searchDomainMapper.afterMappingEntity2SearchResultModel(bauvorhaben, model);

        Mockito.verify(this.koordinatenService, Mockito.times(0)).getMultiPolygonCentroid(Mockito.any());
        assertThat(model, is(expected));
    }

    @Test
    void afterEntity2BauvorhabenSearchResultModelVerortung() throws GeometryOperationFailedException {
        final var bauvorhaben = new Bauvorhaben();

        VerortungMultiPolygon verortung = new VerortungMultiPolygon();
        MultiPolygonGeometry multiPolygon = new MultiPolygonGeometry();
        multiPolygon.setType("MultiPolygon");
        multiPolygon.setCoordinates(List.of(List.of(List.of(List.of(BigDecimal.TEN, BigDecimal.ONE)))));
        verortung.setMultiPolygon(multiPolygon);
        bauvorhaben.setVerortung(verortung);

        BauvorhabenSearchResultModel model = new BauvorhabenSearchResultModel();

        WGS84Model mockCoordinateModel = new WGS84Model();
        mockCoordinateModel.setLongitude(10.0);
        mockCoordinateModel.setLatitude(20.0);

        Mockito.when(this.koordinatenService.getMultiPolygonCentroid(multiPolygon)).thenReturn(mockCoordinateModel);

        BauvorhabenSearchResultModel expected = new BauvorhabenSearchResultModel();
        expected.setCoordinate(mockCoordinateModel);
        MultiPolygonGeometryModel multiPolygonModel = new MultiPolygonGeometryModel();
        multiPolygonModel.setType("MultiPolygon");
        multiPolygonModel.setCoordinates(List.of(List.of(List.of(List.of(BigDecimal.TEN, BigDecimal.ONE)))));
        expected.setUmgriff(multiPolygonModel);

        searchDomainMapper.afterMappingEntity2SearchResultModel(bauvorhaben, model);

        Mockito.verify(this.koordinatenService, Mockito.times(1)).getMultiPolygonCentroid(multiPolygon);
        assertThat(model, is(expected));
    }

    @Test
    void afterEntity2BauvorhabenSearchResultModelAdresseNullVerortungNull() throws GeometryOperationFailedException {
        final var bauvorhaben = new Bauvorhaben();

        bauvorhaben.setVerortung(null);

        BauvorhabenSearchResultModel model = new BauvorhabenSearchResultModel();

        BauvorhabenSearchResultModel expected = new BauvorhabenSearchResultModel();
        expected.setCoordinate(null);
        expected.setUmgriff(null);

        searchDomainMapper.afterMappingEntity2SearchResultModel(bauvorhaben, model);

        Mockito.verify(this.koordinatenService, Mockito.times(0)).getMultiPolygonCentroid(Mockito.any());
        assertThat(model, is(expected));
    }

    @Test
    void afterEntity2BauvorhabenSearchResultModelVerortungMultiPolygonNull() throws GeometryOperationFailedException {
        final var bauvorhaben = new Bauvorhaben();

        VerortungMultiPolygon verortung = new VerortungMultiPolygon();
        verortung.setMultiPolygon(null);
        bauvorhaben.setVerortung(verortung);

        BauvorhabenSearchResultModel model = new BauvorhabenSearchResultModel();

        BauvorhabenSearchResultModel expected = new BauvorhabenSearchResultModel();
        expected.setUmgriff(null);

        searchDomainMapper.afterMappingEntity2SearchResultModel(bauvorhaben, model);

        Mockito.verify(this.koordinatenService, Mockito.times(0)).getMultiPolygonCentroid(Mockito.any());
        assertThat(model, is(expected));
    }

    @Test
    void testAfterEntity2InfrastruktureinrichtungSearchResultModelAdresse() {
        Wgs84 mockCoordinate = new Wgs84();
        mockCoordinate.setLongitude(10.0);
        mockCoordinate.setLatitude(20.0);

        WGS84Model mockCoordinateModel = new WGS84Model();
        mockCoordinateModel.setLongitude(10.0);
        mockCoordinateModel.setLatitude(20.0);

        final var infrastruktureinrichtung = new Kinderkrippe();

        Adresse adresse = new Adresse();
        adresse.setCoordinate(mockCoordinate);
        infrastruktureinrichtung.setAdresse(adresse);

        InfrastruktureinrichtungSearchResultModel model = new InfrastruktureinrichtungSearchResultModel();

        InfrastruktureinrichtungSearchResultModel expected = new InfrastruktureinrichtungSearchResultModel();
        expected.setCoordinate(mockCoordinateModel);

        searchDomainMapper.afterMappingEntity2SearchResultModel(infrastruktureinrichtung, model);

        assertThat(model, is(expected));
    }

    @Test
    void testAfterEntity2InfrastruktureinrichtungSearchResultModelVerortung() {
        // Mock-Coordinate erstellen
        WGS84Model mockCoordinateModel = new WGS84Model();
        mockCoordinateModel.setLongitude(10.0);
        mockCoordinateModel.setLatitude(20.0);

        final var infrastruktureinrichtung = new Kinderkrippe();

        VerortungPoint verortung = new VerortungPoint();
        PointGeometry pointGeometry = new PointGeometry();
        List<BigDecimal> coordinates = new ArrayList<>();
        coordinates.add(BigDecimal.valueOf(10.0));
        coordinates.add(BigDecimal.valueOf(20.0));
        pointGeometry.setCoordinates(coordinates);
        verortung.setPoint(pointGeometry);
        infrastruktureinrichtung.setVerortung(verortung);

        InfrastruktureinrichtungSearchResultModel model = new InfrastruktureinrichtungSearchResultModel();

        InfrastruktureinrichtungSearchResultModel expected = new InfrastruktureinrichtungSearchResultModel();
        expected.setCoordinate(mockCoordinateModel);

        searchDomainMapper.afterMappingEntity2SearchResultModel(infrastruktureinrichtung, model);

        assertThat(model, is(expected));
    }

    @Test
    void testAfterEntity2InfrastruktureinrichtungSearchResultModelVerortungNull() {
        // Mock-Coordinate erstellen
        WGS84Model mockCoordinateModel = new WGS84Model();
        mockCoordinateModel.setLongitude(10.0);
        mockCoordinateModel.setLatitude(20.0);

        final var infrastruktureinrichtung = new Kinderkrippe();
        infrastruktureinrichtung.setVerortung(null);

        InfrastruktureinrichtungSearchResultModel model = new InfrastruktureinrichtungSearchResultModel();

        InfrastruktureinrichtungSearchResultModel expected = new InfrastruktureinrichtungSearchResultModel();
        expected.setCoordinate(null);

        searchDomainMapper.afterMappingEntity2SearchResultModel(infrastruktureinrichtung, model);

        assertThat(model, is(expected));
    }

    @Test
    void testAfterEntity2InfrastruktureinrichtungSearchResultModelVerortungPointNull() {
        // Mock-Coordinate erstellen
        WGS84Model mockCoordinateModel = new WGS84Model();
        mockCoordinateModel.setLongitude(10.0);
        mockCoordinateModel.setLatitude(20.0);

        final var infrastruktureinrichtung = new Kinderkrippe();
        VerortungPoint verortung = new VerortungPoint();
        verortung.setPoint(null);
        infrastruktureinrichtung.setVerortung(verortung);

        InfrastruktureinrichtungSearchResultModel model = new InfrastruktureinrichtungSearchResultModel();

        InfrastruktureinrichtungSearchResultModel expected = new InfrastruktureinrichtungSearchResultModel();
        expected.setCoordinate(null);

        searchDomainMapper.afterMappingEntity2SearchResultModel(infrastruktureinrichtung, model);

        assertThat(model, is(expected));
    }

    @Test
    void afterEntity2AbfrageSearchResultModelAdresse() throws GeometryOperationFailedException {
        Wgs84 mockCoordinate = new Wgs84();
        mockCoordinate.setLongitude(10.0);
        mockCoordinate.setLatitude(20.0);

        WGS84Model mockCoordinateModel = new WGS84Model();
        mockCoordinateModel.setLongitude(10.0);
        mockCoordinateModel.setLatitude(20.0);

        final var abfrage = new Bauleitplanverfahren();

        Adresse adresse = new Adresse();
        adresse.setCoordinate(mockCoordinate);
        abfrage.setAdresse(adresse);

        AbfrageSearchResultModel model = new AbfrageSearchResultModel();

        AbfrageSearchResultModel expected = new AbfrageSearchResultModel();
        expected.setCoordinate(mockCoordinateModel);

        // Test der Methode
        searchDomainMapper.afterMappingEntity2SearchResultModel(abfrage, model);

        Mockito.verify(this.koordinatenService, Mockito.times(0)).getMultiPolygonCentroid(Mockito.any());
        assertThat(model, is(expected));
    }

    @Test
    void afterEntity2AbfrageSearchResultModelVerortung() throws GeometryOperationFailedException {
        final var abfrage = new Bauleitplanverfahren();

        VerortungMultiPolygon verortung = new VerortungMultiPolygon();
        MultiPolygonGeometry multiPolygon = new MultiPolygonGeometry();
        multiPolygon.setType("MultiPolygon");
        multiPolygon.setCoordinates(List.of(List.of(List.of(List.of(BigDecimal.TEN, BigDecimal.ONE)))));
        verortung.setMultiPolygon(multiPolygon);
        abfrage.setVerortung(verortung);

        AbfrageSearchResultModel model = new AbfrageSearchResultModel();

        WGS84Model mockCoordinateModel = new WGS84Model();
        mockCoordinateModel.setLongitude(10.0);
        mockCoordinateModel.setLatitude(20.0);

        Mockito.when(this.koordinatenService.getMultiPolygonCentroid(multiPolygon)).thenReturn(mockCoordinateModel);

        AbfrageSearchResultModel expected = new AbfrageSearchResultModel();
        expected.setCoordinate(mockCoordinateModel);

        searchDomainMapper.afterMappingEntity2SearchResultModel(abfrage, model);

        Mockito.verify(this.koordinatenService, Mockito.times(1)).getMultiPolygonCentroid(multiPolygon);
        assertThat(model, is(expected));
    }

    @Test
    void afterEntity2AbfrageSearchResultModelAdresseNullVerortungNull() throws GeometryOperationFailedException {
        final var abfrage = new Bauleitplanverfahren();
        abfrage.setVerortung(null);
        abfrage.setAdresse(null);

        AbfrageSearchResultModel model = new AbfrageSearchResultModel();

        AbfrageSearchResultModel expected = new AbfrageSearchResultModel();
        expected.setCoordinate(null);

        searchDomainMapper.afterMappingEntity2SearchResultModel(abfrage, model);

        Mockito.verify(this.koordinatenService, Mockito.times(0)).getMultiPolygonCentroid(Mockito.any());
        assertThat(model, is(expected));
    }

    @Test
    void afterEntity2AbfrageSearchResultModelVerortungMultiPolygonNull() throws GeometryOperationFailedException {
        final var abfrage = new Bauleitplanverfahren();

        VerortungMultiPolygon verortung = new VerortungMultiPolygon();
        verortung.setMultiPolygon(null);
        abfrage.setVerortung(verortung);

        AbfrageSearchResultModel model = new AbfrageSearchResultModel();

        AbfrageSearchResultModel expected = new AbfrageSearchResultModel();
        expected.setCoordinate(null);

        searchDomainMapper.afterMappingEntity2SearchResultModel(abfrage, model);

        Mockito.verify(this.koordinatenService, Mockito.times(0)).getMultiPolygonCentroid(Mockito.any());
        assertThat(model, is(expected));
    }

    @Test
    void mapBauvorhabenToUuid() {
        assertThat(searchDomainMapper.map(null), is(nullValue()));
        final var bauvorhaben = new Bauvorhaben();
        assertThat(searchDomainMapper.map(bauvorhaben), is(nullValue()));
        bauvorhaben.setId(UUID.randomUUID());
        assertThat(searchDomainMapper.map(bauvorhaben), is(bauvorhaben.getId()));
    }

    @Test
    void hasAdressCoordinate() {
        assertThat(searchDomainMapper.hasAdressCoordinate(null), is(false));
        final var adresse = new Adresse();
        assertThat(searchDomainMapper.hasAdressCoordinate(adresse), is(false));
        Wgs84 coordinate = new Wgs84();
        coordinate.setLongitude(10.0);
        coordinate.setLatitude(20.0);
        adresse.setCoordinate(coordinate);
        assertThat(searchDomainMapper.hasAdressCoordinate(adresse), is(true));
    }

    @Test
    void getCoordinateFromAdresseOrAdresseNullAndVerortungNull() throws GeometryOperationFailedException {
        assertThat(searchDomainMapper.getCoordinateFromAdresseOrVerortung(null, null), is(nullValue()));
        Mockito.verify(this.koordinatenService, Mockito.times(0)).getMultiPolygonCentroid(Mockito.any());
    }

    @Test
    void getCoordinateFromAdresseOrAdresseAndVerortungNull() throws GeometryOperationFailedException {
        Wgs84 coordinate = new Wgs84();
        coordinate.setLongitude(10.0);
        coordinate.setLatitude(20.0);

        Adresse adresse = new Adresse();
        adresse.setCoordinate(coordinate);

        WGS84Model expected = new WGS84Model();
        expected.setLongitude(10.0);
        expected.setLatitude(20.0);

        assertThat(searchDomainMapper.getCoordinateFromAdresseOrVerortung(adresse, null), is(expected));

        Mockito.verify(this.koordinatenService, Mockito.times(0)).getMultiPolygonCentroid(Mockito.any());
    }

    @Test
    void getCoordinateFromAdresseOrAdresseNullAndVerortung() throws GeometryOperationFailedException {
        VerortungMultiPolygon verortung = new VerortungMultiPolygon();
        MultiPolygonGeometry multiPolygon = new MultiPolygonGeometry();
        multiPolygon.setType("MultiPolygon");
        multiPolygon.setCoordinates(List.of(List.of(List.of(List.of(BigDecimal.TEN, BigDecimal.ONE)))));
        verortung.setMultiPolygon(multiPolygon);

        WGS84Model mockCoordinateModel = new WGS84Model();
        mockCoordinateModel.setLongitude(10.0);
        mockCoordinateModel.setLatitude(20.0);

        WGS84Model expected = new WGS84Model();
        expected.setLongitude(10.0);
        expected.setLatitude(20.0);

        Mockito.when(this.koordinatenService.getMultiPolygonCentroid(multiPolygon)).thenReturn(mockCoordinateModel);

        assertThat(searchDomainMapper.getCoordinateFromAdresseOrVerortung(null, verortung), is(expected));

        Mockito.verify(this.koordinatenService, Mockito.times(1)).getMultiPolygonCentroid(multiPolygon);
    }

    @Test
    void getCoordinateFromAdresseOrAdresseNullAndVerortungMultipolygonNull() throws GeometryOperationFailedException {
        VerortungMultiPolygon verortung = new VerortungMultiPolygon();
        verortung.setMultiPolygon(null);

        assertThat(searchDomainMapper.getCoordinateFromAdresseOrVerortung(null, verortung), is(nullValue()));

        Mockito.verify(this.koordinatenService, Mockito.times(0)).getMultiPolygonCentroid(Mockito.any());
    }
}
