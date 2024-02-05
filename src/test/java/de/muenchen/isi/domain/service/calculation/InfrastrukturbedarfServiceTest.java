package de.muenchen.isi.domain.service.calculation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.TestData;
import de.muenchen.isi.domain.exception.CalculationException;
import de.muenchen.isi.domain.mapper.StammdatenDomainMapperImpl;
import de.muenchen.isi.domain.model.calculation.InfrastrukturbedarfProJahrModel;
import de.muenchen.isi.domain.model.calculation.PersonenProJahrModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenProFoerderartProJahrModel;
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
class InfrastrukturbedarfServiceTest {

    @Mock
    private SobonOrientierungswertSozialeInfrastrukturRepository sobonOrientierungswertSozialeInfrastrukturRepository;

    @Mock
    private VersorgungsquoteGruppenstaerkeRepository versorgungsquoteGruppenstaerkeRepository;

    private InfrastrukturbedarfService infrastrukturbedarfService;

    @BeforeEach
    public void beforeEach() {
        this.infrastrukturbedarfService =
            new InfrastrukturbedarfService(
                sobonOrientierungswertSozialeInfrastrukturRepository,
                versorgungsquoteGruppenstaerkeRepository,
                new StammdatenDomainMapperImpl()
            );
        Mockito.reset(sobonOrientierungswertSozialeInfrastrukturRepository, versorgungsquoteGruppenstaerkeRepository);
    }

