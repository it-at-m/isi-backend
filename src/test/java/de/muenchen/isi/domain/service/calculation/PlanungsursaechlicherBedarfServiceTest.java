package de.muenchen.isi.domain.service.calculation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.TestData;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.mapper.StammdatenDomainMapperImpl;
import de.muenchen.isi.domain.model.calculation.PlanungsursachlicheWohneinheitenModel;
import de.muenchen.isi.domain.model.calculation.PlanungsursaechlicherBedarfModel;
import de.muenchen.isi.domain.model.stammdaten.SobonOrientierungswertSozialeInfrastrukturModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.entity.stammdaten.SobonOrientierungswertSozialeInfrastruktur;
import de.muenchen.isi.infrastructure.entity.stammdaten.VersorgungsquoteGruppenstaerke;
import de.muenchen.isi.infrastructure.repository.stammdaten.SobonOrientierungswertSozialeInfrastrukturRepository;
import de.muenchen.isi.infrastructure.repository.stammdaten.VersorgungsquoteGruppenstaerkeRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
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
class PlanungsursaechlicherBedarfServiceTest {

    @Mock
    private SobonOrientierungswertSozialeInfrastrukturRepository sobonOrientierungswertSozialeInfrastrukturRepository;

    @Mock
    private VersorgungsquoteGruppenstaerkeRepository versorgungsquoteGruppenstaerkeRepository;

    private PlanungsursaechlicherBedarfService planungsursaechlicherBedarfService;

    @BeforeEach
    public void beforeEach() {
        this.planungsursaechlicherBedarfService =
            new PlanungsursaechlicherBedarfService(
                sobonOrientierungswertSozialeInfrastrukturRepository,
                versorgungsquoteGruppenstaerkeRepository,
                new StammdatenDomainMapperImpl()
            );
        Mockito.reset(sobonOrientierungswertSozialeInfrastrukturRepository, versorgungsquoteGruppenstaerkeRepository);
    }

