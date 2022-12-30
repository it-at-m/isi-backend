package de.muenchen.isi.domain.service.infrastruktureinrichtung;

import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.mapper.InfrastruktureinrichtungDomainMapper;
import de.muenchen.isi.domain.mapper.InfrastruktureinrichtungDomainMapperImpl;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.MittelschuleModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Mittelschule;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.MittelschuleRepository;
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
class MittelschuleServiceTest {

    private final InfrastruktureinrichtungDomainMapper infrastruktureinrichtungDomainMapper = new InfrastruktureinrichtungDomainMapperImpl();

    @Mock
    private MittelschuleRepository mittelschuleRepository;

    private MittelschuleService mittelschuleService;

    @BeforeEach
    public void beforeEach() {
        this.mittelschuleService = new MittelschuleService(
                this.infrastruktureinrichtungDomainMapper,
                this.mittelschuleRepository
        );
        Mockito.reset(this.mittelschuleRepository);
    }

    @Test
    void getMittelschulen() {
        final Mittelschule entity1 = new Mittelschule();
        entity1.setId(UUID.randomUUID());
        final Mittelschule entity2 = new Mittelschule();
        entity2.setId(UUID.randomUUID());

        Mockito.when(this.mittelschuleRepository.findAllByOrderByInfrastruktureinrichtungNameEinrichtungAsc()).thenReturn(Stream.of(entity1, entity2));

        final List<MittelschuleModel> result = this.mittelschuleService.getMittelschulen();

        final MittelschuleModel model1 = new MittelschuleModel();
        model1.setId(entity1.getId());
        final MittelschuleModel model2 = new MittelschuleModel();
        model2.setId(entity2.getId());

        assertThat(
                result,
                is(List.of(model1, model2))
        );
    }

    @Test
    void getMittelschuleById() throws EntityNotFoundException {
        final UUID id = UUID.randomUUID();

        Mockito.when(this.mittelschuleRepository.findById(id)).thenReturn(Optional.of(new Mittelschule()));
        final MittelschuleModel result = this.mittelschuleService.getMittelschuleById(id);
        assertThat(result, is((new MittelschuleModel())));
        Mockito.verify(this.mittelschuleRepository, Mockito.times(1)).findById(id);
        Mockito.reset(this.mittelschuleRepository);

        Mockito.when(this.mittelschuleRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.mittelschuleService.getMittelschuleById(id));
        Mockito.verify(this.mittelschuleRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void saveMittelschule() {
        final MittelschuleModel mittelschuleModel = new MittelschuleModel();
        mittelschuleModel.setId(null);

        final Mittelschule mittelschuleEntity = new Mittelschule();
        mittelschuleEntity.setId(mittelschuleModel.getId());

        final Mittelschule saveResult = new Mittelschule();
        saveResult.setId(UUID.randomUUID());

        Mockito.when(this.mittelschuleRepository.save(mittelschuleEntity)).thenReturn(saveResult);

        final MittelschuleModel result = this.mittelschuleService.saveMittelschule(mittelschuleModel);

        final MittelschuleModel expected = new MittelschuleModel();
        expected.setId(saveResult.getId());

        assertThat(
                result,
                is(expected)
        );

        Mockito.verify(this.mittelschuleRepository, Mockito.times(1)).save(mittelschuleEntity);
    }

    @Test
    void updateMittelschule() throws EntityNotFoundException {
        final MittelschuleModel mittelschuleModel = new MittelschuleModel();
        mittelschuleModel.setId(UUID.randomUUID());

        final Mittelschule entity = new Mittelschule();
        entity.setId(mittelschuleModel.getId());

        Mockito.when(this.mittelschuleRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito.when(this.mittelschuleRepository.save(entity)).thenReturn(entity);

        final MittelschuleModel result = this.mittelschuleService.updateMittelschule(mittelschuleModel);

        final MittelschuleModel expected = new MittelschuleModel();
        expected.setId(mittelschuleModel.getId());

        assertThat(
                result,
                is(mittelschuleModel)
        );

        Mockito.verify(this.mittelschuleRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.mittelschuleRepository, Mockito.times(1)).save(entity);
    }

    @Test
    void deleteMittelschule() throws EntityNotFoundException, EntityIsReferencedException {
        final UUID id = UUID.randomUUID();

        final Mittelschule entity = new Mittelschule();
        entity.setId(id);
        final Infrastruktureinrichtung infrastruktureinrichtung = new Infrastruktureinrichtung();
        entity.setInfrastruktureinrichtung(infrastruktureinrichtung);

        Mockito.when(this.mittelschuleRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        this.mittelschuleService.deleteMittelschuleById(id);

        Mockito.verify(this.mittelschuleRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.mittelschuleRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void deleteMittelschuleException() {
        final UUID id = UUID.randomUUID();

        final Mittelschule entity = new Mittelschule();
        entity.setId(id);
        final Infrastruktureinrichtung infrastruktureinrichtung = new Infrastruktureinrichtung();
        infrastruktureinrichtung.setBauvorhaben(new Bauvorhaben());
        entity.setInfrastruktureinrichtung(infrastruktureinrichtung);

        Mockito.when(this.mittelschuleRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        Assertions.assertThrows(EntityIsReferencedException.class, () -> this.mittelschuleService.deleteMittelschuleById(id));

        Mockito.verify(this.mittelschuleRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.mittelschuleRepository, Mockito.times(0)).deleteById(id);
    }

    @Test
    void throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben() throws EntityIsReferencedException {
        this.mittelschuleService.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(new InfrastruktureinrichtungModel());

        final InfrastruktureinrichtungModel infrastruktureinrichtung = new InfrastruktureinrichtungModel();
        infrastruktureinrichtung.setBauvorhaben(new BauvorhabenModel());
        Assertions.assertThrows(EntityIsReferencedException.class, () -> this.mittelschuleService.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(infrastruktureinrichtung));
    }

}