    @Test
    void calculateBedarfForKinderkrippe() throws CalculationException {
        final SobonOrientierungswertJahr sobonJahr = SobonOrientierungswertJahr.JAHR_2017;
        final LocalDate gueltigAb = LocalDate.of(1998, 1, 1);
        final var wohneinheiten = new ArrayList<WohneinheitenProFoerderartProJahrModel>();
        var wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart1");
        wohneinheitenModel.setJahr("2000");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(10000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart1");
        wohneinheitenModel.setJahr("2001");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(20000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart1");
        wohneinheitenModel.setJahr("2002");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(30000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart2");
        wohneinheitenModel.setJahr("2000");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(40000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart2");
        wohneinheitenModel.setJahr("2001");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(50000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart3");
        wohneinheitenModel.setJahr("2000");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(60000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart1");
        wohneinheitenModel.setJahr("Gesamt 10 Jare");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(60000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart2");
        wohneinheitenModel.setJahr("Gesamt 10 Jahre");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(90000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
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

        final var result = infrastrukturbedarfService.calculateBedarfForKinderkrippe(
            wohneinheiten,
            sobonJahr,
            InfrastrukturbedarfService.ArtInfrastrukturbedarf.PLANUNGSURSAECHLICH,
            gueltigAb
        );

        final var expected = new ArrayList<InfrastrukturbedarfProJahrModel>();
        var bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2000));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("52900.763358778610000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("31740.458015267166000"));
        bedarf.setAnzahlGruppen(new BigDecimal("2645.038167938930500"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2001));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("85809.160305343550000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("51485.496183206130000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4290.458015267177500"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2002));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("99000.000000000030000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("59400.000000000018000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4950.000000000001500"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2003));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("97557.251908397030000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("58534.351145038218000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4877.862595419851500"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2004));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("96114.503816793960000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("57668.702290076376000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4805.725190839698000"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2005));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("94671.755725190850000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("56803.053435114510000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4733.587786259542500"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2006));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("93229.007633587820000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("55937.404580152692000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4661.450381679391000"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2007));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("91786.259541984750000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("55071.755725190850000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4589.312977099237500"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2008));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("90343.511450381750000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("54206.106870229050000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4517.175572519087500"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2009));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("88900.763358778680000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("53340.458015267208000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4445.038167938934000"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2010));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("87752.748091603150000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("52651.648854961890000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4387.637404580157500"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2011));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("86796.900000000110000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("52078.140000000066000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4339.845000000005500"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2012));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("85928.931000000100000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("51557.358600000060000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4296.446550000005000"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2013));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("85069.641690000030000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("51041.785014000018000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4253.482084500001500"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2014));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("84218.945273100060000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("50531.367163860036000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4210.947263655003000"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2015));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("83376.755820369010000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("50026.053492221406000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4168.837791018450500"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2016));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("82542.988262165380000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("49525.792957299228000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4127.149413108269000"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2017));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("81717.558379543700000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("49030.535027726220000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4085.877918977185000"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2018));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("80900.382795748270000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("48540.229677448962000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4045.019139787413500"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2019));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("80091.378967790760000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("48054.827380674456000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4004.568948389538000"));
        expected.add(bedarf);

        assertThat(result, is(expected));
    }

    @Test
    void calculateBedarfForKindergarten() throws CalculationException {
        final SobonOrientierungswertJahr sobonJahr = SobonOrientierungswertJahr.JAHR_2017;
        final LocalDate gueltigAb = LocalDate.of(1998, 1, 1);
        final var wohneinheiten = new ArrayList<WohneinheitenProFoerderartProJahrModel>();
        var wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart1");
        wohneinheitenModel.setJahr("2000");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(10000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart1");
        wohneinheitenModel.setJahr("2001");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(20000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart1");
        wohneinheitenModel.setJahr("2002");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(30000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart2");
        wohneinheitenModel.setJahr("2000");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(40000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart2");
        wohneinheitenModel.setJahr("2001");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(50000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart3");
        wohneinheitenModel.setJahr("2000");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(60000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart1");
        wohneinheitenModel.setJahr("Gesamt 10 Jare");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(60000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart2");
        wohneinheitenModel.setJahr("Gesamt 10 Jahre");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(90000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart3");
        wohneinheitenModel.setJahr("Gesamt 10 Jare");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(60000));
        wohneinheiten.add(wohneinheitenModel);

        var sobonOrientierungswertFoerderart1 = TestData.createSobonOrientierungswertSozialeInfrastrukturEntity();
        sobonOrientierungswertFoerderart1.setFoerderartBezeichnung("foerderart1");
        Mockito
            .when(
                this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                        InfrastruktureinrichtungTyp.KINDERGARTEN,
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
                        InfrastruktureinrichtungTyp.KINDERGARTEN,
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
                        InfrastruktureinrichtungTyp.KINDERGARTEN,
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
                        InfrastruktureinrichtungTyp.KINDERGARTEN,
                        gueltigAb
                    )
            )
            .thenReturn(Optional.of(versorgungsQuote));

        final var result = infrastrukturbedarfService.calculateBedarfForKindergarten(
            wohneinheiten,
            sobonJahr,
            InfrastrukturbedarfService.ArtInfrastrukturbedarf.PLANUNGSURSAECHLICH,
            gueltigAb
        );

        final var expected = new ArrayList<InfrastrukturbedarfProJahrModel>();
        var bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2000));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("52900.763358778610000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("31740.458015267166000"));
        bedarf.setAnzahlGruppen(new BigDecimal("2645.038167938930500"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2001));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("85809.160305343550000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("51485.496183206130000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4290.458015267177500"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2002));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("99000.000000000030000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("59400.000000000018000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4950.000000000001500"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2003));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("97557.251908397030000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("58534.351145038218000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4877.862595419851500"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2004));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("96114.503816793960000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("57668.702290076376000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4805.725190839698000"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2005));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("94671.755725190850000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("56803.053435114510000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4733.587786259542500"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2006));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("93229.007633587820000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("55937.404580152692000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4661.450381679391000"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2007));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("91786.259541984750000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("55071.755725190850000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4589.312977099237500"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2008));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("90343.511450381750000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("54206.106870229050000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4517.175572519087500"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2009));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("88900.763358778680000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("53340.458015267208000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4445.038167938934000"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2010));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("87752.748091603150000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("52651.648854961890000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4387.637404580157500"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2011));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("86796.900000000110000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("52078.140000000066000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4339.845000000005500"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2012));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("85928.931000000100000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("51557.358600000060000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4296.446550000005000"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2013));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("85069.641690000030000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("51041.785014000018000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4253.482084500001500"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2014));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("84218.945273100060000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("50531.367163860036000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4210.947263655003000"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2015));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("83376.755820369010000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("50026.053492221406000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4168.837791018450500"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2016));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("82542.988262165380000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("49525.792957299228000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4127.149413108269000"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2017));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("81717.558379543700000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("49030.535027726220000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4085.877918977185000"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2018));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("80900.382795748270000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("48540.229677448962000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4045.019139787413500"));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2019));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("80091.378967790760000"));
        bedarf.setAnzahlPersonenZuVersorgen(new BigDecimal("48054.827380674456000"));
        bedarf.setAnzahlGruppen(new BigDecimal("4004.568948389538000"));
        expected.add(bedarf);

        assertThat(result, is(expected));
    }

    @Test
    void calculateAlleEinwohner() throws CalculationException {
        final SobonOrientierungswertJahr sobonJahr = SobonOrientierungswertJahr.JAHR_2017;
        final var wohneinheiten = new ArrayList<WohneinheitenProFoerderartProJahrModel>();
        var wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart1");
        wohneinheitenModel.setJahr("2000");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(10000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart1");
        wohneinheitenModel.setJahr("2001");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(20000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart1");
        wohneinheitenModel.setJahr("2002");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(30000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart2");
        wohneinheitenModel.setJahr("2000");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(40000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart2");
        wohneinheitenModel.setJahr("2001");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(50000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart3");
        wohneinheitenModel.setJahr("2000");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(60000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart1");
        wohneinheitenModel.setJahr("Gesamt 10 Jare");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(60000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart2");
        wohneinheitenModel.setJahr("Gesamt 10 Jahre");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(90000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart3");
        wohneinheitenModel.setJahr("Gesamt 10 Jare");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(60000));
        wohneinheiten.add(wohneinheitenModel);

        var sobonOrientierungswertFoerderart1 = TestData.createSobonOrientierungswertSozialeInfrastrukturEntity();
        sobonOrientierungswertFoerderart1.setFoerderartBezeichnung("foerderart1");
        Mockito
            .when(
                this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                        InfrastruktureinrichtungTyp.UNSPECIFIED,
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
                        InfrastruktureinrichtungTyp.UNSPECIFIED,
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
                        InfrastruktureinrichtungTyp.UNSPECIFIED,
                        "foerderart3",
                        SobonOrientierungswertJahr.JAHR_2017.getGueltigAb()
                    )
            )
            .thenReturn(Optional.of(sobonOrientierungswertFoerderart3));

        final var result = infrastrukturbedarfService.calculateAlleEinwohner(wohneinheiten, sobonJahr);

        final var expected = new ArrayList<PersonenProJahrModel>();
        var bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2000));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("52900.763358778610000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2001));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("85809.160305343550000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2002));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("99000.000000000030000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2003));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("97557.251908397030000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2004));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("96114.503816793960000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2005));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("94671.755725190850000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2006));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("93229.007633587820000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2007));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("91786.259541984750000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2008));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("90343.511450381750000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2009));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("88900.763358778680000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2010));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("87752.748091603150000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2011));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("86796.900000000110000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2012));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("85928.931000000100000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2013));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("85069.641690000030000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2014));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("84218.945273100060000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2015));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("83376.755820369010000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2016));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("82542.988262165380000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2017));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("81717.558379543700000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2018));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("80900.382795748270000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2019));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("80091.378967790760000"));
        expected.add(bedarf);

        assertThat(result, is(expected));
    }

    @Test
    void calculatePersonen() throws CalculationException {
        final InfrastruktureinrichtungTyp einrichtung = InfrastruktureinrichtungTyp.KINDERKRIPPE;
        final SobonOrientierungswertJahr sobonJahr = SobonOrientierungswertJahr.JAHR_2017;
        final LocalDate gueltigAb = LocalDate.of(1998, 1, 1);
        final var wohneinheiten = new ArrayList<WohneinheitenProFoerderartProJahrModel>();
        var wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart1");
        wohneinheitenModel.setJahr("2000");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(10000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart1");
        wohneinheitenModel.setJahr("2001");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(20000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart1");
        wohneinheitenModel.setJahr("2002");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(30000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart2");
        wohneinheitenModel.setJahr("2000");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(40000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart2");
        wohneinheitenModel.setJahr("2001");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(50000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart3");
        wohneinheitenModel.setJahr("2000");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(60000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart1");
        wohneinheitenModel.setJahr("Gesamt 10 Jare");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(60000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenModel.setFoerderart("foerderart2");
        wohneinheitenModel.setJahr("Gesamt 10 Jahre");
        wohneinheitenModel.setWohneinheiten(BigDecimal.valueOf(90000));
        wohneinheiten.add(wohneinheitenModel);
        wohneinheitenModel = new WohneinheitenProFoerderartProJahrModel();
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

        final var result = infrastrukturbedarfService.calculatePersonen(einrichtung, wohneinheiten, sobonJahr);

        final var expected = new ArrayList<PersonenProJahrModel>();
        var bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2000));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("52900.763358778610000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2001));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("85809.160305343550000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2002));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("99000.000000000030000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2003));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("97557.251908397030000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2004));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("96114.503816793960000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2005));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("94671.755725190850000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2006));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("93229.007633587820000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2007));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("91786.259541984750000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2008));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("90343.511450381750000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2009));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("88900.763358778680000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2010));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("87752.748091603150000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2011));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("86796.900000000110000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2012));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("85928.931000000100000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2013));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("85069.641690000030000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2014));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("84218.945273100060000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2015));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("83376.755820369010000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2016));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("82542.988262165380000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2017));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("81717.558379543700000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2018));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("80900.382795748270000"));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2019));
        bedarf.setAnzahlPersonenGesamt(new BigDecimal("80091.378967790760000"));
        expected.add(bedarf);

