package de.muenchen.isi.domain.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.model.AbfragevarianteModel;
import de.muenchen.isi.domain.model.AbfragevarianteSachbearbeitungModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfrageAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfragevarianteAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.InfrastrukturabfrageAngelegtModel;
import de.muenchen.isi.domain.model.abfrageSachbearbeitungInBearbeitungSachbearbeitung.AbfragevarianteInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageSachbearbeitungInBearbeitungSachbearbeitung.AbfragevarianteSachbearbeitungInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageSachbearbeitungInBearbeitungSachbearbeitung.InfrastrukturabfrageInBearbeitungSachbearbeitungModel;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration(
    classes = {
        AbfrageDomainMapperImpl.class,
        AbfragevarianteDomainMapperImpl.class,
        BauabschnittDomainMapperImpl.class,
        DokumentDomainMapperImpl.class,
    }
)
public class AbfrageDomainMapperTest {

    @Autowired
    AbfrageDomainMapper abfrageDomainMapper;

    @Autowired
    AbfragevarianteDomainMapper abfragevarianteDomainMapper;

    @Autowired
    BauabschnittDomainMapper bauabschnittDomainMapper;

    @Autowired
    DokumentDomainMapper dokumentDomainMapper;

    @Test
    public void abfrageErstellungInfrastrukturabfrageToInfrastrukturabfrageNoExistingAbfragevariante() {
        InfrastrukturabfrageAngelegtModel infrastrukturabfrageAngelegtModel = new InfrastrukturabfrageAngelegtModel();
        infrastrukturabfrageAngelegtModel.setVersion(1L);
        AbfrageAngelegtModel abfrageAngelegtModel = new AbfrageAngelegtModel();
        abfrageAngelegtModel.setNameAbfrage("Abfrage");
        infrastrukturabfrageAngelegtModel.setAbfrage(abfrageAngelegtModel);

        AbfragevarianteAngelegtModel abfragevarianteAngelegtModel = new AbfragevarianteAngelegtModel();
        abfragevarianteAngelegtModel.setAbfragevariantenName("Abfragevariante 1");

        AbfragevarianteAngelegtModel abfragevarianteAngelegtModel2 = new AbfragevarianteAngelegtModel();
        abfragevarianteAngelegtModel2.setAbfragevariantenName("Abfragevariante 2");

        List<AbfragevarianteAngelegtModel> abfragevarianten = new ArrayList<>();

        abfragevarianten.add(abfragevarianteAngelegtModel);
        abfragevarianten.add(abfragevarianteAngelegtModel2);
        infrastrukturabfrageAngelegtModel.setAbfragevarianten(abfragevarianten);

        var result = abfrageDomainMapper.request2Model(
            infrastrukturabfrageAngelegtModel,
            new InfrastrukturabfrageModel()
        );

        assertThat(result.getVersion(), is(infrastrukturabfrageAngelegtModel.getVersion()));
        assertThat(
            result.getAbfrage().getNameAbfrage(),
            is(infrastrukturabfrageAngelegtModel.getAbfrage().getNameAbfrage())
        );
        assertThat(
            result.getAbfragevarianten().get(0).getAbfragevariantenName(),
            is(infrastrukturabfrageAngelegtModel.getAbfragevarianten().get(0).getAbfragevariantenName())
        );
        assertThat(
            result.getAbfragevarianten().get(1).getAbfragevariantenName(),
            is(infrastrukturabfrageAngelegtModel.getAbfragevarianten().get(1).getAbfragevariantenName())
        );
    }

