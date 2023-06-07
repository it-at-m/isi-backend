package de.muenchen.isi.domain.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.model.AbfragevarianteModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfrageerstellungAbfrageAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfrageerstellungAbfragevarianteAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfrageerstellungInfrastrukturabfrageAngelegtModel;
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
    public void abfrageErstellunInfrastrukturabfrageToInfrastrukturabfrageNoExistingAbfragevariante() {
        AbfrageerstellungInfrastrukturabfrageAngelegtModel infrastrukturabfrageAngelegtModel =
            new AbfrageerstellungInfrastrukturabfrageAngelegtModel();
        infrastrukturabfrageAngelegtModel.setVersion(1L);
        AbfrageerstellungAbfrageAngelegtModel abfrageerstellungAbfrageAngelegtModel =
            new AbfrageerstellungAbfrageAngelegtModel();
        abfrageerstellungAbfrageAngelegtModel.setNameAbfrage("Abfrage");
        infrastrukturabfrageAngelegtModel.setAbfrage(abfrageerstellungAbfrageAngelegtModel);

        AbfrageerstellungAbfragevarianteAngelegtModel abfragevarianteAngelegtModel =
            new AbfrageerstellungAbfragevarianteAngelegtModel();
        abfragevarianteAngelegtModel.setAbfragevariantenName("Abfragevariante 1");

        AbfrageerstellungAbfragevarianteAngelegtModel abfragevarianteAngelegtModel2 =
            new AbfrageerstellungAbfragevarianteAngelegtModel();
        abfragevarianteAngelegtModel2.setAbfragevariantenName("Abfragevariante 2");

        List<AbfrageerstellungAbfragevarianteAngelegtModel> abfragevarianten = new ArrayList<>();

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
    public void abfrageErstellunInfrastrukturabfrageToInfrastrukturabfrageExistingAbfragevariante() {
        var abfragevarianteId = UUID.randomUUID();
        AbfrageerstellungInfrastrukturabfrageAngelegtModel infrastrukturabfrageAngelegtModel =
            new AbfrageerstellungInfrastrukturabfrageAngelegtModel();
        infrastrukturabfrageAngelegtModel.setVersion(1L);

        AbfrageerstellungAbfrageAngelegtModel abfrageerstellungAbfrageAngelegtModel =
            new AbfrageerstellungAbfrageAngelegtModel();
        abfrageerstellungAbfrageAngelegtModel.setNameAbfrage("Abfrage");
        infrastrukturabfrageAngelegtModel.setAbfrage(abfrageerstellungAbfrageAngelegtModel);

        AbfrageerstellungAbfragevarianteAngelegtModel abfragevarianteAngelegtModel =
            new AbfrageerstellungAbfragevarianteAngelegtModel();
        abfragevarianteAngelegtModel.setAbfragevariantenName("Abfragevariante 1");

        AbfrageerstellungAbfragevarianteAngelegtModel abfragevarianteAngelegtModel2 =
            new AbfrageerstellungAbfragevarianteAngelegtModel();
        abfragevarianteAngelegtModel2.setAbfragevariantenName("Abfragevariante 2");
        abfragevarianteAngelegtModel2.setId(abfragevarianteId);

        List<AbfrageerstellungAbfragevarianteAngelegtModel> abfragevarianteAngelegtModels = new ArrayList<>();

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
}
