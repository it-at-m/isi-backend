package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.mapper.AbfrageDomainMapper;
import de.muenchen.isi.domain.mapper.AbfrageDomainMapperImpl;
import de.muenchen.isi.domain.mapper.AbfragevarianteDomainMapperImpl;
import de.muenchen.isi.domain.mapper.BauabschnittDomainMapperImpl;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
import de.muenchen.isi.infrastructure.repository.InfrastrukturabfrageRepository;
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
class AbfrageServiceTest {

    private final AbfrageDomainMapper abfrageDomainMapper = new AbfrageDomainMapperImpl(
            new AbfragevarianteDomainMapperImpl(
                    new BauabschnittDomainMapperImpl()
            )
    );

    @Mock
    private InfrastrukturabfrageRepository infrastrukturabfrageRepository;

    private AbfrageService abfrageService;

    @BeforeEach
    public void beforeEach() {
        this.abfrageService = new AbfrageService(
                this.abfrageDomainMapper,
                this.infrastrukturabfrageRepository
        );
        Mockito.reset(this.infrastrukturabfrageRepository);
    }

    @Test
    void getInfrastrukturabfragen() {
        final Infrastrukturabfrage entity1 = new Infrastrukturabfrage();
        entity1.setId(UUID.randomUUID());
        final Infrastrukturabfrage entity2 = new Infrastrukturabfrage();
        entity2.setId(UUID.randomUUID());

        Mockito.when(this.infrastrukturabfrageRepository.findAllByOrderByAbfrageFristStellungnahmeDesc()).thenReturn(Stream.of(entity1, entity2));

        final List<InfrastrukturabfrageModel> result = this.abfrageService.getInfrastrukturabfragen();

        final InfrastrukturabfrageModel model1 = new InfrastrukturabfrageModel();
        model1.setId(entity1.getId());
        final InfrastrukturabfrageModel model2 = new InfrastrukturabfrageModel();
        model2.setId(entity2.getId());

        assertThat(
                result,
                is(List.of(model1, model2))
        );
    }

    @Test
    void getInfrastrukturabfrageById() throws EntityNotFoundException {
        final UUID id = UUID.randomUUID();

        Mockito.when(this.infrastrukturabfrageRepository.findById(id)).thenReturn(Optional.of(new Infrastrukturabfrage()));
        final InfrastrukturabfrageModel result = this.abfrageService.getInfrastrukturabfrageById(id);
        assertThat(result, is((new InfrastrukturabfrageModel())));
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).findById(id);
        Mockito.reset(this.infrastrukturabfrageRepository);

        Mockito.when(this.infrastrukturabfrageRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.abfrageService.getInfrastrukturabfrageById(id));
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void saveInfrastrukturabfrage() {
        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(null);

        final Infrastrukturabfrage abfrageEntity = new Infrastrukturabfrage();
        abfrageEntity.setId(abfrage.getId());

        final Infrastrukturabfrage saveResult = new Infrastrukturabfrage();
        saveResult.setId(UUID.randomUUID());

        Mockito.when(this.infrastrukturabfrageRepository.save(abfrageEntity)).thenReturn(saveResult);

        final InfrastrukturabfrageModel result = this.abfrageService.saveInfrastrukturabfrage(abfrage);

        final InfrastrukturabfrageModel expected = new InfrastrukturabfrageModel();
        expected.setId(saveResult.getId());

        assertThat(
                result,
                is(expected)
        );

        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).save(abfrageEntity);
    }

    @Test
    void updateInfrastrukturabfrage() throws EntityNotFoundException {
        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(UUID.randomUUID());

        final Infrastrukturabfrage entity = new Infrastrukturabfrage();
        entity.setId(abfrage.getId());

        Mockito.when(this.infrastrukturabfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito.when(this.infrastrukturabfrageRepository.save(entity)).thenReturn(entity);

        final InfrastrukturabfrageModel result = this.abfrageService.updateInfrastrukturabfrage(abfrage);

        final InfrastrukturabfrageModel expected = new InfrastrukturabfrageModel();
        expected.setId(abfrage.getId());

        assertThat(
                result,
                is(abfrage)
        );

        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).save(entity);
    }

    @Test
    void deleteInfrastrukturabfrage() throws EntityNotFoundException, EntityIsReferencedException {
        final UUID id = UUID.randomUUID();

        final Infrastrukturabfrage entity = new Infrastrukturabfrage();
        entity.setId(id);
        final Abfrage abfrage = new Abfrage();
        entity.setAbfrage(abfrage);

        Mockito.when(this.infrastrukturabfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        this.abfrageService.deleteInfrasturkturabfrageById(id);

        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void deleteInfrastrukturabfrageException() {
        final UUID id = UUID.randomUUID();

        final Infrastrukturabfrage entity = new Infrastrukturabfrage();
        entity.setId(id);
        final Abfrage abfrage = new Abfrage();
        abfrage.setBauvorhaben(new Bauvorhaben());
        entity.setAbfrage(abfrage);

        Mockito.when(this.infrastrukturabfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        Assertions.assertThrows(EntityIsReferencedException.class, () -> this.abfrageService.deleteInfrasturkturabfrageById(id));

        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(0)).deleteById(id);
    }

    @Test
    void throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben() throws EntityIsReferencedException {
        this.abfrageService.throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(new AbfrageModel());

        final AbfrageModel abfrage = new AbfrageModel();
        abfrage.setBauvorhaben(new BauvorhabenModel());
        Assertions.assertThrows(EntityIsReferencedException.class, () -> this.abfrageService.throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(abfrage));
    }

}
