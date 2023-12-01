package de.muenchen.isi.domain.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.model.AbfragevarianteBaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.AbfragevarianteBauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.AbfragevarianteWeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.BaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.WeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfragevarianteBaugenehmigungsverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfragevarianteBauleitplanverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfragevarianteWeiteresVerfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.BaugenehmigungsverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.BauleitplanverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.WeiteresVerfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.AbfragevarianteBaugenehmigungsverfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.AbfragevarianteBaugenehmigungsverfahrenSachbearbeitungInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.AbfragevarianteBauleitplanverfahrenSachbearbeitungInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.AbfragevarianteWeiteresVerfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.AbfragevarianteWeiteresVerfahrenSachbearbeitungInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.BaugenehmigungsverfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.BauleitplanverfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.WeiteresVerfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.repository.BauvorhabenRepository;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
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
public class AbfrageDomainMapperTest {

    private DokumentDomainMapper dokumentDomainMapper = new DokumentDomainMapperImpl();

    private BauabschnittDomainMapper bauabschnittDomainMapper = new BauabschnittDomainMapperImpl();

    private AbfragevarianteDomainMapper abfragevarianteDomainMapper = new AbfragevarianteDomainMapperImpl(
        bauabschnittDomainMapper
    );

    private AbfrageDomainMapper abfrageDomainMapper = new AbfrageDomainMapperImpl(
        abfragevarianteDomainMapper,
        dokumentDomainMapper
    );

    @Mock
    private BauvorhabenRepository bauvorhabenRepository;

    @BeforeEach
    public void beforeEach() throws NoSuchFieldException, IllegalAccessException {
        Field field = abfrageDomainMapper.getClass().getSuperclass().getDeclaredField("bauvorhabenRepository");
        field.setAccessible(true);
        field.set(abfrageDomainMapper, bauvorhabenRepository);
        field = abfrageDomainMapper.getClass().getSuperclass().getDeclaredField("abfragevarianteDomainMapper");
        field.setAccessible(true);
        field.set(abfrageDomainMapper, abfragevarianteDomainMapper);
        Mockito.reset(this.bauvorhabenRepository);
    }

    @Test
    public void abfrageErstellungAbfrageToAbfrageNoExistingAbfragevarianteBauleitplanverfahren() {
        BauleitplanverfahrenAngelegtModel bauleitplanverfahrenModel = new BauleitplanverfahrenAngelegtModel();
        bauleitplanverfahrenModel.setVersion(1L);
        bauleitplanverfahrenModel.setName("Abfrage");

        AbfragevarianteBauleitplanverfahrenAngelegtModel abfragevarianteAngelegtModel =
            new AbfragevarianteBauleitplanverfahrenAngelegtModel();
        abfragevarianteAngelegtModel.setName("Abfragevariante 1");

        AbfragevarianteBauleitplanverfahrenAngelegtModel abfragevarianteAngelegtModel2 =
            new AbfragevarianteBauleitplanverfahrenAngelegtModel();
        abfragevarianteAngelegtModel2.setName("Abfragevariante 2");

        List<AbfragevarianteBauleitplanverfahrenAngelegtModel> abfragevarianten = new ArrayList<>();

        abfragevarianten.add(abfragevarianteAngelegtModel);
        abfragevarianten.add(abfragevarianteAngelegtModel2);
        bauleitplanverfahrenModel.setAbfragevariantenBauleitplanverfahren(abfragevarianten);

        var result = abfrageDomainMapper.request2Model(bauleitplanverfahrenModel, new BauleitplanverfahrenModel());

        assertThat(result.getVersion(), is(bauleitplanverfahrenModel.getVersion()));
        assertThat(result.getName(), is(bauleitplanverfahrenModel.getName()));
        assertThat(
            result.getAbfragevariantenBauleitplanverfahren().get(0).getName(),
            is(bauleitplanverfahrenModel.getAbfragevariantenBauleitplanverfahren().get(0).getName())
        );
        assertThat(
            result.getAbfragevariantenBauleitplanverfahren().get(1).getName(),
            is(bauleitplanverfahrenModel.getAbfragevariantenBauleitplanverfahren().get(1).getName())
        );
    }

    @Test
    public void abfrageErstellungAbfrageToAbfrageNoExistingAbfragevarianteBaugenehmigungsverfahren() {
        BaugenehmigungsverfahrenAngelegtModel baugenehmigungsverfahrenModel =
            new BaugenehmigungsverfahrenAngelegtModel();
        baugenehmigungsverfahrenModel.setVersion(1L);
        baugenehmigungsverfahrenModel.setName("Abfrage");

        AbfragevarianteBaugenehmigungsverfahrenAngelegtModel abfragevarianteAngelegtModel =
            new AbfragevarianteBaugenehmigungsverfahrenAngelegtModel();
        abfragevarianteAngelegtModel.setName("Abfragevariante 1");

        AbfragevarianteBaugenehmigungsverfahrenAngelegtModel abfragevarianteAngelegtModel2 =
            new AbfragevarianteBaugenehmigungsverfahrenAngelegtModel();
        abfragevarianteAngelegtModel2.setName("Abfragevariante 2");

        List<AbfragevarianteBaugenehmigungsverfahrenAngelegtModel> abfragevarianten = new ArrayList<>();

        abfragevarianten.add(abfragevarianteAngelegtModel);
        abfragevarianten.add(abfragevarianteAngelegtModel2);
        baugenehmigungsverfahrenModel.setAbfragevariantenBaugenehmigungsverfahren(abfragevarianten);

        var result = abfrageDomainMapper.request2Model(
            baugenehmigungsverfahrenModel,
            new BaugenehmigungsverfahrenModel()
        );

        assertThat(result.getVersion(), is(baugenehmigungsverfahrenModel.getVersion()));
        assertThat(result.getName(), is(baugenehmigungsverfahrenModel.getName()));
        assertThat(
            result.getAbfragevariantenBaugenehmigungsverfahren().get(0).getName(),
            is(baugenehmigungsverfahrenModel.getAbfragevariantenBaugenehmigungsverfahren().get(0).getName())
        );
        assertThat(
            result.getAbfragevariantenBaugenehmigungsverfahren().get(1).getName(),
            is(baugenehmigungsverfahrenModel.getAbfragevariantenBaugenehmigungsverfahren().get(1).getName())
        );
    }

