package de.muenchen.isi.domain.service.infrastruktureinrichtung;

import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.mapper.InfrastruktureinrichtungDomainMapper;
import de.muenchen.isi.domain.mapper.InfrastruktureinrichtungDomainMapperImpl;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.KindergartenModel;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kindergarten;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.KindergartenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class KindergartenServiceTest {

    private final InfrastruktureinrichtungDomainMapper infrastruktureinrichtungDomainMapper = new InfrastruktureinrichtungDomainMapperImpl();

    @Mock
    private KindergartenRepository kindergartenRepository;

    private KindergartenService kindergartenService;

    @BeforeEach
    public void beforeEach() {
        this.kindergartenService = new KindergartenService(
                this.infrastruktureinrichtungDomainMapper,
                this.kindergartenRepository
        );
        Mockito.reset(this.kindergartenRepository);
    }

    @Test
    void getKindergarten() {
        final Kindergarten entity1 = new Kindergarten();
        entity1.setId(UUID.randomUUID());
        final Kindergarten entity2 = new Kindergarten();
        entity2.setId(UUID.randomUUID());

        Mockito.when(this.kindergartenRepository.findAllByOrderByInfrastruktureinrichtungNameEinrichtungAsc()).thenReturn(Stream.of(entity1, entity2));

        final List<KindergartenModel> result = this.kindergartenService.getKindergaerten();

        final KindergartenModel model1 = new KindergartenModel();
        model1.setId(entity1.getId());
        final KindergartenModel model2 = new KindergartenModel();
        model2.setId(entity2.getId());

        assertThat(
                result,
                is(List.of(model1, model2))
        );
    }

    @Test
    void getKindergartenById() throws EntityNotFoundException {
        final UUID id = UUID.randomUUID();

        Mockito.when(this.kindergartenRepository.findById(id)).thenReturn(Optional.of(new Kindergarten()));
        final KindergartenModel result = this.kindergartenService.getKindergartenById(id);
        assertThat(result, is((new KindergartenModel())));
        Mockito.verify(this.kindergartenRepository, Mockito.times(1)).findById(id);
        Mockito.reset(this.kindergartenRepository);

        Mockito.when(this.kindergartenRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.kindergartenService.getKindergartenById(id));
        Mockito.verify(this.kindergartenRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void saveKindergarten() throws OptimisticLockingException {
        final KindergartenModel kindergartenModel = new KindergartenModel();
        kindergartenModel.setId(null);

        final Kindergarten kindergartenEntity = new Kindergarten();
        kindergartenEntity.setId(kindergartenModel.getId());

        final Kindergarten saveResult = new Kindergarten();
        saveResult.setId(UUID.randomUUID());

        Mockito.when(this.kindergartenRepository.saveAndFlush(kindergartenEntity)).thenReturn(saveResult);

        final KindergartenModel result = this.kindergartenService.saveKindergarten(kindergartenModel);

        final KindergartenModel expected = new KindergartenModel();
        expected.setId(saveResult.getId());

        assertThat(
                result,
                is(expected)
        );

        Mockito.verify(this.kindergartenRepository, Mockito.times(1)).saveAndFlush(kindergartenEntity);
    }

    @Test
    void updateKindergarten() throws EntityNotFoundException, OptimisticLockingException {
        final KindergartenModel kindergartenModel = new KindergartenModel();
        kindergartenModel.setId(UUID.randomUUID());

        final Kindergarten entity = new Kindergarten();
        entity.setId(kindergartenModel.getId());

        Mockito.when(this.kindergartenRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito.when(this.kindergartenRepository.saveAndFlush(entity)).thenReturn(entity);

        final KindergartenModel result = this.kindergartenService.updateKindergarten(kindergartenModel);

        final KindergartenModel expected = new KindergartenModel();
        expected.setId(kindergartenModel.getId());

        assertThat(
                result,
                is(kindergartenModel)
        );

        Mockito.verify(this.kindergartenRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.kindergartenRepository, Mockito.times(1)).saveAndFlush(entity);
    }

    @Test
    void deleteKindergarten() throws EntityNotFoundException, EntityIsReferencedException {
        final UUID id = UUID.randomUUID();

        final Kindergarten entity = new Kindergarten();
        entity.setId(id);
        final Infrastruktureinrichtung infrastruktureinrichtung = new Infrastruktureinrichtung();
        entity.setInfrastruktureinrichtung(infrastruktureinrichtung);

        Mockito.when(this.kindergartenRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        this.kindergartenService.deleteKindergartenById(id);

        Mockito.verify(this.kindergartenRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.kindergartenRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void deleteKindergartenException() {
        final UUID id = UUID.randomUUID();

        final Kindergarten entity = new Kindergarten();
        entity.setId(id);
        final Infrastruktureinrichtung infrastruktureinrichtung = new Infrastruktureinrichtung();
        infrastruktureinrichtung.setBauvorhaben(new Bauvorhaben());
        entity.setInfrastruktureinrichtung(infrastruktureinrichtung);

        Mockito.when(this.kindergartenRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        Assertions.assertThrows(EntityIsReferencedException.class, () -> this.kindergartenService.deleteKindergartenById(id));

        Mockito.verify(this.kindergartenRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.kindergartenRepository, Mockito.times(0)).deleteById(id);
    }

    @Test
    void throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben() throws EntityIsReferencedException {
        this.kindergartenService.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(new InfrastruktureinrichtungModel());

        final InfrastruktureinrichtungModel infrastruktureinrichtung = new InfrastruktureinrichtungModel();
        infrastruktureinrichtung.setBauvorhaben(new BauvorhabenModel());
        Assertions.assertThrows(EntityIsReferencedException.class, () -> this.kindergartenService.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(infrastruktureinrichtung));
    }

}
