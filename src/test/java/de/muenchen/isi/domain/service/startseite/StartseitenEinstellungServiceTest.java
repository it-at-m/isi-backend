package de.muenchen.isi.domain.service.startseite;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.domain.model.enums.SchnellfilterVorgaenge;
import de.muenchen.isi.domain.model.enums.SortAttribute;
import de.muenchen.isi.domain.model.startseite.StartseitenEinstellungModel;
import de.muenchen.isi.infrastructure.entity.filter.FilterSettings;
import de.muenchen.isi.infrastructure.entity.filter.PersonalFilter;
import de.muenchen.isi.infrastructure.repository.filter.PersonalFilterRepository;
import de.muenchen.isi.security.AuthenticationUtils;
import java.util.Optional;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StartseitenEinstellungServiceTest {

    private static final String USER_SUB = "user-sub";

    @Mock
    private PersonalFilterRepository personalFilterRepository;

    @Spy
    private AuthenticationUtils authenticationUtils;

    private StartseitenEinstellungService startseitenEinstellungService;

    @BeforeEach
    void setUp() {
        startseitenEinstellungService = new StartseitenEinstellungService(
            personalFilterRepository,
            authenticationUtils
        );
        Mockito.reset(personalFilterRepository, authenticationUtils);
        when(authenticationUtils.getUserSub()).thenReturn(USER_SUB);
        when(authenticationUtils.isSubFromUnauthenticatedUser(USER_SUB)).thenReturn(false);
    }

    @Test
    void getStartseitenEinstellungLiefertDefaultsOhneDatensatz() throws Exception {
        when(personalFilterRepository.findByPersonalIDAndIstStartseiteTrue(USER_SUB)).thenReturn(Optional.empty());

        final var result = startseitenEinstellungService.getStartseitenEinstellung();

        assertThat(result.getSchnellfilter(), is(StartseitenEinstellungService.DEFAULT_SCHNELLFILTER));
        assertThat(result.getSortBy(), is(StartseitenEinstellungService.DEFAULT_SORT_BY));
        assertThat(result.getSortOrder(), is(StartseitenEinstellungService.DEFAULT_SORT_ORDER));
        verify(personalFilterRepository, never()).saveAndFlush(any());
    }

    @Test
    void getStartseitenEinstellungLiefertGespeichertenDatensatz() throws Exception {
        final var entity = createEntity(SchnellfilterVorgaenge.ZUR_KENNTNIS, SortAttribute.FRIST_BEARBEITUNG);
        when(personalFilterRepository.findByPersonalIDAndIstStartseiteTrue(USER_SUB)).thenReturn(Optional.of(entity));

        final var result = startseitenEinstellungService.getStartseitenEinstellung();

        assertThat(result.getSchnellfilter(), is(SchnellfilterVorgaenge.ZUR_KENNTNIS));
        assertThat(result.getSortBy(), is(SortAttribute.FRIST_BEARBEITUNG));
        assertThat(result.getSortOrder(), is(SortOrder.DESC));
    }

    @Test
    void saveLegtNeueStartseitenZeileAn() throws Exception {
        final var model = createModel(SchnellfilterVorgaenge.ZUR_BEARBEITUNG, SortAttribute.FRIST_BEARBEITUNG);
        when(personalFilterRepository.findByPersonalIDAndIstStartseiteTrue(USER_SUB)).thenReturn(Optional.empty());
        when(personalFilterRepository.saveAndFlush(any())).thenAnswer(invocation -> invocation.getArgument(0));

        final var result = startseitenEinstellungService.save(model);

        final var captor = ArgumentCaptor.forClass(PersonalFilter.class);
        verify(personalFilterRepository).saveAndFlush(captor.capture());
        final var gespeichert = captor.getValue();
        assertThat(gespeichert.getPersonalID(), is(USER_SUB));
        assertThat(gespeichert.getIstStartseite(), is(true));
        assertThat(gespeichert.getFilterName(), is(PersonalFilter.STARTSEITE_FILTER_NAME));
        assertThat(gespeichert.getFilterSettings().getSchnellfilter(), is(SchnellfilterVorgaenge.ZUR_BEARBEITUNG));
        assertThat(gespeichert.getFilterSettings().getSortBy(), is(SortAttribute.FRIST_BEARBEITUNG));
        assertThat(gespeichert.getFilterSettings().getSortOrder(), is(SortOrder.ASC));
        // Die Startseiten-Zeile nutzt die uebrigen Filtereinstellungen nicht.
        assertThat(gespeichert.getFilterSettings().getSelectBauleitplanverfahren(), is(nullValue()));
        assertThat(gespeichert.getFilterSettings().getSobonRelevant(), is(nullValue()));
        assertThat(result, is(model));
    }

    @Test
    void saveAktualisiertBestehendeStartseitenZeile() throws Exception {
        final var entity = createEntity(SchnellfilterVorgaenge.ALLE, SortAttribute.CREATED_DATE_TIME);
        final var model = createModel(SchnellfilterVorgaenge.ABGESCHLOSSEN, SortAttribute.LAST_MODIFIED_DATE_TIME);
        when(personalFilterRepository.findByPersonalIDAndIstStartseiteTrue(USER_SUB)).thenReturn(Optional.of(entity));
        when(personalFilterRepository.saveAndFlush(entity)).thenReturn(entity);

        final var result = startseitenEinstellungService.save(model);

        verify(personalFilterRepository).saveAndFlush(entity);
        assertThat(entity.getFilterSettings().getSchnellfilter(), is(SchnellfilterVorgaenge.ABGESCHLOSSEN));
        assertThat(entity.getFilterSettings().getSortBy(), is(SortAttribute.LAST_MODIFIED_DATE_TIME));
        assertThat(entity.getFilterSettings().getSortOrder(), is(SortOrder.ASC));
        assertThat(result, is(model));
    }

    @Test
    void saveWirftOptimisticLockingException() {
        final var entity = createEntity(SchnellfilterVorgaenge.ALLE, SortAttribute.CREATED_DATE_TIME);
        final var model = createModel(SchnellfilterVorgaenge.ALLE, SortAttribute.CREATED_DATE_TIME);
        when(personalFilterRepository.findByPersonalIDAndIstStartseiteTrue(USER_SUB)).thenReturn(Optional.of(entity));
        when(personalFilterRepository.saveAndFlush(entity)).thenThrow(
            new ObjectOptimisticLockingFailureException(PersonalFilter.class, entity.getId())
        );

        assertThrows(OptimisticLockingException.class, () -> startseitenEinstellungService.save(model));
    }

    @Test
    void saveWirftOptimisticLockingExceptionBeiParallelemErstanlegen() {
        final var model = createModel(SchnellfilterVorgaenge.ALLE, SortAttribute.CREATED_DATE_TIME);
        when(personalFilterRepository.findByPersonalIDAndIstStartseiteTrue(USER_SUB)).thenReturn(Optional.empty());
        // Der Unique-Index auf (personalid) WHERE ist_startseite greift, wenn ein paralleler Request
        // die Startseiten-Zeile bereits angelegt hat.
        when(personalFilterRepository.saveAndFlush(any())).thenThrow(
            new DataIntegrityViolationException("personal_filter_startseite_personalid_uidx")
        );

        assertThrows(OptimisticLockingException.class, () -> startseitenEinstellungService.save(model));
    }

    @Test
    void nichtAuthentifizierterNutzerWirdAbgelehnt() {
        when(authenticationUtils.isSubFromUnauthenticatedUser(USER_SUB)).thenReturn(true);

        assertThrows(UserRoleNotAllowedException.class, () ->
            startseitenEinstellungService.getStartseitenEinstellung()
        );
        assertThrows(UserRoleNotAllowedException.class, () ->
            startseitenEinstellungService.save(createModel(SchnellfilterVorgaenge.ALLE, SortAttribute.NAME))
        );
        verify(personalFilterRepository, never()).saveAndFlush(any());
    }

    private static PersonalFilter createEntity(final SchnellfilterVorgaenge schnellfilter, final SortAttribute sortBy) {
        final var filterSettings = new FilterSettings();
        filterSettings.setSchnellfilter(schnellfilter);
        filterSettings.setSortBy(sortBy);
        filterSettings.setSortOrder(SortOrder.DESC);
        final var entity = new PersonalFilter();
        entity.setPersonalID(USER_SUB);
        entity.setFilterName(PersonalFilter.STARTSEITE_FILTER_NAME);
        entity.setIstStartseite(true);
        entity.setFilterSettings(filterSettings);
        return entity;
    }

    private static StartseitenEinstellungModel createModel(
        final SchnellfilterVorgaenge schnellfilter,
        final SortAttribute sortBy
    ) {
        final var model = new StartseitenEinstellungModel();
        model.setSchnellfilter(schnellfilter);
        model.setSortBy(sortBy);
        model.setSortOrder(SortOrder.ASC);
        return model;
    }
}
