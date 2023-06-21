package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.BaurateModel;
import de.muenchen.isi.infrastructure.entity.enums.IdealtypischeBaurateTyp;
import de.muenchen.isi.infrastructure.entity.stammdaten.baurate.IdealtypischeBaurate;
import de.muenchen.isi.infrastructure.entity.stammdaten.baurate.Jahresrate;
import de.muenchen.isi.infrastructure.repository.stammdaten.IdealtypischeBaurateRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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
        idealtypischeBaurate.setTyp(IdealtypischeBaurateTyp.ANZAHL_WOHNEINHEITEN_GESAMT);
        idealtypischeBaurate.setVon(BigDecimal.valueOf(100L));
        idealtypischeBaurate.setBisExklusiv(BigDecimal.valueOf(200L));

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

        // Mit Wohneinheiten
        Mockito
            .when(
                this.idealtypischeBaurateRepository.findByTypAndVonLessThanEqualAndBisExklusivGreaterThan(
                        IdealtypischeBaurateTyp.ANZAHL_WOHNEINHEITEN_GESAMT,
                        BigDecimal.valueOf(132L)
                    )
            )
            .thenReturn(Optional.of(idealtypischeBaurate));

        var result = this.baurateService.determineBauraten(2000, 132L, BigDecimal.valueOf(1320.53));

        var baurate1 = new BaurateModel();
        baurate1.setJahr(2000);
        baurate1.setAnzahlWeGeplant(13);
        baurate1.setGeschossflaecheWohnenGeplant(null);
        var baurate2 = new BaurateModel();
        baurate2.setJahr(2001);
        baurate2.setAnzahlWeGeplant(40);
        baurate2.setGeschossflaecheWohnenGeplant(null);
        var baurate3 = new BaurateModel();
        baurate3.setJahr(2002);
        baurate3.setAnzahlWeGeplant(79);
        baurate3.setGeschossflaecheWohnenGeplant(null);

        var expected = List.of(baurate1, baurate2, baurate3);

        assertThat(result, is(expected));
        Mockito.reset(this.idealtypischeBaurateRepository);

        // Ohne Wohneinheiten
        idealtypischeBaurate.setTyp(IdealtypischeBaurateTyp.GESCHOSSFLAECHE_WOHNEN_GESAMT);
        Mockito
            .when(
                this.idealtypischeBaurateRepository.findByTypAndVonLessThanEqualAndBisExklusivGreaterThan(
                        IdealtypischeBaurateTyp.GESCHOSSFLAECHE_WOHNEN_GESAMT,
                        BigDecimal.valueOf(1320.53)
                    )
            )
            .thenReturn(Optional.of(idealtypischeBaurate));

        result = this.baurateService.determineBauraten(2000, null, BigDecimal.valueOf(1320.53));

        baurate1 = new BaurateModel();
        baurate1.setJahr(2000);
        baurate1.setAnzahlWeGeplant(null);
        baurate1.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(138L));
        baurate2 = new BaurateModel();
        baurate2.setJahr(2001);
        baurate2.setAnzahlWeGeplant(null);
        baurate2.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(402L));
        baurate3 = new BaurateModel();
        baurate3.setJahr(2002);
        baurate3.setAnzahlWeGeplant(null);
        baurate3.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(780.53));

        expected = List.of(baurate1, baurate2, baurate3);

        assertThat(result, is(expected));
        Mockito.reset(this.idealtypischeBaurateRepository);

        // Keine idealtypische Bauraten für Wohneinheiten
        Mockito
            .when(
                this.idealtypischeBaurateRepository.findByTypAndVonLessThanEqualAndBisExklusivGreaterThan(
                        IdealtypischeBaurateTyp.ANZAHL_WOHNEINHEITEN_GESAMT,
                        BigDecimal.valueOf(100L)
                    )
            )
            .thenReturn(Optional.empty());
        EntityNotFoundException resultingException = Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.baurateService.determineBauraten(2000, 100L, null)
        );
        assertThat(
            resultingException.getMessage(),
            is("Für den Wert von 100 des Typs Wohneinheiten konnte keine idealtypische Baurate ermittelt werden.")
        );
        Mockito
            .verify(this.idealtypischeBaurateRepository, Mockito.times(1))
            .findByTypAndVonLessThanEqualAndBisExklusivGreaterThan(
                IdealtypischeBaurateTyp.ANZAHL_WOHNEINHEITEN_GESAMT,
                BigDecimal.valueOf(100L)
            );

        // Keine idealtypische Bauraten für Geschossfläche Wohnen
        Mockito
            .when(
                this.idealtypischeBaurateRepository.findByTypAndVonLessThanEqualAndBisExklusivGreaterThan(
                        IdealtypischeBaurateTyp.GESCHOSSFLAECHE_WOHNEN_GESAMT,
                        BigDecimal.TEN
                    )
            )
            .thenReturn(Optional.empty());
        resultingException =
            Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> this.baurateService.determineBauraten(2000, null, BigDecimal.TEN)
            );
        assertThat(
            resultingException.getMessage(),
            is("Für den Wert von 10 des Typs Geschoßfläche Wohnen konnte keine idealtypische Baurate ermittelt werden.")
        );
        Mockito
            .verify(this.idealtypischeBaurateRepository, Mockito.times(1))
            .findByTypAndVonLessThanEqualAndBisExklusivGreaterThan(
                IdealtypischeBaurateTyp.GESCHOSSFLAECHE_WOHNEN_GESAMT,
                BigDecimal.TEN
            );

        // Ohne Wohneinheiten und ohne Geschossfläche Wohnen
        resultingException =
            Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> this.baurateService.determineBauraten(2000, null, null)
            );
        assertThat(
            resultingException.getMessage(),
            is(
                "Es konnten keine idealtypischen Bauraten für Wohneinheiten oder für Geschoßfläche Wohnen ermittelt werden."
            )
        );
    }

    @Test
    void determineIdealtypischeBaurate() throws EntityNotFoundException {
        final var idealtypischeBaurate = new IdealtypischeBaurate();

        Mockito
            .when(
                this.idealtypischeBaurateRepository.findByTypAndVonLessThanEqualAndBisExklusivGreaterThan(
                        IdealtypischeBaurateTyp.ANZAHL_WOHNEINHEITEN_GESAMT,
                        BigDecimal.valueOf(132L)
                    )
            )
            .thenReturn(Optional.of(idealtypischeBaurate));

        var result = this.baurateService.determineIdealtypischeBaurate(132L, BigDecimal.valueOf(1320.53));
        assertThat(result, is(idealtypischeBaurate));
        Mockito
            .verify(this.idealtypischeBaurateRepository, Mockito.times(1))
            .findByTypAndVonLessThanEqualAndBisExklusivGreaterThan(
                IdealtypischeBaurateTyp.ANZAHL_WOHNEINHEITEN_GESAMT,
                BigDecimal.valueOf(132L)
            );
        Mockito.reset(this.idealtypischeBaurateRepository);

        Mockito
            .when(
                this.idealtypischeBaurateRepository.findByTypAndVonLessThanEqualAndBisExklusivGreaterThan(
                        IdealtypischeBaurateTyp.GESCHOSSFLAECHE_WOHNEN_GESAMT,
                        BigDecimal.valueOf(1320.53)
                    )
            )
            .thenReturn(Optional.of(idealtypischeBaurate));

        result = this.baurateService.determineIdealtypischeBaurate(null, BigDecimal.valueOf(1320.53));
        assertThat(result, is(idealtypischeBaurate));
        Mockito
            .verify(this.idealtypischeBaurateRepository, Mockito.times(1))
            .findByTypAndVonLessThanEqualAndBisExklusivGreaterThan(
                IdealtypischeBaurateTyp.GESCHOSSFLAECHE_WOHNEN_GESAMT,
                BigDecimal.valueOf(1320.53)
            );
        Mockito.reset(this.idealtypischeBaurateRepository);

        Mockito
            .when(
                this.idealtypischeBaurateRepository.findByTypAndVonLessThanEqualAndBisExklusivGreaterThan(
                        IdealtypischeBaurateTyp.GESCHOSSFLAECHE_WOHNEN_GESAMT,
                        null
                    )
            )
            .thenReturn(Optional.empty());
        final EntityNotFoundException resultingException = Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.baurateService.determineIdealtypischeBaurate(null, null)
        );
        assertThat(
            resultingException.getMessage(),
            is(
                "Es konnten keine idealtypischen Bauraten für Wohneinheiten oder für Geschoßfläche Wohnen ermittelt werden."
            )
        );
    }

    @Test
    void determineIdealtypischeBaurateForWertAndTyp() {
        EntityNotFoundException resultingException = Assertions.assertThrows(
            EntityNotFoundException.class,
            () ->
                this.baurateService.determineIdealtypischeBaurateForWertAndTyp(
                        BigDecimal.valueOf(100),
                        IdealtypischeBaurateTyp.ANZAHL_WOHNEINHEITEN_GESAMT
                    )
        );
        assertThat(
            resultingException.getMessage(),
            is("Für den Wert von 100 des Typs Wohneinheiten konnte keine idealtypische Baurate ermittelt werden.")
        );
        Mockito
            .verify(this.idealtypischeBaurateRepository, Mockito.times(1))
            .findByTypAndVonLessThanEqualAndBisExklusivGreaterThan(
                IdealtypischeBaurateTyp.ANZAHL_WOHNEINHEITEN_GESAMT,
                BigDecimal.valueOf(100L)
            );
    }

    @Test
    void calculateRoundedDownRatenwertForGesamtwertAndRate() {
        var result = baurateService.calculateRoundedDownRatenwertForGesamtwertAndRate(
            BigDecimal.TEN,
            BigDecimal.valueOf(0.053)
        );
        assertThat(result, is(BigDecimal.valueOf(0)));
        result =
            baurateService.calculateRoundedDownRatenwertForGesamtwertAndRate(BigDecimal.TEN, BigDecimal.valueOf(0.153));
        assertThat(result, is(BigDecimal.valueOf(1)));
        result =
            baurateService.calculateRoundedDownRatenwertForGesamtwertAndRate(BigDecimal.TEN, BigDecimal.valueOf(0.353));
        assertThat(result, is(BigDecimal.valueOf(3)));
        result =
            baurateService.calculateRoundedDownRatenwertForGesamtwertAndRate(BigDecimal.TEN, BigDecimal.valueOf(0.453));
        assertThat(result, is(BigDecimal.valueOf(4)));
        result =
            baurateService.calculateRoundedDownRatenwertForGesamtwertAndRate(BigDecimal.TEN, BigDecimal.valueOf(0.553));
        assertThat(result, is(BigDecimal.valueOf(5)));
        result =
            baurateService.calculateRoundedDownRatenwertForGesamtwertAndRate(BigDecimal.TEN, BigDecimal.valueOf(0.953));
        assertThat(result, is(BigDecimal.valueOf(9)));
        result =
            baurateService.calculateRoundedDownRatenwertForGesamtwertAndRate(BigDecimal.TEN, BigDecimal.valueOf(1));
        assertThat(result, is(BigDecimal.TEN));
    }
}