    @Test
    public void abfrageErstellungAbfrageToAbfrageNoExistingAbfragevarianteWeiteresVerfahren() {
        WeiteresVerfahrenAngelegtModel weiteresVerfahrenModel = new WeiteresVerfahrenAngelegtModel();
        weiteresVerfahrenModel.setVersion(1L);
        weiteresVerfahrenModel.setName("Abfrage");

        AbfragevarianteWeiteresVerfahrenAngelegtModel abfragevarianteAngelegtModel =
            new AbfragevarianteWeiteresVerfahrenAngelegtModel();
        abfragevarianteAngelegtModel.setName("Abfragevariante 1");

        AbfragevarianteWeiteresVerfahrenAngelegtModel abfragevarianteAngelegtModel2 =
            new AbfragevarianteWeiteresVerfahrenAngelegtModel();
        abfragevarianteAngelegtModel2.setName("Abfragevariante 2");

        List<AbfragevarianteWeiteresVerfahrenAngelegtModel> abfragevarianten = new ArrayList<>();

        abfragevarianten.add(abfragevarianteAngelegtModel);
        abfragevarianten.add(abfragevarianteAngelegtModel2);
        weiteresVerfahrenModel.setAbfragevariantenWeiteresVerfahren(abfragevarianten);

        var result = abfrageDomainMapper.request2Model(weiteresVerfahrenModel, new WeiteresVerfahrenModel());

        assertThat(result.getVersion(), is(weiteresVerfahrenModel.getVersion()));
        assertThat(result.getName(), is(weiteresVerfahrenModel.getName()));
        assertThat(
            result.getAbfragevariantenWeiteresVerfahren().get(0).getName(),
            is(weiteresVerfahrenModel.getAbfragevariantenWeiteresVerfahren().get(0).getName())
        );
        assertThat(
            result.getAbfragevariantenWeiteresVerfahren().get(1).getName(),
            is(weiteresVerfahrenModel.getAbfragevariantenWeiteresVerfahren().get(1).getName())
        );
    }

    @Test
    public void abfrageErstellungAbfrageToAbfrageExistingAbfragevarianteBauleitplanverfahren() {
        var abfragevarianteId = UUID.randomUUID();
        BauleitplanverfahrenAngelegtModel bauleitplanverfahrenAngelegtModel = new BauleitplanverfahrenAngelegtModel();
        bauleitplanverfahrenAngelegtModel.setVersion(1L);
        bauleitplanverfahrenAngelegtModel.setName("Abfrage");

        AbfragevarianteBauleitplanverfahrenAngelegtModel abfragevarianteAngelegtModel =
            new AbfragevarianteBauleitplanverfahrenAngelegtModel();
        abfragevarianteAngelegtModel.setName("Abfragevariante 1");

        AbfragevarianteBauleitplanverfahrenAngelegtModel abfragevarianteAngelegtModel2 =
            new AbfragevarianteBauleitplanverfahrenAngelegtModel();
        abfragevarianteAngelegtModel2.setName("Abfragevariante 2");
        abfragevarianteAngelegtModel2.setId(abfragevarianteId);

        List<AbfragevarianteBauleitplanverfahrenAngelegtModel> abfragevarianteAngelegtModels = new ArrayList<>();

        abfragevarianteAngelegtModels.add(abfragevarianteAngelegtModel);
        abfragevarianteAngelegtModels.add(abfragevarianteAngelegtModel2);

        BauleitplanverfahrenModel bauleitplanverfahrenModel = new BauleitplanverfahrenModel();

        AbfragevarianteBauleitplanverfahrenModel abfragevarianteModel = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevarianteModel.setName("Abfragevariante 3");
        abfragevarianteModel.setId(abfragevarianteId);

        List<AbfragevarianteBauleitplanverfahrenModel> abfragevariantenModelList = new ArrayList<>();
        abfragevariantenModelList.add(abfragevarianteModel);

        bauleitplanverfahrenModel.setAbfragevariantenBauleitplanverfahren(abfragevariantenModelList);

        bauleitplanverfahrenAngelegtModel.setAbfragevariantenBauleitplanverfahren(abfragevarianteAngelegtModels);

        var result = abfrageDomainMapper.request2Model(bauleitplanverfahrenAngelegtModel, bauleitplanverfahrenModel);

        assertThat(result.getVersion(), is(bauleitplanverfahrenAngelegtModel.getVersion()));
        assertThat(result.getName(), is(bauleitplanverfahrenAngelegtModel.getName()));
        assertThat(
            result.getAbfragevariantenBauleitplanverfahren().get(0).getName(),
            is(bauleitplanverfahrenAngelegtModel.getAbfragevariantenBauleitplanverfahren().get(0).getName())
        );
        assertThat(
            result.getAbfragevariantenBauleitplanverfahren().get(1).getName(),
            is(bauleitplanverfahrenAngelegtModel.getAbfragevariantenBauleitplanverfahren().get(1).getName())
        );
    }

