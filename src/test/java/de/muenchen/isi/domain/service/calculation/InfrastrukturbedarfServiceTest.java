package de.muenchen.isi.domain.service.calculation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.TestData;
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
    void calculateBedarfForKinderkrippeRoundedAndWithMean() {
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

        final var result = infrastrukturbedarfService.calculateBedarfForKinderkrippeRoundedAndWithMean(
            wohneinheiten,
            sobonJahr,
            InfrastrukturbedarfService.ArtInfrastrukturbedarf.PLANUNGSURSAECHLICH,
            gueltigAb
        );

        final var expected = new ArrayList<InfrastrukturbedarfProJahrModel>();
        var bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2000));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(52899));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(31739));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(264495, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2001));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(85803));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(51482));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(429015, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2002));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(98999));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(59399));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(494995, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2003));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(97557));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(58534));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(487785, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2004));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(96111));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(57667));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(480555, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2005));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(94673));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(56804));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(473365, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2006));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(93231));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(55939));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(466155, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2007));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(91785));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(55071));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(458925, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2008));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(90336));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(54202));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(451680, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2009));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(88898));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(53339));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(444490, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2010));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(87753));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(52652));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(438765, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2011));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(86804));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(52082));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(434020, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2012));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(85940));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(51564));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(429700, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2013));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(85079));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(51047));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(425395, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2014));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(84229));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(50537));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(421145, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2015));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(83386));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(50032));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(416930, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2016));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(82546));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(49528));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(412730, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2017));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(81717));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(49030));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(408585, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2018));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(80906));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(48544));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(404530, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2019));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(80094));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(48056));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(400470, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(InfrastrukturbedarfService.TITLE_MEAN_YEAR_10);
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(8902920, 2));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(5341760, 2));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(445146, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(InfrastrukturbedarfService.TITLE_MEAN_YEAR_15);
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(8800647, 2));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(5280387, 2));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(440032, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(InfrastrukturbedarfService.TITLE_MEAN_YEAR_20);
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(8643730, 2));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(5186240, 2));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(432186, 2));
        expected.add(bedarf);

        assertThat(result, is(expected));
    }

    @Test
    void calculateBedarfForKindergartenRoundedAndWithMean() {
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

        final var result = infrastrukturbedarfService.calculateBedarfForKindergartenRoundedAndWithMean(
            wohneinheiten,
            sobonJahr,
            InfrastrukturbedarfService.ArtInfrastrukturbedarf.PLANUNGSURSAECHLICH,
            gueltigAb
        );

        final var expected = new ArrayList<InfrastrukturbedarfProJahrModel>();
        var bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2000));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(52899));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(31739));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(264495, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2001));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(85803));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(51482));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(429015, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2002));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(98999));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(59399));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(494995, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2003));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(97557));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(58534));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(487785, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2004));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(96111));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(57667));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(480555, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2005));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(94673));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(56804));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(473365, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2006));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(93231));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(55939));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(466155, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2007));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(91785));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(55071));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(458925, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2008));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(90336));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(54202));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(451680, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2009));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(88898));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(53339));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(444490, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2010));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(87753));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(52652));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(438765, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2011));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(86804));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(52082));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(434020, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2012));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(85940));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(51564));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(429700, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2013));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(85079));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(51047));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(425395, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2014));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(84229));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(50537));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(421145, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2015));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(83386));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(50032));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(416930, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2016));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(82546));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(49528));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(412730, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2017));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(81717));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(49030));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(408585, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2018));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(80906));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(48544));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(404530, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2019));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(80094));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(48056));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(400470, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(InfrastrukturbedarfService.TITLE_MEAN_YEAR_10);
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(8902920, 2));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(5341760, 2));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(445146, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(InfrastrukturbedarfService.TITLE_MEAN_YEAR_15);
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(8800647, 2));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(5280387, 2));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(440032, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(InfrastrukturbedarfService.TITLE_MEAN_YEAR_20);
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(8643730, 2));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(5186240, 2));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(432186, 2));
        expected.add(bedarf);

        assertThat(result, is(expected));
    }

    @Test
    void calculateAlleEinwohnerRoundedAndWithMean() {
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

        final var result = infrastrukturbedarfService.calculateAlleEinwohnerRoundedAndWithMean(
            wohneinheiten,
            sobonJahr
        );

        final var expected = new ArrayList<PersonenProJahrModel>();
        var bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2000));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(52899));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2001));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(85803));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2002));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(98999));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2003));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(97557));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2004));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(96111));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2005));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(94673));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2006));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(93231));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2007));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(91785));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2008));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(90336));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2009));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(88898));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2010));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(87753));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2011));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(86804));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2012));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(85940));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2013));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(85079));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2014));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(84229));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2015));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(83386));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2016));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(82546));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2017));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(81717));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2018));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(80906));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2019));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(80094));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(InfrastrukturbedarfService.TITLE_MEAN_YEAR_10);
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(8902920, 2));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(InfrastrukturbedarfService.TITLE_MEAN_YEAR_15);
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(8800647, 2));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(InfrastrukturbedarfService.TITLE_MEAN_YEAR_20);
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(8643730, 2));
        expected.add(bedarf);

        assertThat(result, is(expected));
    }

    @Test
    void calculatePersonen() {
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
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(528990000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2001));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(858030000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2002));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(989990000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2003));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(975570000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2004));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(961110000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2005));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(946730000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2006));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(932310000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2007));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(917850000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2008));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(903360000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2009));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(888980000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2010));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(877530000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2011));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(868040000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2012));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(859400000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2013));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(850790000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2014));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(842290000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2015));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(833860000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2016));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(825460000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2017));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(817170000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2018));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(809060000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2019));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(800940000000000L, 10));
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
        expected.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(6000, 2));
        expected.setAnzahlGruppen(BigDecimal.valueOf(600, 2));
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
        expected.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(6000, 2));
        expected.setAnzahlGruppen(BigDecimal.valueOf(600, 2));
        assertThat(result, is(expected));

        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(73));
        versorgungsQuote.setVersorgungsquotePlanungsursaechlich(BigDecimal.valueOf(58, 2));
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
        expected.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(4234, 2));
        expected.setAnzahlGruppen(BigDecimal.valueOf(423, 2));
        assertThat(result, is(expected));

        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(73));
        versorgungsQuote.setVersorgungsquotePlanungsursaechlich(null);
        versorgungsQuote.setVersorgungsquoteSobonUrsaechlich(BigDecimal.valueOf(58, 2));
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
        expected.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(4234, 2));
        expected.setAnzahlGruppen(BigDecimal.valueOf(423, 2));
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
    void roundValuesAndReturnModelWithRoundedValuesInfrastrukturbedarf() {
        final var model = new InfrastrukturbedarfProJahrModel();
        model.setJahr("2000");
        model.setAnzahlPersonenGesamt(BigDecimal.valueOf(46, 1));
        model.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(44, 1));
        model.setAnzahlGruppen(BigDecimal.valueOf(44460, 4));

        final var result = infrastrukturbedarfService.roundValuesAndReturnModelWithRoundedValues(model);

        final var expected = new InfrastrukturbedarfProJahrModel();
        expected.setJahr("2000");
        expected.setAnzahlPersonenGesamt(BigDecimal.valueOf(5));
        expected.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(4));
        expected.setAnzahlGruppen(BigDecimal.valueOf(445, 2));

        assertThat(result, is(expected));
    }

    @Test
    void roundValuesAndReturnModelWithRoundedValuesPersonen() {
        final var model = new PersonenProJahrModel();
        model.setJahr("2000");
        model.setAnzahlPersonenGesamt(BigDecimal.valueOf(46, 1));

        final var result = infrastrukturbedarfService.roundValuesAndReturnModelWithRoundedValues(model);

        final var expected = new PersonenProJahrModel();
        expected.setJahr("2000");
        expected.setAnzahlPersonenGesamt(BigDecimal.valueOf(5));

        assertThat(result, is(expected));
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
    void getSobonOrientierungswertGroupedByFoerderart() {
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
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(48090000000000L, 10));
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
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(48090000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2009));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(47400000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2010));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(46720000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2011));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(46030000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2012));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(45340000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2013));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(44660000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2014));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(43970000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2015));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(43280000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2016));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(42590000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2017));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(41910000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2018));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(41490000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2019));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(41080000000000L, 10));
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
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(48090000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2001));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(47400000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2002));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(46720000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2003));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(46030000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2004));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(45340000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2005));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(44660000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2006));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(43970000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2007));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(43280000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2008));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(42590000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2009));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(41910000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2010));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(41490000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2011));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(41080000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2012));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(40670000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2013));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(40260000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2014));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(39860000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2015));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(39460000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2016));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(39060000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2017));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(38670000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2018));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(38290000000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2019));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(37900000000000L, 10));
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

    @Test
    void calculate10Year15YearAnd20YearMeanInfrastrukturbedarfe() {
        final var bedarfe = new ArrayList<InfrastrukturbedarfProJahrModel>();
        var bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr("2000");
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(10));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(10));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(10));
        bedarfe.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr("2001");
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(20));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(20));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(20));
        bedarfe.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr("2003");
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(30));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(30));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(30));
        bedarfe.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr("2004");
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(40));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(40));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(40));
        bedarfe.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr("2005");
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(50));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(50));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(50));
        bedarfe.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr("2006");
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(60));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(60));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(60));
        bedarfe.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr("2007");
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(70));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(70));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(70));
        bedarfe.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr("2008");
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(80));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(80));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(80));
        bedarfe.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr("2009");
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(90));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(90));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(90));
        bedarfe.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr("2010");
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(100));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(100));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(100));
        bedarfe.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr("2011");
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(110));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(110));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(110));
        bedarfe.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr("2012");
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(120));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(120));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(120));
        bedarfe.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr("2013");
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(130));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(130));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(130));
        bedarfe.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr("2014");
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(140));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(140));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(140));
        bedarfe.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr("2015");
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(150));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(150));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(150));
        bedarfe.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr("2016");
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(160));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(160));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(160));
        bedarfe.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr("2017");
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(170));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(170));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(170));
        bedarfe.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr("2018");
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(180));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(180));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(180));
        bedarfe.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr("2019");
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(190));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(190));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(190));
        bedarfe.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr("2020");
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(200));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(200));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(200));
        bedarfe.add(bedarf);

        final var result = infrastrukturbedarfService.calculate10Year15YearAnd20YearMeanInfrastrukturbedarfe(bedarfe);

        final var expected = new ArrayList<InfrastrukturbedarfProJahrModel>();
        final var meanYear10 = new InfrastrukturbedarfProJahrModel();
        meanYear10.setJahr(InfrastrukturbedarfService.TITLE_MEAN_YEAR_10);
        meanYear10.setAnzahlPersonenGesamt(BigDecimal.valueOf(5500, 2));
        meanYear10.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(5500, 2));
        meanYear10.setAnzahlGruppen(BigDecimal.valueOf(5500, 2));
        expected.add(meanYear10);
        final var meanYear15 = new InfrastrukturbedarfProJahrModel();
        meanYear15.setJahr(InfrastrukturbedarfService.TITLE_MEAN_YEAR_15);
        meanYear15.setAnzahlPersonenGesamt(BigDecimal.valueOf(8000, 2));
        meanYear15.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(8000, 2));
        meanYear15.setAnzahlGruppen(BigDecimal.valueOf(8000, 2));
        expected.add(meanYear15);
        final var meanYear20 = new InfrastrukturbedarfProJahrModel();
        meanYear20.setJahr(InfrastrukturbedarfService.TITLE_MEAN_YEAR_20);
        meanYear20.setAnzahlPersonenGesamt(BigDecimal.valueOf(10500, 2));
        meanYear20.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(10500, 2));
        meanYear20.setAnzahlGruppen(BigDecimal.valueOf(10500, 2));
        expected.add(meanYear20);

        assertThat(result, is(expected));
    }

    @Test
    void calculate10Year15YearAnd20YearMeanForPersonen() {
        final var personen = new ArrayList<PersonenProJahrModel>();
        var person = new PersonenProJahrModel();
        person.setJahr("2000");
        person.setAnzahlPersonenGesamt(BigDecimal.valueOf(10));
        personen.add(person);
        person = new PersonenProJahrModel();
        person.setJahr("2001");
        person.setAnzahlPersonenGesamt(BigDecimal.valueOf(20));
        personen.add(person);
        person = new PersonenProJahrModel();
        person.setJahr("2003");
        person.setAnzahlPersonenGesamt(BigDecimal.valueOf(30));
        personen.add(person);
        person = new PersonenProJahrModel();
        person.setJahr("2004");
        person.setAnzahlPersonenGesamt(BigDecimal.valueOf(40));
        personen.add(person);
        person = new PersonenProJahrModel();
        person.setJahr("2005");
        person.setAnzahlPersonenGesamt(BigDecimal.valueOf(50));
        personen.add(person);
        person = new PersonenProJahrModel();
        person.setJahr("2006");
        person.setAnzahlPersonenGesamt(BigDecimal.valueOf(60));
        personen.add(person);
        person = new PersonenProJahrModel();
        person.setJahr("2007");
        person.setAnzahlPersonenGesamt(BigDecimal.valueOf(70));
        personen.add(person);
        person = new PersonenProJahrModel();
        person.setJahr("2008");
        person.setAnzahlPersonenGesamt(BigDecimal.valueOf(80));
        personen.add(person);
        person = new PersonenProJahrModel();
        person.setJahr("2009");
        person.setAnzahlPersonenGesamt(BigDecimal.valueOf(90));
        personen.add(person);
        person = new PersonenProJahrModel();
        person.setJahr("2010");
        person.setAnzahlPersonenGesamt(BigDecimal.valueOf(100));
        personen.add(person);
        person = new PersonenProJahrModel();
        person.setJahr("2011");
        person.setAnzahlPersonenGesamt(BigDecimal.valueOf(110));
        personen.add(person);
        person = new PersonenProJahrModel();
        person.setJahr("2012");
        person.setAnzahlPersonenGesamt(BigDecimal.valueOf(120));
        personen.add(person);
        person = new PersonenProJahrModel();
        person.setJahr("2013");
        person.setAnzahlPersonenGesamt(BigDecimal.valueOf(130));
        personen.add(person);
        person = new PersonenProJahrModel();
        person.setJahr("2014");
        person.setAnzahlPersonenGesamt(BigDecimal.valueOf(140));
        personen.add(person);
        person = new PersonenProJahrModel();
        person.setJahr("2015");
        person.setAnzahlPersonenGesamt(BigDecimal.valueOf(150));
        personen.add(person);
        person = new PersonenProJahrModel();
        person.setJahr("2016");
        person.setAnzahlPersonenGesamt(BigDecimal.valueOf(160));
        personen.add(person);
        person = new PersonenProJahrModel();
        person.setJahr("2017");
        person.setAnzahlPersonenGesamt(BigDecimal.valueOf(170));
        personen.add(person);
        person = new PersonenProJahrModel();
        person.setJahr("2018");
        person.setAnzahlPersonenGesamt(BigDecimal.valueOf(180));
        personen.add(person);
        person = new PersonenProJahrModel();
        person.setJahr("2019");
        person.setAnzahlPersonenGesamt(BigDecimal.valueOf(190));
        personen.add(person);
        person = new PersonenProJahrModel();
        person.setJahr("2020");
        person.setAnzahlPersonenGesamt(BigDecimal.valueOf(200));
        personen.add(person);

        final var result = infrastrukturbedarfService.calculate10Year15YearAnd20YearMeanForPersonen(personen);

        final var expected = new ArrayList<PersonenProJahrModel>();
        final var meanYear10 = new PersonenProJahrModel();
        meanYear10.setJahr(InfrastrukturbedarfService.TITLE_MEAN_YEAR_10);
        meanYear10.setAnzahlPersonenGesamt(BigDecimal.valueOf(5500, 2));
        expected.add(meanYear10);
        final var meanYear15 = new PersonenProJahrModel();
        meanYear15.setJahr(InfrastrukturbedarfService.TITLE_MEAN_YEAR_15);
        meanYear15.setAnzahlPersonenGesamt(BigDecimal.valueOf(8000, 2));
        expected.add(meanYear15);
        final var meanYear20 = new PersonenProJahrModel();
        meanYear20.setJahr(InfrastrukturbedarfService.TITLE_MEAN_YEAR_20);
        meanYear20.setAnzahlPersonenGesamt(BigDecimal.valueOf(10500, 2));
        expected.add(meanYear20);

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
