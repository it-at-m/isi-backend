package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.mapper.BaurateDomainMapper;
import de.muenchen.isi.domain.mapper.BaurateDomainMapperImpl;
import de.muenchen.isi.domain.model.BaurateModel;
import de.muenchen.isi.infrastructure.entity.Baurate;
import de.muenchen.isi.infrastructure.repository.BaurateRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
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
class BaurateServiceTest {

    private final BaurateDomainMapper baurateDomainMapper = new BaurateDomainMapperImpl();

    @Mock
    private BaurateRepository baurateRepository;

    private BaurateService baurateService;

    @BeforeEach
    public void beforeEach() {
        this.baurateService = new BaurateService(this.baurateDomainMapper, this.baurateRepository);
        Mockito.reset(this.baurateRepository);
    }

    @Test
    void getBauraten() {
        final Baurate baurate1 = new Baurate();
        baurate1.setId(UUID.randomUUID());
        final Baurate baurate2 = new Baurate();
        baurate2.setId(UUID.randomUUID());

        Mockito.when(this.baurateRepository.findAllByOrderByJahrDesc()).thenReturn(Stream.of(baurate1, baurate2));

        final List<BaurateModel> result = this.baurateService.getBauraten();

        final BaurateModel model1 = new BaurateModel();
        model1.setId(baurate1.getId());
        final BaurateModel model2 = new BaurateModel();
        model2.setId(baurate2.getId());

        assertThat(result, is(List.of(model1, model2)));
    }

    @Test
    void getBaurateById() throws EntityNotFoundException {
        final UUID id = UUID.randomUUID();

        Mockito.when(this.baurateRepository.findById(id)).thenReturn(Optional.of(new Baurate()));
        final BaurateModel result = this.baurateService.getBaurateById(id);
        assertThat(result, is(new BaurateModel()));
        Mockito.verify(this.baurateRepository, Mockito.times(1)).findById(id);
        Mockito.reset(this.baurateRepository);

        Mockito.when(this.baurateRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.baurateService.getBaurateById(id));
        Mockito.verify(this.baurateRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void saveBaurate() throws OptimisticLockingException {
        final BaurateModel model = new BaurateModel();
        model.setId(null);

        final Baurate entity = new Baurate();
        entity.setId(model.getId());

        final Baurate saveResult = new Baurate();
        saveResult.setId(UUID.randomUUID());

        Mockito.when(this.baurateRepository.saveAndFlush(entity)).thenReturn(saveResult);

        final BaurateModel result = this.baurateService.saveBaurate(model);

        final BaurateModel expected = new BaurateModel();
        expected.setId(saveResult.getId());

        assertThat(result, is(expected));

        Mockito.verify(this.baurateRepository, Mockito.times(1)).saveAndFlush(entity);
    }

    @Test
    void updateBaurate() throws EntityNotFoundException, OptimisticLockingException {
        final BaurateModel model = new BaurateModel();
        model.setId(UUID.randomUUID());

        final Baurate entity = new Baurate();
        entity.setId(model.getId());

        Mockito.when(this.baurateRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito.when(this.baurateRepository.saveAndFlush(entity)).thenReturn(entity);

        final BaurateModel result = this.baurateService.updateBaurate(model);

        final BaurateModel expected = new BaurateModel();
        expected.setId(model.getId());

        assertThat(result, is(model));

        Mockito.verify(this.baurateRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.baurateRepository, Mockito.times(1)).saveAndFlush(entity);
    }

    @Test
    void deleteBaurateById() throws EntityNotFoundException {
        final UUID id = UUID.randomUUID();

        final Baurate entity = new Baurate();
        entity.setId(id);

        Mockito.when(this.baurateRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        this.baurateService.deleteBaurateById(id);

        Mockito.verify(this.baurateRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.baurateRepository, Mockito.times(1)).deleteById(id);
    }
}
