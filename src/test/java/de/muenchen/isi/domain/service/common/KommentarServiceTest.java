package de.muenchen.isi.domain.service.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.mapper.KommentarDomainMapperImpl;
import de.muenchen.isi.domain.model.common.KommentarModel;
import de.muenchen.isi.domain.service.filehandling.DokumentService;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.common.Kommentar;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kinderkrippe;
import de.muenchen.isi.infrastructure.repository.BauvorhabenRepository;
import de.muenchen.isi.infrastructure.repository.InfrastruktureinrichtungRepository;
import de.muenchen.isi.infrastructure.repository.common.KommentarRepository;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
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
class KommentarServiceTest {

    private KommentarService kommentarService;

    @Mock
    private InfrastruktureinrichtungRepository infrastruktureinrichtungRepository;

    @Mock
    private BauvorhabenRepository bauvorhabenRepository;

    @Mock
    private KommentarRepository kommentarRepository;

    @Mock
    private DokumentService dokumentService;

    @BeforeEach
    public void beforeEach() throws NoSuchFieldException, IllegalAccessException {
        final var kommentarMapper = new KommentarDomainMapperImpl();
        Field field = kommentarMapper.getClass().getSuperclass().getDeclaredField("bauvorhabenRepository");
        field.setAccessible(true);
        field.set(kommentarMapper, bauvorhabenRepository);
        field = kommentarMapper.getClass().getSuperclass().getDeclaredField("infrastruktureinrichtungRepository");
        field.setAccessible(true);
        field.set(kommentarMapper, infrastruktureinrichtungRepository);
        this.kommentarService = new KommentarService(this.kommentarRepository, kommentarMapper, dokumentService);
        Mockito.reset(this.infrastruktureinrichtungRepository, this.bauvorhabenRepository, this.kommentarRepository);
    }

    @Test
    void getKommentareForBauvorhaben() {
        final var uuidBauvorhaben = UUID.randomUUID();
        final var kommentar1 = new Kommentar();
        final var bauvorhaben = new Bauvorhaben();
        bauvorhaben.setId(uuidBauvorhaben);
        kommentar1.setId(UUID.randomUUID());
        kommentar1.setDatum("datum 1");
        kommentar1.setText("text 1");
        kommentar1.setBauvorhaben(bauvorhaben);

        final var kommentar2 = new Kommentar();
        kommentar2.setId(UUID.randomUUID());
        kommentar2.setDatum("datum 2");
        kommentar2.setText("text 2");
        kommentar2.setBauvorhaben(bauvorhaben);

        Mockito
            .when(this.kommentarRepository.findAllByBauvorhabenIdOrderByCreatedDateTimeDesc(uuidBauvorhaben))
            .thenReturn(Stream.of(kommentar2, kommentar1));

        final var result = kommentarService.getKommentareForBauvorhaben(uuidBauvorhaben);

        final var kommentar1Model = new KommentarModel();
        kommentar1Model.setId(kommentar1.getId());
        kommentar1Model.setDatum("datum 1");
        kommentar1Model.setText("text 1");
        kommentar1Model.setBauvorhaben(uuidBauvorhaben);

        final var kommentar2Model = new KommentarModel();
        kommentar2Model.setId(kommentar2.getId());
        kommentar2Model.setDatum("datum 2");
        kommentar2Model.setText("text 2");
        kommentar2Model.setBauvorhaben(uuidBauvorhaben);

        assertThat(List.of(kommentar2Model, kommentar1Model), is(result));

        Mockito
            .verify(this.kommentarRepository, Mockito.times(1))
            .findAllByBauvorhabenIdOrderByCreatedDateTimeDesc(uuidBauvorhaben);
    }

