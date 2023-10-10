package de.muenchen.isi.domain.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.common.KommentarModel;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.common.Kommentar;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kinderkrippe;
import de.muenchen.isi.infrastructure.repository.BauvorhabenRepository;
import de.muenchen.isi.infrastructure.repository.InfrastruktureinrichtungRepository;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;
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
class KommentarDomainMapperTest {

    @Mock
    private BauvorhabenRepository bauvorhabenRepository;

    @Mock
    private InfrastruktureinrichtungRepository infrastruktureinrichtungRepository;

    private KommentarDomainMapper kommentarDomainMapper = new KommentarDomainMapperImpl();

    @BeforeEach
    public void beforeEach() throws NoSuchFieldException, IllegalAccessException {
        Field field = kommentarDomainMapper.getClass().getSuperclass().getDeclaredField("bauvorhabenRepository");
        field.setAccessible(true);
        field.set(kommentarDomainMapper, bauvorhabenRepository);
        field = kommentarDomainMapper.getClass().getSuperclass().getDeclaredField("infrastruktureinrichtungRepository");
        field.setAccessible(true);
        field.set(kommentarDomainMapper, infrastruktureinrichtungRepository);
        Mockito.reset(this.infrastruktureinrichtungRepository, this.bauvorhabenRepository);
    }

    @Test
    void afterMappingModel2Entity() throws EntityNotFoundException {
        var kommentar = new Kommentar();
        var kommentarModel = new KommentarModel();

        try {
            kommentarDomainMapper.afterMappingModel2Entity(kommentarModel, kommentar);
        } catch (EntityNotFoundException exception) {
            assertThat(
                exception.getMessage(),
                is("Der Kommentar referenziert weder ein Bauvorhaben noch eine Infrastruktureinrichtung")
            );
        }
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(0)).findById(null);
        Mockito.verify(this.infrastruktureinrichtungRepository, Mockito.times(0)).findById(null);
        Mockito.reset(this.infrastruktureinrichtungRepository, this.bauvorhabenRepository);

        final var uuidBauvorhaben = UUID.randomUUID();
        final var bauvorhaben = new Bauvorhaben();
        bauvorhaben.setId(uuidBauvorhaben);
        kommentar = new Kommentar();

        kommentarModel = new KommentarModel();
        kommentarModel.setBauvorhaben(uuidBauvorhaben);

        Mockito.when(this.bauvorhabenRepository.findById(uuidBauvorhaben)).thenReturn(Optional.of(bauvorhaben));

        kommentarDomainMapper.afterMappingModel2Entity(kommentarModel, kommentar);

        assertThat(kommentar.getBauvorhaben(), is(bauvorhaben));
        assertThat(kommentar.getInfrastruktureinrichtung(), is(nullValue()));

        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).findById(uuidBauvorhaben);
        Mockito.verify(this.infrastruktureinrichtungRepository, Mockito.times(0)).findById(uuidBauvorhaben);
        Mockito.reset(this.infrastruktureinrichtungRepository, this.bauvorhabenRepository);

        final var uuidInfrastruktureinrichtung = UUID.randomUUID();
        final var infrastruktureinrichtung = new Kinderkrippe();
        infrastruktureinrichtung.setId(uuidInfrastruktureinrichtung);
        kommentar = new Kommentar();
        kommentarModel = new KommentarModel();
        kommentarModel.setInfrastruktureinrichtung(uuidInfrastruktureinrichtung);

        Mockito
            .when(this.infrastruktureinrichtungRepository.findById(uuidInfrastruktureinrichtung))
            .thenReturn(Optional.of(infrastruktureinrichtung));

        kommentarDomainMapper.afterMappingModel2Entity(kommentarModel, kommentar);

        assertThat(kommentar.getBauvorhaben(), is(nullValue()));
        assertThat(kommentar.getInfrastruktureinrichtung(), is(infrastruktureinrichtung));

        Mockito.verify(this.bauvorhabenRepository, Mockito.times(0)).findById(uuidInfrastruktureinrichtung);
        Mockito
            .verify(this.infrastruktureinrichtungRepository, Mockito.times(1))
            .findById(uuidInfrastruktureinrichtung);
    }
}
