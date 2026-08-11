package de.muenchen.isi.domain.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import de.muenchen.isi.domain.mapper.converter.AbfrageConverterCommonMapperImpl;
import de.muenchen.isi.domain.mapper.converter.AbfrageConverterDomainMapper;
import de.muenchen.isi.domain.mapper.converter.AbfrageConverterDomainMapperImpl;
import de.muenchen.isi.domain.mapper.converter.AbfragevarianteConverterDomainMapper;
import de.muenchen.isi.domain.mapper.converter.AbfragevarianteConverterDomainMapperImpl;
import de.muenchen.isi.domain.model.AbfragevarianteBauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.AbfragevarianteWeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.WeiteresVerfahrenModel;
import de.muenchen.isi.infrastructure.entity.Bauleitplanverfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AbfrageConverterDomainMapperTest {

    private final AbfragevarianteConverterDomainMapper abfragevarianteConverterDomainMapper =
        new AbfragevarianteConverterDomainMapperImpl();

    private final AbfrageConverterDomainMapper abfrageConverterDomainMapper = new AbfrageConverterDomainMapperImpl(
        new AbfrageConverterCommonMapperImpl()
    );

    @BeforeEach
    public void beforeEach() throws NoSuchFieldException, IllegalAccessException {
        Field field = abfrageConverterDomainMapper
            .getClass()
            .getSuperclass()
            .getDeclaredField("abfragevarianteConverterDomainMapper");
        field.setAccessible(true);
        field.set(abfrageConverterDomainMapper, abfragevarianteConverterDomainMapper);
    }

    @Test
    public void convertWvModel2BlvModel() {
        WeiteresVerfahrenModel wvModel = new WeiteresVerfahrenModel();
        wvModel.setId(UUID.randomUUID());
        wvModel.setVersion(1L);
        wvModel.setSub("Testuser");
        wvModel.setName("Abfrage");
        wvModel.setArtAbfrage(ArtAbfrage.WEITERES_VERFAHREN);
        wvModel.setStatusAbfrage(StatusAbfrage.START_BEARBEITUNG);

        AbfragevarianteWeiteresVerfahrenModel abfragevarianteWeiteresVerfahrenModel1 =
            new AbfragevarianteWeiteresVerfahrenModel();
        abfragevarianteWeiteresVerfahrenModel1.setName("Abfragevariante 1");
        abfragevarianteWeiteresVerfahrenModel1.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);

        AbfragevarianteWeiteresVerfahrenModel abfragevarianteWeiteresVerfahrenModel2 =
            new AbfragevarianteWeiteresVerfahrenModel();
        abfragevarianteWeiteresVerfahrenModel2.setName("Abfragevariante 2");
        abfragevarianteWeiteresVerfahrenModel2.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);

        List<AbfragevarianteWeiteresVerfahrenModel> abfragevarianten = new ArrayList<>();
        List<AbfragevarianteWeiteresVerfahrenModel> abfragevariantenSachbearbeitung = new ArrayList<>();

        abfragevarianten.add(abfragevarianteWeiteresVerfahrenModel1);
        abfragevariantenSachbearbeitung.add(abfragevarianteWeiteresVerfahrenModel2);
        wvModel.setAbfragevariantenWeiteresVerfahren(abfragevarianten);
        wvModel.setAbfragevariantenSachbearbeitungWeiteresVerfahren(abfragevariantenSachbearbeitung);

        var blvModel = abfrageConverterDomainMapper.convertWv2BlvModel(wvModel);

        assertThat(blvModel.getId(), is(nullValue()));
        assertThat(blvModel.getVersion(), is(nullValue()));
        assertThat(blvModel.getArtAbfrage(), is(ArtAbfrage.BAULEITPLANVERFAHREN));
        assertThat(blvModel.getName(), is(wvModel.getName()));
        assertThat(
            blvModel.getAbfragevariantenBauleitplanverfahren().get(0).getName(),
            is(wvModel.getAbfragevariantenWeiteresVerfahren().get(0).getName())
        );
        assertThat(
            blvModel.getAbfragevariantenBauleitplanverfahren().get(0).getArtAbfragevariante(),
            is(ArtAbfrage.BAULEITPLANVERFAHREN)
        );
        assertThat(
            blvModel.getAbfragevariantenSachbearbeitungBauleitplanverfahren().get(0).getName(),
            is(wvModel.getAbfragevariantenSachbearbeitungWeiteresVerfahren().get(0).getName())
        );
        assertThat(
            blvModel.getAbfragevariantenSachbearbeitungBauleitplanverfahren().get(0).getArtAbfragevariante(),
            is(ArtAbfrage.BAULEITPLANVERFAHREN)
        );
    }

    @Test
    public void convertWvModel2BgvModel() {
        WeiteresVerfahrenModel wvModel = new WeiteresVerfahrenModel();
        wvModel.setId(UUID.randomUUID());
        wvModel.setVersion(1L);
        wvModel.setSub("Testuser");
        wvModel.setName("Abfrage");
        wvModel.setArtAbfrage(ArtAbfrage.WEITERES_VERFAHREN);
        wvModel.setStatusAbfrage(StatusAbfrage.START_BEARBEITUNG);

        AbfragevarianteWeiteresVerfahrenModel abfragevarianteWeiteresVerfahrenModel1 =
            new AbfragevarianteWeiteresVerfahrenModel();
        abfragevarianteWeiteresVerfahrenModel1.setName("Abfragevariante 1");
        abfragevarianteWeiteresVerfahrenModel1.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);

        AbfragevarianteWeiteresVerfahrenModel abfragevarianteWeiteresVerfahrenModel2 =
            new AbfragevarianteWeiteresVerfahrenModel();
        abfragevarianteWeiteresVerfahrenModel2.setName("Abfragevariante 2");
        abfragevarianteWeiteresVerfahrenModel2.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);

        List<AbfragevarianteWeiteresVerfahrenModel> abfragevarianten = new ArrayList<>();
        List<AbfragevarianteWeiteresVerfahrenModel> abfragevariantenSachbearbeitung = new ArrayList<>();

        abfragevarianten.add(abfragevarianteWeiteresVerfahrenModel1);
        abfragevariantenSachbearbeitung.add(abfragevarianteWeiteresVerfahrenModel2);
        wvModel.setAbfragevariantenWeiteresVerfahren(abfragevarianten);
        wvModel.setAbfragevariantenSachbearbeitungWeiteresVerfahren(abfragevariantenSachbearbeitung);

        var bgvModel = abfrageConverterDomainMapper.convertWv2BgvModel(wvModel);

        assertThat(bgvModel.getId(), is(nullValue()));
        assertThat(bgvModel.getVersion(), is(nullValue()));
        assertThat(bgvModel.getArtAbfrage(), is(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN));
        assertThat(bgvModel.getName(), is(wvModel.getName()));
        assertThat(
            bgvModel.getAbfragevariantenBaugenehmigungsverfahren().get(0).getName(),
            is(wvModel.getAbfragevariantenWeiteresVerfahren().get(0).getName())
        );
        assertThat(
            bgvModel.getAbfragevariantenBaugenehmigungsverfahren().get(0).getArtAbfragevariante(),
            is(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN)
        );
        assertThat(
            bgvModel.getAbfragevariantenSachbearbeitungBaugenehmigungsverfahren().get(0).getName(),
            is(wvModel.getAbfragevariantenSachbearbeitungWeiteresVerfahren().get(0).getName())
        );
        assertThat(
            bgvModel.getAbfragevariantenSachbearbeitungBaugenehmigungsverfahren().get(0).getArtAbfragevariante(),
            is(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN)
        );
    }

    @Test
    public void convertBlvModel2BgvModel() {
        BauleitplanverfahrenModel blvModel = new BauleitplanverfahrenModel();
        blvModel.setId(UUID.randomUUID());
        blvModel.setVersion(1L);
        blvModel.setSub("Testuser");
        blvModel.setName("Abfrage");
        blvModel.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        blvModel.setStatusAbfrage(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG);

        AbfragevarianteBauleitplanverfahrenModel abfragevarianteBauleitplanverfahrenModel1 =
            new AbfragevarianteBauleitplanverfahrenModel();
        abfragevarianteBauleitplanverfahrenModel1.setName("Abfragevariante 1");
        abfragevarianteBauleitplanverfahrenModel1.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);

        AbfragevarianteBauleitplanverfahrenModel abfragevarianteBauleitplanverfahrenModel2 =
            new AbfragevarianteBauleitplanverfahrenModel();
        abfragevarianteBauleitplanverfahrenModel2.setName("Abfragevariante 2");
        abfragevarianteBauleitplanverfahrenModel2.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);

        List<AbfragevarianteBauleitplanverfahrenModel> abfragevarianten = new ArrayList<>();
        List<AbfragevarianteBauleitplanverfahrenModel> abfragevariantenSachbearbeitung = new ArrayList<>();

        abfragevarianten.add(abfragevarianteBauleitplanverfahrenModel1);
        abfragevariantenSachbearbeitung.add(abfragevarianteBauleitplanverfahrenModel2);
        blvModel.setAbfragevariantenBauleitplanverfahren(abfragevarianten);
        blvModel.setAbfragevariantenSachbearbeitungBauleitplanverfahren(abfragevariantenSachbearbeitung);

        var bgvModel = abfrageConverterDomainMapper.convertBlv2BgvModel(blvModel);

        assertThat(bgvModel.getId(), is(nullValue()));
        assertThat(bgvModel.getVersion(), is(nullValue()));
        assertThat(bgvModel.getArtAbfrage(), is(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN));
        assertThat(bgvModel.getName(), is(blvModel.getName()));
        assertThat(
            bgvModel.getAbfragevariantenBaugenehmigungsverfahren().get(0).getName(),
            is(blvModel.getAbfragevariantenBauleitplanverfahren().get(0).getName())
        );
        assertThat(
            bgvModel.getAbfragevariantenBaugenehmigungsverfahren().get(0).getArtAbfragevariante(),
            is(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN)
        );
        assertThat(
            bgvModel.getAbfragevariantenSachbearbeitungBaugenehmigungsverfahren().get(0).getName(),
            is(blvModel.getAbfragevariantenSachbearbeitungBauleitplanverfahren().get(0).getName())
        );
        assertThat(
            bgvModel.getAbfragevariantenSachbearbeitungBaugenehmigungsverfahren().get(0).getArtAbfragevariante(),
            is(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN)
        );
    }
}
