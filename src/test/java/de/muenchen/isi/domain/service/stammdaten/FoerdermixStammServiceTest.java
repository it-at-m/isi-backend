package de.muenchen.isi.domain.service.stammdaten;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.mapper.StammdatenDomainMapper;
import de.muenchen.isi.domain.mapper.StammdatenDomainMapperImpl;
import de.muenchen.isi.domain.model.stammdaten.FoerdermixStammModel;
import de.muenchen.isi.infrastructure.entity.stammdaten.FoerdermixStamm;
import de.muenchen.isi.infrastructure.repository.stammdaten.FoerdermixStammRepository;
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
class FoerdermixStammServiceTest {

    private final StammdatenDomainMapper stammdatenDomainMapper = new StammdatenDomainMapperImpl();

    @Mock
    private FoerdermixStammRepository foerdermixStammRepository;

    private FoerdermixStammService foerdermixStammService;

    @BeforeEach
    public void beforeEach() {
        this.foerdermixStammService =
        new FoerdermixStammService(this.stammdatenDomainMapper, this.foerdermixStammRepository);
        Mockito.reset(this.foerdermixStammRepository);
    }

    @Test
    void getFoerdermixStaemme() {
        final FoerdermixStamm entity1 = new FoerdermixStamm();
        entity1.setId(UUID.randomUUID());
        final FoerdermixStamm entity2 = new FoerdermixStamm();
        entity2.setId(UUID.randomUUID());

        Mockito
            .when(this.foerdermixStammRepository.findAllByOrderByBezeichnungAsc())
            .thenReturn(Stream.of(entity1, entity2));

        final List<FoerdermixStammModel> result = this.foerdermixStammService.getFoerdermixStaemme();

        final FoerdermixStammModel model1 = new FoerdermixStammModel();
        model1.setId(entity1.getId());
        final FoerdermixStammModel model2 = new FoerdermixStammModel();
        model2.setId(entity2.getId());

        assertThat(result, is(List.of(model1, model2)));
    }

    @Test
    void getFoerdermixStammById() throws EntityNotFoundException {
        final UUID id = UUID.randomUUID();

        Mockito.when(this.foerdermixStammRepository.findById(id)).thenReturn(Optional.of(new FoerdermixStamm()));
        final FoerdermixStammModel result = this.foerdermixStammService.getFoerdermixStammById(id);
        assertThat(result, is(new FoerdermixStammModel()));
        Mockito.verify(this.foerdermixStammRepository, Mockito.times(1)).findById(id);
        Mockito.reset(this.foerdermixStammRepository);

        Mockito.when(this.foerdermixStammRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.foerdermixStammService.getFoerdermixStammById(id)
        );
        Mockito.verify(this.foerdermixStammRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void saveFoerdermixStamm() throws UniqueViolationException, OptimisticLockingException {
        final FoerdermixStammModel model = new FoerdermixStammModel();
        model.setId(null);

        final FoerdermixStamm entity = new FoerdermixStamm();
        entity.setId(model.getId());

        final FoerdermixStamm saveResult = new FoerdermixStamm();
        saveResult.setId(UUID.randomUUID());

        Mockito.when(this.foerdermixStammRepository.saveAndFlush(entity)).thenReturn(saveResult);

        final FoerdermixStammModel result = this.foerdermixStammService.saveFoerdermixStamm(model);

        final FoerdermixStammModel expected = new FoerdermixStammModel();
        expected.setId(saveResult.getId());

        assertThat(result, is(expected));

        Mockito.verify(this.foerdermixStammRepository, Mockito.times(1)).saveAndFlush(entity);
    }

    @Test
    void saveFoerdermixStammUniqueViolationTest() throws UniqueViolationException, OptimisticLockingException {
        final FoerdermixStammModel model = new FoerdermixStammModel();
        model.setId(UUID.randomUUID());
        model.setBezeichnungJahr("Test 2022");
        model.setBezeichnung("Testfall 1");

        final FoerdermixStamm entity = this.stammdatenDomainMapper.model2Entity(model);
        entity.setId(UUID.randomUUID());

        final FoerdermixStamm saveResult = new FoerdermixStamm();
        saveResult.setId(UUID.randomUUID());

        Mockito.when(this.foerdermixStammRepository.saveAndFlush(entity)).thenReturn(saveResult);
        Mockito
            .when(
                this.foerdermixStammRepository.findByBezeichnungJahrIgnoreCaseAndBezeichnungIgnoreCase(
                        "Test 2022",
                        "Testfall 1"
                    )
            )
            .thenReturn(Optional.of(entity));

        Assertions.assertThrows(
            UniqueViolationException.class,
            () -> this.foerdermixStammService.saveFoerdermixStamm(model)
        );

        Mockito.verify(this.foerdermixStammRepository, Mockito.times(0)).saveAndFlush(entity);
        Mockito
            .verify(this.foerdermixStammRepository, Mockito.times(1))
            .findByBezeichnungJahrIgnoreCaseAndBezeichnungIgnoreCase("Test 2022", "Testfall 1");
    }

    @Test
    void updateFoerdermixStamm() throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException {
        final FoerdermixStammModel model = new FoerdermixStammModel();
        model.setId(UUID.randomUUID());

        final FoerdermixStamm entity = new FoerdermixStamm();
        entity.setId(model.getId());

        Mockito.when(this.foerdermixStammRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito.when(this.foerdermixStammRepository.saveAndFlush(entity)).thenReturn(entity);

        final FoerdermixStammModel result = this.foerdermixStammService.updateFoerdermixStamm(model);

        final FoerdermixStammModel expected = new FoerdermixStammModel();
        expected.setId(model.getId());

        assertThat(result, is(model));

        Mockito.verify(this.foerdermixStammRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.foerdermixStammRepository, Mockito.times(1)).saveAndFlush(entity);
    }

    @Test
    void deleteFoerdermixStammById() throws EntityNotFoundException {
        final UUID id = UUID.randomUUID();

        final FoerdermixStamm entity = new FoerdermixStamm();
        entity.setId(id);

        Mockito.when(this.foerdermixStammRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        this.foerdermixStammService.deleteFoerdermixStammById(id);

        Mockito.verify(this.foerdermixStammRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.foerdermixStammRepository, Mockito.times(1)).deleteById(id);
    }
}