    @Test
    public void abfrageErstellungInfrastrukturabfrageToInfrastrukturabfrageExistingAbfragevariante() {
        var abfragevarianteId = UUID.randomUUID();
        InfrastrukturabfrageAngelegtModel infrastrukturabfrageAngelegtModel = new InfrastrukturabfrageAngelegtModel();
        infrastrukturabfrageAngelegtModel.setVersion(1L);

        AbfrageAngelegtModel abfrageAngelegtModel = new AbfrageAngelegtModel();
        abfrageAngelegtModel.setNameAbfrage("Abfrage");
        infrastrukturabfrageAngelegtModel.setAbfrage(abfrageAngelegtModel);

        AbfragevarianteAngelegtModel abfragevarianteAngelegtModel = new AbfragevarianteAngelegtModel();
        abfragevarianteAngelegtModel.setAbfragevariantenName("Abfragevariante 1");

        AbfragevarianteAngelegtModel abfragevarianteAngelegtModel2 = new AbfragevarianteAngelegtModel();
        abfragevarianteAngelegtModel2.setAbfragevariantenName("Abfragevariante 2");
        abfragevarianteAngelegtModel2.setId(abfragevarianteId);

        List<AbfragevarianteAngelegtModel> abfragevarianteAngelegtModels = new ArrayList<>();

        abfragevarianteAngelegtModels.add(abfragevarianteAngelegtModel);
        abfragevarianteAngelegtModels.add(abfragevarianteAngelegtModel2);

        InfrastrukturabfrageModel infrastrukturabfrageModel = new InfrastrukturabfrageModel();

        AbfragevarianteModel abfragevarianteModel = new AbfragevarianteModel();
        abfragevarianteModel.setAbfragevariantenName("Abfragevariante 3");
        abfragevarianteModel.setId(abfragevarianteId);

        List<AbfragevarianteModel> abfragevariantenModelList = new ArrayList<>();
        abfragevariantenModelList.add(abfragevarianteModel);

        infrastrukturabfrageModel.setAbfragevarianten(abfragevariantenModelList);

        infrastrukturabfrageAngelegtModel.setAbfragevarianten(abfragevarianteAngelegtModels);

        var result = abfrageDomainMapper.request2Model(infrastrukturabfrageAngelegtModel, infrastrukturabfrageModel);

        assertThat(result.getVersion(), is(infrastrukturabfrageAngelegtModel.getVersion()));
        assertThat(
            result.getAbfrage().getNameAbfrage(),
            is(infrastrukturabfrageAngelegtModel.getAbfrage().getNameAbfrage())
        );
        assertThat(
            result.getAbfragevarianten().get(0).getAbfragevariantenName(),
            is(infrastrukturabfrageAngelegtModel.getAbfragevarianten().get(0).getAbfragevariantenName())
        );
        assertThat(
            result.getAbfragevarianten().get(1).getAbfragevariantenName(),
            is(infrastrukturabfrageAngelegtModel.getAbfragevarianten().get(1).getAbfragevariantenName())
        );
    }

    @Test
    void sachbearbeitungInfrastrukturabfrageToInfrastrukturabfrageNonExistingAbfragevariante() {
        var infrastrukturabfrage = new InfrastrukturabfrageInBearbeitungSachbearbeitungModel();
        infrastrukturabfrage.setVersion(99L);
        infrastrukturabfrage.setAbfragevarianten(List.of());

        var abfragevarianteSachbearbeitung1 = new AbfragevarianteInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung1.setAbfragevariantenNr(1);
        abfragevarianteSachbearbeitung1.setAbfragevariantenName("Abfragevariante 1");

        var abfragevarianteSachbearbeitung2 = new AbfragevarianteInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung2.setAbfragevariantenNr(2);
        abfragevarianteSachbearbeitung2.setAbfragevariantenName("Abfragevariante 2");

        infrastrukturabfrage.setAbfragevariantenSachbearbeitung(
            List.of(abfragevarianteSachbearbeitung1, abfragevarianteSachbearbeitung2)
        );

        final var result = abfrageDomainMapper.request2Model(infrastrukturabfrage, new InfrastrukturabfrageModel());

        final var expected = new InfrastrukturabfrageModel();
        expected.setVersion(99L);
        expected.setAbfragevarianten(List.of());

        final var abfragevariante1 = new AbfragevarianteModel();
        abfragevariante1.setAbfragevariantenNr(1);
        abfragevariante1.setAbfragevariantenName("Abfragevariante 1");

        final var abfragevariante2 = new AbfragevarianteModel();
        abfragevariante2.setAbfragevariantenNr(2);
        abfragevariante2.setAbfragevariantenName("Abfragevariante 2");

        expected.setAbfragevariantenSachbearbeitung(List.of(abfragevariante1, abfragevariante2));

        assertThat(result, is(expected));
    }

