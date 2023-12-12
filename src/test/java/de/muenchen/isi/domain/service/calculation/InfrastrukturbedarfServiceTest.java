package de.muenchen.isi.domain.service.calculation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

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
    void calculateBedarfForKinderkrippeRounded() throws CalculationException {
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

        final var result = infrastrukturbedarfService.calculateBedarfForKinderkrippeRounded(
            wohneinheiten,
            sobonJahr,
            InfrastrukturbedarfService.ArtInfrastrukturbedarf.PLANUNGSURSAECHLICH,
            gueltigAb
        );

        final var expected = new ArrayList<InfrastrukturbedarfProJahrModel>();
        var bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2000));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(52901));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(31740));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(264504, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2001));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(85809));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(51485));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(429046, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2002));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(99000));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(59400));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(495000, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2003));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(97557));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(58534));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(487786, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2004));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(96115));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(57669));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(480573, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2005));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(94672));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(56803));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(473359, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2006));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(93229));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(55937));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(466145, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2007));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(91786));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(55072));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(458931, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2008));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(90344));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(54206));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(451718, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2009));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(88901));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(53340));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(444504, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2010));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(87753));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(52652));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(438764, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2011));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(86797));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(52078));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(433985, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2012));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(85929));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(51557));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(429645, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2013));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(85070));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(51042));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(425348, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2014));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(84219));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(50531));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(421095, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2015));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(83377));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(50026));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(416884, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2016));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(82543));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(49526));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(412715, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2017));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(81718));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(49031));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(408588, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2018));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(80900));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(48540));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(404502, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2019));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(80091));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(48055));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(400457, 2));
        expected.add(bedarf);

        assertThat(result, is(expected));
    }

    @Test
    void calculateBedarfForKindergartenRounded() throws CalculationException {
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

        final var result = infrastrukturbedarfService.calculateBedarfForKindergartenRounded(
            wohneinheiten,
            sobonJahr,
            InfrastrukturbedarfService.ArtInfrastrukturbedarf.PLANUNGSURSAECHLICH,
            gueltigAb
        );

        final var expected = new ArrayList<InfrastrukturbedarfProJahrModel>();
        var bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2000));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(52901));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(31740));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(264504, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2001));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(85809));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(51485));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(429046, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2002));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(99000));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(59400));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(495000, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2003));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(97557));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(58534));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(487786, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2004));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(96115));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(57669));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(480573, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2005));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(94672));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(56803));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(473359, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2006));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(93229));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(55937));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(466145, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2007));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(91786));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(55072));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(458931, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2008));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(90344));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(54206));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(451718, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2009));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(88901));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(53340));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(444504, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2010));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(87753));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(52652));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(438764, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2011));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(86797));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(52078));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(433985, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2012));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(85929));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(51557));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(429645, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2013));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(85070));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(51042));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(425348, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2014));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(84219));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(50531));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(421095, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2015));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(83377));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(50026));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(416884, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2016));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(82543));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(49526));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(412715, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2017));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(81718));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(49031));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(408588, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2018));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(80900));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(48540));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(404502, 2));
        expected.add(bedarf);
        bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(2019));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(80091));
        bedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(48055));
        bedarf.setAnzahlGruppen(BigDecimal.valueOf(400457, 2));
        expected.add(bedarf);

        assertThat(result, is(expected));
    }

    @Test
    void calculateAlleEinwohnerRounded() throws CalculationException {
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

        final var result = infrastrukturbedarfService.calculateAlleEinwohnerRounded(wohneinheiten, sobonJahr);

        final var expected = new ArrayList<PersonenProJahrModel>();
        var bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2000));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(52901));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2001));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(85809));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2002));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(99000));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2003));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(97557));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2004));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(96115));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2005));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(94672));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2006));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(93229));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2007));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(91786));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2008));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(90344));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2009));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(88901));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2010));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(87753));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2011));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(86797));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2012));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(85929));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2013));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(85070));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2014));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(84219));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2015));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(83377));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2016));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(82543));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2017));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(81718));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2018));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(80900));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2019));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(80091));
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
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(529007633550000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2001));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(858091603000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2002));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(989999999950000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2003));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(975572519050000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2004));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(961145038150000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2005));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(946717557250000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2006));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(932290076350000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2007));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(917862595450000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2008));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(903435114550000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2009));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(889007633650000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2010));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(877527480980000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2011));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(867969000060000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2012));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(859289310040000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2013));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(850696417000000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2014));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(842189452860000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2015));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(833767558290000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2016));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(825429882650000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2017));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(817175583860000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2018));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(809003828050000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2019));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(800913789720000L, 10));
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
        expected.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(600000000000L, 10));
        expected.setAnzahlGruppen(BigDecimal.valueOf(60000000000L, 10));
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
        expected.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(600000000000L, 10));
        expected.setAnzahlGruppen(BigDecimal.valueOf(60000000000L, 10));
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
        expected.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(423400000000L, 10));
        expected.setAnzahlGruppen(BigDecimal.valueOf(42340000000L, 10));
        assertThat(result, is(expected));

        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(73));
        versorgungsQuote.setVersorgungsquotePlanungsursaechlich(null);
        versorgungsQuote.setVersorgungsquoteSobonUrsaechlich(BigDecimal.valueOf(5800000000L, 10));
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
        expected.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(423400000000L, 10));
        expected.setAnzahlGruppen(BigDecimal.valueOf(42340000000L, 10));
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
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(48091603050000L, 10));
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
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(48091603050000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2009));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(47404580150000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2010));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(46717557250000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2011));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(46030534350000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2012));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(45343511450000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2013));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(44656488550000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2014));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(43969465650000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2015));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(43282442750000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2016));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(42595419850000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2017));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(41908396950000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2018));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(41489312980000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2019));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(41074419850000L, 10));
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
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(48091603050000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2001));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(47404580150000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2002));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(46717557250000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2003));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(46030534350000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2004));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(45343511450000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2005));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(44656488550000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2006));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(43969465650000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2007));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(43282442750000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2008));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(42595419850000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2009));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(41908396950000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2010));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(41489312980000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2011));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(41074419850000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2012));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(40663675650000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2013));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(40257038900000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2014));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(39854468510000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2015));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(39455923820000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2016));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(39061364580000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2017));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(38670750940000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2018));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(38284043430000L, 10));
        expected.add(bedarf);
        bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(2019));
        bedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(37901202990000L, 10));
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
    void calculateMeanInfrastrukturbedarfe() {
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

        var result = infrastrukturbedarfService.calculateMeanInfrastrukturbedarfe(bedarfe, 10);
        var expected = new InfrastrukturbedarfProJahrModel();
        expected.setJahr("Mittelwert 10 J.");
        expected.setAnzahlPersonenGesamt(BigDecimal.valueOf(5500, 2));
        expected.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(5500, 2));
        expected.setAnzahlGruppen(BigDecimal.valueOf(5500, 2));
        assertThat(result, is(expected));

        result = infrastrukturbedarfService.calculateMeanInfrastrukturbedarfe(bedarfe, 15);
        expected = new InfrastrukturbedarfProJahrModel();
        expected.setJahr("Mittelwert 15 J.");
        expected.setAnzahlPersonenGesamt(BigDecimal.valueOf(8000, 2));
        expected.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(8000, 2));
        expected.setAnzahlGruppen(BigDecimal.valueOf(8000, 2));
        assertThat(result, is(expected));

        result = infrastrukturbedarfService.calculateMeanInfrastrukturbedarfe(bedarfe, 20);
        expected = new InfrastrukturbedarfProJahrModel();
        expected.setJahr("Mittelwert 20 J.");
        expected.setAnzahlPersonenGesamt(BigDecimal.valueOf(10500, 2));
        expected.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(10500, 2));
        expected.setAnzahlGruppen(BigDecimal.valueOf(10500, 2));
        assertThat(result, is(expected));

        result = infrastrukturbedarfService.calculateMeanInfrastrukturbedarfe(bedarfe, 21);
        assertThat(result, is(nullValue()));

        result = infrastrukturbedarfService.calculateMeanInfrastrukturbedarfe(List.of(), 20);
        assertThat(result, is(nullValue()));
    }

    @Test
    void calculateMeanPersonen() {
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

        var result = infrastrukturbedarfService.calculateMeanPersonen(personen, 10);
        var expected = new PersonenProJahrModel();
        expected.setJahr("Mittelwert 10 J.");
        expected.setAnzahlPersonenGesamt(BigDecimal.valueOf(5500, 2));
        assertThat(result, is(expected));

        result = infrastrukturbedarfService.calculateMeanPersonen(personen, 15);
        expected = new PersonenProJahrModel();
        expected.setJahr("Mittelwert 15 J.");
        expected.setAnzahlPersonenGesamt(BigDecimal.valueOf(8000, 2));
        assertThat(result, is(expected));

        result = infrastrukturbedarfService.calculateMeanPersonen(personen, 20);
        expected = new PersonenProJahrModel();
        expected.setJahr("Mittelwert 20 J.");
        expected.setAnzahlPersonenGesamt(BigDecimal.valueOf(10500, 2));
        assertThat(result, is(expected));

        result = infrastrukturbedarfService.calculateMeanPersonen(personen, 21);
        assertThat(result, is(nullValue()));

        result = infrastrukturbedarfService.calculateMeanPersonen(List.of(), 20);
        assertThat(result, is(nullValue()));
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
