package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.BaurateModel;
import de.muenchen.isi.infrastructure.entity.stammdaten.baurate.IdealtypischeBaurate;
import de.muenchen.isi.infrastructure.entity.stammdaten.baurate.Jahresrate;
import de.muenchen.isi.infrastructure.repository.stammdaten.IdealtypischeBaurateRepository;
import java.math.BigDecimal;
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
class BaurateServiceTest {

    @Mock
    private IdealtypischeBaurateRepository idealtypischeBaurateRepository;

    private BaurateService baurateService;

    @BeforeEach
    public void beforeEach() {
        this.baurateService = new BaurateService(this.idealtypischeBaurateRepository);
        Mockito.reset(this.idealtypischeBaurateRepository);
    }

    @Test
    void determineBauraten() throws EntityNotFoundException {
        final var idealtypischeBaurate = new IdealtypischeBaurate();
        idealtypischeBaurate.setWohneinheitenVon(100L);
        idealtypischeBaurate.setWohneinheitenBisEinschliesslich(200L);
        idealtypischeBaurate.setGeschossflaecheWohnenVon(BigDecimal.valueOf(100));
        idealtypischeBaurate.setGeschossflaecheWohnenBisEinschliesslich(BigDecimal.valueOf(100));

        final var jahresrate1 = new Jahresrate();
        jahresrate1.setJahr(1);
        jahresrate1.setRate(BigDecimal.valueOf(0.1049));
        final var jahresrate2 = new Jahresrate();
        jahresrate2.setJahr(2);
        jahresrate2.setRate(BigDecimal.valueOf(0.305));
        final var jahresrate3 = new Jahresrate();
        jahresrate3.setJahr(3);
        jahresrate3.setRate(BigDecimal.valueOf(0.5901));
        idealtypischeBaurate.setJahresraten(List.of(jahresrate1, jahresrate2, jahresrate3));

        Mockito
            .when(
                this.idealtypischeBaurateRepository.findByWohneinheitenVonLessThanEqualAndWohneinheitenBisEinschliesslichGreaterThanEqual(
                        132L
                    )
            )
            .thenReturn(Optional.of(idealtypischeBaurate));

        final var result = baurateService.determineBauraten(2000, 132L, BigDecimal.valueOf(1320L));

        final var baurate1 = new BaurateModel();
        baurate1.setJahr(2000);
        baurate1.setAnzahlWeGeplant(13);
        baurate1.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(138L));
        final var baurate2 = new BaurateModel();
        baurate2.setJahr(2001);
        baurate2.setAnzahlWeGeplant(40);
        baurate2.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(402L));
        final var baurate3 = new BaurateModel();
        baurate3.setJahr(2002);
        baurate3.setAnzahlWeGeplant(79);
        baurate3.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(780));

        final var expected = List.of(baurate1, baurate2, baurate3);

        assertThat(result, is(expected));
    }
}