    @Test
    void calculatePlanungsursaechlicherBedarf() throws EntityNotFoundException {
        final InfrastruktureinrichtungTyp einrichtung = InfrastruktureinrichtungTyp.KINDERKRIPPE;
        final SobonOrientierungswertJahr sobonJahr = SobonOrientierungswertJahr.JAHR_2017;
        final LocalDate gueltigAb = LocalDate.of(1998, 1, 1);
        final var wohneinheiten = new ArrayList<PlanungsursachlicheWohneinheitenModel>();
        var wohneinheitenModel = new PlanungsursachlicheWohneinheitenModel();
        wohneinheitenModel.setFoerderart("foerderart1");
        wohneinheitenModel.setJahr("2000");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(10000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new PlanungsursachlicheWohneinheitenModel();
        wohneinheitenModel.setFoerderart("foerderart1");
        wohneinheitenModel.setJahr("2001");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(20000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new PlanungsursachlicheWohneinheitenModel();
        wohneinheitenModel.setFoerderart("foerderart1");
        wohneinheitenModel.setJahr("2002");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(30000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new PlanungsursachlicheWohneinheitenModel();
        wohneinheitenModel.setFoerderart("foerderart2");
        wohneinheitenModel.setJahr("2000");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(40000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new PlanungsursachlicheWohneinheitenModel();
        wohneinheitenModel.setFoerderart("foerderart2");
        wohneinheitenModel.setJahr("2001");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(50000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new PlanungsursachlicheWohneinheitenModel();
        wohneinheitenModel.setFoerderart("foerderart3");
        wohneinheitenModel.setJahr("2000");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(60000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new PlanungsursachlicheWohneinheitenModel();
        wohneinheitenModel.setFoerderart("foerderart1");
        wohneinheitenModel.setJahr("Gesamt 10 Jare");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(60000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new PlanungsursachlicheWohneinheitenModel();
        wohneinheitenModel.setFoerderart("foerderart2");
        wohneinheitenModel.setJahr("Gesamt 10 Jahre");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(90000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new PlanungsursachlicheWohneinheitenModel();
        wohneinheitenModel.setFoerderart("foerderart3");
        wohneinheitenModel.setJahr("Gesamt 10 Jare");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(60000));
        wohneinheiten.add(wohneinheitenModel);

        var sobonOrientierungswertFoerderart1 = TestData.createSobonOrientierungswertSozialeInfrastrukturEntity();
        sobonOrientierungswertFoerderart1.setFoerderartBezeichnung("foerderart1");
        Mockito
            .when(
                this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                        InfrastruktureinrichtungTyp.KINDERKRIPPE,
                        "foerderart1",
                        SobonOrientierungswertJahr.JAHR_2017.getGueltigAb()
                    )
            )
            .thenReturn(Optional.of(sobonOrientierungswertFoerderart1));
        var sobonOrientierungswertFoerderart2 = TestData.createSobonOrientierungswertSozialeInfrastrukturEntity();
        sobonOrientierungswertFoerderart2.setFoerderartBezeichnung("foerderart2");
        Mockito
            .when(
                this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                        InfrastruktureinrichtungTyp.KINDERKRIPPE,
                        "foerderart2",
                        SobonOrientierungswertJahr.JAHR_2017.getGueltigAb()
                    )
            )
            .thenReturn(Optional.of(sobonOrientierungswertFoerderart2));
        var sobonOrientierungswertFoerderart3 = TestData.createSobonOrientierungswertSozialeInfrastrukturEntity();
        sobonOrientierungswertFoerderart3.setFoerderartBezeichnung("foerderart3");
        Mockito
            .when(
                this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                        InfrastruktureinrichtungTyp.KINDERKRIPPE,
                        "foerderart3",
                        SobonOrientierungswertJahr.JAHR_2017.getGueltigAb()
                    )
            )
            .thenReturn(Optional.of(sobonOrientierungswertFoerderart3));

        final var versorgungsQuote = new VersorgungsquoteGruppenstaerke();
        versorgungsQuote.setVersorgungsquotePlanungsursaechlich(BigDecimal.valueOf(60, 2));
        versorgungsQuote.setGruppenstaerke(12);
        Mockito
            .when(
                this.versorgungsquoteGruppenstaerkeRepository.findFirstByInfrastruktureinrichtungTypAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                        InfrastruktureinrichtungTyp.KINDERKRIPPE,
                        gueltigAb
                    )
            )
            .thenReturn(Optional.of(versorgungsQuote));

        final var result = planungsursaechlicherBedarfService.calculatePlanungsursaechlicherBedarf(
            einrichtung,
            wohneinheiten,
            sobonJahr,
            gueltigAb
        );

        final var expected = new ArrayList<PlanungsursaechlicherBedarfModel>();
        var planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2000));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(528990000, 4));
        planungsursaechlicherBedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(31739400000L, 6));
        planungsursaechlicherBedarf.setAnzahlGruppen(BigDecimal.valueOf(264495, 2));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2001));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(858030000, 4));
        planungsursaechlicherBedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(51481800000L, 6));
        planungsursaechlicherBedarf.setAnzahlGruppen(BigDecimal.valueOf(429015, 2));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2002));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(989990000, 4));
        planungsursaechlicherBedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(59399400000L, 6));
        planungsursaechlicherBedarf.setAnzahlGruppen(BigDecimal.valueOf(494995, 2));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2003));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(975570000, 4));
        planungsursaechlicherBedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(58534200000L, 6));
        planungsursaechlicherBedarf.setAnzahlGruppen(BigDecimal.valueOf(487785, 2));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2004));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(961110000, 4));
        planungsursaechlicherBedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(57666600000L, 6));
        planungsursaechlicherBedarf.setAnzahlGruppen(BigDecimal.valueOf(480555, 2));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2005));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(946730000, 4));
        planungsursaechlicherBedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(56803800000L, 6));
        planungsursaechlicherBedarf.setAnzahlGruppen(BigDecimal.valueOf(473365, 2));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2006));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(932310000, 4));
        planungsursaechlicherBedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(55938600000L, 6));
        planungsursaechlicherBedarf.setAnzahlGruppen(BigDecimal.valueOf(466155, 2));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2007));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(917850000, 4));
        planungsursaechlicherBedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(55071000000L, 6));
        planungsursaechlicherBedarf.setAnzahlGruppen(BigDecimal.valueOf(458925, 2));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2008));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(903360000, 4));
        planungsursaechlicherBedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(54201600000L, 6));
        planungsursaechlicherBedarf.setAnzahlGruppen(BigDecimal.valueOf(451680, 2));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2009));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(888980000, 4));
        planungsursaechlicherBedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(53338800000L, 6));
        planungsursaechlicherBedarf.setAnzahlGruppen(BigDecimal.valueOf(444490, 2));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2010));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(877530000, 4));
        planungsursaechlicherBedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(52651800000L, 6));
        planungsursaechlicherBedarf.setAnzahlGruppen(BigDecimal.valueOf(438765, 2));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2011));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(868040000, 4));
        planungsursaechlicherBedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(52082400000L, 6));
        planungsursaechlicherBedarf.setAnzahlGruppen(BigDecimal.valueOf(434020, 2));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2012));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(859400000, 4));
        planungsursaechlicherBedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(51564000000L, 6));
        planungsursaechlicherBedarf.setAnzahlGruppen(BigDecimal.valueOf(429700, 2));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2013));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(850790000, 4));
        planungsursaechlicherBedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(51047400000L, 6));
        planungsursaechlicherBedarf.setAnzahlGruppen(BigDecimal.valueOf(425395, 2));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2014));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(842290000, 4));
        planungsursaechlicherBedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(50537400000L, 6));
        planungsursaechlicherBedarf.setAnzahlGruppen(BigDecimal.valueOf(421145, 2));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2015));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(833860000, 4));
        planungsursaechlicherBedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(50031600000L, 6));
        planungsursaechlicherBedarf.setAnzahlGruppen(BigDecimal.valueOf(416930, 2));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2016));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(825460000, 4));
        planungsursaechlicherBedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(49527600000L, 6));
        planungsursaechlicherBedarf.setAnzahlGruppen(BigDecimal.valueOf(412730, 2));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2017));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(817170000, 4));
        planungsursaechlicherBedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(49030200000L, 6));
        planungsursaechlicherBedarf.setAnzahlGruppen(BigDecimal.valueOf(408585, 2));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2018));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(809060000, 4));
        planungsursaechlicherBedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(48543600000L, 6));
        planungsursaechlicherBedarf.setAnzahlGruppen(BigDecimal.valueOf(404530, 2));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2019));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(800940000, 4));
        planungsursaechlicherBedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(48056400000L, 6));
        planungsursaechlicherBedarf.setAnzahlGruppen(BigDecimal.valueOf(400470, 2));
        expected.add(planungsursaechlicherBedarf);

        assertThat(result.collect(Collectors.toList()), is(expected));
    }

    @Test
    void setVersorgungsquoteAndGruppenstaerkeInPlanungsursaechlichenBedarf() {
        final var planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(100));
        final var versorgungsQuote = new VersorgungsquoteGruppenstaerke();
        versorgungsQuote.setVersorgungsquotePlanungsursaechlich(BigDecimal.valueOf(60, 2));
        versorgungsQuote.setGruppenstaerke(10);
        var result =
            planungsursaechlicherBedarfService.setVersorgungsquoteAndGruppenstaerkeInPlanungsursaechlichenBedarf(
                planungsursaechlicherBedarf,
                versorgungsQuote
            );
        var expected = new PlanungsursaechlicherBedarfModel();
        expected.setAnzahlKinderGesamt(BigDecimal.valueOf(100));
        expected.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(6000, 2));
        expected.setAnzahlGruppen(BigDecimal.valueOf(600, 2));
        assertThat(result, is(expected));

        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(73));
        versorgungsQuote.setVersorgungsquotePlanungsursaechlich(BigDecimal.valueOf(58, 2));
        versorgungsQuote.setGruppenstaerke(10);
        result =
            planungsursaechlicherBedarfService.setVersorgungsquoteAndGruppenstaerkeInPlanungsursaechlichenBedarf(
                planungsursaechlicherBedarf,
                versorgungsQuote
            );
        expected = new PlanungsursaechlicherBedarfModel();
        expected.setAnzahlKinderGesamt(BigDecimal.valueOf(73));
        expected.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(4234, 2));
        expected.setAnzahlGruppen(BigDecimal.valueOf(423, 2));
        assertThat(result, is(expected));

        planungsursaechlicherBedarf.setAnzahlKinderGesamt(null);
        versorgungsQuote.setVersorgungsquotePlanungsursaechlich(BigDecimal.valueOf(58, 2));
        versorgungsQuote.setGruppenstaerke(10);
        Assertions.assertThrows(
            NullPointerException.class,
            () ->
                planungsursaechlicherBedarfService.setVersorgungsquoteAndGruppenstaerkeInPlanungsursaechlichenBedarf(
                    planungsursaechlicherBedarf,
                    versorgungsQuote
                )
        );

        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(73));
        versorgungsQuote.setVersorgungsquotePlanungsursaechlich(null);
        versorgungsQuote.setGruppenstaerke(10);
        Assertions.assertThrows(
            NullPointerException.class,
            () ->
                planungsursaechlicherBedarfService.setVersorgungsquoteAndGruppenstaerkeInPlanungsursaechlichenBedarf(
                    planungsursaechlicherBedarf,
                    versorgungsQuote
                )
        );
    }

    @Test
    void roundValuesAndReturnModelWithRoundedValues() {
        final var model = new PlanungsursaechlicherBedarfModel();
        model.setJahr("2000");
        model.setAnzahlKinderGesamt(BigDecimal.valueOf(46, 1));
        model.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(44, 1));
        model.setAnzahlGruppen(BigDecimal.valueOf(44460, 4));

        final var result = planungsursaechlicherBedarfService.roundValuesAndReturnModelWithRoundedValues(model);

        final var expected = new PlanungsursaechlicherBedarfModel();
        expected.setJahr("2000");
        expected.setAnzahlKinderGesamt(BigDecimal.valueOf(5));
        expected.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(4));
        expected.setAnzahlGruppen(BigDecimal.valueOf(445, 2));

        assertThat(result, is(expected));
    }

    @Test
    void add() {
        final var model1 = new PlanungsursaechlicherBedarfModel();
        model1.setJahr("2000");
        model1.setAnzahlKinderGesamt(BigDecimal.valueOf(10));
        final var model2 = new PlanungsursaechlicherBedarfModel();
        model2.setJahr("2001");
        model2.setAnzahlKinderGesamt(BigDecimal.valueOf(22));
        var result = planungsursaechlicherBedarfService.add(model1, model2);
        var expected = new PlanungsursaechlicherBedarfModel();
        expected.setJahr("2000");
        expected.setAnzahlKinderGesamt(BigDecimal.valueOf(32));
        assertThat(result, is(expected));

        model1.setJahr("2000");
        model1.setAnzahlKinderGesamt(null);
        model2.setJahr("2001");
        model2.setAnzahlKinderGesamt(BigDecimal.valueOf(22));
        result = planungsursaechlicherBedarfService.add(model1, model2);
        expected = new PlanungsursaechlicherBedarfModel();
        expected.setJahr("2000");
        expected.setAnzahlKinderGesamt(BigDecimal.valueOf(22));
        assertThat(result, is(expected));

        model1.setJahr("2000");
        model1.setAnzahlKinderGesamt(BigDecimal.valueOf(10));
        model2.setJahr("2001");
        model2.setAnzahlKinderGesamt(null);
        result = planungsursaechlicherBedarfService.add(model1, model2);
        expected = new PlanungsursaechlicherBedarfModel();
        expected.setJahr("2000");
        expected.setAnzahlKinderGesamt(BigDecimal.valueOf(10));
        assertThat(result, is(expected));

        model1.setJahr("2000");
        model1.setAnzahlKinderGesamt(null);
        model2.setJahr("2001");
        model2.setAnzahlKinderGesamt(null);
        result = planungsursaechlicherBedarfService.add(model1, model2);
        expected = new PlanungsursaechlicherBedarfModel();
        expected.setJahr("2000");
        expected.setAnzahlKinderGesamt(BigDecimal.valueOf(0));
        assertThat(result, is(expected));

        model1.setJahr("2000");
        model1.setAnzahlKinderGesamt(null);
        model2.setJahr(null);
        model2.setAnzahlKinderGesamt(null);
        result = planungsursaechlicherBedarfService.add(model1, model2);
        expected = new PlanungsursaechlicherBedarfModel();
        expected.setJahr("2000");
        expected.setAnzahlKinderGesamt(BigDecimal.valueOf(0));
        assertThat(result, is(expected));

        model1.setJahr(null);
        model1.setAnzahlKinderGesamt(null);
        model2.setJahr("2001");
        model2.setAnzahlKinderGesamt(null);
        result = planungsursaechlicherBedarfService.add(model1, model2);
        expected = new PlanungsursaechlicherBedarfModel();
        expected.setJahr("2001");
        expected.setAnzahlKinderGesamt(BigDecimal.valueOf(0));
        assertThat(result, is(expected));

        model1.setJahr(null);
        model1.setAnzahlKinderGesamt(null);
        model2.setJahr(null);
        model2.setAnzahlKinderGesamt(null);
        result = planungsursaechlicherBedarfService.add(model1, model2);
        expected = new PlanungsursaechlicherBedarfModel();
        expected.setJahr(null);
        expected.setAnzahlKinderGesamt(BigDecimal.valueOf(0));
        assertThat(result, is(expected));
    }

    @Test
    void getSobonOrientierungswertForFoerderart() {
        final var wohneinheiten = new ArrayList<PlanungsursachlicheWohneinheitenModel>();
        var planungsursachlicheWohneinheiten = new PlanungsursachlicheWohneinheitenModel();
        planungsursachlicheWohneinheiten.setFoerderart("foerderart1");
        wohneinheiten.add(planungsursachlicheWohneinheiten);
        planungsursachlicheWohneinheiten = new PlanungsursachlicheWohneinheitenModel();
        planungsursachlicheWohneinheiten.setFoerderart("foerderart2");
        wohneinheiten.add(planungsursachlicheWohneinheiten);
        planungsursachlicheWohneinheiten = new PlanungsursachlicheWohneinheitenModel();
        planungsursachlicheWohneinheiten.setFoerderart("foerderart1");
        wohneinheiten.add(planungsursachlicheWohneinheiten);
        planungsursachlicheWohneinheiten = new PlanungsursachlicheWohneinheitenModel();
        planungsursachlicheWohneinheiten.setFoerderart("foerderart1");
        wohneinheiten.add(planungsursachlicheWohneinheiten);
        planungsursachlicheWohneinheiten = new PlanungsursachlicheWohneinheitenModel();
        planungsursachlicheWohneinheiten.setFoerderart("foerderart3");
        wohneinheiten.add(planungsursachlicheWohneinheiten);
        planungsursachlicheWohneinheiten = new PlanungsursachlicheWohneinheitenModel();
        planungsursachlicheWohneinheiten.setFoerderart("foerderart4");
        wohneinheiten.add(planungsursachlicheWohneinheiten);

        var sobonOrientierungswertFoerderart1 = new SobonOrientierungswertSozialeInfrastruktur();
        sobonOrientierungswertFoerderart1.setFoerderartBezeichnung("foerderart1");
        Mockito
            .when(
                this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                        InfrastruktureinrichtungTyp.KINDERKRIPPE,
                        "foerderart1",
                        SobonOrientierungswertJahr.JAHR_2017.getGueltigAb()
                    )
            )
            .thenReturn(Optional.of(sobonOrientierungswertFoerderart1));

        var sobonOrientierungswertFoerderart2 = new SobonOrientierungswertSozialeInfrastruktur();
        sobonOrientierungswertFoerderart2.setFoerderartBezeichnung("foerderart2");
        Mockito
            .when(
                this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                        InfrastruktureinrichtungTyp.KINDERKRIPPE,
                        "foerderart2",
                        SobonOrientierungswertJahr.JAHR_2017.getGueltigAb()
                    )
            )
            .thenReturn(Optional.of(sobonOrientierungswertFoerderart2));

        var sobonOrientierungswertFoerderart3 = new SobonOrientierungswertSozialeInfrastruktur();
        sobonOrientierungswertFoerderart3.setFoerderartBezeichnung("foerderart3");
        Mockito
            .when(
                this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                        InfrastruktureinrichtungTyp.KINDERKRIPPE,
                        "foerderart3",
                        SobonOrientierungswertJahr.JAHR_2017.getGueltigAb()
                    )
            )
            .thenReturn(Optional.of(sobonOrientierungswertFoerderart3));

        var sobonOrientierungswertFoerderart4 = new SobonOrientierungswertSozialeInfrastruktur();
        sobonOrientierungswertFoerderart4.setFoerderartBezeichnung("foerderart4");
        Mockito
            .when(
                this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                        InfrastruktureinrichtungTyp.KINDERKRIPPE,
                        "foerderart4",
                        SobonOrientierungswertJahr.JAHR_2017.getGueltigAb()
                    )
            )
            .thenReturn(Optional.of(sobonOrientierungswertFoerderart4));

        final var result = planungsursaechlicherBedarfService.getSobonOrientierungswertForFoerderart(
            wohneinheiten,
            SobonOrientierungswertJahr.JAHR_2017,
            InfrastruktureinrichtungTyp.KINDERKRIPPE
        );

        final var expected = new HashMap<String, SobonOrientierungswertSozialeInfrastrukturModel>();
        var sobonOrientierungswertModel = new SobonOrientierungswertSozialeInfrastrukturModel();
        sobonOrientierungswertModel.setFoerderartBezeichnung("foerderart1");
        expected.put("foerderart1", sobonOrientierungswertModel);
        sobonOrientierungswertModel = new SobonOrientierungswertSozialeInfrastrukturModel();
        sobonOrientierungswertModel.setFoerderartBezeichnung("foerderart2");
        expected.put("foerderart2", sobonOrientierungswertModel);
        sobonOrientierungswertModel = new SobonOrientierungswertSozialeInfrastrukturModel();
        sobonOrientierungswertModel.setFoerderartBezeichnung("foerderart3");
        expected.put("foerderart3", sobonOrientierungswertModel);
        sobonOrientierungswertModel = new SobonOrientierungswertSozialeInfrastrukturModel();
        sobonOrientierungswertModel.setFoerderartBezeichnung("foerderart4");
        expected.put("foerderart4", sobonOrientierungswertModel);

        assertThat(result, is(expected));

        Mockito
            .verify(this.sobonOrientierungswertSozialeInfrastrukturRepository, Mockito.times(1))
            .findFirstByEinrichtungstypAndFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                InfrastruktureinrichtungTyp.KINDERKRIPPE,
                "foerderart1",
                SobonOrientierungswertJahr.JAHR_2017.getGueltigAb()
            );
        Mockito
            .verify(this.sobonOrientierungswertSozialeInfrastrukturRepository, Mockito.times(1))
            .findFirstByEinrichtungstypAndFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                InfrastruktureinrichtungTyp.KINDERKRIPPE,
                "foerderart2",
                SobonOrientierungswertJahr.JAHR_2017.getGueltigAb()
            );
        Mockito
            .verify(this.sobonOrientierungswertSozialeInfrastrukturRepository, Mockito.times(1))
            .findFirstByEinrichtungstypAndFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                InfrastruktureinrichtungTyp.KINDERKRIPPE,
                "foerderart3",
                SobonOrientierungswertJahr.JAHR_2017.getGueltigAb()
            );
        Mockito
            .verify(this.sobonOrientierungswertSozialeInfrastrukturRepository, Mockito.times(1))
            .findFirstByEinrichtungstypAndFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                InfrastruktureinrichtungTyp.KINDERKRIPPE,
                "foerderart4",
                SobonOrientierungswertJahr.JAHR_2017.getGueltigAb()
            );
        Mockito
            .verify(this.sobonOrientierungswertSozialeInfrastrukturRepository, Mockito.times(4))
            .findFirstByEinrichtungstypAndFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                Mockito.any(InfrastruktureinrichtungTyp.class),
                Mockito.anyString(),
                Mockito.any(LocalDate.class)
            );
    }

    @Test
    void calculatePlanungsursaechlicheBedarfeStartAtYear20() {
        final var planungsursachlicheWohneinheiten = createListOfPlanungsursachlicheWohneinheiten(2019, 1);
        final var sobonOrientierungswertSozialeInfrastruktur =
            TestData.createSobonOrientierungswertSozialeInfrastrukturModel();

        final var result = planungsursaechlicherBedarfService.calculatePlanungsursaechlicheBedarfe(
            2000,
            planungsursachlicheWohneinheiten.get(0),
            sobonOrientierungswertSozialeInfrastruktur
        );

        final var expected = new ArrayList<PlanungsursaechlicherBedarfModel>();
        var planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2019));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(48090000, 4));
        expected.add(planungsursaechlicherBedarf);

        assertThat(result.collect(Collectors.toList()), is(expected));
    }

    @Test
    void calculatePlanungsursaechlicheBedarfeStartAtYear9() {
        final var planungsursachlicheWohneinheiten = createListOfPlanungsursachlicheWohneinheiten(2008, 1);
        final var sobonOrientierungswertSozialeInfrastruktur =
            TestData.createSobonOrientierungswertSozialeInfrastrukturModel();

        final var result = planungsursaechlicherBedarfService.calculatePlanungsursaechlicheBedarfe(
            2000,
            planungsursachlicheWohneinheiten.get(0),
            sobonOrientierungswertSozialeInfrastruktur
        );

        final var expected = new ArrayList<PlanungsursaechlicherBedarfModel>();
        var planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2008));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(48090000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2009));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(47400000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2010));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(46720000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2011));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(46030000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2012));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(45340000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2013));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(44660000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2014));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(43970000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2015));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(43280000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2016));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(42590000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2017));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(41910000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2018));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(41490000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2019));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(41080000, 4));
        expected.add(planungsursaechlicherBedarf);

        assertThat(result.collect(Collectors.toList()), is(expected));
    }

    @Test
    void calculatePlanungsursaechlicheBedarfeStartAtYear1() {
        final var planungsursachlicheWohneinheiten = createListOfPlanungsursachlicheWohneinheiten(2000, 1);
        final var sobonOrientierungswertSozialeInfrastruktur =
            TestData.createSobonOrientierungswertSozialeInfrastrukturModel();

        final var result = planungsursaechlicherBedarfService.calculatePlanungsursaechlicheBedarfe(
            2000,
            planungsursachlicheWohneinheiten.get(0),
            sobonOrientierungswertSozialeInfrastruktur
        );

        final var expected = new ArrayList<PlanungsursaechlicherBedarfModel>();
        var planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2000));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(48090000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2001));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(47400000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2002));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(46720000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2003));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(46030000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2004));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(45340000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2005));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(44660000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2006));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(43970000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2007));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(43280000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2008));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(42590000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2009));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(41910000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2010));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(41490000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2011));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(41080000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2012));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(40670000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2013));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(40260000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2014));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(39860000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2015));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(39460000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2016));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(39060000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2017));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(38670000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2018));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(38290000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(2019));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(37900000, 4));
        expected.add(planungsursaechlicherBedarf);

        assertThat(result.collect(Collectors.toList()), is(expected));
    }

    @Test
    void createPlanungsursaechlicherBedarf() {
        final var result = planungsursaechlicherBedarfService.createPlanungsursaechlicherBedarf(2023, BigDecimal.TEN);

        final var expected = new PlanungsursaechlicherBedarfModel();
        expected.setJahr(Integer.toString(2023));
        expected.setAnzahlKinderGesamt(BigDecimal.TEN);

        assertThat(result, is(expected));
    }

    @Test
    void calculate10Year15YearAnd20YearMean() {
        final var bedarfe = new ArrayList<PlanungsursaechlicherBedarfModel>();
        var bedarf = new PlanungsursaechlicherBedarfModel();
        bedarf.setJahr("2000");
        bedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(10));
        bedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(10));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(10));
        bedarfe.add(bedarf);
        bedarf = new PlanungsursaechlicherBedarfModel();
        bedarf.setJahr("2001");
        bedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(20));
        bedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(20));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(20));
        bedarfe.add(bedarf);
        bedarf = new PlanungsursaechlicherBedarfModel();
        bedarf.setJahr("2003");
        bedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(30));
        bedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(30));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(30));
        bedarfe.add(bedarf);
        bedarf = new PlanungsursaechlicherBedarfModel();
        bedarf.setJahr("2004");
        bedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(40));
        bedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(40));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(40));
        bedarfe.add(bedarf);
        bedarf = new PlanungsursaechlicherBedarfModel();
        bedarf.setJahr("2005");
        bedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(50));
        bedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(50));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(50));
        bedarfe.add(bedarf);
        bedarf = new PlanungsursaechlicherBedarfModel();
        bedarf.setJahr("2006");
        bedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(60));
        bedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(60));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(60));
        bedarfe.add(bedarf);
        bedarf = new PlanungsursaechlicherBedarfModel();
        bedarf.setJahr("2007");
        bedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(70));
        bedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(70));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(70));
        bedarfe.add(bedarf);
        bedarf = new PlanungsursaechlicherBedarfModel();
        bedarf.setJahr("2008");
        bedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(80));
        bedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(80));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(80));
        bedarfe.add(bedarf);
        bedarf = new PlanungsursaechlicherBedarfModel();
        bedarf.setJahr("2009");
        bedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(90));
        bedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(90));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(90));
        bedarfe.add(bedarf);
        bedarf = new PlanungsursaechlicherBedarfModel();
        bedarf.setJahr("2010");
        bedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(100));
        bedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(100));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(100));
        bedarfe.add(bedarf);
        bedarf = new PlanungsursaechlicherBedarfModel();
        bedarf.setJahr("2011");
        bedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(110));
        bedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(110));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(110));
        bedarfe.add(bedarf);
        bedarf = new PlanungsursaechlicherBedarfModel();
        bedarf.setJahr("2012");
        bedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(120));
        bedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(120));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(120));
        bedarfe.add(bedarf);
        bedarf = new PlanungsursaechlicherBedarfModel();
        bedarf.setJahr("2013");
        bedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(130));
        bedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(130));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(130));
        bedarfe.add(bedarf);
        bedarf = new PlanungsursaechlicherBedarfModel();
        bedarf.setJahr("2014");
        bedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(140));
        bedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(140));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(140));
        bedarfe.add(bedarf);
        bedarf = new PlanungsursaechlicherBedarfModel();
        bedarf.setJahr("2015");
        bedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(150));
        bedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(150));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(150));
        bedarfe.add(bedarf);
        bedarf = new PlanungsursaechlicherBedarfModel();
        bedarf.setJahr("2016");
        bedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(160));
        bedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(160));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(160));
        bedarfe.add(bedarf);
        bedarf = new PlanungsursaechlicherBedarfModel();
        bedarf.setJahr("2017");
        bedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(170));
        bedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(170));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(170));
        bedarfe.add(bedarf);
        bedarf = new PlanungsursaechlicherBedarfModel();
        bedarf.setJahr("2018");
        bedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(180));
        bedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(180));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(180));
        bedarfe.add(bedarf);
        bedarf = new PlanungsursaechlicherBedarfModel();
        bedarf.setJahr("2019");
        bedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(190));
        bedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(190));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(190));
        bedarfe.add(bedarf);
        bedarf = new PlanungsursaechlicherBedarfModel();
        bedarf.setJahr("2020");
        bedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(200));
        bedarf.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(200));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(200));
        bedarfe.add(bedarf);

        final var result = planungsursaechlicherBedarfService.calculate10Year15YearAnd20YearMean(bedarfe);

        final var expected = new ArrayList<PlanungsursaechlicherBedarfModel>();
        final var meanYear10 = new PlanungsursaechlicherBedarfModel();
        meanYear10.setJahr(PlanungsursaechlicherBedarfService.TITLE_MEAN_YEAR_10);
        meanYear10.setAnzahlKinderGesamt(BigDecimal.valueOf(5500, 2));
        meanYear10.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(5500, 2));
        meanYear10.setAnzahlGruppen(BigDecimal.valueOf(5500, 2));
        expected.add(meanYear10);
        final var meanYear15 = new PlanungsursaechlicherBedarfModel();
        meanYear15.setJahr(PlanungsursaechlicherBedarfService.TITLE_MEAN_YEAR_15);
        meanYear15.setAnzahlKinderGesamt(BigDecimal.valueOf(8000, 2));
        meanYear15.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(8000, 2));
        meanYear15.setAnzahlGruppen(BigDecimal.valueOf(8000, 2));
        expected.add(meanYear15);
        final var meanYear20 = new PlanungsursaechlicherBedarfModel();
        meanYear20.setJahr(PlanungsursaechlicherBedarfService.TITLE_MEAN_YEAR_20);
        meanYear20.setAnzahlKinderGesamt(BigDecimal.valueOf(10500, 2));
        meanYear20.setAnzahlKinderZuVersorgen(BigDecimal.valueOf(10500, 2));
        meanYear20.setAnzahlGruppen(BigDecimal.valueOf(10500, 2));
        expected.add(meanYear20);

        assertThat(result, is(expected));
    }

    private List<PlanungsursachlicheWohneinheitenModel> createListOfPlanungsursachlicheWohneinheiten(
        final int firstYear,
        final int size
    ) {
        final var planungsursachlicheWohneinheiten = new ArrayList<PlanungsursachlicheWohneinheitenModel>();
        for (int index = 0; index < size; index++) {
            final var planungsursachlicheWohneinheit = new PlanungsursachlicheWohneinheitenModel();
            planungsursachlicheWohneinheit.setJahr(Integer.toString(firstYear + index));
            planungsursachlicheWohneinheit.setWohneinheiten(BigDecimal.valueOf(10000, 0));
            planungsursachlicheWohneinheiten.add(planungsursachlicheWohneinheit);
        }
        return planungsursachlicheWohneinheiten;
    }
}
