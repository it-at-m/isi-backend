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

import de.muenchen.isi.domain.model.FoerderartModel;
import de.muenchen.isi.domain.model.FoerdermixModel;
import de.muenchen.isi.infrastructure.entity.Foerderart;
import de.muenchen.isi.infrastructure.entity.stammdaten.UmlegungFoerderarten;
import de.muenchen.isi.infrastructure.repository.stammdaten.UmlegungFoerderartenRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
public class FoerdermixUmlageServiceTest {

    private FoerdermixUmlageService foerdermixUmlageService;

    @Mock
    private UmlegungFoerderartenRepository umlegungFoerderartenRepository;

    @BeforeEach
    public void beforeEach() {
        this.foerdermixUmlageService = new FoerdermixUmlageService(umlegungFoerderartenRepository);
        Mockito.reset(umlegungFoerderartenRepository);
    }

    @Test
    void legeFoerdermixUmTest() {
        fillUmlegungFoerderartenRepository(umlegungFoerderartenRepository);

        final var PMB25 = new FoerderartModel();
        PMB25.setBezeichnung(PMB);
        PMB25.setAnteilProzent(new BigDecimal("0.25"));

        final var KMB25 = new FoerderartModel();
        KMB25.setBezeichnung(KMB);
        KMB25.setAnteilProzent(new BigDecimal("0.25"));

        final var BAU50 = new FoerderartModel();
        BAU50.setBezeichnung(BAU);
        BAU50.setAnteilProzent(new BigDecimal("0.5"));

        final var FF125 = new FoerderartModel();
        FF125.setBezeichnung(FF);
        FF125.setAnteilProzent(new BigDecimal("0.1250"));

        final var EOF375 = new FoerderartModel();
        EOF375.setBezeichnung(EOF);
        EOF375.setAnteilProzent(new BigDecimal("0.3750"));

        final var MM25 = new FoerderartModel();
        MM25.setBezeichnung(MM);
        MM25.setAnteilProzent(new BigDecimal("0.250"));

        final var FH25 = new FoerderartModel();
        FH25.setBezeichnung(FH);
        FH25.setAnteilProzent(new BigDecimal("0.250"));

        final var foerdermix = new FoerdermixModel();
        foerdermix.setFoerderarten(List.of(PMB25, KMB25, BAU50));

        final var expected = new FoerdermixModel();
        expected.setFoerderarten(List.of(FF125, EOF375, MM25, FH25));

        final var actual = foerdermixUmlageService.legeFoerdermixUm(foerdermix, LocalDate.EPOCH);
        assertThat(actual.getFoerderarten(), containsInAnyOrder(expected.getFoerderarten().toArray()));
    }

    public static void fillUmlegungFoerderartenRepository(UmlegungFoerderartenRepository repository) {
        final var FF25 = new Foerderart();
        FF25.setBezeichnung(FF);
        FF25.setAnteilProzent(new BigDecimal("0.25"));

        final var EOF75 = new Foerderart();
        EOF75.setBezeichnung(EOF);
        EOF75.setAnteilProzent(new BigDecimal("0.75"));

        final var FH50 = new Foerderart();
        FH50.setBezeichnung(FH);
        FH50.setAnteilProzent(new BigDecimal("0.50"));

        final var MM50 = new Foerderart();
        MM50.setBezeichnung(MM);
        MM50.setAnteilProzent(new BigDecimal("0.50"));

        final var umlegungPMB = new UmlegungFoerderarten();
        umlegungPMB.setBezeichnung(PMB);
        umlegungPMB.setGueltigAb(LocalDate.EPOCH);
        umlegungPMB.setUmlegungsschluessel(Set.of(FF25, EOF75));

        final var umlegungKMB = new UmlegungFoerderarten();
        umlegungKMB.setBezeichnung(KMB);
        umlegungKMB.setGueltigAb(LocalDate.EPOCH);
        umlegungKMB.setUmlegungsschluessel(Set.of(FF25, EOF75));

        final var umlegungBAU = new UmlegungFoerderarten();
        umlegungBAU.setBezeichnung(BAU);
        umlegungBAU.setGueltigAb(LocalDate.EPOCH);
        umlegungBAU.setUmlegungsschluessel(Set.of(FH50, MM50));

        Mockito
            .when(
                repository.findFirstByBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(PMB, LocalDate.EPOCH)
            )
            .thenReturn(Optional.of(umlegungPMB));
        Mockito
            .when(
                repository.findFirstByBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(KMB, LocalDate.EPOCH)
            )
            .thenReturn(Optional.of(umlegungKMB));
        Mockito
            .when(
                repository.findFirstByBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(BAU, LocalDate.EPOCH)
            )
            .thenReturn(Optional.of(umlegungBAU));
    }
}
