package de.muenchen.isi.domain.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.model.AbfragevarianteBauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfragevarianteBauleitplanverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.BauleitplanverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.AbfragevarianteBauleitplanverfahrenSachbearbeitungInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.BauleitplanverfahrenInBearbeitungSachbearbeitungModel;
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
    public void abfrageErstellungAbfrageToAbfrageNoExistingAbfragevariante() {
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
        bauleitplanverfahrenModel.setAbfragevarianten(abfragevarianten);

        var result = abfrageDomainMapper.request2Model(bauleitplanverfahrenModel, new BauleitplanverfahrenModel());

        assertThat(result.getVersion(), is(bauleitplanverfahrenModel.getVersion()));
        assertThat(result.getName(), is(bauleitplanverfahrenModel.getName()));
        assertThat(
            result.getAbfragevarianten().get(0).getName(),
            is(bauleitplanverfahrenModel.getAbfragevarianten().get(0).getName())
        );
        assertThat(
            result.getAbfragevarianten().get(1).getName(),
            is(bauleitplanverfahrenModel.getAbfragevarianten().get(1).getName())
        );
    }

    @Test
    public void abfrageErstellungAbfrageToAbfrageExistingAbfragevariante() {
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

        bauleitplanverfahrenModel.setAbfragevarianten(abfragevariantenModelList);

        bauleitplanverfahrenAngelegtModel.setAbfragevarianten(abfragevarianteAngelegtModels);

        var result = abfrageDomainMapper.request2Model(bauleitplanverfahrenAngelegtModel, bauleitplanverfahrenModel);

        assertThat(result.getVersion(), is(bauleitplanverfahrenAngelegtModel.getVersion()));
        assertThat(result.getName(), is(bauleitplanverfahrenAngelegtModel.getName()));
        assertThat(
            result.getAbfragevarianten().get(0).getName(),
            is(bauleitplanverfahrenAngelegtModel.getAbfragevarianten().get(0).getName())
        );
        assertThat(
            result.getAbfragevarianten().get(1).getName(),
            is(bauleitplanverfahrenAngelegtModel.getAbfragevarianten().get(1).getName())
        );
    }

    @Test
    void sachbearbeitungAbfrageToAbfrageNonExistingAbfragevariante() {
        var bauleitplanverfahren = new BauleitplanverfahrenInBearbeitungSachbearbeitungModel();
        bauleitplanverfahren.setVersion(99L);
        bauleitplanverfahren.setAbfragevarianten(List.of());

        var abfragevarianteSachbearbeitung1 =
            new AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung1.setAbfragevariantenNr(1);
        abfragevarianteSachbearbeitung1.setName("Abfragevariante 1");

        var abfragevarianteSachbearbeitung2 =
            new AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung2.setAbfragevariantenNr(2);
        abfragevarianteSachbearbeitung2.setName("Abfragevariante 2");

        bauleitplanverfahren.setAbfragevariantenSachbearbeitung(
            List.of(abfragevarianteSachbearbeitung1, abfragevarianteSachbearbeitung2)
        );

        final var result = abfrageDomainMapper.request2Model(bauleitplanverfahren, new BauleitplanverfahrenModel());

        final var expected = new BauleitplanverfahrenModel();
        expected.setVersion(99L);

        final var abfragevariante1 = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevariante1.setAbfragevariantenNr(1);
        abfragevariante1.setName("Abfragevariante 1");

        final var abfragevariante2 = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevariante2.setAbfragevariantenNr(2);
        abfragevariante2.setName("Abfragevariante 2");

        expected.setAbfragevarianten(List.of());
        expected.setAbfragevariantenSachbearbeitung(List.of(abfragevariante1, abfragevariante2));

        assertThat(result, is(expected));
    }

    @Test
    void sachbearbeitungAbfrageToAbfrageExistingAbfragevariante() {
        var bauleitplanverfahren = new BauleitplanverfahrenInBearbeitungSachbearbeitungModel();
        bauleitplanverfahren.setVersion(99L);
        bauleitplanverfahren.setAbfragevarianten(List.of());

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

        bauleitplanverfahren.setAbfragevariantenSachbearbeitung(
            List.of(abfragevarianteSachbearbeitung1, abfragevarianteSachbearbeitung2)
        );

        final var savedBauleitplanverfahren = new BauleitplanverfahrenModel();
        savedBauleitplanverfahren.setAbfragevarianten(List.of());
        savedBauleitplanverfahren.setVersion(98L);

        final var savedAbfragevariante1 = new AbfragevarianteBauleitplanverfahrenModel();
        savedAbfragevariante1.setId(abfragevarianteSachbearbeitung1.getId());
        savedAbfragevariante1.setAbfragevariantenNr(99);
        savedAbfragevariante1.setName("Old Name Abfragevariante 1");

        final var savedAbfragevariante2 = new AbfragevarianteBauleitplanverfahrenModel();
        savedAbfragevariante2.setId(abfragevarianteSachbearbeitung2.getId());
        savedAbfragevariante2.setAbfragevariantenNr(97);
        savedAbfragevariante2.setName("Old Name Abfragevariante 2");

        savedBauleitplanverfahren.setAbfragevariantenSachbearbeitung(
            List.of(savedAbfragevariante1, savedAbfragevariante2)
        );

        final var result = abfrageDomainMapper.request2Model(bauleitplanverfahren, savedBauleitplanverfahren);

        final var expected = new BauleitplanverfahrenModel();
        expected.setVersion(99L);
        expected.setAbfragevarianten(List.of());

        final var abfragevariante1 = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevariante1.setId(abfragevarianteSachbearbeitung1.getId());
        abfragevariante1.setAbfragevariantenNr(1);
        abfragevariante1.setName("New Name Abfragevariante 1");

        final var abfragevariante2 = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevariante2.setId(abfragevarianteSachbearbeitung2.getId());
        abfragevariante2.setAbfragevariantenNr(2);
        abfragevariante2.setName("New Name Abfragevariante 2");

        expected.setAbfragevariantenSachbearbeitung(List.of(abfragevariante1, abfragevariante2));

        assertThat(result, is(expected));
    }

    @Test
    void sachbearbeitungAbfrageToAbfrageNonExistingAbfragevarianteForAbfragevarianteSachbearbeitung() {
        var bauleitplanverfahren = new BauleitplanverfahrenInBearbeitungSachbearbeitungModel();
        bauleitplanverfahren.setVersion(99L);
        bauleitplanverfahren.setAbfragevariantenSachbearbeitung(List.of());

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

        bauleitplanverfahren.setAbfragevarianten(
            List.of(abfragevarianteSachbearbeitung1, abfragevarianteSachbearbeitung2)
        );

        final var modelFromDb = new BauleitplanverfahrenModel();
        modelFromDb.setVersion(99L);
        modelFromDb.setAbfragevariantenSachbearbeitung(List.of());

        final var abfragevarianteModelFromDb1 = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevarianteModelFromDb1.setId(abfragevarianteSachbearbeitung1.getId());
        abfragevarianteModelFromDb1.setVersion(1L);

        final var abfragevarianteModelFromDb2 = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevarianteModelFromDb2.setId(abfragevarianteSachbearbeitung2.getId());
        abfragevarianteModelFromDb2.setVersion(2L);

        modelFromDb.setAbfragevarianten(List.of(abfragevarianteModelFromDb1, abfragevarianteModelFromDb2));

        final var result = abfrageDomainMapper.request2Model(bauleitplanverfahren, modelFromDb);

        final var expected = new BauleitplanverfahrenModel();
        expected.setVersion(99L);
        expected.setAbfragevariantenSachbearbeitung(List.of());

        final var abfragevarianteExpected1 = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevarianteExpected1.setId(abfragevarianteSachbearbeitung1.getId());
        abfragevarianteExpected1.setVersion(1L);
        abfragevarianteExpected1.setAnmerkung("Test1");

        final var abfragevarianteExpected2 = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevarianteExpected2.setId(abfragevarianteSachbearbeitung2.getId());
        abfragevarianteExpected2.setVersion(2L);
        abfragevarianteExpected2.setAnmerkung("Test2");

        expected.setAbfragevarianten(List.of(abfragevarianteExpected1, abfragevarianteExpected2));

        assertThat(result, is(expected));
    }
}
