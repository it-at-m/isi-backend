package de.muenchen.isi.domain.service.infrastruktureinrichtung;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.mapper.InfrastruktureinrichtungListElementDomainMapper;
import de.muenchen.isi.domain.mapper.InfrastruktureinrichtungListElementDomainMapperImpl;
import de.muenchen.isi.domain.model.enums.InfrastruktureinrichtungTyp;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.GrundschuleModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.GsNachmittagBetreuungModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.HausFuerKinderModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.KindergartenModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.KinderkrippeModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.MittelschuleModel;
import de.muenchen.isi.domain.model.list.InfrastruktureinrichtungListElementModel;
import de.muenchen.isi.domain.model.list.InfrastruktureinrichtungListElementsModel;
import java.util.ArrayList;
import java.util.Comparator;
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
class InfrastruktureinrichungListServiceTest {

    @Mock
    private InfrastruktureinrichtungListService infrastruktureinrichtungListService;

    @Mock
    private KinderkrippeService kinderkrippeService;

    @Mock
    private KindergartenService kindergartenService;

    @Mock
    private HausFuerKinderService hausFuerKinderService;

    @Mock
    private GsNachmittagBetreuungService gsNachmittagBetreuungService;

    @Mock
    private GrundschuleService grundschuleService;

    @Mock
    private MittelschuleService mittelschuleService;

    private final InfrastruktureinrichtungListElementDomainMapper infrastruktureinrichtungListElementDomainMapper =
        new InfrastruktureinrichtungListElementDomainMapperImpl();

    @BeforeEach
    public void beforeEach() {
        this.infrastruktureinrichtungListService =
            new InfrastruktureinrichtungListService(
                this.kinderkrippeService,
                this.kindergartenService,
                this.hausFuerKinderService,
                this.gsNachmittagBetreuungService,
                this.grundschuleService,
                this.mittelschuleService,
                this.infrastruktureinrichtungListElementDomainMapper
            );
        Mockito.reset(
            this.kinderkrippeService,
            this.kindergartenService,
            this.hausFuerKinderService,
            this.gsNachmittagBetreuungService,
            this.grundschuleService,
            this.mittelschuleService
        );
    }