    @Test
    public void abfrageErstellungAbfrageToAbfrageExistingAbfragevarianteBaugenehmigungsverfahren() {
        var abfragevarianteId = UUID.randomUUID();
        BaugenehmigungsverfahrenAngelegtModel baugenehmigungsverfahrenAngelegtModel =
            new BaugenehmigungsverfahrenAngelegtModel();
        baugenehmigungsverfahrenAngelegtModel.setVersion(1L);
        baugenehmigungsverfahrenAngelegtModel.setName("Abfrage");

        AbfragevarianteBaugenehmigungsverfahrenAngelegtModel abfragevarianteAngelegtModel =
            new AbfragevarianteBaugenehmigungsverfahrenAngelegtModel();
        abfragevarianteAngelegtModel.setName("Abfragevariante 1");

        AbfragevarianteBaugenehmigungsverfahrenAngelegtModel abfragevarianteAngelegtModel2 =
            new AbfragevarianteBaugenehmigungsverfahrenAngelegtModel();
        abfragevarianteAngelegtModel2.setName("Abfragevariante 2");
        abfragevarianteAngelegtModel2.setId(abfragevarianteId);

        List<AbfragevarianteBaugenehmigungsverfahrenAngelegtModel> abfragevarianteAngelegtModels = new ArrayList<>();

        abfragevarianteAngelegtModels.add(abfragevarianteAngelegtModel);
        abfragevarianteAngelegtModels.add(abfragevarianteAngelegtModel2);

        BaugenehmigungsverfahrenModel baugenehmigungsverfahrenModel = new BaugenehmigungsverfahrenModel();

        AbfragevarianteBaugenehmigungsverfahrenModel abfragevarianteModel =
            new AbfragevarianteBaugenehmigungsverfahrenModel();
        abfragevarianteModel.setName("Abfragevariante 3");
        abfragevarianteModel.setId(abfragevarianteId);

        List<AbfragevarianteBaugenehmigungsverfahrenModel> abfragevariantenModelList = new ArrayList<>();
        abfragevariantenModelList.add(abfragevarianteModel);

        baugenehmigungsverfahrenModel.setAbfragevariantenBaugenehmigungsverfahren(abfragevariantenModelList);

        baugenehmigungsverfahrenAngelegtModel.setAbfragevariantenBaugenehmigungsverfahren(
            abfragevarianteAngelegtModels
        );

        var result = abfrageDomainMapper.request2Model(
            baugenehmigungsverfahrenAngelegtModel,
            baugenehmigungsverfahrenModel
        );

        assertThat(result.getVersion(), is(baugenehmigungsverfahrenAngelegtModel.getVersion()));
        assertThat(result.getName(), is(baugenehmigungsverfahrenAngelegtModel.getName()));
        assertThat(
            result.getAbfragevariantenBaugenehmigungsverfahren().get(0).getName(),
            is(baugenehmigungsverfahrenAngelegtModel.getAbfragevariantenBaugenehmigungsverfahren().get(0).getName())
        );
        assertThat(
            result.getAbfragevariantenBaugenehmigungsverfahren().get(1).getName(),
            is(baugenehmigungsverfahrenAngelegtModel.getAbfragevariantenBaugenehmigungsverfahren().get(1).getName())
        );
    }

