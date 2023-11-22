package de.muenchen.isi.domain.service.calculation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.TestData;
import de.muenchen.isi.domain.mapper.StammdatenDomainMapperImpl;
import de.muenchen.isi.domain.model.calculation.PlanungsursachlicheWohneinheitenModel;
import de.muenchen.isi.domain.model.calculation.PlanungsursaechlicherBedarfModel;
import de.muenchen.isi.infrastructure.repository.stammdaten.SobonOrientierungswertSozialeInfrastrukturRepository;
import de.muenchen.isi.infrastructure.repository.stammdaten.VersorgungsquoteGruppenstaerkeRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
    void calculatePlanungsursaechlicheBedarfe20Wohneinheiten() {
        final var planungsursachlicheWohneinheiten = createListOfPlanungsursachlicheWohneinheiten(20);
        final var sobonOrientierungswertSozialeInfrastruktur =
            TestData.createSobonOrientierungswertSozialeInfrastrukturModel();

        final var result = planungsursaechlicherBedarfService.calculatePlanungsursaechlicheBedarfe(
            planungsursachlicheWohneinheiten,
            sobonOrientierungswertSozialeInfrastruktur
        );

        final var expected = new ArrayList<PlanungsursaechlicherBedarfModel>();
        var planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(2000);
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(48090000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(2001);
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(95490000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(2002);
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(142210000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(2003);
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(188240000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(2004);
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(233580000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(2005);
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(278240000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(2006);
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(322210000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(2007);
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(365490000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(2008);
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(408080000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(2009);
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(449990000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(2010);
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(491480000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(2011);
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(532560000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(2012);
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(573230000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(2013);
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(613490000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(2014);
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(653350000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(2015);
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(692810000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(2016);
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(731870000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(2017);
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(770540000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(2018);
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(808830000, 4));
        expected.add(planungsursaechlicherBedarf);
        planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(2019);
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(BigDecimal.valueOf(846730000, 4));
        expected.add(planungsursaechlicherBedarf);

        assertThat(result, is(expected));
    }

    @Test
    void createPlanungsursaechlicherBedarf() {
        final var result = planungsursaechlicherBedarfService.createPlanungsursaechlicherBedarf(2023, BigDecimal.TEN);

        final var expected = new PlanungsursaechlicherBedarfModel();
        expected.setJahr(2023);
        expected.setAnzahlKinderGesamt(BigDecimal.TEN);

        assertThat(result, is(expected));
    }

    private List<PlanungsursachlicheWohneinheitenModel> createListOfPlanungsursachlicheWohneinheiten(final int size) {
        final var planungsursachlicheWohneinheiten = new ArrayList<PlanungsursachlicheWohneinheitenModel>();
        int baseYear = 2000;
        for (int index = 0; index < size; index++) {
            final var planungsursachlicheWohneinheit = new PlanungsursachlicheWohneinheitenModel();
            planungsursachlicheWohneinheit.setJahr(baseYear + index);
            planungsursachlicheWohneinheit.setWohneinheiten(BigDecimal.valueOf(10000, 0));
            planungsursachlicheWohneinheiten.add(planungsursachlicheWohneinheit);
        }
        return planungsursachlicheWohneinheiten;
    }
}
