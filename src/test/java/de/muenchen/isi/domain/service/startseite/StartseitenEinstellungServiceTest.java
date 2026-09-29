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
import de.muenchen.isi.domain.mapper.StartseitenEinstellungDomainMapper;
import de.muenchen.isi.domain.model.enums.SchnellfilterVorgaenge;
import de.muenchen.isi.domain.model.enums.SortAttribute;
import de.muenchen.isi.domain.model.startseite.StartseitenEinstellungModel;
import de.muenchen.isi.infrastructure.entity.startseite.StartseitenEinstellung;
import de.muenchen.isi.infrastructure.repository.startseite.StartseitenEinstellungRepository;
import de.muenchen.isi.security.AuthenticationUtils;
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
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StartseitenEinstellungServiceTest {

    private static final String USER_SUB = "user-sub";

    @Mock
    private StartseitenEinstellungDomainMapper startseitenEinstellungDomainMapper;

    @Mock
    private StartseitenEinstellungRepository startseitenEinstellungRepository;

    @Mock
    private AuthenticationUtils authenticationUtils;

    private StartseitenEinstellungService startseitenEinstellungService;

    @BeforeEach
    void setUp() {
        startseitenEinstellungService = new StartseitenEinstellungService(
            startseitenEinstellungDomainMapper,
            startseitenEinstellungRepository,
            authenticationUtils
        );
        Mockito.reset(startseitenEinstellungDomainMapper, startseitenEinstellungRepository, authenticationUtils);
        when(authenticationUtils.getUserSub()).thenReturn(USER_SUB);
        when(authenticationUtils.isSubFromUnauthenticatedUser(USER_SUB)).thenReturn(false);
    }

    @Test
    void getStartseitenEinstellungLiefertDefaultsOhneDatensatz() throws Exception {
        when(startseitenEinstellungRepository.findByPersonalID(USER_SUB)).thenReturn(Optional.empty());

        final var result = startseitenEinstellungService.getStartseitenEinstellung();

        assertThat(result.getSchnellfilter(), is(StartseitenEinstellungService.DEFAULT_SCHNELLFILTER));
        assertThat(result.getSortBy(), is(StartseitenEinstellungService.DEFAULT_SORT_BY));
        assertThat(result.getSortOrder(), is(StartseitenEinstellungService.DEFAULT_SORT_ORDER));
        verify(startseitenEinstellungRepository, never()).saveAndFlush(any());
    }

    @Test
    void getStartseitenEinstellungLiefertGespeichertenDatensatz() throws Exception {
        final var entity = createEntity();
        final var model = createModel(SchnellfilterVorgaenge.ZUR_KENNTNIS, SortAttribute.FRIST_BEARBEITUNG);
        when(startseitenEinstellungRepository.findByPersonalID(USER_SUB)).thenReturn(Optional.of(entity));
        when(startseitenEinstellungDomainMapper.entity2Model(entity)).thenReturn(model);

        final var result = startseitenEinstellungService.getStartseitenEinstellung();

        assertThat(result, is(model));
    }

    @Test
    void saveLegtNeuenDatensatzMitUserSubAn() throws Exception {
        final var model = createModel(SchnellfilterVorgaenge.ZUR_BEARBEITUNG, SortAttribute.FRIST_BEARBEITUNG);
        when(startseitenEinstellungRepository.findByPersonalID(USER_SUB)).thenReturn(Optional.empty());
        when(startseitenEinstellungRepository.saveAndFlush(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(startseitenEinstellungDomainMapper.entity2Model(any())).thenReturn(model);

        final var result = startseitenEinstellungService.save(model);

        final var captor = ArgumentCaptor.forClass(StartseitenEinstellung.class);
        verify(startseitenEinstellungRepository).saveAndFlush(captor.capture());
        assertThat(captor.getValue().getPersonalID(), is(USER_SUB));
        verify(startseitenEinstellungDomainMapper).updateEntityFromModel(model, captor.getValue());
        assertThat(result, is(model));
    }

    @Test
    void saveAktualisiertBestehendenDatensatz() throws Exception {
        final var entity = createEntity();
        final var model = createModel(SchnellfilterVorgaenge.ABGESCHLOSSEN, SortAttribute.LAST_MODIFIED_DATE_TIME);
        when(startseitenEinstellungRepository.findByPersonalID(USER_SUB)).thenReturn(Optional.of(entity));
        when(startseitenEinstellungRepository.saveAndFlush(entity)).thenReturn(entity);
        when(startseitenEinstellungDomainMapper.entity2Model(entity)).thenReturn(model);

        final var result = startseitenEinstellungService.save(model);

        verify(startseitenEinstellungDomainMapper).updateEntityFromModel(model, entity);
        assertThat(result, is(model));
    }

    @Test
    void saveWirftOptimisticLockingException() {
        final var entity = createEntity();
        final var model = createModel(SchnellfilterVorgaenge.ALLE, SortAttribute.CREATED_DATE_TIME);
        when(startseitenEinstellungRepository.findByPersonalID(USER_SUB)).thenReturn(Optional.of(entity));
        when(startseitenEinstellungRepository.saveAndFlush(entity)).thenThrow(
            new ObjectOptimisticLockingFailureException(StartseitenEinstellung.class, entity.getId())
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
        verify(startseitenEinstellungRepository, never()).saveAndFlush(any());
    }

    private static StartseitenEinstellung createEntity() {
        final var entity = new StartseitenEinstellung();
        entity.setPersonalID(USER_SUB);
        entity.setSchnellfilter(SchnellfilterVorgaenge.ALLE);
        entity.setSortBy(SortAttribute.CREATED_DATE_TIME);
        entity.setSortOrder(SortOrder.DESC);
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