    @Test
    public void abfrageErstellungAbfrageToAbfrageExistingAbfragevarianteWeiteresVerfahren() {
        var abfragevarianteId = UUID.randomUUID();
        WeiteresVerfahrenAngelegtModel weiteresVerfahrenAngelegtModel = new WeiteresVerfahrenAngelegtModel();
        weiteresVerfahrenAngelegtModel.setVersion(1L);
        weiteresVerfahrenAngelegtModel.setName("Abfrage");

        AbfragevarianteWeiteresVerfahrenAngelegtModel abfragevarianteAngelegtModel =
            new AbfragevarianteWeiteresVerfahrenAngelegtModel();
        abfragevarianteAngelegtModel.setName("Abfragevariante 1");

        AbfragevarianteWeiteresVerfahrenAngelegtModel abfragevarianteAngelegtModel2 =
            new AbfragevarianteWeiteresVerfahrenAngelegtModel();
        abfragevarianteAngelegtModel2.setName("Abfragevariante 2");
        abfragevarianteAngelegtModel2.setId(abfragevarianteId);

        List<AbfragevarianteWeiteresVerfahrenAngelegtModel> abfragevarianteAngelegtModels = new ArrayList<>();

        abfragevarianteAngelegtModels.add(abfragevarianteAngelegtModel);
        abfragevarianteAngelegtModels.add(abfragevarianteAngelegtModel2);

        WeiteresVerfahrenModel weiteresVerfahrenModel = new WeiteresVerfahrenModel();

        AbfragevarianteWeiteresVerfahrenModel abfragevarianteModel = new AbfragevarianteWeiteresVerfahrenModel();
        abfragevarianteModel.setName("Abfragevariante 3");
        abfragevarianteModel.setId(abfragevarianteId);

        List<AbfragevarianteWeiteresVerfahrenModel> abfragevariantenModelList = new ArrayList<>();
        abfragevariantenModelList.add(abfragevarianteModel);

        weiteresVerfahrenModel.setAbfragevariantenWeiteresVerfahren(abfragevariantenModelList);

        weiteresVerfahrenAngelegtModel.setAbfragevariantenWeiteresVerfahren(abfragevarianteAngelegtModels);

        var result = abfrageDomainMapper.request2Model(weiteresVerfahrenAngelegtModel, weiteresVerfahrenModel);

        assertThat(result.getVersion(), is(weiteresVerfahrenAngelegtModel.getVersion()));
        assertThat(result.getName(), is(weiteresVerfahrenAngelegtModel.getName()));
        assertThat(
            result.getAbfragevariantenWeiteresVerfahren().get(0).getName(),
            is(weiteresVerfahrenAngelegtModel.getAbfragevariantenWeiteresVerfahren().get(0).getName())
        );
        assertThat(
            result.getAbfragevariantenWeiteresVerfahren().get(1).getName(),
            is(weiteresVerfahrenAngelegtModel.getAbfragevariantenWeiteresVerfahren().get(1).getName())
        );
    }

    @Test
    void sachbearbeitungAbfrageToAbfrageNonExistingAbfragevarianteBauleitplanverfahren() {
        var bauleitplanverfahren = new BauleitplanverfahrenInBearbeitungSachbearbeitungModel();
        bauleitplanverfahren.setVersion(99L);
        bauleitplanverfahren.setAbfragevariantenBauleitplanverfahren(List.of());

        var abfragevarianteSachbearbeitung1 =
            new AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung1.setAbfragevariantenNr(1);
        abfragevarianteSachbearbeitung1.setName("Abfragevariante 1");

        var abfragevarianteSachbearbeitung2 =
            new AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung2.setAbfragevariantenNr(2);
        abfragevarianteSachbearbeitung2.setName("Abfragevariante 2");

        bauleitplanverfahren.setAbfragevariantenSachbearbeitungBauleitplanverfahren(
            List.of(abfragevarianteSachbearbeitung1, abfragevarianteSachbearbeitung2)
        );

        final var result = abfrageDomainMapper.request2Model(bauleitplanverfahren, new BauleitplanverfahrenModel());

        final var expected = new BauleitplanverfahrenModel();
        expected.setVersion(99L);

        final var abfragevariante1 = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevariante1.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfragevariante1.setAbfragevariantenNr(1);
        abfragevariante1.setName("Abfragevariante 1");

        final var abfragevariante2 = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevariante2.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfragevariante2.setAbfragevariantenNr(2);
        abfragevariante2.setName("Abfragevariante 2");

        expected.setAbfragevariantenBauleitplanverfahren(List.of());
        expected.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of(abfragevariante1, abfragevariante2));

