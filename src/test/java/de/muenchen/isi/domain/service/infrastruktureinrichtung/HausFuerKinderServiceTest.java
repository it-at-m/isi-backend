package de.muenchen.isi.domain.service.infrastruktureinrichtung;

import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.mapper.InfrastruktureinrichtungDomainMapper;
import de.muenchen.isi.domain.mapper.InfrastruktureinrichtungDomainMapperImpl;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.HausFuerKinderModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.HausFuerKinder;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.HausFuerKinderRepository;
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
class HausFuerKinderServiceTest {

    private final InfrastruktureinrichtungDomainMapper infrastruktureinrichtungDomainMapper = new InfrastruktureinrichtungDomainMapperImpl();

    @Mock
    private HausFuerKinderRepository hausFuerKinderRepository;

    private HausFuerKinderService hausFuerKinderService;

    @BeforeEach
    public void beforeEach() {
        this.hausFuerKinderService = new HausFuerKinderService(
                this.infrastruktureinrichtungDomainMapper,
                this.hausFuerKinderRepository
        );
        Mockito.reset(this.hausFuerKinderRepository);
    }

    @Test
    void getHaeuserFuerKinder() {
        final HausFuerKinder entity1 = new HausFuerKinder();
        entity1.setId(UUID.randomUUID());
        final HausFuerKinder entity2 = new HausFuerKinder();
        entity2.setId(UUID.randomUUID());

        Mockito.when(this.hausFuerKinderRepository.findAllByOrderByInfrastruktureinrichtungNameEinrichtungAsc()).thenReturn(Stream.of(entity1, entity2));

        final List<HausFuerKinderModel> result = this.hausFuerKinderService.getHaeuserFuerKinder();

        final HausFuerKinderModel model1 = new HausFuerKinderModel();
        model1.setId(entity1.getId());
        final HausFuerKinderModel model2 = new HausFuerKinderModel();
        model2.setId(entity2.getId());

        assertThat(
                result,
                is(List.of(model1, model2))
        );
    }

    @Test
    void getHausFuerKinderById() throws EntityNotFoundException {
        final UUID id = UUID.randomUUID();

        Mockito.when(this.hausFuerKinderRepository.findById(id)).thenReturn(Optional.of(new HausFuerKinder()));
        final HausFuerKinderModel result = this.hausFuerKinderService.getHausFuerKinderById(id);
        assertThat(result, is((new HausFuerKinderModel())));
        Mockito.verify(this.hausFuerKinderRepository, Mockito.times(1)).findById(id);
        Mockito.reset(this.hausFuerKinderRepository);

        Mockito.when(this.hausFuerKinderRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.hausFuerKinderService.getHausFuerKinderById(id));
        Mockito.verify(this.hausFuerKinderRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void saveHausFuerKinder() {
        final HausFuerKinderModel hausFuerKinderModel = new HausFuerKinderModel();
        hausFuerKinderModel.setId(null);

        final HausFuerKinder hausFuerKinderEntity = new HausFuerKinder();
        hausFuerKinderEntity.setId(hausFuerKinderModel.getId());

        final HausFuerKinder saveResult = new HausFuerKinder();
        saveResult.setId(UUID.randomUUID());

        Mockito.when(this.hausFuerKinderRepository.saveAndFlush(hausFuerKinderEntity)).thenReturn(saveResult);

        final HausFuerKinderModel result = this.hausFuerKinderService.saveHausFuerKinder(hausFuerKinderModel);

        final HausFuerKinderModel expected = new HausFuerKinderModel();
        expected.setId(saveResult.getId());

        assertThat(
                result,
                is(expected)
        );

        Mockito.verify(this.hausFuerKinderRepository, Mockito.times(1)).saveAndFlush(hausFuerKinderEntity);
    }

    @Test
    void updateHausFuerKinder() throws EntityNotFoundException {
        final HausFuerKinderModel hausFuerKinderModel = new HausFuerKinderModel();
        hausFuerKinderModel.setId(UUID.randomUUID());

        final HausFuerKinder entity = new HausFuerKinder();
        entity.setId(hausFuerKinderModel.getId());

        Mockito.when(this.hausFuerKinderRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito.when(this.hausFuerKinderRepository.saveAndFlush(entity)).thenReturn(entity);

        final HausFuerKinderModel result = this.hausFuerKinderService.updateHausFuerKinder(hausFuerKinderModel);

        final HausFuerKinderModel expected = new HausFuerKinderModel();
        expected.setId(hausFuerKinderModel.getId());

        assertThat(
                result,
                is(hausFuerKinderModel)
        );

        Mockito.verify(this.hausFuerKinderRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.hausFuerKinderRepository, Mockito.times(1)).saveAndFlush(entity);
    }

    @Test
    void deleteHausFuerKinder() throws EntityNotFoundException, EntityIsReferencedException {
        final UUID id = UUID.randomUUID();

        final HausFuerKinder entity = new HausFuerKinder();
        entity.setId(id);
        final Infrastruktureinrichtung infrastruktureinrichtung = new Infrastruktureinrichtung();
        entity.setInfrastruktureinrichtung(infrastruktureinrichtung);

        Mockito.when(this.hausFuerKinderRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        this.hausFuerKinderService.deleteHausFuerKinderById(id);

        Mockito.verify(this.hausFuerKinderRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.hausFuerKinderRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void deleteHausFuerKinderException() {
        final UUID id = UUID.randomUUID();

        final HausFuerKinder entity = new HausFuerKinder();
        entity.setId(id);
        final Infrastruktureinrichtung infrastruktureinrichtung = new Infrastruktureinrichtung();
        infrastruktureinrichtung.setBauvorhaben(new Bauvorhaben());
        entity.setInfrastruktureinrichtung(infrastruktureinrichtung);

        Mockito.when(this.hausFuerKinderRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        Assertions.assertThrows(EntityIsReferencedException.class, () -> this.hausFuerKinderService.deleteHausFuerKinderById(id));

        Mockito.verify(this.hausFuerKinderRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.hausFuerKinderRepository, Mockito.times(0)).deleteById(id);
    }

    @Test
    void throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben() throws EntityIsReferencedException {
        this.hausFuerKinderService.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(new InfrastruktureinrichtungModel());

        final InfrastruktureinrichtungModel infrastruktureinrichtung = new InfrastruktureinrichtungModel();
        infrastruktureinrichtung.setBauvorhaben(new BauvorhabenModel());
        Assertions.assertThrows(EntityIsReferencedException.class, () -> this.hausFuerKinderService.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(infrastruktureinrichtung));
    }

}
