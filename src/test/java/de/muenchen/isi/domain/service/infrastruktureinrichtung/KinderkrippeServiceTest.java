package de.muenchen.isi.domain.service.infrastruktureinrichtung;

import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.mapper.InfrastruktureinrichtungDomainMapper;
import de.muenchen.isi.domain.mapper.InfrastruktureinrichtungDomainMapperImpl;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.KinderkrippeModel;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kinderkrippe;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.KinderkrippeRepository;
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
class KinderkrippeServiceTest {

    private final InfrastruktureinrichtungDomainMapper infrastruktureinrichtungDomainMapper = new InfrastruktureinrichtungDomainMapperImpl();

    @Mock
    private KinderkrippeRepository kinderkrippeRepository;

    private KinderkrippeService kinderkrippeService;

    @BeforeEach
    public void beforeEach() {
        this.kinderkrippeService = new KinderkrippeService(
                this.infrastruktureinrichtungDomainMapper,
                this.kinderkrippeRepository
        );
        Mockito.reset(this.kinderkrippeRepository);
    }

    @Test
    void getKinderkrippen() {
        final Kinderkrippe entity1 = new Kinderkrippe();
        entity1.setId(UUID.randomUUID());
        final Kinderkrippe entity2 = new Kinderkrippe();
        entity2.setId(UUID.randomUUID());

        Mockito.when(this.kinderkrippeRepository.findAllByOrderByInfrastruktureinrichtungNameEinrichtungAsc()).thenReturn(Stream.of(entity1, entity2));

        final List<KinderkrippeModel> result = this.kinderkrippeService.getKinderkrippen();

        final KinderkrippeModel model1 = new KinderkrippeModel();
        model1.setId(entity1.getId());
        final KinderkrippeModel model2 = new KinderkrippeModel();
        model2.setId(entity2.getId());

        assertThat(
                result,
                is(List.of(model1, model2))
        );
    }

    @Test
    void getKinderkrippeById() throws EntityNotFoundException {
        final UUID id = UUID.randomUUID();

        Mockito.when(this.kinderkrippeRepository.findById(id)).thenReturn(Optional.of(new Kinderkrippe()));
        final KinderkrippeModel result = this.kinderkrippeService.getKinderkrippeById(id);
        assertThat(result, is((new KinderkrippeModel())));
        Mockito.verify(this.kinderkrippeRepository, Mockito.times(1)).findById(id);
        Mockito.reset(this.kinderkrippeRepository);

        Mockito.when(this.kinderkrippeRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.kinderkrippeService.getKinderkrippeById(id));
        Mockito.verify(this.kinderkrippeRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void saveKinderkrippe() throws OptimisticLockingException {
        final KinderkrippeModel kinderkrippeModel = new KinderkrippeModel();
        kinderkrippeModel.setId(null);

        final Kinderkrippe kinderkrippeEntity = new Kinderkrippe();
        kinderkrippeEntity.setId(kinderkrippeModel.getId());

        final Kinderkrippe saveResult = new Kinderkrippe();
        saveResult.setId(UUID.randomUUID());

        Mockito.when(this.kinderkrippeRepository.saveAndFlush(kinderkrippeEntity)).thenReturn(saveResult);

        final KinderkrippeModel result = this.kinderkrippeService.saveKinderkrippe(kinderkrippeModel);

        final KinderkrippeModel expected = new KinderkrippeModel();
        expected.setId(saveResult.getId());

        assertThat(
                result,
                is(expected)
        );

        Mockito.verify(this.kinderkrippeRepository, Mockito.times(1)).saveAndFlush(kinderkrippeEntity);
    }

    @Test
    void updateKinderkrippe() throws EntityNotFoundException, OptimisticLockingException {
        final KinderkrippeModel kinderkrippeModel = new KinderkrippeModel();
        kinderkrippeModel.setId(UUID.randomUUID());

        final Kinderkrippe entity = new Kinderkrippe();
        entity.setId(kinderkrippeModel.getId());

        Mockito.when(this.kinderkrippeRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito.when(this.kinderkrippeRepository.saveAndFlush(entity)).thenReturn(entity);

        final KinderkrippeModel result = this.kinderkrippeService.updateKinderkrippe(kinderkrippeModel);

        final KinderkrippeModel expected = new KinderkrippeModel();
        expected.setId(kinderkrippeModel.getId());

        assertThat(
                result,
                is(kinderkrippeModel)
        );

        Mockito.verify(this.kinderkrippeRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.kinderkrippeRepository, Mockito.times(1)).saveAndFlush(entity);
    }

    @Test
    void deleteKinderkrippe() throws EntityNotFoundException, EntityIsReferencedException {
        final UUID id = UUID.randomUUID();

        final Kinderkrippe entity = new Kinderkrippe();
        entity.setId(id);
        final Infrastruktureinrichtung infrastruktureinrichtung = new Infrastruktureinrichtung();
        entity.setInfrastruktureinrichtung(infrastruktureinrichtung);

        Mockito.when(this.kinderkrippeRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        this.kinderkrippeService.deleteKinderkrippeById(id);

        Mockito.verify(this.kinderkrippeRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.kinderkrippeRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void deleteKinderkrippeException() {
        final UUID id = UUID.randomUUID();

        final Kinderkrippe entity = new Kinderkrippe();
        entity.setId(id);
        final Infrastruktureinrichtung infrastruktureinrichtung = new Infrastruktureinrichtung();
        infrastruktureinrichtung.setBauvorhaben(new Bauvorhaben());
        entity.setInfrastruktureinrichtung(infrastruktureinrichtung);

        Mockito.when(this.kinderkrippeRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        Assertions.assertThrows(EntityIsReferencedException.class, () -> this.kinderkrippeService.deleteKinderkrippeById(id));

        Mockito.verify(this.kinderkrippeRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.kinderkrippeRepository, Mockito.times(0)).deleteById(id);
    }

    @Test
    void throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben() throws EntityIsReferencedException {
        this.kinderkrippeService.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(new InfrastruktureinrichtungModel());

        final InfrastruktureinrichtungModel infrastruktureinrichtung = new InfrastruktureinrichtungModel();
        infrastruktureinrichtung.setBauvorhaben(new BauvorhabenModel());
        Assertions.assertThrows(EntityIsReferencedException.class, () -> this.kinderkrippeService.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(infrastruktureinrichtung));
    }

}