        assertThat(result, is(expected));
    }

    @Test
    void sachbearbeitungAbfrageToAbfrageNonExistingAbfragevarianteBaugenehmigungsverfahren() {
        var baugenehmigungsverfahren = new BaugenehmigungsverfahrenInBearbeitungSachbearbeitungModel();
        baugenehmigungsverfahren.setVersion(99L);
        baugenehmigungsverfahren.setAbfragevariantenBaugenehmigungsverfahren(List.of());

        var abfragevarianteSachbearbeitung1 =
            new AbfragevarianteBaugenehmigungsverfahrenInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung1.setAbfragevariantenNr(1);
        abfragevarianteSachbearbeitung1.setName("Abfragevariante 1");

        var abfragevarianteSachbearbeitung2 =
            new AbfragevarianteBaugenehmigungsverfahrenInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung2.setAbfragevariantenNr(2);
        abfragevarianteSachbearbeitung2.setName("Abfragevariante 2");

        baugenehmigungsverfahren.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(
            List.of(abfragevarianteSachbearbeitung1, abfragevarianteSachbearbeitung2)
        );

        final var result = abfrageDomainMapper.request2Model(
            baugenehmigungsverfahren,
            new BaugenehmigungsverfahrenModel()
        );

        final var expected = new BaugenehmigungsverfahrenModel();
        expected.setVersion(99L);

        final var abfragevariante1 = new AbfragevarianteBaugenehmigungsverfahrenModel();
        abfragevariante1.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfragevariante1.setAbfragevariantenNr(1);
        abfragevariante1.setName("Abfragevariante 1");

        final var abfragevariante2 = new AbfragevarianteBaugenehmigungsverfahrenModel();
        abfragevariante2.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfragevariante2.setAbfragevariantenNr(2);
        abfragevariante2.setName("Abfragevariante 2");

        expected.setAbfragevariantenBaugenehmigungsverfahren(List.of());
        expected.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(
            List.of(abfragevariante1, abfragevariante2)
        );

        assertThat(result, is(expected));
    }

    @Test
    void sachbearbeitungAbfrageToAbfrageNonExistingAbfragevarianteWeiteresVerfahren() {
        var weiteresVerfahren = new WeiteresVerfahrenInBearbeitungSachbearbeitungModel();
        weiteresVerfahren.setVersion(99L);
        weiteresVerfahren.setAbfragevariantenWeiteresVerfahren(List.of());

        var abfragevarianteSachbearbeitung1 = new AbfragevarianteWeiteresVerfahrenInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung1.setAbfragevariantenNr(1);
        abfragevarianteSachbearbeitung1.setName("Abfragevariante 1");

        var abfragevarianteSachbearbeitung2 = new AbfragevarianteWeiteresVerfahrenInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung2.setAbfragevariantenNr(2);
        abfragevarianteSachbearbeitung2.setName("Abfragevariante 2");

        weiteresVerfahren.setAbfragevariantenSachbearbeitungWeiteresVerfahren(
            List.of(abfragevarianteSachbearbeitung1, abfragevarianteSachbearbeitung2)
        );

        final var result = abfrageDomainMapper.request2Model(weiteresVerfahren, new WeiteresVerfahrenModel());

        final var expected = new WeiteresVerfahrenModel();
        expected.setVersion(99L);

        final var abfragevariante1 = new AbfragevarianteWeiteresVerfahrenModel();
        abfragevariante1.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
        abfragevariante1.setAbfragevariantenNr(1);
        abfragevariante1.setName("Abfragevariante 1");

        final var abfragevariante2 = new AbfragevarianteWeiteresVerfahrenModel();
        abfragevariante2.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
        abfragevariante2.setAbfragevariantenNr(2);
        abfragevariante2.setName("Abfragevariante 2");

        expected.setAbfragevariantenWeiteresVerfahren(List.of());
        expected.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of(abfragevariante1, abfragevariante2));

        assertThat(result, is(expected));
    }

    @Test
    void sachbearbeitungAbfrageToAbfrageExistingAbfragevarianteBauleitplanverfahren() {
        var bauleitplanverfahren = new BauleitplanverfahrenInBearbeitungSachbearbeitungModel();
        bauleitplanverfahren.setVersion(99L);
        bauleitplanverfahren.setAbfragevariantenBauleitplanverfahren(List.of());

        var abfragevarianteSachbearbeitung1 =
            new AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung1.setId(UUID.randomUUID());
        abfragevarianteSachbearbeitung1.setAbfragevariantenNr(1);
        abfragevarianteSachbearbeitung1.setName("New Name Abfragevariante 1");

        var abfragevarianteSachbearbeitung2 =
            new AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung2.setId(UUID.randomUUID());
        abfragevarianteSachbearbeitung2.setAbfragevariantenNr(2);
        abfragevarianteSachbearbeitung2.setName("New Name Abfragevariante 2");

        bauleitplanverfahren.setAbfragevariantenSachbearbeitungBauleitplanverfahren(
            List.of(abfragevarianteSachbearbeitung1, abfragevarianteSachbearbeitung2)
        );

        final var savedBauleitplanverfahren = new BauleitplanverfahrenModel();
        savedBauleitplanverfahren.setAbfragevariantenBauleitplanverfahren(List.of());
        savedBauleitplanverfahren.setVersion(98L);

        final var savedAbfragevariante1 = new AbfragevarianteBauleitplanverfahrenModel();
        savedAbfragevariante1.setId(abfragevarianteSachbearbeitung1.getId());
        savedAbfragevariante1.setAbfragevariantenNr(99);
        savedAbfragevariante1.setName("Old Name Abfragevariante 1");

        final var savedAbfragevariante2 = new AbfragevarianteBauleitplanverfahrenModel();
        savedAbfragevariante2.setId(abfragevarianteSachbearbeitung2.getId());
        savedAbfragevariante2.setAbfragevariantenNr(97);
        savedAbfragevariante2.setName("Old Name Abfragevariante 2");

        savedBauleitplanverfahren.setAbfragevariantenSachbearbeitungBauleitplanverfahren(
            List.of(savedAbfragevariante1, savedAbfragevariante2)
        );

        final var result = abfrageDomainMapper.request2Model(bauleitplanverfahren, savedBauleitplanverfahren);

        final var expected = new BauleitplanverfahrenModel();
        expected.setVersion(99L);
        expected.setAbfragevariantenBauleitplanverfahren(List.of());

        final var abfragevariante1 = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevariante1.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfragevariante1.setId(abfragevarianteSachbearbeitung1.getId());
        abfragevariante1.setAbfragevariantenNr(1);
        abfragevariante1.setName("New Name Abfragevariante 1");

        final var abfragevariante2 = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevariante2.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfragevariante2.setId(abfragevarianteSachbearbeitung2.getId());
        abfragevariante2.setAbfragevariantenNr(2);
        abfragevariante2.setName("New Name Abfragevariante 2");

        expected.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of(abfragevariante1, abfragevariante2));

        assertThat(result, is(expected));
    }

    @Test
    void sachbearbeitungAbfrageToAbfrageExistingAbfragevarianteBaugenehmigungsverfahren() {
        var baugenehmigungsverfahren = new BaugenehmigungsverfahrenInBearbeitungSachbearbeitungModel();
        baugenehmigungsverfahren.setVersion(99L);
        baugenehmigungsverfahren.setAbfragevariantenBaugenehmigungsverfahren(List.of());

        var abfragevarianteSachbearbeitung1 =
            new AbfragevarianteBaugenehmigungsverfahrenInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung1.setId(UUID.randomUUID());
        abfragevarianteSachbearbeitung1.setAbfragevariantenNr(1);
        abfragevarianteSachbearbeitung1.setName("New Name Abfragevariante 1");

        var abfragevarianteSachbearbeitung2 =
            new AbfragevarianteBaugenehmigungsverfahrenInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung2.setId(UUID.randomUUID());
        abfragevarianteSachbearbeitung2.setAbfragevariantenNr(2);
        abfragevarianteSachbearbeitung2.setName("New Name Abfragevariante 2");

        baugenehmigungsverfahren.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(
            List.of(abfragevarianteSachbearbeitung1, abfragevarianteSachbearbeitung2)
        );

        final var savedBaugenehmigungsverfahren = new BaugenehmigungsverfahrenModel();
        savedBaugenehmigungsverfahren.setAbfragevariantenBaugenehmigungsverfahren(List.of());
        savedBaugenehmigungsverfahren.setVersion(98L);

        final var savedAbfragevariante1 = new AbfragevarianteBaugenehmigungsverfahrenModel();
        savedAbfragevariante1.setId(abfragevarianteSachbearbeitung1.getId());
        savedAbfragevariante1.setAbfragevariantenNr(99);
        savedAbfragevariante1.setName("Old Name Abfragevariante 1");

        final var savedAbfragevariante2 = new AbfragevarianteBaugenehmigungsverfahrenModel();
        savedAbfragevariante2.setId(abfragevarianteSachbearbeitung2.getId());
        savedAbfragevariante2.setAbfragevariantenNr(97);
        savedAbfragevariante2.setName("Old Name Abfragevariante 2");

        savedBaugenehmigungsverfahren.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(
            List.of(savedAbfragevariante1, savedAbfragevariante2)
        );

        final var result = abfrageDomainMapper.request2Model(baugenehmigungsverfahren, savedBaugenehmigungsverfahren);

        final var expected = new BaugenehmigungsverfahrenModel();
        expected.setVersion(99L);
        expected.setAbfragevariantenBaugenehmigungsverfahren(List.of());

        final var abfragevariante1 = new AbfragevarianteBaugenehmigungsverfahrenModel();
        abfragevariante1.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfragevariante1.setId(abfragevarianteSachbearbeitung1.getId());
        abfragevariante1.setAbfragevariantenNr(1);
        abfragevariante1.setName("New Name Abfragevariante 1");

        final var abfragevariante2 = new AbfragevarianteBaugenehmigungsverfahrenModel();
        abfragevariante2.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfragevariante2.setId(abfragevarianteSachbearbeitung2.getId());
        abfragevariante2.setAbfragevariantenNr(2);
        abfragevariante2.setName("New Name Abfragevariante 2");

        expected.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(
            List.of(abfragevariante1, abfragevariante2)
        );

        assertThat(result, is(expected));
    }

    @Test
    void sachbearbeitungAbfrageToAbfrageExistingAbfragevarianteWeiteresVerfahren() {
        var weiteresVerfahren = new WeiteresVerfahrenInBearbeitungSachbearbeitungModel();
        weiteresVerfahren.setVersion(99L);
        weiteresVerfahren.setAbfragevariantenWeiteresVerfahren(List.of());

        var abfragevarianteSachbearbeitung1 = new AbfragevarianteWeiteresVerfahrenInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung1.setId(UUID.randomUUID());
        abfragevarianteSachbearbeitung1.setAbfragevariantenNr(1);
        abfragevarianteSachbearbeitung1.setName("New Name Abfragevariante 1");

        var abfragevarianteSachbearbeitung2 = new AbfragevarianteWeiteresVerfahrenInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung2.setId(UUID.randomUUID());
        abfragevarianteSachbearbeitung2.setAbfragevariantenNr(2);
        abfragevarianteSachbearbeitung2.setName("New Name Abfragevariante 2");

        weiteresVerfahren.setAbfragevariantenSachbearbeitungWeiteresVerfahren(
            List.of(abfragevarianteSachbearbeitung1, abfragevarianteSachbearbeitung2)
        );

        final var savedWeiteresVerfahren = new WeiteresVerfahrenModel();
        savedWeiteresVerfahren.setAbfragevariantenWeiteresVerfahren(List.of());
        savedWeiteresVerfahren.setVersion(98L);

        final var savedAbfragevariante1 = new AbfragevarianteWeiteresVerfahrenModel();
        savedAbfragevariante1.setId(abfragevarianteSachbearbeitung1.getId());
        savedAbfragevariante1.setAbfragevariantenNr(99);
        savedAbfragevariante1.setName("Old Name Abfragevariante 1");

        final var savedAbfragevariante2 = new AbfragevarianteWeiteresVerfahrenModel();
        savedAbfragevariante2.setId(abfragevarianteSachbearbeitung2.getId());
        savedAbfragevariante2.setAbfragevariantenNr(97);
        savedAbfragevariante2.setName("Old Name Abfragevariante 2");

        savedWeiteresVerfahren.setAbfragevariantenSachbearbeitungWeiteresVerfahren(
            List.of(savedAbfragevariante1, savedAbfragevariante2)
        );

        final var result = abfrageDomainMapper.request2Model(weiteresVerfahren, savedWeiteresVerfahren);

        final var expected = new WeiteresVerfahrenModel();
        expected.setVersion(99L);
        expected.setAbfragevariantenWeiteresVerfahren(List.of());

        final var abfragevariante1 = new AbfragevarianteWeiteresVerfahrenModel();
        abfragevariante1.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
        abfragevariante1.setId(abfragevarianteSachbearbeitung1.getId());
        abfragevariante1.setAbfragevariantenNr(1);
        abfragevariante1.setName("New Name Abfragevariante 1");

        final var abfragevariante2 = new AbfragevarianteWeiteresVerfahrenModel();
        abfragevariante2.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
        abfragevariante2.setId(abfragevarianteSachbearbeitung2.getId());
        abfragevariante2.setAbfragevariantenNr(2);
        abfragevariante2.setName("New Name Abfragevariante 2");

        expected.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of(abfragevariante1, abfragevariante2));

        assertThat(result, is(expected));
    }

    @Test
    void sachbearbeitungAbfrageToAbfrageNonExistingAbfragevarianteForAbfragevarianteSachbearbeitungBauleitplanverfahren() {
        var bauleitplanverfahren = new BauleitplanverfahrenInBearbeitungSachbearbeitungModel();
        bauleitplanverfahren.setVersion(99L);
        bauleitplanverfahren.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of());

        var abfragevarianteSachbearbeitung1 =
            new AbfragevarianteBauleitplanverfahrenSachbearbeitungInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung1.setId(UUID.randomUUID());
        abfragevarianteSachbearbeitung1.setVersion(1L);
        abfragevarianteSachbearbeitung1.setAnmerkung("Test1");

        var abfragevarianteSachbearbeitung2 =
            new AbfragevarianteBauleitplanverfahrenSachbearbeitungInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung2.setId(UUID.randomUUID());
        abfragevarianteSachbearbeitung2.setVersion(2L);
        abfragevarianteSachbearbeitung2.setAnmerkung("Test2");

        bauleitplanverfahren.setAbfragevariantenBauleitplanverfahren(
            List.of(abfragevarianteSachbearbeitung1, abfragevarianteSachbearbeitung2)
        );

        final var modelFromDb = new BauleitplanverfahrenModel();
        modelFromDb.setVersion(99L);
        modelFromDb.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of());

        final var abfragevarianteModelFromDb1 = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevarianteModelFromDb1.setId(abfragevarianteSachbearbeitung1.getId());
        abfragevarianteModelFromDb1.setVersion(1L);

        final var abfragevarianteModelFromDb2 = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevarianteModelFromDb2.setId(abfragevarianteSachbearbeitung2.getId());
        abfragevarianteModelFromDb2.setVersion(2L);

        modelFromDb.setAbfragevariantenBauleitplanverfahren(
            List.of(abfragevarianteModelFromDb1, abfragevarianteModelFromDb2)
        );

        final var result = abfrageDomainMapper.request2Model(bauleitplanverfahren, modelFromDb);

        final var expected = new BauleitplanverfahrenModel();
        expected.setVersion(99L);
        expected.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of());

        final var abfragevarianteExpected1 = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevarianteExpected1.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfragevarianteExpected1.setId(abfragevarianteSachbearbeitung1.getId());
        abfragevarianteExpected1.setVersion(1L);
        abfragevarianteExpected1.setAnmerkung("Test1");

        final var abfragevarianteExpected2 = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevarianteExpected2.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfragevarianteExpected2.setId(abfragevarianteSachbearbeitung2.getId());
        abfragevarianteExpected2.setVersion(2L);
        abfragevarianteExpected2.setAnmerkung("Test2");

        expected.setAbfragevariantenBauleitplanverfahren(List.of(abfragevarianteExpected1, abfragevarianteExpected2));

        assertThat(result, is(expected));
    }

    @Test
    void sachbearbeitungAbfrageToAbfrageNonExistingAbfragevarianteForAbfragevarianteSachbearbeitungBaugenehmigungsverfahren() {
        var baugenehmigungsverfahren = new BaugenehmigungsverfahrenInBearbeitungSachbearbeitungModel();
        baugenehmigungsverfahren.setVersion(99L);
        baugenehmigungsverfahren.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(List.of());

        var abfragevarianteSachbearbeitung1 =
            new AbfragevarianteBaugenehmigungsverfahrenSachbearbeitungInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung1.setId(UUID.randomUUID());
        abfragevarianteSachbearbeitung1.setVersion(1L);
        abfragevarianteSachbearbeitung1.setAnmerkung("Test1");

        var abfragevarianteSachbearbeitung2 =
            new AbfragevarianteBaugenehmigungsverfahrenSachbearbeitungInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung2.setId(UUID.randomUUID());
        abfragevarianteSachbearbeitung2.setVersion(2L);
        abfragevarianteSachbearbeitung2.setAnmerkung("Test2");

        baugenehmigungsverfahren.setAbfragevariantenBaugenehmigungsverfahren(
            List.of(abfragevarianteSachbearbeitung1, abfragevarianteSachbearbeitung2)
        );

        final var modelFromDb = new BaugenehmigungsverfahrenModel();
        modelFromDb.setVersion(99L);
        modelFromDb.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(List.of());

        final var abfragevarianteModelFromDb1 = new AbfragevarianteBaugenehmigungsverfahrenModel();
        abfragevarianteModelFromDb1.setId(abfragevarianteSachbearbeitung1.getId());
        abfragevarianteModelFromDb1.setVersion(1L);

        final var abfragevarianteModelFromDb2 = new AbfragevarianteBaugenehmigungsverfahrenModel();
        abfragevarianteModelFromDb2.setId(abfragevarianteSachbearbeitung2.getId());
        abfragevarianteModelFromDb2.setVersion(2L);

        modelFromDb.setAbfragevariantenBaugenehmigungsverfahren(
            List.of(abfragevarianteModelFromDb1, abfragevarianteModelFromDb2)
        );

        final var result = abfrageDomainMapper.request2Model(baugenehmigungsverfahren, modelFromDb);

        final var expected = new BaugenehmigungsverfahrenModel();
        expected.setVersion(99L);
        expected.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(List.of());

        final var abfragevarianteExpected1 = new AbfragevarianteBaugenehmigungsverfahrenModel();
        abfragevarianteExpected1.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfragevarianteExpected1.setId(abfragevarianteSachbearbeitung1.getId());
        abfragevarianteExpected1.setVersion(1L);
        abfragevarianteExpected1.setAnmerkung("Test1");

        final var abfragevarianteExpected2 = new AbfragevarianteBaugenehmigungsverfahrenModel();
        abfragevarianteExpected2.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfragevarianteExpected2.setId(abfragevarianteSachbearbeitung2.getId());
        abfragevarianteExpected2.setVersion(2L);
        abfragevarianteExpected2.setAnmerkung("Test2");

        expected.setAbfragevariantenBaugenehmigungsverfahren(
            List.of(abfragevarianteExpected1, abfragevarianteExpected2)
        );

        assertThat(result, is(expected));
    }

    @Test
    void sachbearbeitungAbfrageToAbfrageNonExistingAbfragevarianteForAbfragevarianteSachbearbeitungWeiteresVerfahren() {
        var weiteresVerfahren = new WeiteresVerfahrenInBearbeitungSachbearbeitungModel();
        weiteresVerfahren.setVersion(99L);
        weiteresVerfahren.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of());

        var abfragevarianteSachbearbeitung1 =
            new AbfragevarianteWeiteresVerfahrenSachbearbeitungInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung1.setId(UUID.randomUUID());
        abfragevarianteSachbearbeitung1.setVersion(1L);
        abfragevarianteSachbearbeitung1.setAnmerkung("Test1");

        var abfragevarianteSachbearbeitung2 =
            new AbfragevarianteWeiteresVerfahrenSachbearbeitungInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung2.setId(UUID.randomUUID());
        abfragevarianteSachbearbeitung2.setVersion(2L);
        abfragevarianteSachbearbeitung2.setAnmerkung("Test2");

        weiteresVerfahren.setAbfragevariantenWeiteresVerfahren(
            List.of(abfragevarianteSachbearbeitung1, abfragevarianteSachbearbeitung2)
        );

        final var modelFromDb = new WeiteresVerfahrenModel();
        modelFromDb.setVersion(99L);
        modelFromDb.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of());

        final var abfragevarianteModelFromDb1 = new AbfragevarianteWeiteresVerfahrenModel();
        abfragevarianteModelFromDb1.setId(abfragevarianteSachbearbeitung1.getId());
        abfragevarianteModelFromDb1.setVersion(1L);

        final var abfragevarianteModelFromDb2 = new AbfragevarianteWeiteresVerfahrenModel();
        abfragevarianteModelFromDb2.setId(abfragevarianteSachbearbeitung2.getId());
        abfragevarianteModelFromDb2.setVersion(2L);

        modelFromDb.setAbfragevariantenWeiteresVerfahren(
            List.of(abfragevarianteModelFromDb1, abfragevarianteModelFromDb2)
        );

        final var result = abfrageDomainMapper.request2Model(weiteresVerfahren, modelFromDb);

        final var expected = new WeiteresVerfahrenModel();
        expected.setVersion(99L);
        expected.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of());

        final var abfragevarianteExpected1 = new AbfragevarianteWeiteresVerfahrenModel();
        abfragevarianteExpected1.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
        abfragevarianteExpected1.setId(abfragevarianteSachbearbeitung1.getId());
        abfragevarianteExpected1.setVersion(1L);
        abfragevarianteExpected1.setAnmerkung("Test1");

        final var abfragevarianteExpected2 = new AbfragevarianteWeiteresVerfahrenModel();
        abfragevarianteExpected2.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
        abfragevarianteExpected2.setId(abfragevarianteSachbearbeitung2.getId());
        abfragevarianteExpected2.setVersion(2L);
        abfragevarianteExpected2.setAnmerkung("Test2");

        expected.setAbfragevariantenWeiteresVerfahren(List.of(abfragevarianteExpected1, abfragevarianteExpected2));

        assertThat(result, is(expected));
    }
}