    @Test
    void getInfrastruktureinrichtungListElements() {
        final List<InfrastruktureinrichtungListElementModel> expectedNameEinrichtung = new ArrayList<>();

        final KinderkrippeModel model1 = new KinderkrippeModel();
        model1.setId(UUID.randomUUID());
        model1.setInfrastruktureinrichtung(new InfrastruktureinrichtungModel());
        model1.getInfrastruktureinrichtung().setNameEinrichtung("Name der Kinderkrippe");
        var infrastruktureinrichtungListElementModel = new InfrastruktureinrichtungListElementModel();
        infrastruktureinrichtungListElementModel.setId(model1.getId());
        infrastruktureinrichtungListElementModel.setNameEinrichtung(
            model1.getInfrastruktureinrichtung().getNameEinrichtung()
        );
        infrastruktureinrichtungListElementModel.setInfrastruktureinrichtungTyp(
            InfrastruktureinrichtungTyp.KINDERKRIPPE
        );
        expectedNameEinrichtung.add(infrastruktureinrichtungListElementModel);

        final KindergartenModel model2 = new KindergartenModel();
        model2.setId(UUID.randomUUID());
        model2.setInfrastruktureinrichtung(new InfrastruktureinrichtungModel());
        model2.getInfrastruktureinrichtung().setNameEinrichtung("Name des Kindergartens 1");
        infrastruktureinrichtungListElementModel = new InfrastruktureinrichtungListElementModel();
        infrastruktureinrichtungListElementModel.setId(model2.getId());
        infrastruktureinrichtungListElementModel.setNameEinrichtung(
            model2.getInfrastruktureinrichtung().getNameEinrichtung()
        );
        infrastruktureinrichtungListElementModel.setInfrastruktureinrichtungTyp(
            InfrastruktureinrichtungTyp.KINDERGARTEN
        );
        expectedNameEinrichtung.add(infrastruktureinrichtungListElementModel);

        final KindergartenModel model3 = new KindergartenModel();
        model3.setId(UUID.randomUUID());
        model3.setInfrastruktureinrichtung(new InfrastruktureinrichtungModel());
        model3.getInfrastruktureinrichtung().setNameEinrichtung("Name des Kindergartens 2");
        infrastruktureinrichtungListElementModel = new InfrastruktureinrichtungListElementModel();
        infrastruktureinrichtungListElementModel.setId(model3.getId());
        infrastruktureinrichtungListElementModel.setNameEinrichtung(
            model3.getInfrastruktureinrichtung().getNameEinrichtung()
        );
        infrastruktureinrichtungListElementModel.setInfrastruktureinrichtungTyp(
            InfrastruktureinrichtungTyp.KINDERGARTEN
        );
        expectedNameEinrichtung.add(infrastruktureinrichtungListElementModel);

        final HausFuerKinderModel model4 = new HausFuerKinderModel();
        model4.setId(UUID.randomUUID());
        model4.setInfrastruktureinrichtung(new InfrastruktureinrichtungModel());
        model4.getInfrastruktureinrichtung().setNameEinrichtung("Name des Hauses der Kinder");
        infrastruktureinrichtungListElementModel = new InfrastruktureinrichtungListElementModel();
        infrastruktureinrichtungListElementModel.setId(model4.getId());
        infrastruktureinrichtungListElementModel.setNameEinrichtung(
            model4.getInfrastruktureinrichtung().getNameEinrichtung()
        );
        infrastruktureinrichtungListElementModel.setInfrastruktureinrichtungTyp(
            InfrastruktureinrichtungTyp.HAUS_FUER_KINDER
        );
        expectedNameEinrichtung.add(infrastruktureinrichtungListElementModel);

        final GsNachmittagBetreuungModel model5 = new GsNachmittagBetreuungModel();
        model5.setId(UUID.randomUUID());
        model5.setInfrastruktureinrichtung(new InfrastruktureinrichtungModel());
        model5.getInfrastruktureinrichtung().setNameEinrichtung("Name der Nachmittagsbetreuung für Grundschulkinder");
        infrastruktureinrichtungListElementModel = new InfrastruktureinrichtungListElementModel();
        infrastruktureinrichtungListElementModel.setId(model5.getId());
        infrastruktureinrichtungListElementModel.setNameEinrichtung(
            model5.getInfrastruktureinrichtung().getNameEinrichtung()
        );
        infrastruktureinrichtungListElementModel.setInfrastruktureinrichtungTyp(
            InfrastruktureinrichtungTyp.GS_NACHMITTAG_BETREUUNG
        );
        expectedNameEinrichtung.add(infrastruktureinrichtungListElementModel);

        final GrundschuleModel model6 = new GrundschuleModel();
        model6.setId(UUID.randomUUID());
        model6.setInfrastruktureinrichtung(new InfrastruktureinrichtungModel());
        model6.getInfrastruktureinrichtung().setNameEinrichtung("Name der Grundschule");
        infrastruktureinrichtungListElementModel = new InfrastruktureinrichtungListElementModel();
        infrastruktureinrichtungListElementModel.setId(model6.getId());
        infrastruktureinrichtungListElementModel.setNameEinrichtung(
            model6.getInfrastruktureinrichtung().getNameEinrichtung()
        );
        infrastruktureinrichtungListElementModel.setInfrastruktureinrichtungTyp(
            InfrastruktureinrichtungTyp.GRUNDSCHULE
        );
        expectedNameEinrichtung.add(infrastruktureinrichtungListElementModel);

        final MittelschuleModel model7 = new MittelschuleModel();
        model7.setId(UUID.randomUUID());
        model7.setInfrastruktureinrichtung(new InfrastruktureinrichtungModel());
        model7.getInfrastruktureinrichtung().setNameEinrichtung("Name der Mittelschule");
        infrastruktureinrichtungListElementModel = new InfrastruktureinrichtungListElementModel();
        infrastruktureinrichtungListElementModel.setId(model7.getId());
        infrastruktureinrichtungListElementModel.setNameEinrichtung(
            model7.getInfrastruktureinrichtung().getNameEinrichtung()
        );
        infrastruktureinrichtungListElementModel.setInfrastruktureinrichtungTyp(
            InfrastruktureinrichtungTyp.MITTELSCHULE
        );
        expectedNameEinrichtung.add(infrastruktureinrichtungListElementModel);

        Mockito.when(this.kinderkrippeService.getKinderkrippen()).thenReturn(List.of(model1));
        Mockito.when(this.kindergartenService.getKindergaerten()).thenReturn(List.of(model2, model3));
        Mockito.when(this.hausFuerKinderService.getHaeuserFuerKinder()).thenReturn(List.of(model4));
        Mockito.when(this.gsNachmittagBetreuungService.getGsNachmittagBetreuungen()).thenReturn(List.of(model5));
        Mockito.when(this.grundschuleService.getGrundschulen()).thenReturn(List.of(model6));
        Mockito.when(this.mittelschuleService.getMittelschulen()).thenReturn(List.of(model7));

        expectedNameEinrichtung.sort(
            Comparator.comparing(InfrastruktureinrichtungListElementModel::getNameEinrichtung)
        );
        final var expected = new InfrastruktureinrichtungListElementsModel();
        expected.setListElements(expectedNameEinrichtung);

        assertThat(expected, is(this.infrastruktureinrichtungListService.getInfrastruktureinrichtungListElements()));

        Mockito.verify(this.kinderkrippeService, Mockito.times(1)).getKinderkrippen();
        Mockito.verify(this.kindergartenService, Mockito.times(1)).getKindergaerten();
        Mockito.verify(this.hausFuerKinderService, Mockito.times(1)).getHaeuserFuerKinder();
        Mockito.verify(this.gsNachmittagBetreuungService, Mockito.times(1)).getGsNachmittagBetreuungen();
        Mockito.verify(this.grundschuleService, Mockito.times(1)).getGrundschulen();
        Mockito.verify(this.mittelschuleService, Mockito.times(1)).getMittelschulen();
    }
}