    @Test
    void sachbearbeitungInfrastrukturabfrageToInfrastrukturabfrageExistingAbfragevariante() {
        var infrastrukturabfrage = new InfrastrukturabfrageInBearbeitungSachbearbeitungModel();
        infrastrukturabfrage.setVersion(99L);
        infrastrukturabfrage.setAbfragevarianten(List.of());

        var abfragevarianteSachbearbeitung1 = new AbfragevarianteInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung1.setId(UUID.randomUUID());
        abfragevarianteSachbearbeitung1.setAbfragevariantenNr(1);
        abfragevarianteSachbearbeitung1.setAbfragevariantenName("New Name Abfragevariante 1");

        var abfragevarianteSachbearbeitung2 = new AbfragevarianteInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung2.setId(UUID.randomUUID());
        abfragevarianteSachbearbeitung2.setAbfragevariantenNr(2);
        abfragevarianteSachbearbeitung2.setAbfragevariantenName("New Name Abfragevariante 2");

        infrastrukturabfrage.setAbfragevariantenSachbearbeitung(
            List.of(abfragevarianteSachbearbeitung1, abfragevarianteSachbearbeitung2)
        );

        final var savedInfrastrukturabfrage = new InfrastrukturabfrageModel();
        savedInfrastrukturabfrage.setAbfragevarianten(List.of());
        savedInfrastrukturabfrage.setVersion(98L);

        final var savedAbfragevariante1 = new AbfragevarianteModel();
        savedAbfragevariante1.setId(abfragevarianteSachbearbeitung1.getId());
        savedAbfragevariante1.setAbfragevariantenNr(99);
        savedAbfragevariante1.setAbfragevariantenName("Old Name Abfragevariante 1");

        final var savedAbfragevariante2 = new AbfragevarianteModel();
        savedAbfragevariante2.setId(abfragevarianteSachbearbeitung2.getId());
        savedAbfragevariante2.setAbfragevariantenNr(97);
        savedAbfragevariante2.setAbfragevariantenName("Old Name Abfragevariante 2");

        savedInfrastrukturabfrage.setAbfragevariantenSachbearbeitung(
            List.of(savedAbfragevariante1, savedAbfragevariante2)
        );

        final var result = abfrageDomainMapper.request2Model(infrastrukturabfrage, savedInfrastrukturabfrage);

        final var expected = new InfrastrukturabfrageModel();
        expected.setVersion(99L);
        expected.setAbfragevarianten(List.of());

        final var abfragevariante1 = new AbfragevarianteModel();
        abfragevariante1.setId(abfragevarianteSachbearbeitung1.getId());
        abfragevariante1.setAbfragevariantenNr(1);
        abfragevariante1.setAbfragevariantenName("New Name Abfragevariante 1");

        final var abfragevariante2 = new AbfragevarianteModel();
        abfragevariante2.setId(abfragevarianteSachbearbeitung2.getId());
        abfragevariante2.setAbfragevariantenNr(2);
        abfragevariante2.setAbfragevariantenName("New Name Abfragevariante 2");

        expected.setAbfragevariantenSachbearbeitung(List.of(abfragevariante1, abfragevariante2));

        assertThat(result, is(expected));
    }

