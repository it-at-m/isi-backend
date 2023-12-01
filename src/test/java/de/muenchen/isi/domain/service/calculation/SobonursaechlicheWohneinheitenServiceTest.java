package de.muenchen.isi.domain.service.calculation;

import static de.muenchen.isi.TestConstants.BAU;
import static de.muenchen.isi.TestConstants.EOF;
import static de.muenchen.isi.TestConstants.FF;
import static de.muenchen.isi.TestConstants.FH;
import static de.muenchen.isi.TestConstants.KMB;
import static de.muenchen.isi.TestConstants.MM;
import static de.muenchen.isi.TestConstants.PMB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import de.muenchen.isi.domain.model.BaurateModel;
import de.muenchen.isi.domain.model.FoerderartModel;
import de.muenchen.isi.domain.model.FoerdermixModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenProFoerderartProJahrModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.repository.stammdaten.StaedtebaulicheOrientierungswertRepository;
import de.muenchen.isi.infrastructure.repository.stammdaten.UmlegungFoerderartenRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
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
public class SobonursaechlicheWohneinheitenServiceTest {

    private SobonursaechlicheWohneinheitenService sobonursaechlicheWohneinheitenService;

    @Mock
    private UmlegungFoerderartenRepository umlegungFoerderartenRepository;

    @Mock
    private StaedtebaulicheOrientierungswertRepository staedtebaulicheOrientierungswertRepository;

    @BeforeEach
    public void beforeEach() {
        final var foerdermixUmlageService = new FoerdermixUmlageService(umlegungFoerderartenRepository);
        this.sobonursaechlicheWohneinheitenService =
            new SobonursaechlicheWohneinheitenService(
                foerdermixUmlageService,
                staedtebaulicheOrientierungswertRepository
            );
        Mockito.reset(umlegungFoerderartenRepository, staedtebaulicheOrientierungswertRepository);
    }

