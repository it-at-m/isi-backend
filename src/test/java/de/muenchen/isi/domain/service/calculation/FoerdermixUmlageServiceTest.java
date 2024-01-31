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
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.model.FoerderartModel;
import de.muenchen.isi.domain.model.FoerdermixModel;
import de.muenchen.isi.infrastructure.entity.Foerderart;
import de.muenchen.isi.infrastructure.entity.stammdaten.UmlegungFoerderarten;
import de.muenchen.isi.infrastructure.entity.stammdaten.Umlegungsschluessel;
import de.muenchen.isi.infrastructure.repository.stammdaten.UmlegungFoerderartenRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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
        PMB25.setAnteilProzent(new BigDecimal("25.00"));

        final var KMB25 = new FoerderartModel();
        KMB25.setBezeichnung(KMB);
        KMB25.setAnteilProzent(new BigDecimal("25.00"));

        final var BAU50 = new FoerderartModel();
        BAU50.setBezeichnung(BAU);
        BAU50.setAnteilProzent(new BigDecimal("50.00"));

        final var FF125 = new FoerderartModel();
        FF125.setBezeichnung(FF);
        FF125.setAnteilProzent(new BigDecimal("12.5000"));

        final var EOF375 = new FoerderartModel();
        EOF375.setBezeichnung(EOF);
        EOF375.setAnteilProzent(new BigDecimal("37.5000"));

        final var MM25 = new FoerderartModel();
        MM25.setBezeichnung(MM);
        MM25.setAnteilProzent(new BigDecimal("25.0000"));

        final var FH25 = new FoerderartModel();
        FH25.setBezeichnung(FH);
        FH25.setAnteilProzent(new BigDecimal("25.0000"));

        final var foerdermix = new FoerdermixModel();
        foerdermix.setFoerderarten(List.of(PMB25, KMB25, BAU50));

        final var expected = new FoerdermixModel();
        expected.setFoerderarten(List.of(FF125, EOF375, MM25, FH25));

        final var actual = foerdermixUmlageService.legeFoerdermixUm(foerdermix, LocalDate.EPOCH);
        assertThat(actual.getFoerderarten(), containsInAnyOrder(expected.getFoerderarten().toArray()));
    }

    @Test
    void mergeFoerderartTest() {
        final var bezeichnung1 = FF;
        final var bezeichnung2 = EOF;
        final var anteil1 = new BigDecimal("25.00");
        final var anteil2 = new BigDecimal("30.00");
        final var anteil3 = new BigDecimal("45.00");

        final var foerderart1 = new FoerderartModel();
        foerderart1.setBezeichnung(bezeichnung1);
        foerderart1.setAnteilProzent(anteil1);
        final var foerderarten = new ArrayList<FoerderartModel>();
        foerderarten.add(foerderart1);

        foerdermixUmlageService.mergeFoerderart(foerderarten, bezeichnung2, anteil2);
        assertThat(foerderarten.size(), is(2));
        assertThat(foerderarten.get(0).getBezeichnung(), is(bezeichnung1));
        assertThat(foerderarten.get(0).getAnteilProzent(), is(anteil1));
        assertThat(foerderarten.get(1).getBezeichnung(), is(bezeichnung2));
        assertThat(foerderarten.get(1).getAnteilProzent(), is(anteil2));

        foerdermixUmlageService.mergeFoerderart(foerderarten, bezeichnung2, anteil3);
        assertThat(foerderarten.size(), is(2));
        assertThat(foerderarten.get(0).getBezeichnung(), is(bezeichnung1));
        assertThat(foerderarten.get(0).getAnteilProzent(), is(anteil1));
        assertThat(foerderarten.get(1).getBezeichnung(), is(bezeichnung2));
        assertThat(foerderarten.get(1).getAnteilProzent(), is(anteil2.add(anteil3)));
    }

    public static void fillUmlegungFoerderartenRepository(UmlegungFoerderartenRepository repository) {
        final var FF25 = new Foerderart();
        FF25.setBezeichnung(FF);
        FF25.setAnteilProzent(new BigDecimal("25"));

        final var EOF75 = new Foerderart();
        EOF75.setBezeichnung(EOF);
        EOF75.setAnteilProzent(new BigDecimal("75"));

        final var FH50 = new Foerderart();
        FH50.setBezeichnung(FH);
        FH50.setAnteilProzent(new BigDecimal("50"));

        final var MM50 = new Foerderart();
        MM50.setBezeichnung(MM);
        MM50.setAnteilProzent(new BigDecimal("50"));

        final var umlegungPMB = new UmlegungFoerderarten();
        umlegungPMB.setBezeichnung(PMB);
        umlegungPMB.setGueltigAb(LocalDate.EPOCH);
        final var umlegungsschluesselPMB = new Umlegungsschluessel();
        umlegungsschluesselPMB.setFoerderarten(Set.of(FF25, EOF75));
        umlegungPMB.setUmlegungsschluessel(umlegungsschluesselPMB);

        final var umlegungKMB = new UmlegungFoerderarten();
        umlegungKMB.setBezeichnung(KMB);
        umlegungKMB.setGueltigAb(LocalDate.EPOCH);
        final var umlegungsschluesselKMB = new Umlegungsschluessel();
        umlegungsschluesselKMB.setFoerderarten(Set.of(FF25, EOF75));
        umlegungKMB.setUmlegungsschluessel(umlegungsschluesselKMB);

        final var umlegungBAU = new UmlegungFoerderarten();
        umlegungBAU.setBezeichnung(BAU);
        umlegungBAU.setGueltigAb(LocalDate.EPOCH);
        final var umlegungsschluesselBAU = new Umlegungsschluessel();
        umlegungsschluesselBAU.setFoerderarten(Set.of(FH50, MM50));
        umlegungBAU.setUmlegungsschluessel(umlegungsschluesselBAU);

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
