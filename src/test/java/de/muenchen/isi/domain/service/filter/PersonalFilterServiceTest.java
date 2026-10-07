package de.muenchen.isi.domain.service.filter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.MaxCreationsReachedException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.domain.mapper.PersonalFilterDomainMapper;
import de.muenchen.isi.domain.model.filter.PersonalFilterRequestModel;
import de.muenchen.isi.domain.model.filter.PersonalFilterResponseModel;
import de.muenchen.isi.domain.service.benutzer.BenutzerService;
import de.muenchen.isi.infrastructure.entity.benutzer.Benutzer;
import de.muenchen.isi.infrastructure.entity.filter.PersoenlicherFilter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
class PersonalFilterServiceTest {

    private static final String USER_SUB = "user-sub";

    @Mock
    private PersonalFilterDomainMapper personalFilterDomainMapper;

    @Mock
    private BenutzerService benutzerService;

    private PersonalFilterService personalFilterService;

    @BeforeEach
    void setUp() {
        personalFilterService = new PersonalFilterService(personalFilterDomainMapper, benutzerService);
        Mockito.reset(personalFilterDomainMapper, benutzerService);
    }

    @Test
    void getPersonalFiltersLiefertAbsteigendNachLetzterAenderung() throws Exception {
        final var aelter = createFilter("alt", LocalDateTime.of(2026, 1, 1, 10, 0));
        final var neuer = createFilter("neu", LocalDateTime.of(2026, 5, 1, 10, 0));
        when(benutzerService.findBenutzer(any())).thenReturn(Optional.of(createBenutzer(aelter, neuer)));
        when(personalFilterDomainMapper.entities2Models(any())).thenAnswer(invocation -> {
            final List<PersoenlicherFilter> uebergeben = invocation.getArgument(0);
            return uebergeben.stream().map(PersonalFilterServiceTest::toModel).collect(Collectors.toList());
        });

        final var result = personalFilterService.getPersonalFilters();

        final var namen = result.stream().map(PersonalFilterResponseModel::getFilterName).collect(Collectors.toList());
        assertThat(namen, contains("neu", "alt"));
    }

    @Test
    void getPersonalFiltersOhneDatensatzLiefertLeereListe() throws Exception {
        when(benutzerService.findBenutzer(any())).thenReturn(Optional.empty());
        when(personalFilterDomainMapper.entities2Models(any())).thenReturn(List.of());

        assertThat(personalFilterService.getPersonalFilters(), is(empty()));
    }

    @Test
    void getPersonalFiltersUnauthenticatedUser() throws Exception {
        when(benutzerService.findBenutzer(any())).thenThrow(new UserRoleNotAllowedException("nicht erlaubt"));

        assertThrows(UserRoleNotAllowedException.class, () -> personalFilterService.getPersonalFilters());
    }

    @Test
    void getByFilterID() throws Exception {
        final var filter = createFilter("meiner", LocalDateTime.now());
        final var model = new PersonalFilterResponseModel();
        when(benutzerService.findBenutzer(any())).thenReturn(Optional.of(createBenutzer(filter)));
        when(personalFilterDomainMapper.entity2Model(filter)).thenReturn(model);

        assertThat(personalFilterService.getByFilterID(filter.getId()), is(model));
    }

    @Test
    void getByFilterIDUnbekannteIdWirftEntityNotFound() throws Exception {
        when(benutzerService.findBenutzer(any())).thenReturn(Optional.of(createBenutzer()));

        assertThrows(EntityNotFoundException.class, () -> personalFilterService.getByFilterID(UUID.randomUUID()));
    }

    @Test
    void save() throws Exception {
        final var benutzer = createBenutzer();
        final var neuerFilter = new PersoenlicherFilter();
        final var model = new PersonalFilterResponseModel();
        when(benutzerService.getOrCreateBenutzer(any())).thenReturn(benutzer);
        when(personalFilterDomainMapper.model2Entity(any())).thenReturn(neuerFilter);
        when(personalFilterDomainMapper.entity2Model(neuerFilter)).thenReturn(model);

        final var result = personalFilterService.save(new PersonalFilterRequestModel());

        assertThat(result, is(model));
        assertThat(benutzer.getPersoenlicheFilter(), contains(neuerFilter));
        // Id und Zeitstempel werden im Service vergeben, nicht vom JPA-Auditing.
        assertThat(neuerFilter.getId(), is(notNullValue()));
        assertThat(neuerFilter.getCreatedDateTime(), is(notNullValue()));
        assertThat(neuerFilter.getLastModifiedDateTime(), is(notNullValue()));
        verify(benutzerService).save(benutzer);
    }