        assertThat(result.collect(Collectors.toList()), is(expected));
    }

    @Test
    void getVersorgungsquoteAndGruppenstaerkeWithBedarf() {
        final var bedarf = new PersonenProJahrModel();
        bedarf.setJahr("2031");
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(100));
        final var versorgungsQuote = new VersorgungsquoteGruppenstaerke();
        versorgungsQuote.setVersorgungsquotePlanungsursaechlich(BigDecimal.valueOf(60, 2));
        versorgungsQuote.setVersorgungsquoteSobonUrsaechlich(null);
        versorgungsQuote.setGruppenstaerke(10);
        var result = infrastrukturbedarfService.getVersorgungsquoteAndGruppenstaerkeWithBedarf(
            bedarf,
            versorgungsQuote,
            InfrastrukturbedarfService.ArtInfrastrukturbedarf.PLANUNGSURSAECHLICH
        );
        var expected = new InfrastrukturbedarfProJahrModel();
        expected.setJahr("2031");
        expected.setAnzahlPersonenGesamt(BigDecimal.valueOf(100));
        expected.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(60000000000000000L, 15));
        expected.setAnzahlGruppen(BigDecimal.valueOf(6000000000000000L, 15));
        assertThat(result, is(expected));

        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(100));
        versorgungsQuote.setVersorgungsquotePlanungsursaechlich(null);
        versorgungsQuote.setVersorgungsquoteSobonUrsaechlich(BigDecimal.valueOf(60, 2));
        versorgungsQuote.setGruppenstaerke(10);
        result =
            infrastrukturbedarfService.getVersorgungsquoteAndGruppenstaerkeWithBedarf(
                bedarf,
                versorgungsQuote,
                InfrastrukturbedarfService.ArtInfrastrukturbedarf.SOBON_URSAECHLICH
            );
        expected = new InfrastrukturbedarfProJahrModel();
        expected.setJahr("2031");
        expected.setAnzahlPersonenGesamt(BigDecimal.valueOf(100));
        expected.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(60000000000000000L, 15));
        expected.setAnzahlGruppen(BigDecimal.valueOf(6000000000000000L, 15));
        assertThat(result, is(expected));

        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(73));
        versorgungsQuote.setVersorgungsquotePlanungsursaechlich(BigDecimal.valueOf(5800000000L, 10));
        versorgungsQuote.setVersorgungsquoteSobonUrsaechlich(null);
        versorgungsQuote.setGruppenstaerke(10);
        result =
            infrastrukturbedarfService.getVersorgungsquoteAndGruppenstaerkeWithBedarf(
                bedarf,
                versorgungsQuote,
                InfrastrukturbedarfService.ArtInfrastrukturbedarf.PLANUNGSURSAECHLICH
            );
        expected = new InfrastrukturbedarfProJahrModel();
        expected.setJahr("2031");
        expected.setAnzahlPersonenGesamt(BigDecimal.valueOf(73));
        expected.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(42340000000000000L, 15));
        expected.setAnzahlGruppen(BigDecimal.valueOf(4234000000000000L, 15));
        assertThat(result, is(expected));

        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(73));
        versorgungsQuote.setVersorgungsquotePlanungsursaechlich(null);
        versorgungsQuote.setVersorgungsquoteSobonUrsaechlich(BigDecimal.valueOf(580000000000000L, 15));
        versorgungsQuote.setGruppenstaerke(10);
        result =
            infrastrukturbedarfService.getVersorgungsquoteAndGruppenstaerkeWithBedarf(
                bedarf,
                versorgungsQuote,
                InfrastrukturbedarfService.ArtInfrastrukturbedarf.SOBON_URSAECHLICH
            );
        expected = new InfrastrukturbedarfProJahrModel();
        expected.setJahr("2031");
        expected.setAnzahlPersonenGesamt(BigDecimal.valueOf(73));
        expected.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(42340000000000000L, 15));
        expected.setAnzahlGruppen(BigDecimal.valueOf(4234000000000000L, 15));
        assertThat(result, is(expected));

        bedarf.setAnzahlPersonenGesamt(null);
        versorgungsQuote.setVersorgungsquotePlanungsursaechlich(BigDecimal.valueOf(58, 2));
        versorgungsQuote.setVersorgungsquoteSobonUrsaechlich(null);
        versorgungsQuote.setGruppenstaerke(10);
        Assertions.assertThrows(
            NullPointerException.class,
            () ->
                infrastrukturbedarfService.getVersorgungsquoteAndGruppenstaerkeWithBedarf(
                    bedarf,
                    versorgungsQuote,
                    InfrastrukturbedarfService.ArtInfrastrukturbedarf.PLANUNGSURSAECHLICH
                )
        );

        bedarf.setAnzahlPersonenGesamt(null);
        versorgungsQuote.setVersorgungsquotePlanungsursaechlich(null);
        versorgungsQuote.setVersorgungsquoteSobonUrsaechlich(BigDecimal.valueOf(58, 2));
        versorgungsQuote.setGruppenstaerke(10);
        Assertions.assertThrows(
            NullPointerException.class,
            () ->
                infrastrukturbedarfService.getVersorgungsquoteAndGruppenstaerkeWithBedarf(
                    bedarf,
                    versorgungsQuote,
                    InfrastrukturbedarfService.ArtInfrastrukturbedarf.SOBON_URSAECHLICH
                )
        );

        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(73));
        versorgungsQuote.setVersorgungsquotePlanungsursaechlich(null);
        versorgungsQuote.setVersorgungsquoteSobonUrsaechlich(null);
        versorgungsQuote.setGruppenstaerke(10);
        Assertions.assertThrows(
            NullPointerException.class,
            () ->
                infrastrukturbedarfService.getVersorgungsquoteAndGruppenstaerkeWithBedarf(
                    bedarf,
                    versorgungsQuote,
                    InfrastrukturbedarfService.ArtInfrastrukturbedarf.PLANUNGSURSAECHLICH
                )
        );

        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(73));
        versorgungsQuote.setVersorgungsquotePlanungsursaechlich(null);
        versorgungsQuote.setVersorgungsquoteSobonUrsaechlich(null);
        versorgungsQuote.setGruppenstaerke(10);
        Assertions.assertThrows(
            NullPointerException.class,
            () ->
                infrastrukturbedarfService.getVersorgungsquoteAndGruppenstaerkeWithBedarf(
                    bedarf,
                    versorgungsQuote,
                    InfrastrukturbedarfService.ArtInfrastrukturbedarf.SOBON_URSAECHLICH
                )
        );
    }

    @Test
    void add() {
        final var model1 = new PersonenProJahrModel();
        model1.setJahr("2000");
        model1.setAnzahlPersonenGesamt(BigDecimal.valueOf(10));
        final var model2 = new PersonenProJahrModel();
        model2.setJahr("2001");
        model2.setAnzahlPersonenGesamt(BigDecimal.valueOf(22));
        var result = infrastrukturbedarfService.add(model1, model2);
        var expected = new PersonenProJahrModel();
        expected.setJahr("2000");
        expected.setAnzahlPersonenGesamt(BigDecimal.valueOf(32));
        assertThat(result, is(expected));

        model1.setJahr("2000");
        model1.setAnzahlPersonenGesamt(null);
        model2.setJahr("2001");
        model2.setAnzahlPersonenGesamt(BigDecimal.valueOf(22));
        result = infrastrukturbedarfService.add(model1, model2);
        expected = new PersonenProJahrModel();
        expected.setJahr("2000");
        expected.setAnzahlPersonenGesamt(BigDecimal.valueOf(22));
        assertThat(result, is(expected));

        model1.setJahr("2000");
        model1.setAnzahlPersonenGesamt(BigDecimal.valueOf(10));
        model2.setJahr("2001");
        model2.setAnzahlPersonenGesamt(null);
        result = infrastrukturbedarfService.add(model1, model2);
        expected = new PersonenProJahrModel();
        expected.setJahr("2000");
        expected.setAnzahlPersonenGesamt(BigDecimal.valueOf(10));
        assertThat(result, is(expected));

        model1.setJahr("2000");
        model1.setAnzahlPersonenGesamt(null);
        model2.setJahr("2001");
        model2.setAnzahlPersonenGesamt(null);
        result = infrastrukturbedarfService.add(model1, model2);
        expected = new PersonenProJahrModel();
        expected.setJahr("2000");
        expected.setAnzahlPersonenGesamt(BigDecimal.valueOf(0));
        assertThat(result, is(expected));

        model1.setJahr("2000");
        model1.setAnzahlPersonenGesamt(null);
        model2.setJahr(null);
        model2.setAnzahlPersonenGesamt(null);
        result = infrastrukturbedarfService.add(model1, model2);
        expected = new PersonenProJahrModel();
        expected.setJahr("2000");
        expected.setAnzahlPersonenGesamt(BigDecimal.valueOf(0));
        assertThat(result, is(expected));

        model1.setJahr(null);
        model1.setAnzahlPersonenGesamt(null);
        model2.setJahr("2001");
        model2.setAnzahlPersonenGesamt(null);
        result = infrastrukturbedarfService.add(model1, model2);
        expected = new PersonenProJahrModel();
        expected.setJahr("2001");
        expected.setAnzahlPersonenGesamt(BigDecimal.valueOf(0));
        assertThat(result, is(expected));

        model1.setJahr(null);
        model1.setAnzahlPersonenGesamt(null);
        model2.setJahr(null);
        model2.setAnzahlPersonenGesamt(null);
        result = infrastrukturbedarfService.add(model1, model2);
        expected = new PersonenProJahrModel();
        expected.setJahr(null);
        expected.setAnzahlPersonenGesamt(BigDecimal.valueOf(0));
        assertThat(result, is(expected));
    }

    @Test
    void getSobonOrientierungswertGroupedByFoerderart() throws CalculationException {
        final var wohneinheiten = new ArrayList<WohneinheitenProFoerderartProJahrModel>();
        var wohneinheitenProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProJahr.setFoerderart("foerderart1");
        wohneinheiten.add(wohneinheitenProJahr);
        wohneinheitenProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProJahr.setFoerderart("foerderart2");
        wohneinheiten.add(wohneinheitenProJahr);
        wohneinheitenProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProJahr.setFoerderart("foerderart1");
        wohneinheiten.add(wohneinheitenProJahr);
        wohneinheitenProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProJahr.setFoerderart("foerderart1");
        wohneinheiten.add(wohneinheitenProJahr);
        wohneinheitenProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProJahr.setFoerderart("foerderart3");
        wohneinheiten.add(wohneinheitenProJahr);
        wohneinheitenProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProJahr.setFoerderart("foerderart4");
        wohneinheiten.add(wohneinheitenProJahr);

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

        final var result = infrastrukturbedarfService.getSobonOrientierungswertGroupedByFoerderart(
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
    void getSobonOrientierungswertGroupedByFoerderartCalculationException() {
        final var wohneinheiten = new ArrayList<WohneinheitenProFoerderartProJahrModel>();
        var wohneinheitenProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProJahr.setFoerderart("foerderart-existing");
        wohneinheiten.add(wohneinheitenProJahr);
        wohneinheitenProJahr = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProJahr.setFoerderart("foerderart-NOT-existing");
        wohneinheiten.add(wohneinheitenProJahr);

        var sobonOrientierungswertFoerderartExisting = new SobonOrientierungswertSozialeInfrastruktur();
        sobonOrientierungswertFoerderartExisting.setFoerderartBezeichnung("foerderart-existing");
        Mockito
            .when(
                this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                        InfrastruktureinrichtungTyp.KINDERKRIPPE,
                        "foerderart-existing",
                        SobonOrientierungswertJahr.JAHR_2017.getGueltigAb()
                    )
            )
            .thenReturn(Optional.of(sobonOrientierungswertFoerderartExisting));

        Mockito
            .when(
                this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                        InfrastruktureinrichtungTyp.KINDERKRIPPE,
                        "foerderart-NOT-existing",
                        SobonOrientierungswertJahr.JAHR_2017.getGueltigAb()
                    )
            )
            .thenReturn(Optional.empty());

        Assertions.assertThrows(
            CalculationException.class,
            () ->
                infrastrukturbedarfService.getSobonOrientierungswertGroupedByFoerderart(
                    wohneinheiten,
                    SobonOrientierungswertJahr.JAHR_2017,
                    InfrastruktureinrichtungTyp.KINDERKRIPPE
                )
        );
    }

    @Test
    void calculateBedarfeStartAtYear20() {
        final var wohneinheiten = createListOfWohneinheiten(2019, 1);
        final var sobonOrientierungswertSozialeInfrastruktur =
            TestData.createSobonOrientierungswertSozialeInfrastrukturModel();

        final var result = infrastrukturbedarfService.calculatePersonenForWohneinheitProJahr(
            2000,
            wohneinheiten.get(0),
            sobonOrientierungswertSozialeInfrastruktur
        );

        final var expected = new ArrayList<PersonenProJahrModel>();
        var bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2019));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(4809160305343510000L, 15));
        expected.add(bedarf);

        assertThat(result.collect(Collectors.toList()), is(expected));
    }

    @Test
    void calculateBedarfeStartAtYear9() {
        final var wohneinheiten = createListOfWohneinheiten(2008, 1);
        final var sobonOrientierungswertSozialeInfrastruktur =
            TestData.createSobonOrientierungswertSozialeInfrastrukturModel();

        final var result = infrastrukturbedarfService.calculatePersonenForWohneinheitProJahr(
            2000,
            wohneinheiten.get(0),
            sobonOrientierungswertSozialeInfrastruktur
        );

        final var expected = new ArrayList<PersonenProJahrModel>();
        var bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2008));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(4809160305343510000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2009));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(4740458015267180000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2010));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(4671755725190840000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2011));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(4603053435114510000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2012));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(4534351145038170000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2013));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(4465648854961830000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2014));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(4396946564885500000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2015));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(4328244274809160000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2016));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(4259541984732830000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2017));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(4190839694656490000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2018));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(4148931297709930000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2019));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(4107441984732830000L, 15));
        expected.add(bedarf);

        assertThat(result.collect(Collectors.toList()), is(expected));
    }

    @Test
    void calculateBedarfeStartAtYear1() {
        final var wohneinheiten = createListOfWohneinheiten(2000, 1);
        final var sobonOrientierungswertSozialeInfrastruktur =
            TestData.createSobonOrientierungswertSozialeInfrastrukturModel();

        final var result = infrastrukturbedarfService.calculatePersonenForWohneinheitProJahr(
            2000,
            wohneinheiten.get(0),
            sobonOrientierungswertSozialeInfrastruktur
        );

        final var expected = new ArrayList<PersonenProJahrModel>();
        var bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2000));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(4809160305343510000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2001));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(4740458015267180000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2002));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(4671755725190840000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2003));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(4603053435114510000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2004));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(4534351145038170000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2005));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(4465648854961830000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2006));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(4396946564885500000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2007));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(4328244274809160000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2008));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(4259541984732830000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2009));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(4190839694656490000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2010));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(4148931297709930000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2011));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(4107441984732830000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2012));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(4066367564885500000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2013));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(4025703889236640000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2014));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(3985446850344280000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2015));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(3945592381840830000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2016));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(3906136458022430000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2017));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(3867075093442200000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2018));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(3828404342507780000L, 15));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2019));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(3790120299082700000L, 15));
        expected.add(bedarf);

        assertThat(result.collect(Collectors.toList()), is(expected));
    }

    @Test
    void createBedarf() {
        final var result = infrastrukturbedarfService.createPersonenProJahr(2023, BigDecimal.TEN);

        final var expected = new PersonenProJahrModel();
        expected.setJahr(Integer.toString(2023));
        expected.setAnzahlPersonenGesamt(BigDecimal.TEN);

        assertThat(result, is(expected));
    }

    private List<WohneinheitenProFoerderartProJahrModel> createListOfWohneinheiten(
        final int firstYear,
        final int size
    ) {
        final var wohneinheiten = new ArrayList<WohneinheitenProFoerderartProJahrModel>();
        for (int index = 0; index < size; index++) {
            final var wohneinheit = new WohneinheitenProFoerderartProJahrModel();
            wohneinheit.setJahr(Integer.toString(firstYear + index));
            wohneinheit.setWohneinheiten(BigDecimal.valueOf(10000, 0));
            wohneinheiten.add(wohneinheit);
        }
        return wohneinheiten;
    }
}
