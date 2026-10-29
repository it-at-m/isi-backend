package de.muenchen.isi.domain.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.common.KommentarBauvorhabenModel;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.common.Kommentar;
import de.muenchen.isi.infrastructure.repository.BauvorhabenRepository;
import java.lang.reflect.Field;
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
class KommentarDomainBauvorhabenMapperTest {

    @Mock
    private BauvorhabenRepository bauvorhabenRepository;

    private final KommentarBauvorhabenDomainMapper kommentarBauvorhabenDomainMapper =
        new KommentarBauvorhabenDomainMapperImpl();

    @BeforeEach
    public void beforeEach() throws NoSuchFieldException, IllegalAccessException {
        Field field = kommentarBauvorhabenDomainMapper
            .getClass()
            .getSuperclass()
            .getDeclaredField("bauvorhabenRepository");
        field.setAccessible(true);
        field.set(kommentarBauvorhabenDomainMapper, bauvorhabenRepository);
        Mockito.reset(this.bauvorhabenRepository);
    }

    @Test
    void afterMappingModel2Entity() throws EntityNotFoundException {
        var kommentar = new Kommentar();
        var kommentarBauvorhabenModel = new KommentarBauvorhabenModel();

        try {
            kommentarBauvorhabenDomainMapper.afterMappingModel2Entity(kommentarBauvorhabenModel, kommentar);
        } catch (EntityNotFoundException exception) {
            assertThat(exception.getMessage(), is("Der Kommentar referenziert kein Bauvorhaben"));
        }
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(0)).findById(null);
        Mockito.reset(this.bauvorhabenRepository);

        final var uuidBauvorhaben = UUID.randomUUID();
        final var bauvorhaben = new Bauvorhaben();
        bauvorhaben.setId(uuidBauvorhaben);
        kommentar = new Kommentar();

        kommentarBauvorhabenModel = new KommentarBauvorhabenModel();
        kommentarBauvorhabenModel.setBauvorhaben(uuidBauvorhaben);

        Mockito.when(this.bauvorhabenRepository.existsById(uuidBauvorhaben)).thenReturn(true);
        Mockito.when(this.bauvorhabenRepository.getReferenceById(uuidBauvorhaben)).thenReturn(bauvorhaben);

        kommentarBauvorhabenDomainMapper.afterMappingModel2Entity(kommentarBauvorhabenModel, kommentar);

        assertThat(kommentar.getBauvorhaben(), is(bauvorhaben));
        assertThat(kommentar.getInfrastruktureinrichtung(), is(nullValue()));

        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).existsById(uuidBauvorhaben);
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).getReferenceById(uuidBauvorhaben);
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(0)).findById(uuidBauvorhaben);
        Mockito.reset(this.bauvorhabenRepository);

        kommentar = new Kommentar();
        Mockito.when(this.bauvorhabenRepository.existsById(uuidBauvorhaben)).thenReturn(false);

        final var finalKommentar = kommentar;
        final var finalKommentarBauvorhabenModel = kommentarBauvorhabenModel;
        final var exception = assertThrows(EntityNotFoundException.class, () ->
            kommentarBauvorhabenDomainMapper.afterMappingModel2Entity(finalKommentarBauvorhabenModel, finalKommentar)
        );
        assertThat(exception.getMessage(), is("Bauvorhaben für den Kommentar nicht gefunden"));
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(0)).getReferenceById(uuidBauvorhaben);
        Mockito.reset(this.bauvorhabenRepository);
    }
}