    @Test
    void getKommentareForInfrastruktureinrichtung() {
        final var uuidInfrastruktureinrichtung = UUID.randomUUID();
        final var kommentar1 = new Kommentar();
        final var infrastruktureinrichtung = new Kinderkrippe();
        infrastruktureinrichtung.setId(uuidInfrastruktureinrichtung);
        kommentar1.setId(UUID.randomUUID());
        kommentar1.setDatum("datum 1");
        kommentar1.setText("text 1");
        kommentar1.setInfrastruktureinrichtung(infrastruktureinrichtung);

        final var kommentar2 = new Kommentar();
        kommentar2.setId(UUID.randomUUID());
        kommentar2.setDatum("datum 2");
        kommentar2.setText("text 2");
        kommentar2.setInfrastruktureinrichtung(infrastruktureinrichtung);

        Mockito
            .when(
                this.kommentarRepository.findAllByInfrastruktureinrichtungIdOrderByCreatedDateTimeDesc(
                        uuidInfrastruktureinrichtung
                    )
            )
            .thenReturn(Stream.of(kommentar2, kommentar1));

        final var result = kommentarService.getKommentareForInfrastruktureinrichtung(uuidInfrastruktureinrichtung);

        final var kommentar1Model = new KommentarModel();
        kommentar1Model.setId(kommentar1.getId());
        kommentar1Model.setDatum("datum 1");
        kommentar1Model.setText("text 1");
        kommentar1Model.setInfrastruktureinrichtung(uuidInfrastruktureinrichtung);

        final var kommentar2Model = new KommentarModel();
        kommentar2Model.setId(kommentar2.getId());
        kommentar2Model.setDatum("datum 2");
        kommentar2Model.setText("text 2");
        kommentar2Model.setInfrastruktureinrichtung(uuidInfrastruktureinrichtung);

        assertThat(List.of(kommentar2Model, kommentar1Model), is(result));

        Mockito
            .verify(this.kommentarRepository, Mockito.times(1))
            .findAllByInfrastruktureinrichtungIdOrderByCreatedDateTimeDesc(uuidInfrastruktureinrichtung);
    }

