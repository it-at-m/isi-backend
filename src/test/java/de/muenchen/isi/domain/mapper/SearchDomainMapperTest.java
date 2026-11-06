package de.muenchen.isi.domain.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.muenchen.isi.domain.model.common.Wgs84Model;
import de.muenchen.isi.domain.model.enums.SearchResultType;
import de.muenchen.isi.domain.model.search.request.AbfrageInfrastruktureinrichtungRecord;
import de.muenchen.isi.domain.model.search.request.AbfrageRecord;
import de.muenchen.isi.domain.model.search.request.AllObjectsRecord;
import de.muenchen.isi.domain.model.search.request.BauvorhabenAbfrageRecord;
import de.muenchen.isi.domain.model.search.request.BauvorhabenInfrastruktureinrichtungRecord;
import de.muenchen.isi.domain.model.search.request.InfrastrukturRecord;
import de.muenchen.isi.domain.model.search.response.AbfrageSearchResultModel;
import de.muenchen.isi.domain.model.search.response.BauvorhabenSearchResultModel;
import de.muenchen.isi.domain.model.search.response.InfrastruktureinrichtungSearchResultModel;
import de.muenchen.isi.infrastructure.entity.Bauleitplanverfahren;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.common.PointGeometry;
import de.muenchen.isi.infrastructure.entity.common.Stadtbezirk;
import de.muenchen.isi.infrastructure.entity.common.VerortungMultiPolygon;
import de.muenchen.isi.infrastructure.entity.common.VerortungPoint;
import de.muenchen.isi.infrastructure.entity.common.Wgs84;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ResultType;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Verfahrensstand;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kinderkrippe;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    @BeforeEach
    public void beforeEach() throws NoSuchFieldException, IllegalAccessException {
        Field field = searchDomainMapper.getClass().getSuperclass().getDeclaredField("koordinatenDomainMapper");
        field.setAccessible(true);
        field.set(searchDomainMapper, koordinatenDomainMapper);
    }

    @Test
    void afterEntity2BauvorhabenSearchResultModelAdresse() {
        Wgs84 mockCoordinate = new Wgs84();
        mockCoordinate.setLongitude(10.0);
        mockCoordinate.setLatitude(20.0);

        Wgs84Model mockCoordinateModel = new Wgs84Model();
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

        assertThat(model, is(expected));
    }

    @Test
    void afterEntity2BauvorhabenSearchResultModelVerortung() {
        final var bauvorhaben = new Bauvorhaben();

        VerortungMultiPolygon verortung = new VerortungMultiPolygon();
        verortung.setCentroid(createCentroid(10.0, 20.0));
        bauvorhaben.setVerortung(verortung);

        BauvorhabenSearchResultModel model = new BauvorhabenSearchResultModel();

        Wgs84Model mockCoordinateModel = new Wgs84Model();
        mockCoordinateModel.setLongitude(10.0);
        mockCoordinateModel.setLatitude(20.0);

        BauvorhabenSearchResultModel expected = new BauvorhabenSearchResultModel();
        expected.setCoordinate(mockCoordinateModel);

        searchDomainMapper.afterMappingEntity2SearchResultModel(bauvorhaben, model);

        assertThat(model, is(expected));
    }

    @Test
    void afterEntity2BauvorhabenSearchResultModelAdresseNullVerortungNull() {
        final var bauvorhaben = new Bauvorhaben();

        bauvorhaben.setVerortung(null);

        BauvorhabenSearchResultModel model = new BauvorhabenSearchResultModel();

        BauvorhabenSearchResultModel expected = new BauvorhabenSearchResultModel();
        expected.setCoordinate(null);
        expected.setUmgriff(null);

        searchDomainMapper.afterMappingEntity2SearchResultModel(bauvorhaben, model);

        assertThat(model, is(expected));
    }

    @Test
    void afterEntity2BauvorhabenSearchResultModelVerortungMultiPolygonNull() {
        final var bauvorhaben = new Bauvorhaben();

        VerortungMultiPolygon verortung = new VerortungMultiPolygon();
        bauvorhaben.setVerortung(verortung);

        BauvorhabenSearchResultModel model = new BauvorhabenSearchResultModel();

        BauvorhabenSearchResultModel expected = new BauvorhabenSearchResultModel();
        expected.setUmgriff(null);

        searchDomainMapper.afterMappingEntity2SearchResultModel(bauvorhaben, model);

        assertThat(model, is(expected));
    }

    @Test
    void testAfterEntity2InfrastruktureinrichtungSearchResultModelAdresse() {
        Wgs84 mockCoordinate = new Wgs84();
        mockCoordinate.setLongitude(10.0);
        mockCoordinate.setLatitude(20.0);

        Wgs84Model mockCoordinateModel = new Wgs84Model();
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
        Wgs84Model mockCoordinateModel = new Wgs84Model();
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
        Wgs84Model mockCoordinateModel = new Wgs84Model();
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
        Wgs84Model mockCoordinateModel = new Wgs84Model();
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
    void afterEntity2AbfrageSearchResultModelAdresse() {
        Wgs84 mockCoordinate = new Wgs84();
        mockCoordinate.setLongitude(10.0);
        mockCoordinate.setLatitude(20.0);

        Wgs84Model mockCoordinateModel = new Wgs84Model();
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

        assertThat(model, is(expected));
    }

    @Test
    void afterEntity2AbfrageSearchResultModelVerortung() {
        final var abfrage = new Bauleitplanverfahren();

        VerortungMultiPolygon verortung = new VerortungMultiPolygon();
        verortung.setCentroid(createCentroid(10.0, 20.0));
        abfrage.setVerortung(verortung);

        AbfrageSearchResultModel model = new AbfrageSearchResultModel();

        Wgs84Model mockCoordinateModel = new Wgs84Model();
        mockCoordinateModel.setLongitude(10.0);
        mockCoordinateModel.setLatitude(20.0);

        AbfrageSearchResultModel expected = new AbfrageSearchResultModel();
        expected.setCoordinate(mockCoordinateModel);

        searchDomainMapper.afterMappingEntity2SearchResultModel(abfrage, model);

        assertThat(model, is(expected));
    }

    @Test
    void afterEntity2AbfrageSearchResultModelAdresseNullVerortungNull() {
        final var abfrage = new Bauleitplanverfahren();
        abfrage.setVerortung(null);
        abfrage.setAdresse(null);

        AbfrageSearchResultModel model = new AbfrageSearchResultModel();

        AbfrageSearchResultModel expected = new AbfrageSearchResultModel();
        expected.setCoordinate(null);

        searchDomainMapper.afterMappingEntity2SearchResultModel(abfrage, model);

        assertThat(model, is(expected));
    }

    @Test
    void afterEntity2AbfrageSearchResultModelVerortungMultiPolygonNull() {
        final var abfrage = new Bauleitplanverfahren();

        VerortungMultiPolygon verortung = new VerortungMultiPolygon();
        abfrage.setVerortung(verortung);

        AbfrageSearchResultModel model = new AbfrageSearchResultModel();

        AbfrageSearchResultModel expected = new AbfrageSearchResultModel();
        expected.setCoordinate(null);

        searchDomainMapper.afterMappingEntity2SearchResultModel(abfrage, model);

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
    void getCoordinateFromAdresseOrAdresseNullAndVerortungNull() {
        assertThat(searchDomainMapper.getCoordinateFromAdresseOrVerortung(null, null), is(nullValue()));
    }

    @Test
    void getCoordinateFromAdresseOrAdresseAndVerortungNull() {
        Wgs84 coordinate = new Wgs84();
        coordinate.setLongitude(10.0);
        coordinate.setLatitude(20.0);

        Adresse adresse = new Adresse();
        adresse.setCoordinate(coordinate);

        Wgs84Model expected = new Wgs84Model();
        expected.setLongitude(10.0);
        expected.setLatitude(20.0);

        assertThat(searchDomainMapper.getCoordinateFromAdresseOrVerortung(adresse, null), is(expected));
    }

    @Test
    void getCoordinateFromAdresseOrAdresseNullAndVerortung() {
        VerortungMultiPolygon verortung = new VerortungMultiPolygon();
        verortung.setCentroid(createCentroid(10.0, 20.0));

        Wgs84Model expected = new Wgs84Model();
        expected.setLongitude(10.0);
        expected.setLatitude(20.0);

        assertThat(searchDomainMapper.getCoordinateFromAdresseOrVerortung(null, verortung), is(expected));
    }

    @Test
    void getCoordinateFromAdresseOrAdresseNullAndVerortungWithStoredCentroid() {
        VerortungMultiPolygon verortung = new VerortungMultiPolygon();
        PointGeometry centroid = new PointGeometry();
        centroid.setType("Point");
        centroid.setCoordinates(List.of(BigDecimal.valueOf(11.5), BigDecimal.valueOf(48.1)));
        verortung.setCentroid(centroid);

        Wgs84Model expected = new Wgs84Model();
        expected.setLongitude(11.5);
        expected.setLatitude(48.1);

        assertThat(searchDomainMapper.getCoordinateFromAdresseOrVerortung(null, verortung), is(expected));
    }

    @Test
    void getCoordinateFromAdresseOrAdresseNullAndVerortungWithStoredCentroidOhneGeometrie() {
        VerortungMultiPolygon verortung = new VerortungMultiPolygon();
        PointGeometry centroid = new PointGeometry();
        centroid.setType("Point");
        centroid.setCoordinates(List.of(BigDecimal.valueOf(11.5), BigDecimal.valueOf(48.1)));
        verortung.setCentroid(centroid);

        Wgs84Model expected = new Wgs84Model();
        expected.setLongitude(11.5);
        expected.setLatitude(48.1);

        assertThat(searchDomainMapper.hasVerortungCentroid(verortung), is(true));
        assertThat(searchDomainMapper.getCoordinateFromAdresseOrVerortung(null, verortung), is(expected));
    }

    @Test
    void getCoordinateFromAdresseOrAdresseNullAndVerortungWithIncompleteStoredCentroid() {
        VerortungMultiPolygon verortung = new VerortungMultiPolygon();
        PointGeometry centroid = new PointGeometry();
        centroid.setType("Point");
        centroid.setCoordinates(List.of(BigDecimal.valueOf(11.5)));
        verortung.setCentroid(centroid);

        assertThat(searchDomainMapper.getCoordinateFromAdresseOrVerortung(null, verortung), is(nullValue()));
    }

    @Test
    void entity2ModelStadtbezirkWithoutMultiPolygon() {
        Stadtbezirk stadtbezirk = new Stadtbezirk();
        stadtbezirk.setNummer("08");
        stadtbezirk.setName("Schwanthalerhöhe");

        final var result = searchDomainMapper.entity2Model(stadtbezirk);

        assertThat(result.getNummer(), is("08"));
        assertThat(result.getName(), is("Schwanthalerhöhe"));
        assertThat(result.getMultiPolygon(), is(nullValue()));
    }

    @Test
    void getCoordinateFromAdresseOrAdresseNullAndVerortungMultipolygonNull() {
        VerortungMultiPolygon verortung = new VerortungMultiPolygon();

        assertThat(searchDomainMapper.getCoordinateFromAdresseOrVerortung(null, verortung), is(nullValue()));
    }

    @Test
    void testMapAllObjectsRecordToBauvorhabenSearchResultModel() {
        UUID uuid = UUID.randomUUID();
        Set<Stadtbezirk> stadtbezirke = new HashSet<>();
        Stadtbezirk stadtbezirk = new Stadtbezirk();
        stadtbezirk.setName("Schwanthaler");
        stadtbezirk.setNummer("11110");
        stadtbezirke.add(stadtbezirk);
        VerortungMultiPolygon verortung = new VerortungMultiPolygon();
        verortung.setStadtbezirke(stadtbezirke);
        Adresse adresse = new Adresse();
        Wgs84 wgs84 = new Wgs84();
        wgs84.setLongitude(10.0);
        wgs84.setLatitude(20.0);
        adresse.setCoordinate(wgs84);

        AllObjectsRecord projection = Mockito.mock(AllObjectsRecord.class);
        Mockito.when(projection.resultType()).thenReturn(ResultType.BAUVORHABEN);
        Mockito.when(projection.getId()).thenReturn(uuid);
        Mockito.when(projection.getNameVorhaben()).thenReturn("Test Vorhaben");
        Mockito.when(projection.getGrundstuecksgroesse()).thenReturn(BigDecimal.TEN);
        Mockito.when(projection.getVerfahrensstand()).thenReturn(Verfahrensstand.STRUKTURKONZEPT);
        Mockito.when(projection.getVerortung()).thenReturn(verortung);
        Mockito.when(projection.getAdresse()).thenReturn(adresse);

        BauvorhabenSearchResultModel result =
            (BauvorhabenSearchResultModel) searchDomainMapper.mapProjectionRecordToSearchResultModel(projection);

        assertThat(result.getType(), is(SearchResultType.BAUVORHABEN));
        assertThat(result.getNameVorhaben(), is("Test Vorhaben"));
        assertThat(result.getId(), is(uuid));
        assertThat(result.getStadtbezirke().size(), is(1));
        assertThat(result.getGrundstuecksgroesse(), is(BigDecimal.TEN));
        assertThat(result.getVerfahrensstand(), is(Verfahrensstand.STRUKTURKONZEPT));
        assertThat(result.getUmgriff(), is(nullValue()));
        Wgs84Model wgs84Model = new Wgs84Model();
        wgs84Model.setLongitude(10.0);
        wgs84Model.setLatitude(20.0);
        assertThat(result.getCoordinate(), is(wgs84Model));
    }

    @Test
    void testMapAbfrageRecordToAbfrageSearchResultModel() {
        UUID uuid = UUID.randomUUID();
        Set<Stadtbezirk> stadtbezirke = new HashSet<>();
        Stadtbezirk stadtbezirk = new Stadtbezirk();
        stadtbezirk.setName("Schwanthaler");
        stadtbezirk.setNummer("11110");
        stadtbezirke.add(stadtbezirk);
        VerortungMultiPolygon verortung = new VerortungMultiPolygon();
        verortung.setStadtbezirke(stadtbezirke);
        Adresse adresse = new Adresse();
        Wgs84 wgs84 = new Wgs84();
        wgs84.setLongitude(10.0);
        wgs84.setLatitude(20.0);
        adresse.setCoordinate(wgs84);

        AbfrageRecord projection = Mockito.mock(AbfrageRecord.class);
        Mockito.when(projection.getArtAbfrage()).thenReturn(ArtAbfrage.BAULEITPLANVERFAHREN);
        Mockito.when(projection.getId()).thenReturn(uuid);
        Mockito.when(projection.getName()).thenReturn("Test Abfrage");
        Mockito.when(projection.getStatusAbfrage()).thenReturn(StatusAbfrage.ANGELEGT);
        Mockito.when(projection.getVerfahrensstand()).thenReturn(Verfahrensstand.STRUKTURKONZEPT);
        Mockito.when(projection.getFristBearbeitung()).thenReturn(LocalDate.of(2025, 1, 1));
        Mockito.when(projection.getBauvorhabenId()).thenReturn(uuid);
        Mockito.when(projection.getAdresse()).thenReturn(adresse);
        Mockito.when(projection.getVerortung()).thenReturn(verortung);
        AbfrageSearchResultModel result =
            (AbfrageSearchResultModel) searchDomainMapper.mapProjectionRecordToSearchResultModel(projection);

        assertThat(result.getType(), is(SearchResultType.ABFRAGE));
        assertThat(result.getName(), is("Test Abfrage"));
        assertThat(result.getArtAbfrage(), is(ArtAbfrage.BAULEITPLANVERFAHREN));
        assertThat(result.getId(), is(uuid));
        assertThat(result.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));
        assertThat(result.getVerfahrensstand(), is(Verfahrensstand.STRUKTURKONZEPT));
        assertThat(result.getFristBearbeitung(), is(LocalDate.of(2025, 1, 1)));
        assertThat(result.getBauvorhaben(), is(uuid));
        Wgs84Model wgs84Model = new Wgs84Model();
        wgs84Model.setLongitude(10.0);
        wgs84Model.setLatitude(20.0);
        assertThat(result.getCoordinate(), is(wgs84Model));
        assertThat(result.getStadtbezirke().size(), is(1));
    }

    @Test
    void testMapInfrastrukturRecordToInfrastruktureinrichtungSearchResultModel() {
        UUID uuid = UUID.randomUUID();
        final var point = new VerortungPoint();
        final var pointGeometryModel = new PointGeometry();
        pointGeometryModel.setType("Point");
        pointGeometryModel.setCoordinates(
            List.of(BigDecimal.valueOf(11.542026984158708), BigDecimal.valueOf(48.108341907595324))
        );
        point.setPoint(pointGeometryModel);
        InfrastrukturRecord projection = Mockito.mock(InfrastrukturRecord.class);
        Mockito.when(projection.getInfrastruktureinrichtungTyp()).thenReturn(InfrastruktureinrichtungTyp.KINDERKRIPPE);
        Mockito.when(projection.getNameEinrichtung()).thenReturn("Test Einrichtung");
        Mockito.when(projection.getId()).thenReturn(uuid);
        Mockito.when(projection.getBauvorhabenName()).thenReturn("Test Bauvorhaben");
        Mockito.when(projection.getVerortungPoint()).thenReturn(point);
        InfrastruktureinrichtungSearchResultModel result =
            (InfrastruktureinrichtungSearchResultModel) searchDomainMapper.mapProjectionRecordToSearchResultModel(
                projection
            );

        assertThat(result.getType(), is(SearchResultType.INFRASTRUKTUREINRICHTUNG));
        assertThat(result.getNameEinrichtung(), is("Test Einrichtung"));
        assertThat(result.getInfrastruktureinrichtungTyp(), is(InfrastruktureinrichtungTyp.KINDERKRIPPE));
        assertThat(result.getId(), is(uuid));
        Wgs84Model wgs84Model = new Wgs84Model();
        wgs84Model.setLongitude(11.542026984158708);
        wgs84Model.setLatitude(48.108341907595324);
        assertThat(result.getCoordinate(), is(wgs84Model));
        assertThat(result.getZugehoerigesBauvorhaben(), is("Test Bauvorhaben"));
    }

    @Test
    void testMapProjectionToSearchResultModelUnknownType() {
        Object unknownProjection = new Object();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            searchDomainMapper.mapProjectionRecordToSearchResultModel(unknownProjection);
        });

        assertThat(
            exception.getMessage(),
            is("Projection type: " + unknownProjection.getClass().getName() + " nicht unterstützt")
        );
    }

    @Test
    void testBauvorhabenInfrastruktureinrichtungRecordRecordToBauvorhabenSearchResultModel() {
        UUID uuid = UUID.randomUUID();
        Set<Stadtbezirk> stadtbezirke = new HashSet<>();
        Stadtbezirk stadtbezirk = new Stadtbezirk();
        stadtbezirk.setName("Schwanthaler");
        stadtbezirk.setNummer("11110");
        stadtbezirke.add(stadtbezirk);
        VerortungMultiPolygon verortung = new VerortungMultiPolygon();
        verortung.setStadtbezirke(stadtbezirke);
        Adresse adresse = new Adresse();
        Wgs84 wgs84 = new Wgs84();
        wgs84.setLongitude(10.0);
        wgs84.setLatitude(20.0);
        adresse.setCoordinate(wgs84);

        BauvorhabenInfrastruktureinrichtungRecord projection = Mockito.mock(
            BauvorhabenInfrastruktureinrichtungRecord.class
        );
        Mockito.when(projection.resultType()).thenReturn(ResultType.BAUVORHABEN);
        Mockito.when(projection.getId()).thenReturn(uuid);
        Mockito.when(projection.getNameVorhaben()).thenReturn("Test Vorhaben");
        Mockito.when(projection.getGrundstuecksgroesse()).thenReturn(BigDecimal.TEN);
        Mockito.when(projection.getVerfahrensstand()).thenReturn(Verfahrensstand.STRUKTURKONZEPT);
        Mockito.when(projection.getVerortung()).thenReturn(verortung);
        Mockito.when(projection.getAdresse()).thenReturn(adresse);

        BauvorhabenSearchResultModel result =
            (BauvorhabenSearchResultModel) searchDomainMapper.mapProjectionRecordToSearchResultModel(projection);

        assertThat(result.getType(), is(SearchResultType.BAUVORHABEN));
        assertThat(result.getNameVorhaben(), is("Test Vorhaben"));
        assertThat(result.getId(), is(uuid));
        assertThat(result.getStadtbezirke().size(), is(1));
        assertThat(result.getGrundstuecksgroesse(), is(BigDecimal.TEN));
        assertThat(result.getVerfahrensstand(), is(Verfahrensstand.STRUKTURKONZEPT));
        assertThat(result.getUmgriff(), is(nullValue()));
        Wgs84Model wgs84Model = new Wgs84Model();
        wgs84Model.setLongitude(10.0);
        wgs84Model.setLatitude(20.0);
        assertThat(result.getCoordinate(), is(wgs84Model));
    }

    @Test
    void testBauvorhabenInfrastruktureinrichtungRecordRecordToInfrastruktureinrichtungSearchResultModel() {
        UUID uuid = UUID.randomUUID();
        final var point = new VerortungPoint();
        final var pointGeometryModel = new PointGeometry();
        pointGeometryModel.setType("Point");
        pointGeometryModel.setCoordinates(
            List.of(BigDecimal.valueOf(11.542026984158708), BigDecimal.valueOf(48.108341907595324))
        );
        point.setPoint(pointGeometryModel);
        BauvorhabenInfrastruktureinrichtungRecord projection = Mockito.mock(
            BauvorhabenInfrastruktureinrichtungRecord.class
        );
        Mockito.when(projection.resultType()).thenReturn(ResultType.INFRASTRUKTUREINRICHTUNG);
        Mockito.when(projection.getInfrastruktureinrichtungTyp()).thenReturn(InfrastruktureinrichtungTyp.KINDERKRIPPE);
        Mockito.when(projection.getNameEinrichtung()).thenReturn("Test Einrichtung");
        Mockito.when(projection.getId()).thenReturn(uuid);
        Mockito.when(projection.getBauvorhabenName()).thenReturn("Test Bauvorhaben");
        Mockito.when(projection.getVerortungPoint()).thenReturn(point);

        InfrastruktureinrichtungSearchResultModel result =
            (InfrastruktureinrichtungSearchResultModel) searchDomainMapper.mapProjectionRecordToSearchResultModel(
                projection
            );

        assertThat(result.getType(), is(SearchResultType.INFRASTRUKTUREINRICHTUNG));
        assertThat(result.getNameEinrichtung(), is("Test Einrichtung"));
        assertThat(result.getInfrastruktureinrichtungTyp(), is(InfrastruktureinrichtungTyp.KINDERKRIPPE));
        assertThat(result.getId(), is(uuid));
        Wgs84Model wgs84Model = new Wgs84Model();
        wgs84Model.setLongitude(11.542026984158708);
        wgs84Model.setLatitude(48.108341907595324);
        assertThat(result.getCoordinate(), is(wgs84Model));
        assertThat(result.getZugehoerigesBauvorhaben(), is("Test Bauvorhaben"));
    }

    @Test
    void testBauvorhabenAbfrageRecordRecordToBauvorhabenSearchResultModel() {
        UUID uuid = UUID.randomUUID();
        Set<Stadtbezirk> stadtbezirke = new HashSet<>();
        Stadtbezirk stadtbezirk = new Stadtbezirk();
        stadtbezirk.setName("Schwanthaler");
        stadtbezirk.setNummer("11110");
        stadtbezirke.add(stadtbezirk);
        VerortungMultiPolygon verortung = new VerortungMultiPolygon();
        verortung.setStadtbezirke(stadtbezirke);
        Adresse adresse = new Adresse();
        Wgs84 wgs84 = new Wgs84();
        wgs84.setLongitude(10.0);
        wgs84.setLatitude(20.0);
        adresse.setCoordinate(wgs84);

        BauvorhabenAbfrageRecord projection = Mockito.mock(BauvorhabenAbfrageRecord.class);
        Mockito.when(projection.resultType()).thenReturn(ResultType.BAUVORHABEN);
        Mockito.when(projection.getId()).thenReturn(uuid);
        Mockito.when(projection.getNameVorhaben()).thenReturn("Test Vorhaben");
        Mockito.when(projection.getGrundstuecksgroesse()).thenReturn(BigDecimal.TEN);
        Mockito.when(projection.getVerfahrensstand()).thenReturn(Verfahrensstand.STRUKTURKONZEPT);
        Mockito.when(projection.getVerortung()).thenReturn(verortung);
        Mockito.when(projection.getAdresse()).thenReturn(adresse);

        BauvorhabenSearchResultModel result =
            (BauvorhabenSearchResultModel) searchDomainMapper.mapProjectionRecordToSearchResultModel(projection);

        assertThat(result.getType(), is(SearchResultType.BAUVORHABEN));
        assertThat(result.getNameVorhaben(), is("Test Vorhaben"));
        assertThat(result.getId(), is(uuid));
        assertThat(result.getStadtbezirke().size(), is(1));
        assertThat(result.getGrundstuecksgroesse(), is(BigDecimal.TEN));
        assertThat(result.getVerfahrensstand(), is(Verfahrensstand.STRUKTURKONZEPT));
        assertThat(result.getUmgriff(), is(nullValue()));
        Wgs84Model wgs84Model = new Wgs84Model();
        wgs84Model.setLongitude(10.0);
        wgs84Model.setLatitude(20.0);
        assertThat(result.getCoordinate(), is(wgs84Model));
    }

    @Test
    void testBauvorhabenAbfrageRecordRecordToAbfrageSearchResultModel() {
        UUID uuid = UUID.randomUUID();
        Set<Stadtbezirk> stadtbezirke = new HashSet<>();
        Stadtbezirk stadtbezirk = new Stadtbezirk();
        stadtbezirk.setName("Schwanthaler");
        stadtbezirk.setNummer("11110");
        stadtbezirke.add(stadtbezirk);
        VerortungMultiPolygon verortung = new VerortungMultiPolygon();
        verortung.setStadtbezirke(stadtbezirke);
        Adresse adresse = new Adresse();
        Wgs84 wgs84 = new Wgs84();
        wgs84.setLongitude(10.0);
        wgs84.setLatitude(20.0);
        adresse.setCoordinate(wgs84);

        BauvorhabenAbfrageRecord projection = Mockito.mock(BauvorhabenAbfrageRecord.class);
        Mockito.when(projection.resultType()).thenReturn(ResultType.ABFRAGE);
        Mockito.when(projection.getArtAbfrage()).thenReturn(ArtAbfrage.BAULEITPLANVERFAHREN);
        Mockito.when(projection.getId()).thenReturn(uuid);
        Mockito.when(projection.getName()).thenReturn("Test Abfrage");
        Mockito.when(projection.getStatusAbfrage()).thenReturn(StatusAbfrage.ANGELEGT);
        Mockito.when(projection.getVerfahrensstand()).thenReturn(Verfahrensstand.STRUKTURKONZEPT);
        Mockito.when(projection.getFristBearbeitung()).thenReturn(LocalDate.of(2025, 1, 1));
        Mockito.when(projection.getBauvorhabenId()).thenReturn(uuid);
        Mockito.when(projection.getAdresse()).thenReturn(adresse);
        Mockito.when(projection.getVerortung()).thenReturn(verortung);
        AbfrageSearchResultModel result =
            (AbfrageSearchResultModel) searchDomainMapper.mapProjectionRecordToSearchResultModel(projection);

        assertThat(result.getType(), is(SearchResultType.ABFRAGE));
        assertThat(result.getName(), is("Test Abfrage"));
        assertThat(result.getArtAbfrage(), is(ArtAbfrage.BAULEITPLANVERFAHREN));
        assertThat(result.getId(), is(uuid));
        assertThat(result.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));
        assertThat(result.getVerfahrensstand(), is(Verfahrensstand.STRUKTURKONZEPT));
        assertThat(result.getFristBearbeitung(), is(LocalDate.of(2025, 1, 1)));
        assertThat(result.getBauvorhaben(), is(uuid));
        Wgs84Model wgs84Model = new Wgs84Model();
        wgs84Model.setLongitude(10.0);
        wgs84Model.setLatitude(20.0);
        assertThat(result.getCoordinate(), is(wgs84Model));
        assertThat(result.getStadtbezirke().size(), is(1));
    }

    @Test
    void testAbfrageInfrastruktureinrichtungRecordToAbfrageSearchResultModel() {
        UUID uuid = UUID.randomUUID();
        Set<Stadtbezirk> stadtbezirke = new HashSet<>();
        Stadtbezirk stadtbezirk = new Stadtbezirk();
        stadtbezirk.setName("Schwanthaler");
        stadtbezirk.setNummer("11110");
        stadtbezirke.add(stadtbezirk);
        VerortungMultiPolygon verortung = new VerortungMultiPolygon();
        verortung.setStadtbezirke(stadtbezirke);
        Adresse adresse = new Adresse();
        Wgs84 wgs84 = new Wgs84();
        wgs84.setLongitude(10.0);
        wgs84.setLatitude(20.0);
        adresse.setCoordinate(wgs84);

        AbfrageInfrastruktureinrichtungRecord projection = Mockito.mock(AbfrageInfrastruktureinrichtungRecord.class);
        Mockito.when(projection.resultType()).thenReturn(ResultType.ABFRAGE);
        Mockito.when(projection.getArtAbfrage()).thenReturn(ArtAbfrage.BAULEITPLANVERFAHREN);
        Mockito.when(projection.getId()).thenReturn(uuid);
        Mockito.when(projection.getName()).thenReturn("Test Abfrage");
        Mockito.when(projection.getStatusAbfrage()).thenReturn(StatusAbfrage.ANGELEGT);
        Mockito.when(projection.getVerfahrensstand()).thenReturn(Verfahrensstand.STRUKTURKONZEPT);
        Mockito.when(projection.getFristBearbeitung()).thenReturn(LocalDate.of(2025, 1, 1));
        Mockito.when(projection.getBauvorhabenId()).thenReturn(uuid);
        Mockito.when(projection.getAdresse()).thenReturn(adresse);
        Mockito.when(projection.getVerortung()).thenReturn(verortung);

        AbfrageSearchResultModel result =
            (AbfrageSearchResultModel) searchDomainMapper.mapProjectionRecordToSearchResultModel(projection);

        assertThat(result.getType(), is(SearchResultType.ABFRAGE));
        assertThat(result.getName(), is("Test Abfrage"));
        assertThat(result.getArtAbfrage(), is(ArtAbfrage.BAULEITPLANVERFAHREN));
        assertThat(result.getId(), is(uuid));
        assertThat(result.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));
        assertThat(result.getVerfahrensstand(), is(Verfahrensstand.STRUKTURKONZEPT));
        assertThat(result.getFristBearbeitung(), is(LocalDate.of(2025, 1, 1)));
        assertThat(result.getBauvorhaben(), is(uuid));
        Wgs84Model wgs84Model = new Wgs84Model();
        wgs84Model.setLongitude(10.0);
        wgs84Model.setLatitude(20.0);
        assertThat(result.getCoordinate(), is(wgs84Model));
        assertThat(result.getStadtbezirke().size(), is(1));
    }

    @Test
    void testAbfrageInfrastruktureinrichtungRecordToInfrastruktureinrichtungSearchResultModel() {
        UUID uuid = UUID.randomUUID();
        final var point = new VerortungPoint();
        final var pointGeometryModel = new PointGeometry();
        pointGeometryModel.setType("Point");
        pointGeometryModel.setCoordinates(
            List.of(BigDecimal.valueOf(11.542026984158708), BigDecimal.valueOf(48.108341907595324))
        );
        point.setPoint(pointGeometryModel);
        AbfrageInfrastruktureinrichtungRecord projection = Mockito.mock(AbfrageInfrastruktureinrichtungRecord.class);
        Mockito.when(projection.resultType()).thenReturn(ResultType.INFRASTRUKTUREINRICHTUNG);
        Mockito.when(projection.getInfrastruktureinrichtungTyp()).thenReturn(InfrastruktureinrichtungTyp.KINDERKRIPPE);
        Mockito.when(projection.getNameEinrichtung()).thenReturn("Test Einrichtung");
        Mockito.when(projection.getId()).thenReturn(uuid);
        Mockito.when(projection.getBauvorhabenName()).thenReturn("Test Bauvorhaben");
        Mockito.when(projection.getVerortungPoint()).thenReturn(point);

        InfrastruktureinrichtungSearchResultModel result =
            (InfrastruktureinrichtungSearchResultModel) searchDomainMapper.mapProjectionRecordToSearchResultModel(
                projection
            );

        assertThat(result.getType(), is(SearchResultType.INFRASTRUKTUREINRICHTUNG));
        assertThat(result.getNameEinrichtung(), is("Test Einrichtung"));
        assertThat(result.getInfrastruktureinrichtungTyp(), is(InfrastruktureinrichtungTyp.KINDERKRIPPE));
        assertThat(result.getId(), is(uuid));
        Wgs84Model wgs84Model = new Wgs84Model();
        wgs84Model.setLongitude(11.542026984158708);
        wgs84Model.setLatitude(48.108341907595324);
        assertThat(result.getCoordinate(), is(wgs84Model));
        assertThat(result.getZugehoerigesBauvorhaben(), is("Test Bauvorhaben"));
    }

    private static PointGeometry createCentroid(final double longitude, final double latitude) {
        final var centroid = new PointGeometry();
        centroid.setType("Point");
        centroid.setCoordinates(List.of(BigDecimal.valueOf(longitude), BigDecimal.valueOf(latitude)));
        return centroid;
    }
}
