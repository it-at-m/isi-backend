package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.mapper.AbfrageDomainMapper;
import de.muenchen.isi.domain.mapper.AbfrageDomainMapperImpl;
import de.muenchen.isi.domain.mapper.AbfragevarianteDomainMapperImpl;
import de.muenchen.isi.domain.mapper.BauabschnittDomainMapperImpl;
import de.muenchen.isi.domain.mapper.DokumentDomainMapperImpl;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.domain.model.enums.AbfrageTyp;
import de.muenchen.isi.domain.model.list.AbfrageListElementModel;
import de.muenchen.isi.domain.model.list.AbfrageListElementsModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Planungsrecht;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVorhaben;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.rest.TestData;
import java.math.BigDecimal;
import java.time.LocalDate;
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
class AbfrageListServiceTest {

    private final AbfrageDomainMapper abfrageDomainMapper = new AbfrageDomainMapperImpl(
        new AbfragevarianteDomainMapperImpl(new BauabschnittDomainMapperImpl()),
        new DokumentDomainMapperImpl()
    );

    @Mock
    private AbfrageService abfrageService;

    private AbfrageListService abfrageListService;

    @BeforeEach
    public void beforeEach() {
        this.abfrageListService = new AbfrageListService(this.abfrageService, this.abfrageDomainMapper);

        Mockito.reset(this.abfrageService);
    }

    @Test
    void getAbfrageListElements() {
        final InfrastrukturabfrageModel model1 = new InfrastrukturabfrageModel();
        model1.setId(UUID.randomUUID());
        model1.setAbfrage(new AbfrageModel());
        model1.getAbfrage().setNameAbfrage("NameAbfrage1");
        model1.getAbfrage().setVerortung(TestData.createVerortung());
        model1.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        model1.getAbfrage().setFristStellungnahme(LocalDate.of(2022, 11, 1));

        final InfrastrukturabfrageModel model2 = new InfrastrukturabfrageModel();
        model2.setId(UUID.randomUUID());
        model2.setAbfrage(new AbfrageModel());
        model2.getAbfrage().setNameAbfrage("NameAbfrage2");
        model2.getAbfrage().setVerortung(TestData.createVerortung());
        model2.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        model2.getAbfrage().setFristStellungnahme(LocalDate.of(2022, 9, 1));

        final InfrastrukturabfrageModel model3 = new InfrastrukturabfrageModel();
        model3.setId(UUID.randomUUID());
        model3.setAbfrage(new AbfrageModel());
        model3.getAbfrage().setNameAbfrage("NameAbfrage2");
        model3.getAbfrage().setVerortung(TestData.createVerortung());
        model3.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        model3.getAbfrage().setFristStellungnahme(LocalDate.of(2022, 12, 1));

        final List<InfrastrukturabfrageModel> listInfrastrukturabfrage = List.of(model1, model2, model3);

        final List<AbfrageListElementModel> expectedFristStellungnahmeDesc = new ArrayList<>();

        var abfrageListElementModel = new AbfrageListElementModel();
        abfrageListElementModel.setId(model3.getId());
        abfrageListElementModel.setNameAbfrage(model3.getAbfrage().getNameAbfrage());
        abfrageListElementModel.setStadtbezirke(model3.getAbfrage().getVerortung().getStadtbezirke());
        abfrageListElementModel.setStatusAbfrage(model3.getAbfrage().getStatusAbfrage());
        abfrageListElementModel.setFristStellungnahme(model3.getAbfrage().getFristStellungnahme());
        abfrageListElementModel.setType(AbfrageTyp.INFRASTRUKTURABFRAGE);
        expectedFristStellungnahmeDesc.add(abfrageListElementModel);

        abfrageListElementModel = new AbfrageListElementModel();
        abfrageListElementModel.setId(model1.getId());
        abfrageListElementModel.setNameAbfrage(model1.getAbfrage().getNameAbfrage());
        abfrageListElementModel.setStadtbezirke(model1.getAbfrage().getVerortung().getStadtbezirke());
        abfrageListElementModel.setStatusAbfrage(model1.getAbfrage().getStatusAbfrage());
        abfrageListElementModel.setFristStellungnahme(model1.getAbfrage().getFristStellungnahme());
        abfrageListElementModel.setType(AbfrageTyp.INFRASTRUKTURABFRAGE);
        expectedFristStellungnahmeDesc.add(abfrageListElementModel);

        abfrageListElementModel = new AbfrageListElementModel();
        abfrageListElementModel.setId(model2.getId());
        abfrageListElementModel.setNameAbfrage(model2.getAbfrage().getNameAbfrage());
        abfrageListElementModel.setStadtbezirke(model2.getAbfrage().getVerortung().getStadtbezirke());
        abfrageListElementModel.setStatusAbfrage(model2.getAbfrage().getStatusAbfrage());
        abfrageListElementModel.setFristStellungnahme(model2.getAbfrage().getFristStellungnahme());
        abfrageListElementModel.setType(AbfrageTyp.INFRASTRUKTURABFRAGE);
        expectedFristStellungnahmeDesc.add(abfrageListElementModel);

        final var expected = new AbfrageListElementsModel();
        expected.setListElements(expectedFristStellungnahmeDesc);

        Mockito.when(this.abfrageService.getInfrastrukturabfragen()).thenReturn(listInfrastrukturabfrage);

        assertThat(expected, is(this.abfrageListService.getAbfrageListElements()));

        Mockito.verify(this.abfrageService, Mockito.times(1)).getInfrastrukturabfragen();
    }