    @Test
    void getKommentarById() throws EntityNotFoundException {
        final var uuidBauvorhaben = UUID.randomUUID();
        final var kommentar1 = new Kommentar();
        final var bauvorhaben = new Bauvorhaben();
        bauvorhaben.setId(uuidBauvorhaben);
        kommentar1.setId(UUID.randomUUID());
        kommentar1.setDatum("datum 1");
        kommentar1.setText("text 1");
        kommentar1.setBauvorhaben(bauvorhaben);

        Mockito.when(this.kommentarRepository.findById(kommentar1.getId())).thenReturn(Optional.of(kommentar1));
        final KommentarModel result = this.kommentarService.getKommentarById(kommentar1.getId());

        final var kommentar1Model = new KommentarModel();
        kommentar1Model.setId(kommentar1.getId());
        kommentar1Model.setDatum("datum 1");
        kommentar1Model.setText("text 1");
        kommentar1Model.setBauvorhaben(uuidBauvorhaben);
        assertThat(kommentar1Model, is(result));

        Mockito.verify(this.kommentarRepository, Mockito.times(1)).findById(kommentar1.getId());
        Mockito.reset(this.kommentarRepository);

        Mockito.when(this.kommentarRepository.findById(kommentar1.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> this.kommentarService.getKommentarById(kommentar1.getId()));
        Mockito.verify(this.kommentarRepository, Mockito.times(1)).findById(kommentar1.getId());
    }

    @Test
    void saveKommentar() throws OptimisticLockingException, EntityNotFoundException {
        final var uuidBauvorhaben = UUID.randomUUID();
        var kommentar1 = new Kommentar();
        final var bauvorhaben = new Bauvorhaben();
        bauvorhaben.setId(uuidBauvorhaben);
        kommentar1.setId(UUID.randomUUID());
        kommentar1.setDatum("datum 1");
        kommentar1.setText("text 1");
        kommentar1.setBauvorhaben(bauvorhaben);

        final var kommentar1Model = new KommentarModel();
        kommentar1Model.setId(kommentar1.getId());
        kommentar1Model.setDatum("datum 1");
        kommentar1Model.setText("text 1");
        kommentar1Model.setBauvorhaben(uuidBauvorhaben);

        Mockito.when(this.kommentarRepository.saveAndFlush(kommentar1)).thenReturn(kommentar1);
        Mockito.when(this.bauvorhabenRepository.findById(uuidBauvorhaben)).thenReturn(Optional.of(bauvorhaben));

        final var result = kommentarService.saveKommentar(kommentar1Model);

        assertThat(result, is(kommentar1Model));

        Mockito.verify(this.kommentarRepository, Mockito.times(1)).saveAndFlush(kommentar1);
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).findById(uuidBauvorhaben);
        Mockito.verify(this.infrastruktureinrichtungRepository, Mockito.times(0)).findById(uuidBauvorhaben);
        Mockito.reset(this.infrastruktureinrichtungRepository, this.bauvorhabenRepository, this.kommentarRepository);

        kommentar1 = new Kommentar();
        kommentar1.setDatum("datum 1");
        kommentar1.setText("text 1");
        kommentar1.setBauvorhaben(null);
        kommentar1.setInfrastruktureinrichtung(null);
        assertThrows(EntityNotFoundException.class, () -> this.kommentarService.updateKommentar(kommentar1Model));
        Mockito.verify(this.kommentarRepository, Mockito.times(0)).saveAndFlush(kommentar1);
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(0)).findById(uuidBauvorhaben);
        Mockito.verify(this.infrastruktureinrichtungRepository, Mockito.times(0)).findById(uuidBauvorhaben);
    }

    @Test
    void updateKommentar()
        throws EntityNotFoundException, OptimisticLockingException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        final var uuidBauvorhaben = UUID.randomUUID();
        final var kommentar1 = new Kommentar();
        final var bauvorhaben = new Bauvorhaben();
        bauvorhaben.setId(uuidBauvorhaben);
        kommentar1.setId(UUID.randomUUID());
        kommentar1.setDatum("datum 1");
        kommentar1.setText("text 1");
        kommentar1.setBauvorhaben(bauvorhaben);

        final var kommentar1Model = new KommentarModel();
        kommentar1Model.setId(kommentar1.getId());
        kommentar1Model.setDatum("datum 1");
        kommentar1Model.setText("text 1");
        kommentar1Model.setBauvorhaben(uuidBauvorhaben);

        Mockito.when(this.kommentarRepository.findById(kommentar1.getId())).thenReturn(Optional.of(kommentar1));
        Mockito.when(this.kommentarRepository.saveAndFlush(kommentar1)).thenReturn(kommentar1);
        Mockito.when(this.bauvorhabenRepository.findById(uuidBauvorhaben)).thenReturn(Optional.of(bauvorhaben));

        final var result = kommentarService.updateKommentar(kommentar1Model);

        assertThat(result, is(kommentar1Model));

        Mockito.verify(this.kommentarRepository, Mockito.times(2)).findById(kommentar1.getId());
        Mockito.verify(this.kommentarRepository, Mockito.times(1)).saveAndFlush(kommentar1);
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).findById(uuidBauvorhaben);
        Mockito.verify(this.infrastruktureinrichtungRepository, Mockito.times(0)).findById(uuidBauvorhaben);
        Mockito.reset(this.infrastruktureinrichtungRepository, this.bauvorhabenRepository, this.kommentarRepository);

        Mockito.when(this.kommentarRepository.findById(kommentar1.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> this.kommentarService.updateKommentar(kommentar1Model));
        Mockito.verify(this.kommentarRepository, Mockito.times(1)).findById(kommentar1.getId());
        Mockito.verify(this.kommentarRepository, Mockito.times(0)).saveAndFlush(kommentar1);
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(0)).findById(uuidBauvorhaben);
        Mockito.verify(this.infrastruktureinrichtungRepository, Mockito.times(0)).findById(uuidBauvorhaben);
    }

    @Test
    void deleteKommentarById() {
        final var id = UUID.randomUUID();
        kommentarService.deleteKommentarById(id);
        Mockito.verify(this.kommentarRepository, Mockito.times(1)).deleteById(id);
    }
}
