package de.muenchen.isi.domain.service.startseite;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.domain.mapper.BenutzerDomainMapper;
import de.muenchen.isi.domain.model.enums.SchnellfilterVorgaenge;
import de.muenchen.isi.domain.model.enums.SortAttribute;
import de.muenchen.isi.domain.model.startseite.StartseitenEinstellungModel;
import de.muenchen.isi.domain.service.benutzer.BenutzerService;
import de.muenchen.isi.infrastructure.entity.benutzer.Benutzer;
import de.muenchen.isi.infrastructure.entity.benutzer.StartseitenEinstellung;
import java.util.Optional;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StartseitenEinstellungServiceTest {

    private static final String USER_SUB = "user-sub";

    @Mock
    private BenutzerDomainMapper benutzerDomainMapper;

    @Mock
    private BenutzerService benutzerService;

    private StartseitenEinstellungService startseitenEinstellungService;

    @BeforeEach
    void setUp() {
        startseitenEinstellungService = new StartseitenEinstellungService(benutzerDomainMapper, benutzerService);
        Mockito.reset(benutzerDomainMapper, benutzerService);
    }

    @Test
    void getStartseitenEinstellungLiefertDefaultsOhneDatensatz() throws Exception {
        when(benutzerService.findBenutzer(any())).thenReturn(Optional.empty());

        final var result = startseitenEinstellungService.getStartseitenEinstellung();

        assertThat(result.getSchnellfilter(), is(StartseitenEinstellungService.DEFAULT_SCHNELLFILTER));
        assertThat(result.getSortBy(), is(StartseitenEinstellungService.DEFAULT_SORT_BY));
        assertThat(result.getSortOrder(), is(StartseitenEinstellungService.DEFAULT_SORT_ORDER));
        verify(benutzerService, never()).save(any());
    }

    @Test
    void getStartseitenEinstellungLiefertDefaultsWennDasAttributNochLeerIst() throws Exception {
        // Ein Nutzer kann bereits persönliche Filter besitzen, ohne die Startseite konfiguriert zu haben.
        final var benutzer = createBenutzer(null);
        when(benutzerService.findBenutzer(any())).thenReturn(Optional.of(benutzer));
        when(benutzerDomainMapper.entity2Model(null)).thenReturn(null);

        final var result = startseitenEinstellungService.getStartseitenEinstellung();

        assertThat(result.getSchnellfilter(), is(StartseitenEinstellungService.DEFAULT_SCHNELLFILTER));
        assertThat(result.getSortBy(), is(StartseitenEinstellungService.DEFAULT_SORT_BY));
        assertThat(result.getSortOrder(), is(StartseitenEinstellungService.DEFAULT_SORT_ORDER));
    }

    @Test
    void getStartseitenEinstellungLiefertGespeichertenDatensatz() throws Exception {
        final var einstellung = createEinstellung(SchnellfilterVorgaenge.ZUR_KENNTNIS);
        final var model = createModel(SchnellfilterVorgaenge.ZUR_KENNTNIS);
        when(benutzerService.findBenutzer(any())).thenReturn(Optional.of(createBenutzer(einstellung)));
        when(benutzerDomainMapper.entity2Model(einstellung)).thenReturn(model);

        final var result = startseitenEinstellungService.getStartseitenEinstellung();

        assertThat(result, is(model));
    }

    @Test
    void saveUebernimmtDieEinstellungInDenBenutzerDatensatz() throws Exception {
        final var model = createModel(SchnellfilterVorgaenge.ZUR_BEARBEITUNG);
        final var einstellung = createEinstellung(SchnellfilterVorgaenge.ZUR_BEARBEITUNG);
        final var benutzer = createBenutzer(null);
        when(benutzerService.getOrCreateBenutzer(any())).thenReturn(benutzer);
        when(benutzerDomainMapper.model2Entity(model)).thenReturn(einstellung);
        when(benutzerService.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(benutzerDomainMapper.entity2Model(einstellung)).thenReturn(model);

        final var result = startseitenEinstellungService.save(model);

        final var captor = ArgumentCaptor.forClass(Benutzer.class);
        verify(benutzerService).save(captor.capture());
        assertThat(captor.getValue().getStartseitenEinstellung(), is(einstellung));
        assertThat(result, is(model));
    }

    @Test
    void saveReichtOptimisticLockingExceptionDurch() throws Exception {
        final var model = createModel(SchnellfilterVorgaenge.ALLE);
        when(benutzerService.getOrCreateBenutzer(any())).thenReturn(createBenutzer(null));
        when(benutzerService.save(any())).thenThrow(new OptimisticLockingException("Konflikt", new RuntimeException()));

        assertThrows(OptimisticLockingException.class, () -> startseitenEinstellungService.save(model));
    }

    @Test
    void nichtAuthentifizierterNutzerWirdAbgelehnt() throws Exception {
        when(benutzerService.findBenutzer(any())).thenThrow(new UserRoleNotAllowedException("nicht erlaubt"));
        when(benutzerService.getOrCreateBenutzer(any())).thenThrow(new UserRoleNotAllowedException("nicht erlaubt"));

        assertThrows(UserRoleNotAllowedException.class, () ->
            startseitenEinstellungService.getStartseitenEinstellung()
        );
        assertThrows(UserRoleNotAllowedException.class, () ->
            startseitenEinstellungService.save(createModel(SchnellfilterVorgaenge.ALLE))
        );
        verify(benutzerService, never()).save(any());
    }

    private static Benutzer createBenutzer(final StartseitenEinstellung startseitenEinstellung) {
        final var benutzer = new Benutzer();
        benutzer.setPersonalID(USER_SUB);
        benutzer.setStartseitenEinstellung(startseitenEinstellung);
        return benutzer;
    }

    private static StartseitenEinstellung createEinstellung(final SchnellfilterVorgaenge schnellfilter) {
        final var einstellung = new StartseitenEinstellung();
        einstellung.setSchnellfilter(schnellfilter);
        einstellung.setSortBy(SortAttribute.FRIST_BEARBEITUNG);
        einstellung.setSortOrder(SortOrder.ASC);
        return einstellung;
    }

    private static StartseitenEinstellungModel createModel(final SchnellfilterVorgaenge schnellfilter) {
        final var model = new StartseitenEinstellungModel();
        model.setSchnellfilter(schnellfilter);
        model.setSortBy(SortAttribute.FRIST_BEARBEITUNG);
        model.setSortOrder(SortOrder.ASC);
        return model;
    }
}
