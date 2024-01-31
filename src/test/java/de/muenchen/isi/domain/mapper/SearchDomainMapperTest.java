package de.muenchen.isi.domain.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import de.muenchen.isi.domain.exception.GeometryOperationFailedException;
import de.muenchen.isi.domain.model.common.WGS84Model;
import de.muenchen.isi.domain.model.search.response.AbfrageSearchResultModel;
import de.muenchen.isi.domain.model.search.response.BauvorhabenSearchResultModel;
import de.muenchen.isi.domain.model.search.response.InfrastruktureinrichtungSearchResultModel;
import de.muenchen.isi.domain.service.KoordinatenService;
import de.muenchen.isi.infrastructure.entity.Baugenehmigungsverfahren;
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

    @Mock
    private KoordinatenDomainMapper koordinatenDomainMapper;

    @Mock
    private KoordinatenService koordinatenService;

    private SearchDomainMapper searchDomainMapper = new SearchDomainMapperImpl();

    @BeforeEach
    public void beforeEach() throws NoSuchFieldException, IllegalAccessException {
        Field field = searchDomainMapper.getClass().getSuperclass().getDeclaredField("koordinatenDomainMapper");
        field.setAccessible(true);
        field.set(searchDomainMapper, koordinatenDomainMapper);
        field = searchDomainMapper.getClass().getSuperclass().getDeclaredField("koordinatenService");
        field.setAccessible(true);
        field.set(searchDomainMapper, koordinatenService);
    }

    @Test
    void testAfterEntity2BauvorhabenSearchResultModel() throws GeometryOperationFailedException {
        // Mock-Coordinate erstellen
        Wgs84 mockCoordinate = new Wgs84();
        mockCoordinate.setLongitude(10.0);
        mockCoordinate.setLatitude(20.0);

        WGS84Model mockCoordinateModel = new WGS84Model();
        mockCoordinateModel.setLongitude(10.0);
        mockCoordinateModel.setLatitude(20.0);

        // Mocks für Bauvorhaben und Adresse erstellen
        final var bauvorhaben = new Bauvorhaben();
        bauvorhaben.setId(UUID.randomUUID());

        Adresse adresse = new Adresse();
        adresse.setCoordinate(mockCoordinate);
        bauvorhaben.setAdresse(adresse);

        // Mock-Model erstellen
        BauvorhabenSearchResultModel model = new BauvorhabenSearchResultModel();

        Mockito
            .when(this.koordinatenDomainMapper.entity2Model(bauvorhaben.getAdresse().getCoordinate()))
            .thenReturn(mockCoordinateModel);

        // Test der Methode
        searchDomainMapper.afterEntity2SearchResultModel(bauvorhaben, model);

        Mockito
            .verify(this.koordinatenDomainMapper, Mockito.times(1))
            .entity2Model(bauvorhaben.getAdresse().getCoordinate());
        assertThat(model.getCoordinate(), is(mockCoordinateModel));
    }

    @Test
    void testAfterEntity2InfrastruktureinrichtungSearchResultModel() throws GeometryOperationFailedException {
        // Mock-Coordinate erstellen
        WGS84Model mockCoordinateModel = new WGS84Model();
        mockCoordinateModel.setLongitude(10.0);
        mockCoordinateModel.setLatitude(20.0);

        // Mocks für Bauvorhaben und Adresse erstellen
        final var infrastruktureinrichtung = new Kinderkrippe();
        infrastruktureinrichtung.setId(UUID.randomUUID());

        VerortungPoint verortung = new VerortungPoint();
        PointGeometry pointGeometry = new PointGeometry();
        List<BigDecimal> coordinates = new ArrayList<>();
        coordinates.add(BigDecimal.valueOf(10.0));
        coordinates.add(BigDecimal.valueOf(20.0));
        pointGeometry.setCoordinates(coordinates);
        verortung.setPoint(pointGeometry);
        infrastruktureinrichtung.setVerortung(verortung);

        // Mock-Model erstellen
        InfrastruktureinrichtungSearchResultModel model = new InfrastruktureinrichtungSearchResultModel();

        // Test der Methode
        searchDomainMapper.afterEntity2SearchResultModel(infrastruktureinrichtung, model);

        assertThat(model.getCoordinate(), is(mockCoordinateModel));
    }

    @Test
    void testAfterEntity2AbfrageSearchResultModelVerortung() throws GeometryOperationFailedException {
        // Mock-Coordinate erstellen
        Wgs84 mockCoordinate = new Wgs84();
        mockCoordinate.setLongitude(10.0);
        mockCoordinate.setLatitude(20.0);

        WGS84Model mockCoordinateModel = new WGS84Model();
        mockCoordinateModel.setLongitude(10.0);
        mockCoordinateModel.setLatitude(20.0);

        // Mocks für Bauvorhaben und Adresse erstellen
        final var abfrage = new Bauleitplanverfahren();
        abfrage.setId(UUID.randomUUID());

        VerortungMultiPolygon verortungMultiPolygon = new VerortungMultiPolygon();
        MultiPolygonGeometry multiPolygonGeometry = new MultiPolygonGeometry();
        verortungMultiPolygon.setMultiPolygon(multiPolygonGeometry);
        abfrage.setVerortung(verortungMultiPolygon);

        // Mock-Model erstellen
        AbfrageSearchResultModel model = new AbfrageSearchResultModel();

        Mockito
            .when(this.koordinatenService.getMultiPolygonCentroid(abfrage.getVerortung().getMultiPolygon()))
            .thenReturn(mockCoordinateModel);

        // Test der Methode
        searchDomainMapper.afterEntity2SearchResultModel(abfrage, model);

        Mockito
            .verify(this.koordinatenService, Mockito.times(1))
            .getMultiPolygonCentroid(abfrage.getVerortung().getMultiPolygon());
        assertThat(model.getCoordinate(), is(mockCoordinateModel));
    }

    @Test
    void testAfterEntity2AbfrageSearchResultModelAdresse() throws GeometryOperationFailedException {
        // Mock-Coordinate erstellen
        Wgs84 mockCoordinate = new Wgs84();
        mockCoordinate.setLongitude(10.0);
        mockCoordinate.setLatitude(20.0);

        WGS84Model mockCoordinateModel = new WGS84Model();
        mockCoordinateModel.setLongitude(10.0);
        mockCoordinateModel.setLatitude(20.0);

        // Mocks für Bauvorhaben und Adresse erstellen
        final var abfrage = new Bauleitplanverfahren();
        abfrage.setId(UUID.randomUUID());

        Adresse adresse = new Adresse();
        adresse.setCoordinate(mockCoordinate);
        abfrage.setAdresse(adresse);

        // Mock-Model erstellen
        AbfrageSearchResultModel model = new AbfrageSearchResultModel();

        Mockito
            .when(this.koordinatenDomainMapper.entity2Model(abfrage.getAdresse().getCoordinate()))
            .thenReturn(mockCoordinateModel);

        // Test der Methode
        searchDomainMapper.afterEntity2SearchResultModel(abfrage, model);

        Mockito
            .verify(this.koordinatenDomainMapper, Mockito.times(1))
            .entity2Model(abfrage.getAdresse().getCoordinate());
        assertThat(model.getCoordinate(), is(mockCoordinateModel));
    }

    @Test
    void testAfterEntity2AbfrageSearchResultModelNoAdresse() throws GeometryOperationFailedException {
        //

        // Mocks für Bauvorhaben und Adresse erstellen
        final var abfrage = new Baugenehmigungsverfahren();
        abfrage.setId(UUID.randomUUID());

        // Mock-Model erstellen
        AbfrageSearchResultModel model = new AbfrageSearchResultModel();

        // Test der Methode
        searchDomainMapper.afterEntity2SearchResultModel(abfrage, model);

        assertThat(model.getCoordinate(), is(nullValue()));
    }
}
