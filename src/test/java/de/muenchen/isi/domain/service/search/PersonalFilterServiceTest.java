package de.muenchen.isi.domain.service.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.mapper.PersonalFilterDomainMapper;
import de.muenchen.isi.domain.model.search.filter.PersonalFilterRequestModel;
import de.muenchen.isi.domain.model.search.filter.PersonalFilterResponseModel;
import de.muenchen.isi.infrastructure.entity.search.filter.PersonalFilter;
import de.muenchen.isi.infrastructure.repository.search.PersonalFilterRepository;
import de.muenchen.isi.security.AuthenticationUtils;
import java.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PersonalFilterServiceTest {

    private PersonalFilterService personalFilterService;

    @Mock
    private PersonalFilterDomainMapper personalFilterDomainMapper;

    @Mock
    private AuthenticationUtils authenticationUtils;

    @Mock
    private PersonalFilterRepository personalFilterRepository;

    @BeforeEach
    void setUp() {
        personalFilterService = new PersonalFilterService(
            personalFilterDomainMapper,
            authenticationUtils,
            personalFilterRepository
        );
        Mockito.reset(personalFilterDomainMapper, authenticationUtils, personalFilterRepository);
    }

    @Test
    void getPersonalFilters() throws Exception {
        String userSub = "userSub";
        List<PersonalFilter> entities = List.of(new PersonalFilter());
        List<PersonalFilterResponseModel> models = List.of(new PersonalFilterResponseModel());

        when(authenticationUtils.getUserSub()).thenReturn(userSub);
        when(authenticationUtils.isSubFromUnauthenticatedUser(userSub)).thenReturn(false);
        when(personalFilterRepository.findByPersonalID(userSub)).thenReturn(entities);
        when(personalFilterDomainMapper.entities2Models(entities)).thenReturn(models);

        List<PersonalFilterResponseModel> result = personalFilterService.getPersonalFilters();
        assertThat(result, is(models));
        verify(personalFilterRepository).findByPersonalID(userSub);
        verify(personalFilterDomainMapper).entities2Models(entities);
    }

    @Test
    void getPersonalFiltersUnauthenticatedUser() {
        String userSub = "fallback";
        when(authenticationUtils.getUserSub()).thenReturn(userSub);
        when(authenticationUtils.isSubFromUnauthenticatedUser(userSub)).thenReturn(true);

        assertThrows(IllegalAccessException.class, () -> personalFilterService.getPersonalFilters());
    }

    @Test
    void getByFilterID() throws Exception {
        UUID filterId = UUID.randomUUID();
        String userSub = "userSub";
        PersonalFilter entity = new PersonalFilter();
        PersonalFilterResponseModel model = new PersonalFilterResponseModel();

        when(authenticationUtils.getUserSub()).thenReturn(userSub);
        when(authenticationUtils.isSubFromUnauthenticatedUser(userSub)).thenReturn(false);
        when(personalFilterRepository.findByIdAndPersonalID(filterId, userSub)).thenReturn(entity);
        when(personalFilterDomainMapper.entity2Model(entity)).thenReturn(model);

        PersonalFilterResponseModel result = personalFilterService.getByFilterID(filterId);
        assertThat(result, is(model));
    }

    @Test
    void getByFilterIDUnauthenticatedUser() {
        UUID filterId = UUID.randomUUID();
        String userSub = "userSub";

        when(authenticationUtils.getUserSub()).thenReturn(userSub);
        when(authenticationUtils.isSubFromUnauthenticatedUser(userSub)).thenReturn(false);
        when(personalFilterRepository.findByIdAndPersonalID(filterId, userSub)).thenReturn(null);
        when(personalFilterRepository.findById(filterId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> personalFilterService.getByFilterID(filterId));
    }

    @Test
    void getByFilterIDNotOwner() {
        UUID filterId = UUID.randomUUID();
        String userSub = "userSub";

        when(authenticationUtils.getUserSub()).thenReturn(userSub);
        when(authenticationUtils.isSubFromUnauthenticatedUser(userSub)).thenReturn(false);
        when(personalFilterRepository.findByIdAndPersonalID(filterId, userSub)).thenReturn(null);
        when(personalFilterRepository.findById(filterId)).thenReturn(Optional.of(new PersonalFilter()));

        assertThrows(IllegalAccessException.class, () -> personalFilterService.getByFilterID(filterId));
    }

    @Test
    void update() throws Exception {
        UUID filterId = UUID.randomUUID();
        String userSub = "userSub";
        PersonalFilterRequestModel requestModel = new PersonalFilterRequestModel();
        requestModel.setId(filterId);
        PersonalFilter entity = new PersonalFilter();
        PersonalFilterResponseModel responseModel = new PersonalFilterResponseModel();

        when(authenticationUtils.getUserSub()).thenReturn(userSub);
        when(authenticationUtils.isSubFromUnauthenticatedUser(userSub)).thenReturn(false);
        when(personalFilterRepository.findByIdAndPersonalID(filterId, userSub)).thenReturn(entity);
        when(personalFilterRepository.saveAndFlush(entity)).thenReturn(entity);
        when(personalFilterDomainMapper.entity2Model(entity)).thenReturn(responseModel);

        PersonalFilterResponseModel result = personalFilterService.update(requestModel);
        assertThat(result, is(responseModel));
        verify(personalFilterDomainMapper).updateEntityFromModel(requestModel, entity);
    }

    @Test
    void updateNotFound() {
        UUID filterId = UUID.randomUUID();
        String userSub = "userSub";
        PersonalFilterRequestModel requestModel = new PersonalFilterRequestModel();
        requestModel.setId(filterId);

        when(authenticationUtils.getUserSub()).thenReturn(userSub);
        when(authenticationUtils.isSubFromUnauthenticatedUser(userSub)).thenReturn(false);
        when(personalFilterRepository.findByIdAndPersonalID(filterId, userSub)).thenReturn(null);
        when(personalFilterRepository.findById(filterId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> personalFilterService.update(requestModel));
    }

    @Test
    void updateNotOwner() {
        UUID filterId = UUID.randomUUID();
        String userSub = "userSub";
        PersonalFilterRequestModel requestModel = new PersonalFilterRequestModel();
        requestModel.setId(filterId);

        when(authenticationUtils.getUserSub()).thenReturn(userSub);
        when(authenticationUtils.isSubFromUnauthenticatedUser(userSub)).thenReturn(false);
        when(personalFilterRepository.findByIdAndPersonalID(filterId, userSub)).thenReturn(null);
        when(personalFilterRepository.findById(filterId)).thenReturn(Optional.of(new PersonalFilter()));

        assertThrows(IllegalAccessException.class, () -> personalFilterService.update(requestModel));
    }

    @Test
    void updateOptimisticLocking() {
        UUID filterId = UUID.randomUUID();
        String userSub = "userSub";
        PersonalFilterRequestModel requestModel = new PersonalFilterRequestModel();
        requestModel.setId(filterId);
        PersonalFilter entity = new PersonalFilter();

        when(authenticationUtils.getUserSub()).thenReturn(userSub);
        when(authenticationUtils.isSubFromUnauthenticatedUser(userSub)).thenReturn(false);
        when(personalFilterRepository.findByIdAndPersonalID(filterId, userSub)).thenReturn(entity);
        doThrow(new ObjectOptimisticLockingFailureException("test", "test"))
            .when(personalFilterRepository)
            .saveAndFlush(entity);

        assertThrows(OptimisticLockingException.class, () -> personalFilterService.update(requestModel));
    }

    @Test
    void save() throws Exception {
        String userSub = "userSub";
        PersonalFilterRequestModel requestModel = new PersonalFilterRequestModel();
        PersonalFilter entity = new PersonalFilter();
        PersonalFilterResponseModel responseModel = new PersonalFilterResponseModel();

        when(authenticationUtils.getUserSub()).thenReturn(userSub);
        when(authenticationUtils.isSubFromUnauthenticatedUser(userSub)).thenReturn(false);
        when(personalFilterDomainMapper.model2Entity(requestModel)).thenReturn(entity);
        when(personalFilterRepository.saveAndFlush(entity)).thenReturn(entity);
        when(personalFilterDomainMapper.entity2Model(entity)).thenReturn(responseModel);

        PersonalFilterResponseModel result = personalFilterService.save(requestModel);
        assertThat(result, is(responseModel));
    }

    @Test
    void saveUnauthenticatedUser() {
        String userSub = "userSub";
        PersonalFilterRequestModel requestModel = new PersonalFilterRequestModel();

        when(authenticationUtils.getUserSub()).thenReturn(userSub);
        when(authenticationUtils.isSubFromUnauthenticatedUser(userSub)).thenReturn(false, true);

        assertThrows(IllegalAccessException.class, () -> personalFilterService.save(requestModel));
    }

    @Test
    void save_optimisticLocking_throwsOptimisticLockingException() throws Exception {
        String userSub = "userSub";
        PersonalFilterRequestModel requestModel = new PersonalFilterRequestModel();
        PersonalFilter entity = new PersonalFilter();

        when(authenticationUtils.getUserSub()).thenReturn(userSub);
        when(authenticationUtils.isSubFromUnauthenticatedUser(userSub)).thenReturn(false);
        when(personalFilterDomainMapper.model2Entity(requestModel)).thenReturn(entity);
        doThrow(new ObjectOptimisticLockingFailureException("test", "test"))
            .when(personalFilterRepository)
            .saveAndFlush(entity);

        assertThrows(OptimisticLockingException.class, () -> personalFilterService.save(requestModel));
    }

    @Test
    void delete() throws Exception {
        UUID filterId = UUID.randomUUID();
        String userSub = "userSub";
        PersonalFilter entity = new PersonalFilter();

        when(authenticationUtils.getUserSub()).thenReturn(userSub);
        when(authenticationUtils.isSubFromUnauthenticatedUser(userSub)).thenReturn(false);
        when(personalFilterRepository.findByIdAndPersonalID(filterId, userSub)).thenReturn(entity);

        personalFilterService.delete(filterId);
        verify(personalFilterRepository).deleteById(filterId);
    }

    @Test
    void delete_NotFound() {
        UUID filterId = UUID.randomUUID();
        String userSub = "userSub";

        when(authenticationUtils.getUserSub()).thenReturn(userSub);
        when(authenticationUtils.isSubFromUnauthenticatedUser(userSub)).thenReturn(false);
        when(personalFilterRepository.findByIdAndPersonalID(filterId, userSub)).thenReturn(null);
        when(personalFilterRepository.findById(filterId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> personalFilterService.delete(filterId));
    }

    @Test
    void deleteNotOwner() throws Exception {
        UUID filterId = UUID.randomUUID();
        String userSub = "userSub";

        when(authenticationUtils.getUserSub()).thenReturn(userSub);
        when(authenticationUtils.isSubFromUnauthenticatedUser(userSub)).thenReturn(false);
        when(personalFilterRepository.findByIdAndPersonalID(filterId, userSub)).thenReturn(null);
        when(personalFilterRepository.findById(filterId)).thenReturn(Optional.of(new PersonalFilter()));

        assertThrows(IllegalAccessException.class, () -> personalFilterService.delete(filterId));
    }
}
