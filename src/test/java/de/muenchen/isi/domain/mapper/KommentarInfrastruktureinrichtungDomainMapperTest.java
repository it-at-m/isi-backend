package de.muenchen.isi.domain.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.common.KommentarInfrastruktureinrichtungModel;
import de.muenchen.isi.infrastructure.entity.common.Kommentar;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kinderkrippe;
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
class KommentarInfrastruktureinrichtungDomainMapperTest {

    @Mock
    private InfrastruktureinrichtungRepository infrastruktureinrichtungRepository;

    private final KommentarInfrastruktureinrichtungDomainMapper kommentarInfrastruktureinrichtungDomainMapper =
        new KommentarInfrastruktureinrichtungDomainMapperImpl();

    @BeforeEach
    public void beforeEach() throws NoSuchFieldException, IllegalAccessException {
        Field field = kommentarInfrastruktureinrichtungDomainMapper
            .getClass()
            .getSuperclass()
            .getDeclaredField("infrastruktureinrichtungRepository");
        field.setAccessible(true);
        field.set(kommentarInfrastruktureinrichtungDomainMapper, infrastruktureinrichtungRepository);
        Mockito.reset(this.infrastruktureinrichtungRepository);
    }

    @Test
    void afterMappingModel2Entity() throws EntityNotFoundException {
        var kommentar = new Kommentar();
        var kommentarModel = new KommentarInfrastruktureinrichtungModel();

        try {
            kommentarInfrastruktureinrichtungDomainMapper.afterMappingModel2Entity(kommentarModel, kommentar);
        } catch (EntityNotFoundException exception) {
            assertThat(exception.getMessage(), is("Der Kommentar referenziert keine Infrastruktureinrichtung"));
        }
        Mockito.verify(this.infrastruktureinrichtungRepository, Mockito.times(0)).findById(null);
        Mockito.reset(this.infrastruktureinrichtungRepository);

        final var uuidInfrastruktureinrichtung = UUID.randomUUID();
        final var infrastruktureinrichtung = new Kinderkrippe();
        infrastruktureinrichtung.setId(uuidInfrastruktureinrichtung);
        kommentar = new Kommentar();
        kommentarModel = new KommentarInfrastruktureinrichtungModel();
        kommentarModel.setInfrastruktureinrichtung(uuidInfrastruktureinrichtung);

        Mockito.when(this.infrastruktureinrichtungRepository.findById(uuidInfrastruktureinrichtung)).thenReturn(
            Optional.of(infrastruktureinrichtung)
        );

        kommentarInfrastruktureinrichtungDomainMapper.afterMappingModel2Entity(kommentarModel, kommentar);

        assertThat(kommentar.getBauvorhaben(), is(nullValue()));
        assertThat(kommentar.getInfrastruktureinrichtung(), is(infrastruktureinrichtung));

        Mockito.verify(this.infrastruktureinrichtungRepository, Mockito.times(1)).findById(
            uuidInfrastruktureinrichtung
        );
    }
}
