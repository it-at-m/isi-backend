package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.mapper.AbfrageDomainMapper;
import de.muenchen.isi.domain.mapper.AbfrageDomainMapperImpl;
import de.muenchen.isi.domain.mapper.AbfragevarianteBauleitplanverfahrenDomainMapperImpl;
import de.muenchen.isi.domain.mapper.BauabschnittDomainMapperImpl;
import de.muenchen.isi.domain.mapper.DokumentDomainMapperImpl;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.service.filehandling.DokumentService;
import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.entity.Bauleitplanverfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.repository.AbfrageRepository;
import de.muenchen.isi.security.AuthenticationUtils;
import java.util.Optional;
import java.util.UUID;
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

    private AbfrageService abfrageService;

    @Mock
    private AbfrageRepository abfrageRepository;

    private AbfrageDomainMapper abfrageDomainMapper;

    @Mock
    private BauvorhabenService bauvorhabenService;

    @Mock
    private DokumentService dokumentService;

    @Mock
    private AuthenticationUtils authenticationUtils;

    @BeforeEach
    public void beforeEach() {
        this.abfrageDomainMapper =
            new AbfrageDomainMapperImpl(
                new AbfragevarianteBauleitplanverfahrenDomainMapperImpl(new BauabschnittDomainMapperImpl()),
                new DokumentDomainMapperImpl()
            );
        this.abfrageService =
            new AbfrageService(
                this.abfrageRepository,
                this.abfrageDomainMapper,
                this.bauvorhabenService,
                this.dokumentService,
                this.authenticationUtils
            );
        Mockito.reset(this.abfrageRepository, this.bauvorhabenService, this.dokumentService, this.authenticationUtils);
    }

    @Test
    void getById() throws EntityNotFoundException {
        final UUID id = UUID.randomUUID();

        Mockito.when(this.abfrageRepository.findById(id)).thenReturn(Optional.of(new Bauleitplanverfahren()));
        final AbfrageModel result = this.abfrageService.getById(id);
        final var expected = new BauleitplanverfahrenModel();
        expected.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        assertThat(result, is((expected)));
        Mockito.verify(this.abfrageRepository, Mockito.times(1)).findById(id);
        Mockito.reset(this.abfrageRepository);

        Mockito.when(this.abfrageRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.abfrageService.getById(id));
        Mockito.verify(this.abfrageRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void saveAbfrageWithId() throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException {
        final UUID uuid = UUID.randomUUID();
        final String sub = "1234";
        final BauleitplanverfahrenModel abfrage = new BauleitplanverfahrenModel();
        abfrage.setId(uuid);
        abfrage.setName("hallo");
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);

        final Abfrage abfrageEntity = this.abfrageDomainMapper.model2Entity(abfrage);

        final Abfrage saveResult = new Bauleitplanverfahren();
        saveResult.setId(uuid);
        saveResult.setSub(sub);
        saveResult.setName("hallo");
        saveResult.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);

        Mockito.when(this.authenticationUtils.getUserSub()).thenReturn(sub);
        Mockito.when(this.abfrageRepository.saveAndFlush(abfrageEntity)).thenReturn(saveResult);
        Mockito.when(this.abfrageRepository.findByNameIgnoreCase("hallo")).thenReturn(Optional.empty());

        final AbfrageModel result = this.abfrageService.save(abfrage);

        final BauleitplanverfahrenModel expected = new BauleitplanverfahrenModel();
        expected.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        expected.setId(saveResult.getId());
        expected.setSub(sub);
        expected.setStatusAbfrage(abfrage.getStatusAbfrage());
        expected.setName(abfrage.getName());

        assertThat(result, is(expected));
        Mockito.verify(this.abfrageRepository, Mockito.times(1)).saveAndFlush(abfrageEntity);
        Mockito.verify(this.abfrageRepository, Mockito.times(1)).findByNameIgnoreCase("hallo");
        Mockito.verify(this.bauvorhabenService, Mockito.times(0)).getBauvorhabenById(UUID.randomUUID());
    }

    @Test
    void saveAbfrageWithoutId() throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException {
        final UUID uuid = UUID.randomUUID();
        final String sub = "1234";
        final BauleitplanverfahrenModel abfrage = new BauleitplanverfahrenModel();
        abfrage.setId(null);
        abfrage.setSub(null);
        abfrage.setName("hallo");
        abfrage.setStatusAbfrage(StatusAbfrage.OFFEN);

        // Mockito vergleicht die Objekte auf Feldebene weshalb das Objekt genauso sein muss wie wenn es von der Methode aufgerufen wird.
        final Bauleitplanverfahren abfrageEntity = new Bauleitplanverfahren();
        abfrageEntity.setId(null);
        abfrageEntity.setSub(sub);
        abfrageEntity.setName("hallo");
        abfrageEntity.setStatusAbfrage(StatusAbfrage.ANGELEGT);

        final Bauleitplanverfahren saveResult = new Bauleitplanverfahren();
        saveResult.setId(uuid);
        saveResult.setSub(sub);
        saveResult.setName("hallo");
        saveResult.setStatusAbfrage(StatusAbfrage.ANGELEGT);

        Mockito.when(this.authenticationUtils.getUserSub()).thenReturn(sub);

        Mockito.when(this.abfrageRepository.saveAndFlush(abfrageEntity)).thenReturn(saveResult);
        Mockito.when(this.abfrageRepository.findByNameIgnoreCase("hallo")).thenReturn(Optional.empty());

        final AbfrageModel result = this.abfrageService.save(abfrage);

        final BauleitplanverfahrenModel expected = new BauleitplanverfahrenModel();
        expected.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        expected.setId(saveResult.getId());
        expected.setSub(saveResult.getSub());
        expected.setStatusAbfrage(abfrage.getStatusAbfrage());
        expected.setName(abfrage.getName());

        assertThat(result, is(expected));
        Mockito.verify(this.abfrageRepository, Mockito.times(1)).saveAndFlush(abfrageEntity);
        Mockito.verify(this.abfrageRepository, Mockito.times(1)).findByNameIgnoreCase("hallo");
        Mockito.verify(this.bauvorhabenService, Mockito.times(0)).getBauvorhabenById(UUID.randomUUID());
    }

    @Test
    void saveUniqueViolationTest() throws EntityNotFoundException {
        final BauleitplanverfahrenModel abfrage = new BauleitplanverfahrenModel();
        abfrage.setId(UUID.randomUUID());
        abfrage.setName("hallo");

        final BauleitplanverfahrenModel abfrage2 = new BauleitplanverfahrenModel();
        abfrage2.setId(UUID.randomUUID());
        abfrage2.setName("hallo");

        final Abfrage abfrageEntity = this.abfrageDomainMapper.model2Entity(abfrage);

        Mockito.when(this.abfrageRepository.saveAndFlush(abfrageEntity)).thenReturn(abfrageEntity);
        Mockito.when(this.abfrageRepository.findByNameIgnoreCase("hallo")).thenReturn(Optional.of(abfrageEntity));

        Assertions.assertThrows(UniqueViolationException.class, () -> this.abfrageService.save(abfrage2));

        Mockito.verify(this.abfrageRepository, Mockito.times(0)).saveAndFlush(abfrageEntity);
        Mockito.verify(this.abfrageRepository, Mockito.times(1)).findByNameIgnoreCase("hallo");
        Mockito.verify(this.bauvorhabenService, Mockito.times(0)).getBauvorhabenById(UUID.randomUUID());
    }
}
