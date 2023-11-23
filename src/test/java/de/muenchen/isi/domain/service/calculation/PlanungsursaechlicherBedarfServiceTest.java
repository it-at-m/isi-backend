package de.muenchen.isi.domain.service.calculation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.TestData;
import de.muenchen.isi.domain.mapper.StammdatenDomainMapperImpl;
import de.muenchen.isi.domain.model.calculation.PlanungsursachlicheWohneinheitenModel;
import de.muenchen.isi.domain.model.calculation.PlanungsursaechlicherBedarfModel;
import de.muenchen.isi.domain.model.stammdaten.SobonOrientierungswertSozialeInfrastrukturModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.entity.stammdaten.SobonOrientierungswertSozialeInfrastruktur;
import de.muenchen.isi.infrastructure.repository.stammdaten.SobonOrientierungswertSozialeInfrastrukturRepository;
import de.muenchen.isi.infrastructure.repository.stammdaten.VersorgungsquoteGruppenstaerkeRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
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

        assertThat(result, is(expected));
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

        assertThat(result, is(expected));
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

        assertThat(result, is(expected));
    }

    @Test
    void createPlanungsursaechlicherBedarf() {
        final var result = planungsursaechlicherBedarfService.createPlanungsursaechlicherBedarf(2023, BigDecimal.TEN);

        final var expected = new PlanungsursaechlicherBedarfModel();
        expected.setJahr(Integer.toString(2023));
        expected.setAnzahlKinderGesamt(BigDecimal.TEN);

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
