package de.muenchen.isi.domain.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import de.muenchen.isi.domain.exception.GeometryOperationFailedException;
import de.muenchen.isi.domain.model.common.WGS84Model;
import de.muenchen.isi.domain.model.search.response.BauvorhabenSearchResultModel;
import de.muenchen.isi.domain.service.KoordinatenService;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.common.Wgs84;
import java.util.UUID;
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
    private KoordinatenService koordinatenService;

    private KoordinatenDomainMapper koordinatenDomainMapper = new KoordinatenDomainMapperImpl();

    private SearchDomainMapper searchDomainMapper = new SearchDomainMapperImpl();

    @Test
    void testAfterEntity2BauvorhabenSearchResultModel() throws GeometryOperationFailedException {
        // Mock-Coordinate erstellen
        Wgs84 mockCoordinate = new Wgs84();
        mockCoordinate.setLongitude(10.0);
        mockCoordinate.setLatitude(20.0);

        WGS84Model mockCoordinateModel = new WGS84Model();
        mockCoordinate.setLongitude(10.0);
        mockCoordinate.setLatitude(20.0);

        // Mocks für Bauvorhaben und Adresse erstellen
        final var bauvorhaben = new Bauvorhaben();
        bauvorhaben.setId(UUID.randomUUID());

        Adresse adresse = new Adresse();
        adresse.setCoordinate(mockCoordinate);
        bauvorhaben.setAdresse(adresse);

        // Mock-Model erstellen
        BauvorhabenSearchResultModel model = new BauvorhabenSearchResultModel();

        // Test der Methode
        searchDomainMapper.afterEntity2SearchResultModel(bauvorhaben, model);

        // Verifikation der Aufrufe und Ergebnisse
        verify(koordinatenDomainMapper, times(1)).entity2Model(adresse.getCoordinate());
        verifyNoMoreInteractions(koordinatenDomainMapper, koordinatenService);

        assertThat(model.getCoordinate(), is(mockCoordinateModel));
    }
}