    @Test
    void calculateSobonursaechlicheWohneinheitenTest() {
        FoerdermixUmlageServiceTest.fillUmlegungFoerderartenRepository(umlegungFoerderartenRepository);
        PlanungsursaechlicheWohneinheitenServiceTest.fillStaedtebaulicheOrientierungswertRepository(
            staedtebaulicheOrientierungswertRepository
        );

        // Fördermixe
        final var FF50 = new FoerderartModel();
        FF50.setBezeichnung(FF);
        FF50.setAnteilProzent(new BigDecimal("0.5"));

        final var MM30 = new FoerderartModel();
        MM30.setBezeichnung(MM);
        MM30.setAnteilProzent(new BigDecimal("0.3"));

        final var EOF20 = new FoerderartModel();
        EOF20.setBezeichnung(EOF);
        EOF20.setAnteilProzent(new BigDecimal("0.2"));

        final var meinFoerdermix = new FoerdermixModel();
        meinFoerdermix.setFoerderarten(List.of(FF50, EOF20, MM30));

        final var baurate2024 = new BaurateModel();
        baurate2024.setJahr(2024);
        baurate2024.setFoerdermix(meinFoerdermix);

        // 1. Test unter 1000 Wohneinheiten
        final var expected = List.of(
            new WohneinheitenProFoerderartProJahrModel(FF, "2024", new BigDecimal("157.8947368421")),
            new WohneinheitenProFoerderartProJahrModel(EOF, "2024", new BigDecimal("66.6666666667")),
            new WohneinheitenProFoerderartProJahrModel(MM, "2024", new BigDecimal("90.0000000000"))
        );

        final var actual = sobonursaechlicheWohneinheitenService.calculateSobonursaechlicheWohneinheiten(
            new BigDecimal(30000),
            baurate2024,
            SobonOrientierungswertJahr.JAHR_2022,
            LocalDate.of(2013, 1, 1)
        );

        System.out.println("----------------- TEST A ---------------------------------");
        for (WohneinheitenProFoerderartProJahrModel weModel : actual) {
            System.out.println("Jahr: " + weModel.getJahr()); // Jahr/Info aus Baurate
            System.out.println("Förderart: " + weModel.getFoerderart());
            System.out.println("Wohneinheiten: " + weModel.getWohneinheiten());
            System.out.println("--------------------------------------------------");
        }

        assertThat(actual, containsInAnyOrder(expected.toArray()));

        // 2. Test über 1000 Wohneinheiten mit einem 1000er Block/Jahr
        final var expectedB = List.of(
            new WohneinheitenProFoerderartProJahrModel(FF, "2024", new BigDecimal("501.9520356842")),
            new WohneinheitenProFoerderartProJahrModel(EOF, "2024", new BigDecimal("211.9353039556")),
            new WohneinheitenProFoerderartProJahrModel(MM, "2024", new BigDecimal("286.1126603400")),
            new WohneinheitenProFoerderartProJahrModel(FF, "2025", new BigDecimal("182.2584906316")),
            new WohneinheitenProFoerderartProJahrModel(EOF, "2025", new BigDecimal("76.9535849333")),
            new WohneinheitenProFoerderartProJahrModel(MM, "2025", new BigDecimal("103.8873396600"))
        );

        final var actualB = sobonursaechlicheWohneinheitenService.calculateSobonursaechlicheWohneinheiten(
            new BigDecimal(130000),
            baurate2024,
            SobonOrientierungswertJahr.JAHR_2022,
            LocalDate.of(2013, 1, 1)
        );

        System.out.println("----------------- TEST B ---------------------------------");
        for (WohneinheitenProFoerderartProJahrModel weModel : actualB) {
            System.out.println("Jahr: " + weModel.getJahr()); // Jahr/Info aus Baurate
            System.out.println("Förderart: " + weModel.getFoerderart());
            System.out.println("Wohneinheiten: " + weModel.getWohneinheiten());
            System.out.println("--------------------------------------------------");
        }

        assertThat(actualB, containsInAnyOrder(expectedB.toArray()));

        // 3. Test über 1000 Wohneinheiten mit zwei 1000er Blöcken/Jahren
        final var expectedC = List.of(
            new WohneinheitenProFoerderartProJahrModel(FF, "2024", new BigDecimal("501.9520357105")),
            new WohneinheitenProFoerderartProJahrModel(EOF, "2024", new BigDecimal("211.9353039667")),
            new WohneinheitenProFoerderartProJahrModel(MM, "2024", new BigDecimal("286.1126603550")),
            new WohneinheitenProFoerderartProJahrModel(FF, "2025", new BigDecimal("501.9520357105")),
            new WohneinheitenProFoerderartProJahrModel(EOF, "2025", new BigDecimal("211.9353039667")),
            new WohneinheitenProFoerderartProJahrModel(MM, "2025", new BigDecimal("286.1126603550")),
            new WohneinheitenProFoerderartProJahrModel(FF, "2026", new BigDecimal("206.6222443684")),
            new WohneinheitenProFoerderartProJahrModel(EOF, "2026", new BigDecimal("87.2405031778")),
            new WohneinheitenProFoerderartProJahrModel(MM, "2026", new BigDecimal("117.7746792900"))
        );

        final var actualC = sobonursaechlicheWohneinheitenService.calculateSobonursaechlicheWohneinheiten(
            new BigDecimal(230000),
            baurate2024,
            SobonOrientierungswertJahr.JAHR_2022,
            LocalDate.of(2013, 1, 1)
        );

        System.out.println("----------------- TEST C ---------------------------------");
        for (WohneinheitenProFoerderartProJahrModel weModel : actualC) {
            System.out.println("Jahr: " + weModel.getJahr()); // Jahr/Info aus Baurate
            System.out.println("Förderart: " + weModel.getFoerderart());
            System.out.println("Wohneinheiten: " + weModel.getWohneinheiten());
            System.out.println("--------------------------------------------------");
        }
        assertThat(actualC, containsInAnyOrder(expectedC.toArray()));
    }
}
