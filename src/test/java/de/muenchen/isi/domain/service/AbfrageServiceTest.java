package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.InvalidStatusException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.mapper.AbfrageDomainMapper;
import de.muenchen.isi.domain.mapper.AbfrageDomainMapperImpl;
import de.muenchen.isi.domain.mapper.AbfragevarianteDomainMapperImpl;
import de.muenchen.isi.domain.mapper.BauabschnittDomainMapperImpl;
import de.muenchen.isi.domain.mapper.DokumentDomainMapperImpl;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.domain.service.filehandling.DokumentService;
import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.repository.InfrastrukturabfrageRepository;
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
class AbfrageServiceTest {

    private final AbfrageDomainMapper abfrageDomainMapper = new AbfrageDomainMapperImpl(
        new AbfragevarianteDomainMapperImpl(new BauabschnittDomainMapperImpl()),
        new DokumentDomainMapperImpl()
    );

    private AbfrageService abfrageService;

    @Mock
    private InfrastrukturabfrageRepository infrastrukturabfrageRepository;

    @Mock
    private DokumentService dokumentService;

    @BeforeEach
    public void beforeEach() {
        this.abfrageService =
            new AbfrageService(this.abfrageDomainMapper, this.infrastrukturabfrageRepository, this.dokumentService);
        Mockito.reset(this.infrastrukturabfrageRepository, this.dokumentService);
    }

    @Test
    void getInfrastrukturabfragen() {
        final Infrastrukturabfrage entity1 = new Infrastrukturabfrage();
        entity1.setId(UUID.randomUUID());
        final Infrastrukturabfrage entity2 = new Infrastrukturabfrage();
        entity2.setId(UUID.randomUUID());

        Mockito
            .when(this.infrastrukturabfrageRepository.findAllByOrderByAbfrageFristStellungnahmeDesc())
            .thenReturn(Stream.of(entity1, entity2));

        final List<InfrastrukturabfrageModel> result = this.abfrageService.getInfrastrukturabfragen();

        final InfrastrukturabfrageModel model1 = new InfrastrukturabfrageModel();
        model1.setId(entity1.getId());
        final InfrastrukturabfrageModel model2 = new InfrastrukturabfrageModel();
        model2.setId(entity2.getId());

        assertThat(result, is(List.of(model1, model2)));
    }

    @Test
    void getInfrastrukturabfrageById() throws EntityNotFoundException {
        final UUID id = UUID.randomUUID();

        Mockito
            .when(this.infrastrukturabfrageRepository.findById(id))
            .thenReturn(Optional.of(new Infrastrukturabfrage()));
        final InfrastrukturabfrageModel result = this.abfrageService.getInfrastrukturabfrageById(id);
        assertThat(result, is((new InfrastrukturabfrageModel())));
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).findById(id);
        Mockito.reset(this.infrastrukturabfrageRepository);

