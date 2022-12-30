package de.muenchen.isi.domain.service.infrastruktureinrichtung;

import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.mapper.InfrastruktureinrichtungDomainMapper;
import de.muenchen.isi.domain.mapper.InfrastruktureinrichtungDomainMapperImpl;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.GrundschuleModel;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Grundschule;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.GrundschuleRepository;
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
class GrundschuleServiceTest {

    private final InfrastruktureinrichtungDomainMapper infrastruktureinrichtungDomainMapper = new InfrastruktureinrichtungDomainMapperImpl();

    @Mock
    private GrundschuleRepository grundschuleRepository;

    private GrundschuleService grundschuleService;

    @BeforeEach
    public void beforeEach() {
        this.grundschuleService = new GrundschuleService(
                this.infrastruktureinrichtungDomainMapper,
                this.grundschuleRepository
        );
        Mockito.reset(this.grundschuleRepository);
    }

    @Test
    void getGrundschulen() {
        final Grundschule entity1 = new Grundschule();
        entity1.setId(UUID.randomUUID());
        final Grundschule entity2 = new Grundschule();
        entity2.setId(UUID.randomUUID());

        Mockito.when(this.grundschuleRepository.findAllByOrderByInfrastruktureinrichtungNameEinrichtungAsc()).thenReturn(Stream.of(entity1, entity2));

        final List<GrundschuleModel> result = this.grundschuleService.getGrundschulen();

        final GrundschuleModel model1 = new GrundschuleModel();
        model1.setId(entity1.getId());
        final GrundschuleModel model2 = new GrundschuleModel();
        model2.setId(entity2.getId());

        assertThat(
                result,
                is(List.of(model1, model2))
        );
    }

    @Test
    void getGrundschuleById() throws EntityNotFoundException {
        final UUID id = UUID.randomUUID();

        Mockito.when(this.grundschuleRepository.findById(id)).thenReturn(Optional.of(new Grundschule()));
        final GrundschuleModel result = this.grundschuleService.getGrundschuleById(id);
        assertThat(result, is((new GrundschuleModel())));
        Mockito.verify(this.grundschuleRepository, Mockito.times(1)).findById(id);
        Mockito.reset(this.grundschuleRepository);

        Mockito.when(this.grundschuleRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.grundschuleService.getGrundschuleById(id));
        Mockito.verify(this.grundschuleRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void saveGrundschule() {
        final GrundschuleModel grundschuleModel = new GrundschuleModel();
        grundschuleModel.setId(null);

        final Grundschule grundschuleEntity = new Grundschule();
        grundschuleEntity.setId(grundschuleModel.getId());

        final Grundschule saveResult = new Grundschule();
        saveResult.setId(UUID.randomUUID());

        Mockito.when(this.grundschuleRepository.save(grundschuleEntity)).thenReturn(saveResult);

        final GrundschuleModel result = this.grundschuleService.saveGrundschule(grundschuleModel);

        final GrundschuleModel expected = new GrundschuleModel();
        expected.setId(saveResult.getId());

        assertThat(
                result,
                is(expected)
        );

        Mockito.verify(this.grundschuleRepository, Mockito.times(1)).save(grundschuleEntity);
    }

    @Test
    void updateGrundschule() throws EntityNotFoundException {
        final GrundschuleModel grundschuleModel = new GrundschuleModel();
        grundschuleModel.setId(UUID.randomUUID());

        final Grundschule entity = new Grundschule();
        entity.setId(grundschuleModel.getId());

        Mockito.when(this.grundschuleRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito.when(this.grundschuleRepository.save(entity)).thenReturn(entity);

        final GrundschuleModel result = this.grundschuleService.updateGrundschule(grundschuleModel);

        final GrundschuleModel expected = new GrundschuleModel();
        expected.setId(grundschuleModel.getId());

        assertThat(
                result,
                is(grundschuleModel)
        );

        Mockito.verify(this.grundschuleRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.grundschuleRepository, Mockito.times(1)).save(entity);
    }

    @Test
    void deleteGrundschule() throws EntityNotFoundException, EntityIsReferencedException {
        final UUID id = UUID.randomUUID();

        final Grundschule entity = new Grundschule();
        entity.setId(id);
        final Infrastruktureinrichtung infrastruktureinrichtung = new Infrastruktureinrichtung();
        entity.setInfrastruktureinrichtung(infrastruktureinrichtung);

        Mockito.when(this.grundschuleRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        this.grundschuleService.deleteGrundschuleById(id);

        Mockito.verify(this.grundschuleRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.grundschuleRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void deleteGrundschuleException() {
        final UUID id = UUID.randomUUID();

        final Grundschule entity = new Grundschule();
        entity.setId(id);
        final Infrastruktureinrichtung infrastruktureinrichtung = new Infrastruktureinrichtung();
        infrastruktureinrichtung.setBauvorhaben(new Bauvorhaben());
        entity.setInfrastruktureinrichtung(infrastruktureinrichtung);

        Mockito.when(this.grundschuleRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        Assertions.assertThrows(EntityIsReferencedException.class, () -> this.grundschuleService.deleteGrundschuleById(id));

        Mockito.verify(this.grundschuleRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.grundschuleRepository, Mockito.times(0)).deleteById(id);
    }

    @Test
    void throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben() throws EntityIsReferencedException {
        this.grundschuleService.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(new InfrastruktureinrichtungModel());

        final InfrastruktureinrichtungModel infrastruktureinrichtung = new InfrastruktureinrichtungModel();
        infrastruktureinrichtung.setBauvorhaben(new BauvorhabenModel());
        Assertions.assertThrows(EntityIsReferencedException.class, () -> this.grundschuleService.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(infrastruktureinrichtung));
    }

}