    @Test
    void getReferencedListElements() {
        BauvorhabenModel bauvorhabenModel = new BauvorhabenModel();
        bauvorhabenModel.setBauvorhabenNummer("12345");
        bauvorhabenModel.setEigentuemer("Eigentuemer");
        bauvorhabenModel.setGrundstuecksgroesse(BigDecimal.valueOf(1));
        bauvorhabenModel.setNameVorhaben("Name");
        bauvorhabenModel.setPlanungsrecht(Planungsrecht.BPLAN_PARAG_11);
        bauvorhabenModel.setSobonRelevant(UncertainBoolean.FALSE);
        bauvorhabenModel.setStandVorhaben(StandVorhaben.AUFSTELLUNGSBESCHLUSS);
        bauvorhabenModel.setId(UUID.randomUUID());

        final InfrastrukturabfrageModel model1 = new InfrastrukturabfrageModel();
        model1.setId(UUID.randomUUID());
        model1.setAbfrage(new AbfrageModel());
        model1.getAbfrage().setNameAbfrage("NameAbfrage1");
        model1.getAbfrage().setVerortung(TestData.createVerortung());
        model1.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        model1.getAbfrage().setFristStellungnahme(LocalDate.of(2022, 11, 1));
        model1.getAbfrage().setBauvorhaben(bauvorhabenModel);

        final InfrastrukturabfrageModel model2 = new InfrastrukturabfrageModel();
        model2.setId(UUID.randomUUID());
        model2.setAbfrage(new AbfrageModel());
        model2.getAbfrage().setNameAbfrage("NameAbfrage2");
        model2.getAbfrage().setVerortung(TestData.createVerortung());
        model2.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        model2.getAbfrage().setFristStellungnahme(LocalDate.of(2022, 9, 1));
        model2.getAbfrage().setBauvorhaben(bauvorhabenModel);

        final InfrastrukturabfrageModel model3 = new InfrastrukturabfrageModel();
        model3.setId(UUID.randomUUID());
        model3.setAbfrage(new AbfrageModel());
        model3.getAbfrage().setNameAbfrage("NameAbfrage3");
        model3.getAbfrage().setVerortung(TestData.createVerortung());
        model3.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        model3.getAbfrage().setFristStellungnahme(LocalDate.of(2022, 12, 1));
        model3.getAbfrage().setBauvorhaben(bauvorhabenModel);

        final List<InfrastrukturabfrageModel> listInfrastrukturabfrage = List.of(model1, model2, model3);

        final List<AbfrageListElementModel> expectedCreatedDateTimeAsc = new ArrayList<>();

        var abfrageListElementModel1 = new AbfrageListElementModel();
        abfrageListElementModel1.setId(model3.getId());
        abfrageListElementModel1.setNameAbfrage(model3.getAbfrage().getNameAbfrage());
        abfrageListElementModel1.setStadtbezirke(model3.getAbfrage().getVerortung().getStadtbezirke());
        abfrageListElementModel1.setStatusAbfrage(model3.getAbfrage().getStatusAbfrage());
        abfrageListElementModel1.setFristStellungnahme(model3.getAbfrage().getFristStellungnahme());
        abfrageListElementModel1.setType(AbfrageTyp.INFRASTRUKTURABFRAGE);
        expectedCreatedDateTimeAsc.add(abfrageListElementModel1);

        var abfrageListElementModel2 = new AbfrageListElementModel();
        abfrageListElementModel2.setId(model1.getId());
        abfrageListElementModel2.setNameAbfrage(model1.getAbfrage().getNameAbfrage());
        abfrageListElementModel2.setStadtbezirke(model1.getAbfrage().getVerortung().getStadtbezirke());
        abfrageListElementModel2.setStatusAbfrage(model1.getAbfrage().getStatusAbfrage());
        abfrageListElementModel2.setFristStellungnahme(model1.getAbfrage().getFristStellungnahme());
        abfrageListElementModel2.setType(AbfrageTyp.INFRASTRUKTURABFRAGE);
        expectedCreatedDateTimeAsc.add(abfrageListElementModel2);

        var abfrageListElementModel3 = new AbfrageListElementModel();
        abfrageListElementModel3.setId(model2.getId());
        abfrageListElementModel3.setNameAbfrage(model2.getAbfrage().getNameAbfrage());
        abfrageListElementModel3.setStadtbezirke(model2.getAbfrage().getVerortung().getStadtbezirke());
        abfrageListElementModel3.setStatusAbfrage(model2.getAbfrage().getStatusAbfrage());
        abfrageListElementModel3.setFristStellungnahme(model2.getAbfrage().getFristStellungnahme());
        abfrageListElementModel3.setType(AbfrageTyp.INFRASTRUKTURABFRAGE);
        expectedCreatedDateTimeAsc.add(abfrageListElementModel3);

        final var expected = new AbfrageListElementsModel();
        expected.setListElements(expectedCreatedDateTimeAsc);

        Mockito
            .when(this.abfrageService.getAllReferencedAbfragenForBauvorhaben(bauvorhabenModel.getId()))
            .thenReturn(listInfrastrukturabfrage);

        assertThat(
            expected,
            is(this.abfrageListService.getAbfrageListElementsThatReferenceBauvorhaben(bauvorhabenModel.getId()))
        );

        Mockito
            .verify(this.abfrageService, Mockito.times(1))
            .getAllReferencedAbfragenForBauvorhaben(bauvorhabenModel.getId());
    }
}