    @Test
    void saveMaxFiltersReachedThrowsMaxCreationsReachedException() throws Exception {
        final var vorhandene = IntStream.range(0, 10)
            .mapToObj(index -> createFilter("filter" + index, LocalDateTime.now()))
            .toArray(PersoenlicherFilter[]::new);
        when(benutzerService.getOrCreateBenutzer(any())).thenReturn(createBenutzer(vorhandene));

        final var exception = assertThrows(MaxCreationsReachedException.class, () ->
            personalFilterService.save(new PersonalFilterRequestModel())
        );
        assertThat(exception.getMessage(), containsString("Maximale Anzahl an persönlichen Filtern erreicht"));
        verify(benutzerService, never()).save(any());
    }

    @Test
    void saveReichtOptimisticLockingExceptionDurch() throws Exception {
        when(benutzerService.getOrCreateBenutzer(any())).thenReturn(createBenutzer());
        when(personalFilterDomainMapper.model2Entity(any())).thenReturn(new PersoenlicherFilter());
        when(benutzerService.save(any())).thenThrow(new OptimisticLockingException("Konflikt", new RuntimeException()));

        assertThrows(OptimisticLockingException.class, () ->
            personalFilterService.save(new PersonalFilterRequestModel())
        );
    }

    @Test
    void update() throws Exception {
        final var filter = createFilter("alt", LocalDateTime.of(2026, 1, 1, 10, 0));
        final var benutzer = createBenutzer(filter);
        final var model = new PersonalFilterResponseModel();
        final var requestModel = new PersonalFilterRequestModel();
        requestModel.setId(filter.getId());
        when(benutzerService.getOrCreateBenutzer(any())).thenReturn(benutzer);
        when(personalFilterDomainMapper.entity2Model(filter)).thenReturn(model);

        final var result = personalFilterService.update(requestModel);

        assertThat(result, is(model));
        verify(personalFilterDomainMapper).updateEntityFromModel(requestModel, filter);
        assertThat(filter.getLastModifiedDateTime().isAfter(LocalDateTime.of(2026, 1, 1, 10, 0)), is(true));
        verify(benutzerService).save(benutzer);
    }

    @Test
    void updateUnbekannteIdWirftEntityNotFound() throws Exception {
        final var requestModel = new PersonalFilterRequestModel();
        requestModel.setId(UUID.randomUUID());
        when(benutzerService.getOrCreateBenutzer(any())).thenReturn(createBenutzer());

        assertThrows(EntityNotFoundException.class, () -> personalFilterService.update(requestModel));
        verify(benutzerService, never()).save(any());
    }

    @Test
    void delete() throws Exception {
        final var filter = createFilter("weg damit", LocalDateTime.now());
        final var benutzer = createBenutzer(filter);
        when(benutzerService.getOrCreateBenutzer(any())).thenReturn(benutzer);

        personalFilterService.delete(filter.getId());

        assertThat(benutzer.getPersoenlicheFilter(), is(empty()));
        verify(benutzerService).save(benutzer);
    }

    @Test
    void deleteUnbekannteIdWirftEntityNotFound() throws Exception {
        when(benutzerService.getOrCreateBenutzer(any())).thenReturn(createBenutzer());

        assertThrows(EntityNotFoundException.class, () -> personalFilterService.delete(UUID.randomUUID()));
        verify(benutzerService, never()).save(any());
    }

    private static Benutzer createBenutzer(final PersoenlicherFilter... filter) {
        final var benutzer = new Benutzer();
        benutzer.setPersonalID(USER_SUB);
        benutzer.setPersoenlicheFilter(new ArrayList<>(List.of(filter)));
        return benutzer;
    }

    private static PersonalFilterResponseModel toModel(final PersoenlicherFilter filter) {
        final var model = new PersonalFilterResponseModel();
        model.setFilterName(filter.getFilterName());
        return model;
    }

    private static PersoenlicherFilter createFilter(final String name, final LocalDateTime lastModified) {
        final var filter = new PersoenlicherFilter();
        filter.setId(UUID.randomUUID());
        filter.setFilterName(name);
        filter.setCreatedDateTime(lastModified);
        filter.setLastModifiedDateTime(lastModified);
        return filter;
    }
}
