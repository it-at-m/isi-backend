package de.muenchen.isi.domain.service.infrastruktureinrichtung;

import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.mapper.InfrastruktureinrichtungDomainMapper;
import de.muenchen.isi.domain.mapper.InfrastruktureinrichtungDomainMapperImpl;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.GsNachmittagBetreuungModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.GsNachmittagBetreuung;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.GsNachmittagBetreuungRepository;
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
class GsNachmittagBetreuungServiceTest {

    private final InfrastruktureinrichtungDomainMapper infrastruktureinrichtungDomainMapper = new InfrastruktureinrichtungDomainMapperImpl();

    @Mock
    private GsNachmittagBetreuungRepository gsNachmittagBetreuungRepository;

    private GsNachmittagBetreuungService gsNachmittagBetreuungService;

    @BeforeEach
    public void beforeEach() {
        this.gsNachmittagBetreuungService = new GsNachmittagBetreuungService(
                this.infrastruktureinrichtungDomainMapper,
                this.gsNachmittagBetreuungRepository
        );
        Mockito.reset(this.gsNachmittagBetreuungRepository);
    }

    @Test
    void getGsNachmittagBetreuung() {
        final GsNachmittagBetreuung entity1 = new GsNachmittagBetreuung();
        entity1.setId(UUID.randomUUID());
        final GsNachmittagBetreuung entity2 = new GsNachmittagBetreuung();
        entity2.setId(UUID.randomUUID());

        Mockito.when(this.gsNachmittagBetreuungRepository.findAllByOrderByInfrastruktureinrichtungNameEinrichtungAsc()).thenReturn(Stream.of(entity1, entity2));

        final List<GsNachmittagBetreuungModel> result = this.gsNachmittagBetreuungService.getGsNachmittagBetreuungen();

        final GsNachmittagBetreuungModel model1 = new GsNachmittagBetreuungModel();
        model1.setId(entity1.getId());
        final GsNachmittagBetreuungModel model2 = new GsNachmittagBetreuungModel();
        model2.setId(entity2.getId());

        assertThat(
                result,
                is(List.of(model1, model2))
        );
    }

    @Test
    void getGsNachmittagBetreuungById() throws EntityNotFoundException {
        final UUID id = UUID.randomUUID();

        Mockito.when(this.gsNachmittagBetreuungRepository.findById(id)).thenReturn(Optional.of(new GsNachmittagBetreuung()));
        final GsNachmittagBetreuungModel result = this.gsNachmittagBetreuungService.getGsNachmittagBetreuungById(id);
        assertThat(result, is((new GsNachmittagBetreuungModel())));
        Mockito.verify(this.gsNachmittagBetreuungRepository, Mockito.times(1)).findById(id);
        Mockito.reset(this.gsNachmittagBetreuungRepository);

        Mockito.when(this.gsNachmittagBetreuungRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.gsNachmittagBetreuungService.getGsNachmittagBetreuungById(id));
        Mockito.verify(this.gsNachmittagBetreuungRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void saveGsNachmittagBetreuung() {
        final GsNachmittagBetreuungModel gsNachmittagBetreuungModel = new GsNachmittagBetreuungModel();
        gsNachmittagBetreuungModel.setId(null);

        final GsNachmittagBetreuung gsNachmittagBetreuungEntity = new GsNachmittagBetreuung();
        gsNachmittagBetreuungEntity.setId(gsNachmittagBetreuungModel.getId());

        final GsNachmittagBetreuung saveResult = new GsNachmittagBetreuung();
        saveResult.setId(UUID.randomUUID());

        Mockito.when(this.gsNachmittagBetreuungRepository.saveAndFlush(gsNachmittagBetreuungEntity)).thenReturn(saveResult);

        final GsNachmittagBetreuungModel result = this.gsNachmittagBetreuungService.saveGsNachmittagBetreuung(gsNachmittagBetreuungModel);

        final GsNachmittagBetreuungModel expected = new GsNachmittagBetreuungModel();
        expected.setId(saveResult.getId());

        assertThat(
                result,
                is(expected)
        );

        Mockito.verify(this.gsNachmittagBetreuungRepository, Mockito.times(1)).saveAndFlush(gsNachmittagBetreuungEntity);
    }

    @Test
    void updateGsNachmittagBetreuung() throws EntityNotFoundException {
        final GsNachmittagBetreuungModel gsNachmittagBetreuungModel = new GsNachmittagBetreuungModel();
        gsNachmittagBetreuungModel.setId(UUID.randomUUID());

        final GsNachmittagBetreuung entity = new GsNachmittagBetreuung();
        entity.setId(gsNachmittagBetreuungModel.getId());

        Mockito.when(this.gsNachmittagBetreuungRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito.when(this.gsNachmittagBetreuungRepository.saveAndFlush(entity)).thenReturn(entity);

        final GsNachmittagBetreuungModel result = this.gsNachmittagBetreuungService.updateGsNachmittagBetreuung(gsNachmittagBetreuungModel);

        final GsNachmittagBetreuungModel expected = new GsNachmittagBetreuungModel();
        expected.setId(gsNachmittagBetreuungModel.getId());

        assertThat(
                result,
                is(gsNachmittagBetreuungModel)
        );

        Mockito.verify(this.gsNachmittagBetreuungRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.gsNachmittagBetreuungRepository, Mockito.times(1)).saveAndFlush(entity);
    }

    @Test
    void deleteGsNachmittagBetreuung() throws EntityNotFoundException, EntityIsReferencedException {
        final UUID id = UUID.randomUUID();

        final GsNachmittagBetreuung entity = new GsNachmittagBetreuung();
        entity.setId(id);
        final Infrastruktureinrichtung infrastruktureinrichtung = new Infrastruktureinrichtung();
        entity.setInfrastruktureinrichtung(infrastruktureinrichtung);

        Mockito.when(this.gsNachmittagBetreuungRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        this.gsNachmittagBetreuungService.deleteGsNachmittagBetreuungById(id);

        Mockito.verify(this.gsNachmittagBetreuungRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.gsNachmittagBetreuungRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void deleteGsNachmittagBetreuungException() {
        final UUID id = UUID.randomUUID();

        final GsNachmittagBetreuung entity = new GsNachmittagBetreuung();
        entity.setId(id);
        final Infrastruktureinrichtung infrastruktureinrichtung = new Infrastruktureinrichtung();
        infrastruktureinrichtung.setBauvorhaben(new Bauvorhaben());
        entity.setInfrastruktureinrichtung(infrastruktureinrichtung);

        Mockito.when(this.gsNachmittagBetreuungRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        Assertions.assertThrows(EntityIsReferencedException.class, () -> this.gsNachmittagBetreuungService.deleteGsNachmittagBetreuungById(id));

        Mockito.verify(this.gsNachmittagBetreuungRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.gsNachmittagBetreuungRepository, Mockito.times(0)).deleteById(id);
    }

    @Test
    void throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben() throws EntityIsReferencedException {
        this.gsNachmittagBetreuungService.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(new InfrastruktureinrichtungModel());

        final InfrastruktureinrichtungModel infrastruktureinrichtung = new InfrastruktureinrichtungModel();
        infrastruktureinrichtung.setBauvorhaben(new BauvorhabenModel());
        Assertions.assertThrows(EntityIsReferencedException.class, () -> this.gsNachmittagBetreuungService.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(infrastruktureinrichtung));
    }

}
