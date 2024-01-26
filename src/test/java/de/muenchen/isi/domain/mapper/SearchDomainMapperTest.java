package de.muenchen.isi.domain.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import de.muenchen.isi.domain.exception.GeometryOperationFailedException;
import de.muenchen.isi.domain.model.common.WGS84Model;
import de.muenchen.isi.domain.model.search.response.AbfrageSearchResultModel;
import de.muenchen.isi.domain.model.search.response.BauvorhabenSearchResultModel;
import de.muenchen.isi.domain.model.search.response.InfrastruktureinrichtungSearchResultModel;
import de.muenchen.isi.domain.model.search.response.SearchResultModel;
import de.muenchen.isi.domain.service.KoordinatenService;
import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.Baugenehmigungsverfahren;
import de.muenchen.isi.infrastructure.entity.Bauleitplanverfahren;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.WeiteresVerfahren;
import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.common.MultiPolygonGeometry;
import de.muenchen.isi.infrastructure.entity.common.VerortungMultiPolygon;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import de.muenchen.isi.infrastructure.repository.AbfrageRepository;
import de.muenchen.isi.infrastructure.repository.BauvorhabenRepository;
import de.muenchen.isi.infrastructure.repository.InfrastruktureinrichtungRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@Disabled("Disabled bis ich ihn lokal richtig testen kann")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class SearchDomainMapperTest {

    @Mock
    private BauvorhabenRepository bauvorhabenRepository;

    @Mock
    private AbfrageRepository abfrageRepository;

    @Mock
    private KoordinatenService koordinatenService;

    @Mock
    private InfrastruktureinrichtungRepository infrastruktureinrichtungRepository;

    private KoordinatenDomainMapper koordinatenDomainMapper = new KoordinatenDomainMapperImpl();

    private SearchDomainMapper searchDomainMapper = new SearchDomainMapper() {
        @Override
        public SearchResultModel entity2SearchResultModel(BaseEntity entity) {
            return null;
        }

        @Override
        public BauvorhabenSearchResultModel entity2SearchResultModel(Bauvorhaben entity) {
            return null;
        }

        @Override
        public AbfrageSearchResultModel entity2SearchResultModel(Bauleitplanverfahren entity) {
            return null;
        }

        @Override
        public AbfrageSearchResultModel entity2SearchResultModel(Baugenehmigungsverfahren entity) {
            return null;
        }

        @Override
        public AbfrageSearchResultModel entity2SearchResultModel(WeiteresVerfahren entity) {
            return null;
        }

        @Override
        public InfrastruktureinrichtungSearchResultModel entity2SearchResultModel(Infrastruktureinrichtung entity) {
            return null;
        }
    };

    @Test
    void testAfterEntity2BauvorhabenSearchResultModel() throws GeometryOperationFailedException {
        // Mocks für Bauvorhaben und Adresse erstellen
        Bauvorhaben bauvorhaben = mock(Bauvorhaben.class);
        Adresse adresse = mock(Adresse.class);
        when(bauvorhaben.getAdresse()).thenReturn(adresse);

        // Mock für MultiPolygon erstellen
        MultiPolygonGeometry multiPolygon = mock(MultiPolygonGeometry.class);
        VerortungMultiPolygon verortung = mock(VerortungMultiPolygon.class);
        when(verortung.getMultiPolygon()).thenReturn(multiPolygon);

        // Mock-Model erstellen
        BauvorhabenSearchResultModel model = new BauvorhabenSearchResultModel();

        // Mock-Coordinate erstellen
        WGS84Model mockCoordinate = new WGS84Model();
        mockCoordinate.setLongitude(10.0);
        mockCoordinate.setLatitude(20.0);

        // Mock-Verhalten konfigurieren
        when(koordinatenDomainMapper.entity2Model(adresse.getCoordinate())).thenReturn(mockCoordinate);
        when(koordinatenService.getMultiPolygonCentroid(multiPolygon)).thenReturn(mockCoordinate);

        // Test der Methode
        searchDomainMapper.afterEntity2SearchResultModel(bauvorhaben, model);

        // Verifikation der Aufrufe und Ergebnisse
        verify(koordinatenDomainMapper, times(1)).entity2Model(adresse.getCoordinate());
        verify(koordinatenService, times(1)).getMultiPolygonCentroid(multiPolygon);
        verifyNoMoreInteractions(koordinatenDomainMapper, koordinatenService);

        assertThat(model.getCoordinate(), is(mockCoordinate));
    }

    @Test
    void testAfterEntity2BauleitplanverfahrenSearchResultModel() throws GeometryOperationFailedException {
        // Mocks für Bauvorhaben und Adresse erstellen
        Bauleitplanverfahren bauleitplanverfahren = mock(Bauleitplanverfahren.class);
        Adresse adresse = mock(Adresse.class);
        when(bauleitplanverfahren.getAdresse()).thenReturn(adresse);

        // Mock für MultiPolygon erstellen
        MultiPolygonGeometry multiPolygon = mock(MultiPolygonGeometry.class);
        VerortungMultiPolygon verortung = mock(VerortungMultiPolygon.class);
        when(verortung.getMultiPolygon()).thenReturn(multiPolygon);

        // Mock-Model erstellen
        AbfrageSearchResultModel model = new AbfrageSearchResultModel();

        // Mock-Coordinate erstellen
        WGS84Model mockCoordinate = new WGS84Model();
        mockCoordinate.setLongitude(10.0);
        mockCoordinate.setLatitude(20.0);

        // Mock-Verhalten konfigurieren
        when(koordinatenDomainMapper.entity2Model(adresse.getCoordinate())).thenReturn(mockCoordinate);
        when(koordinatenService.getMultiPolygonCentroid(multiPolygon)).thenReturn(mockCoordinate);

        // Test der Methode
        searchDomainMapper.afterEntity2SearchResultModel(bauleitplanverfahren, model);

        // Verifikation der Aufrufe und Ergebnisse
        verify(koordinatenDomainMapper, times(1)).entity2Model(adresse.getCoordinate());
        verify(koordinatenService, times(1)).getMultiPolygonCentroid(multiPolygon);
        verifyNoMoreInteractions(koordinatenDomainMapper, koordinatenService);

        assertThat(model.getCoordinate(), is(mockCoordinate));
    }

    @Test
    void testAfterEntity2WeiteresVerfahrenSearchResultModel() throws GeometryOperationFailedException {
        // Mocks für Bauvorhaben und Adresse erstellen
        WeiteresVerfahren weiteresVerfahren = mock(WeiteresVerfahren.class);
        Adresse adresse = mock(Adresse.class);
        when(weiteresVerfahren.getAdresse()).thenReturn(adresse);

        // Mock für MultiPolygon erstellen
        MultiPolygonGeometry multiPolygon = mock(MultiPolygonGeometry.class);
        VerortungMultiPolygon verortung = mock(VerortungMultiPolygon.class);
        when(verortung.getMultiPolygon()).thenReturn(multiPolygon);

        // Mock-Model erstellen
        AbfrageSearchResultModel model = new AbfrageSearchResultModel();

        // Mock-Coordinate erstellen
        WGS84Model mockCoordinate = new WGS84Model();
        mockCoordinate.setLongitude(10.0);
        mockCoordinate.setLatitude(20.0);

        // Mock-Verhalten konfigurieren
        when(koordinatenDomainMapper.entity2Model(adresse.getCoordinate())).thenReturn(mockCoordinate);
        when(koordinatenService.getMultiPolygonCentroid(multiPolygon)).thenReturn(mockCoordinate);

        // Test der Methode
        searchDomainMapper.afterEntity2SearchResultModel(weiteresVerfahren, model);

        // Verifikation der Aufrufe und Ergebnisse
        verify(koordinatenDomainMapper, times(1)).entity2Model(adresse.getCoordinate());
        verify(koordinatenService, times(1)).getMultiPolygonCentroid(multiPolygon);
        verifyNoMoreInteractions(koordinatenDomainMapper, koordinatenService);

        assertThat(model.getCoordinate(), is(mockCoordinate));
    }

    @Test
    void testAfterEntity2BaugenehmigungsverfahrenSearchResultModel() throws GeometryOperationFailedException {
        // Mocks für Bauvorhaben und Adresse erstellen
        Baugenehmigungsverfahren baugenehmigungsverfahren = mock(Baugenehmigungsverfahren.class);
        Adresse adresse = mock(Adresse.class);
        when(baugenehmigungsverfahren.getAdresse()).thenReturn(adresse);

        // Mock für MultiPolygon erstellen
        MultiPolygonGeometry multiPolygon = mock(MultiPolygonGeometry.class);
        VerortungMultiPolygon verortung = mock(VerortungMultiPolygon.class);
        when(verortung.getMultiPolygon()).thenReturn(multiPolygon);

        // Mock-Model erstellen
        AbfrageSearchResultModel model = new AbfrageSearchResultModel();

        // Mock-Coordinate erstellen
        WGS84Model mockCoordinate = new WGS84Model();
        mockCoordinate.setLongitude(10.0);
        mockCoordinate.setLatitude(20.0);

        // Mock-Verhalten konfigurieren
        when(koordinatenDomainMapper.entity2Model(adresse.getCoordinate())).thenReturn(mockCoordinate);
        when(koordinatenService.getMultiPolygonCentroid(multiPolygon)).thenReturn(mockCoordinate);

        // Test der Methode
        searchDomainMapper.afterEntity2SearchResultModel(baugenehmigungsverfahren, model);

        // Verifikation der Aufrufe und Ergebnisse
        verify(koordinatenDomainMapper, times(1)).entity2Model(adresse.getCoordinate());
        verify(koordinatenService, times(1)).getMultiPolygonCentroid(multiPolygon);
        verifyNoMoreInteractions(koordinatenDomainMapper, koordinatenService);

        assertThat(model.getCoordinate(), is(mockCoordinate));
    }

    @Test
    void testAfterEntity2InfrastruktureinrichtungSearchResultModel() throws GeometryOperationFailedException {
        // Mocks für Bauvorhaben und Adresse erstellen
        Infrastruktureinrichtung infrastruktureinrichtung = mock(Infrastruktureinrichtung.class);
        Adresse adresse = mock(Adresse.class);
        when(infrastruktureinrichtung.getAdresse()).thenReturn(adresse);

        // Mock-Model erstellen
        InfrastruktureinrichtungSearchResultModel model = new InfrastruktureinrichtungSearchResultModel();

        // Mock-Coordinate erstellen
        WGS84Model mockCoordinate = new WGS84Model();
        mockCoordinate.setLongitude(10.0);
        mockCoordinate.setLatitude(20.0);

        // Mock-Verhalten konfigurieren
        when(koordinatenDomainMapper.entity2Model(adresse.getCoordinate())).thenReturn(mockCoordinate);

        // Test der Methode
        searchDomainMapper.afterEntity2SearchResultModel(infrastruktureinrichtung, model);

        // Verifikation der Aufrufe und Ergebnisse
        verify(koordinatenDomainMapper, times(1)).entity2Model(adresse.getCoordinate());
        verifyNoMoreInteractions(koordinatenDomainMapper, koordinatenService);

        assertThat(model.getCoordinate(), is(mockCoordinate));
    }
}