    @Test
    void sachbearbeitungInfrastrukturabfrageToInfrastrukturabfrageNonExistingAbfragevarianteForAbfragevarianteSachbearbeitung() {
        var infrastrukturabfrage = new InfrastrukturabfrageInBearbeitungSachbearbeitungModel();
        infrastrukturabfrage.setVersion(99L);
        infrastrukturabfrage.setAbfragevariantenSachbearbeitung(List.of());

        var abfragevarianteSachbearbeitung1 = new AbfragevarianteSachbearbeitungInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung1.setId(UUID.randomUUID());
        abfragevarianteSachbearbeitung1.setVersion(1L);
        var abfragevarianteSachbearbeitungEmbedded1 = new AbfragevarianteSachbearbeitungModel();
        abfragevarianteSachbearbeitungEmbedded1.setAnmerkung("Test1");
        abfragevarianteSachbearbeitung1.setAbfragevarianteSachbearbeitung(abfragevarianteSachbearbeitungEmbedded1);

        var abfragevarianteSachbearbeitung2 = new AbfragevarianteSachbearbeitungInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung2.setId(UUID.randomUUID());
        abfragevarianteSachbearbeitung2.setVersion(2L);
        var abfragevarianteSachbearbeitungEmbedded2 = new AbfragevarianteSachbearbeitungModel();
        abfragevarianteSachbearbeitungEmbedded2.setAnmerkung("Test2");
        abfragevarianteSachbearbeitung2.setAbfragevarianteSachbearbeitung(abfragevarianteSachbearbeitungEmbedded2);

        infrastrukturabfrage.setAbfragevarianten(
            List.of(abfragevarianteSachbearbeitung1, abfragevarianteSachbearbeitung2)
        );

        final var modelFromDb = new InfrastrukturabfrageModel();
        modelFromDb.setVersion(99L);
        modelFromDb.setAbfragevariantenSachbearbeitung(List.of());

        final var abfragevarianteModelFromDb1 = new AbfragevarianteModel();
        abfragevarianteModelFromDb1.setId(abfragevarianteSachbearbeitung1.getId());
        abfragevarianteModelFromDb1.setVersion(1L);

        final var abfragevarianteModelFromDb2 = new AbfragevarianteModel();
        abfragevarianteModelFromDb2.setId(abfragevarianteSachbearbeitung2.getId());
        abfragevarianteModelFromDb2.setVersion(2L);

        modelFromDb.setAbfragevarianten(List.of(abfragevarianteModelFromDb1, abfragevarianteModelFromDb2));

        final var result = abfrageDomainMapper.request2Model(infrastrukturabfrage, modelFromDb);

        final var expected = new InfrastrukturabfrageModel();
        expected.setVersion(99L);
        expected.setAbfragevariantenSachbearbeitung(List.of());

        final var abfragevarianteExpected1 = new AbfragevarianteModel();
        abfragevarianteExpected1.setId(abfragevarianteSachbearbeitung1.getId());
        abfragevarianteExpected1.setVersion(1L);
        var abfragevarianteEmbeddedExpected1 = new AbfragevarianteSachbearbeitungModel();
        abfragevarianteEmbeddedExpected1.setAnmerkung("Test1");
        abfragevarianteExpected1.setAbfragevarianteSachbearbeitung(abfragevarianteEmbeddedExpected1);

        final var abfragevarianteExpected2 = new AbfragevarianteModel();
        abfragevarianteExpected2.setId(abfragevarianteSachbearbeitung2.getId());
        abfragevarianteExpected2.setVersion(2L);
        var abfragevarianteEmbeddedExpected2 = new AbfragevarianteSachbearbeitungModel();
        abfragevarianteEmbeddedExpected2.setAnmerkung("Test2");
        abfragevarianteExpected2.setAbfragevarianteSachbearbeitung(abfragevarianteEmbeddedExpected2);

        expected.setAbfragevarianten(List.of(abfragevarianteExpected1, abfragevarianteExpected2));

        assertThat(result, is(expected));
    }
}