        Mockito.when(this.infrastrukturabfrageRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.abfrageService.getInfrastrukturabfrageById(id)
        );
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void saveInfrastrukturabfrage() throws UniqueViolationException, OptimisticLockingException {
        final UUID uuid = UUID.randomUUID();
        final InfrastrukturabfrageModel infrastrukturabfrageModel = new InfrastrukturabfrageModel();
        infrastrukturabfrageModel.setId(uuid);
        final AbfrageModel abfrageModel = new AbfrageModel();
        abfrageModel.setNameAbfrage("hallo");
        abfrageModel.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        infrastrukturabfrageModel.setAbfrage(abfrageModel);

        final Infrastrukturabfrage abfrageEntity = this.abfrageDomainMapper.model2entity(infrastrukturabfrageModel);

        final Infrastrukturabfrage saveResult = new Infrastrukturabfrage();
        saveResult.setId(uuid);
        final Abfrage abfrage = new Abfrage();
        abfrage.setNameAbfrage("hallo");
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        saveResult.setAbfrage(abfrage);

        Mockito.when(this.infrastrukturabfrageRepository.saveAndFlush(abfrageEntity)).thenReturn(saveResult);
        Mockito
            .when(this.infrastrukturabfrageRepository.findByAbfrage_NameAbfrageIgnoreCase("hallo"))
            .thenReturn(Optional.empty());

        final InfrastrukturabfrageModel result =
            this.abfrageService.saveInfrastrukturabfrage(infrastrukturabfrageModel);

        final InfrastrukturabfrageModel expected = new InfrastrukturabfrageModel();
        expected.setId(saveResult.getId());
        final AbfrageModel expectedAbfrage = new AbfrageModel();
        expectedAbfrage.setStatusAbfrage(abfrage.getStatusAbfrage());
        expectedAbfrage.setNameAbfrage(abfrage.getNameAbfrage());
        expected.setAbfrage(expectedAbfrage);

        assertThat(result, is(expected));
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).saveAndFlush(abfrageEntity);
        Mockito
            .verify(this.infrastrukturabfrageRepository, Mockito.times(1))
            .findByAbfrage_NameAbfrageIgnoreCase("hallo");
    }

    @Test
    void saveInfrastrukturabfrageWithAngelegtStatus() throws UniqueViolationException, OptimisticLockingException {
        final UUID uuid = UUID.randomUUID();
        final InfrastrukturabfrageModel infrastrukturabfrageModel = new InfrastrukturabfrageModel();
        infrastrukturabfrageModel.setId(null);
        final AbfrageModel abfrageModel = new AbfrageModel();
        abfrageModel.setNameAbfrage("hallo");
        abfrageModel.setStatusAbfrage(StatusAbfrage.OFFEN);
        infrastrukturabfrageModel.setAbfrage(abfrageModel);

        // Mockito vergleicht die Objekte auf Feldebene weshalb das Objekt genauso sein muss wie wenn es von der Methode aufgerufen wird.
        final Infrastrukturabfrage infrastrukturabfrageEntity = new Infrastrukturabfrage();
        infrastrukturabfrageEntity.setId(null);
        final Abfrage abfrageEntity = new Abfrage();
        abfrageEntity.setNameAbfrage("hallo");
        abfrageEntity.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        infrastrukturabfrageEntity.setAbfrage(abfrageEntity);

        final Infrastrukturabfrage saveResult = new Infrastrukturabfrage();
        saveResult.setId(uuid);
        final Abfrage abfrage = new Abfrage();
        abfrage.setNameAbfrage("hallo");
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        saveResult.setAbfrage(abfrage);

        Mockito
            .when(this.infrastrukturabfrageRepository.saveAndFlush(infrastrukturabfrageEntity))
            .thenReturn(saveResult);
        Mockito
            .when(this.infrastrukturabfrageRepository.findByAbfrage_NameAbfrageIgnoreCase("hallo"))
            .thenReturn(Optional.empty());

        final InfrastrukturabfrageModel result =
            this.abfrageService.saveInfrastrukturabfrage(infrastrukturabfrageModel);

        final InfrastrukturabfrageModel expected = new InfrastrukturabfrageModel();
        expected.setId(saveResult.getId());
        final AbfrageModel expectedAbfrage = new AbfrageModel();
        expectedAbfrage.setStatusAbfrage(abfrage.getStatusAbfrage());
        expectedAbfrage.setNameAbfrage(abfrage.getNameAbfrage());
        expected.setAbfrage(expectedAbfrage);

        assertThat(result, is(expected));
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).saveAndFlush(infrastrukturabfrageEntity);
        Mockito
            .verify(this.infrastrukturabfrageRepository, Mockito.times(1))
            .findByAbfrage_NameAbfrageIgnoreCase("hallo");
    }

    @Test
    void saveInfrastrukturabfrageUniqueViolationTest() throws UniqueViolationException {
        final InfrastrukturabfrageModel infrastrukturabfrageModel = new InfrastrukturabfrageModel();
        infrastrukturabfrageModel.setId(UUID.randomUUID());
        final AbfrageModel abfrageModel = new AbfrageModel();
        abfrageModel.setNameAbfrage("hallo");
        infrastrukturabfrageModel.setAbfrage(abfrageModel);

        final InfrastrukturabfrageModel infrastrukturabfrageModel2 = new InfrastrukturabfrageModel();
        infrastrukturabfrageModel2.setId(UUID.randomUUID());
        infrastrukturabfrageModel2.setAbfrage(abfrageModel);

        final Infrastrukturabfrage abfrageEntity = this.abfrageDomainMapper.model2entity(infrastrukturabfrageModel);

        Mockito.when(this.infrastrukturabfrageRepository.saveAndFlush(abfrageEntity)).thenReturn(abfrageEntity);
        Mockito
            .when(this.infrastrukturabfrageRepository.findByAbfrage_NameAbfrageIgnoreCase("hallo"))
            .thenReturn(Optional.of(abfrageEntity));

        Assertions.assertThrows(
            UniqueViolationException.class,
            () -> this.abfrageService.saveInfrastrukturabfrage(infrastrukturabfrageModel2)
        );

        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(0)).saveAndFlush(abfrageEntity);
        Mockito
            .verify(this.infrastrukturabfrageRepository, Mockito.times(1))
            .findByAbfrage_NameAbfrageIgnoreCase("hallo");
    }

    @Test
    void patchAbfrageAngelegt()
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException, InvalidStatusException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        final InfrastrukturabfrageModel infrastrukturabfrageModel = new InfrastrukturabfrageModel();
        infrastrukturabfrageModel.setId(UUID.randomUUID());
        final AbfrageModel abfrageModel = new AbfrageModel();
        abfrageModel.setNameAbfrage("hallo");
        abfrageModel.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        infrastrukturabfrageModel.setAbfrage(abfrageModel);

        final Infrastrukturabfrage entity = this.abfrageDomainMapper.model2entity(infrastrukturabfrageModel);

        Mockito.when(this.infrastrukturabfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito.when(this.infrastrukturabfrageRepository.saveAndFlush(entity)).thenReturn(entity);
        Mockito
            .when(this.infrastrukturabfrageRepository.findByAbfrage_NameAbfrageIgnoreCase("hallo"))
            .thenReturn(Optional.empty());

        final InfrastrukturabfrageModel result = this.abfrageService.patchAbfrageAngelegt(infrastrukturabfrageModel);

        final InfrastrukturabfrageModel expected = new InfrastrukturabfrageModel();
        expected.setId(infrastrukturabfrageModel.getId());

        assertThat(result, is(infrastrukturabfrageModel));

        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).saveAndFlush(entity);
        Mockito
            .verify(this.infrastrukturabfrageRepository, Mockito.times(1))
            .findByAbfrage_NameAbfrageIgnoreCase("hallo");
        Mockito
            .verify(this.dokumentService, Mockito.times(1))
            .deleteDokumenteFromOriginalDokumentenListWhichAreMissingInParameterAdaptedDokumentenListe(
                Mockito.isNull(),
                Mockito.isNull()
            );
    }

    @Test
    void throwInvalidStatusExceptionWhenStatusAbfrageIsInvalid()
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException, InvalidStatusException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        final InfrastrukturabfrageModel infrastrukturabfrageModel = new InfrastrukturabfrageModel();
        infrastrukturabfrageModel.setId(UUID.randomUUID());
        final AbfrageModel abfrageModel = new AbfrageModel();
        abfrageModel.setNameAbfrage("test");
        abfrageModel.setStatusAbfrage(StatusAbfrage.ABBRUCH);
        infrastrukturabfrageModel.setAbfrage(abfrageModel);

        final Infrastrukturabfrage entity = this.abfrageDomainMapper.model2entity(infrastrukturabfrageModel);

        Mockito.when(this.infrastrukturabfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        Assertions.assertThrows(
            InvalidStatusException.class,
            () -> this.abfrageService.patchAbfrageAngelegt(infrastrukturabfrageModel)
        );

        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(0)).saveAndFlush(entity);
        Mockito
            .verify(this.infrastrukturabfrageRepository, Mockito.times(0))
            .findByAbfrage_NameAbfrageIgnoreCase("test");
        Mockito
            .verify(this.dokumentService, Mockito.times(0))
            .deleteDokumenteFromOriginalDokumentenListWhichAreMissingInParameterAdaptedDokumentenListe(
                Mockito.isNull(),
                Mockito.isNull()
            );
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

        Assertions.assertThrows(
            EntityIsReferencedException.class,
            () -> this.abfrageService.deleteInfrasturkturabfrageById(id)
        );

        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(0)).deleteById(id);
    }

    @Test
    void throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben() throws EntityIsReferencedException {
        this.abfrageService.throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(new AbfrageModel());

        final AbfrageModel abfrage = new AbfrageModel();
        abfrage.setBauvorhaben(new BauvorhabenModel());
        Assertions.assertThrows(
            EntityIsReferencedException.class,
            () -> this.abfrageService.throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(abfrage)
        );
    }
}
