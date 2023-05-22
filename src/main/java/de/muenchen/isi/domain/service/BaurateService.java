package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.BaurateModel;
import de.muenchen.isi.infrastructure.entity.enums.IdealtypischeBaurateTyp;
import de.muenchen.isi.infrastructure.entity.stammdaten.baurate.IdealtypischeBaurate;
import de.muenchen.isi.infrastructure.repository.stammdaten.IdealtypischeBaurateRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BaurateService {

    private final IdealtypischeBaurateRepository idealtypischeBaurateRepository;

    /**
     * Bauratenermittlung auf Basis der idealtypischen Bauraten.
     *
     * Ist im Parameter der Wert für die Wohneinheiten gegeben, so wird die Baurate auschließlich für die Wohneinheiten ermittelt.
     * Sind keine Wohneinheiten gegeben, so bezieht sich die Bauratenermittlung auf den Wert gegeben in geschoßfläche Wohnen.
     *
     * @param realisierungsbeginn
     * @param wohneinheiten
     * @param geschossflaecheWohnen
     * @return die Bauraten auf Basis der idealtypische Bauraten ermittelt durch die in den Parameter gegebenen Informationen.
     * @throws EntityNotFoundException falls für die gegebenen Parameter keine idealtypische Bauraten ermittelt werden können.
     */
    public List<BaurateModel> determineBauraten(
        final Integer realisierungsbeginn,
        final Long wohneinheiten,
        final BigDecimal geschossflaecheWohnen
    ) throws EntityNotFoundException {
        final var idealtypischeBaurate = determineIdealtypischeBaurate(wohneinheiten, geschossflaecheWohnen);
        final var bauraten = new ArrayList<BaurateModel>();
        var partialSum = BigDecimal.ZERO;
        var rate = BigDecimal.ZERO;

        // Ermittlung der Bauraten
        final var jahresraten = idealtypischeBaurate.getJahresraten();
        for (var index = 0; index < jahresraten.size(); index++) {
            final var jahresrate = jahresraten.get(index);
            final var baurate = new BaurateModel();
            baurate.setJahr(jahresrate.getJahr() - 1 + realisierungsbeginn);

            if (index < jahresraten.size() - 1) {
                // Abrunden der Raten.
                if (ObjectUtils.isNotEmpty(wohneinheiten)) {
                    rate =
                        calculateRoundedDownRatenwertForGesamtwertAndRate(
                            BigDecimal.valueOf(wohneinheiten),
                            jahresrate.getRate()
                        );
                    partialSum = partialSum.add(rate);
                    baurate.setAnzahlWeGeplant(rate.intValue());
                } else {
                    rate =
                        calculateRoundedDownRatenwertForGesamtwertAndRate(geschossflaecheWohnen, jahresrate.getRate());
                    partialSum = partialSum.add(rate);
                    baurate.setGeschossflaecheWohnenGeplant(rate);
                }
            } else {
                // Ermitteln der letzten Rate auf Basis der partiellen Summe.
                if (ObjectUtils.isNotEmpty(wohneinheiten)) {
                    rate = BigDecimal.valueOf(wohneinheiten).subtract(partialSum);
                    baurate.setAnzahlWeGeplant(rate.intValue());
                } else {
                    rate = geschossflaecheWohnen.subtract(partialSum);
                    baurate.setGeschossflaecheWohnenGeplant(rate);
                }
            }
            bauraten.add(baurate);
        }

        return bauraten;
    }

    /**
     *
     * @param wohneinheiten
     * @param geschossflaecheWohnen
     * @return
     * @throws EntityNotFoundException falls für die gegebenen Parameter keine idealtypische Baurate ermittelt werden kann.
     */
    protected IdealtypischeBaurate determineIdealtypischeBaurate(
        final Long wohneinheiten,
        final BigDecimal geschossflaecheWohnen
    ) throws EntityNotFoundException {
        final IdealtypischeBaurate idealtypischeBaurate;
        if (ObjectUtils.isNotEmpty(wohneinheiten)) {
            idealtypischeBaurate =
                determineIdealtypischeBaurateForWertAndTyp(
                    BigDecimal.valueOf(wohneinheiten),
                    IdealtypischeBaurateTyp.WOHNEINHEITEN
                );
        } else if (ObjectUtils.isNotEmpty(geschossflaecheWohnen)) {
            idealtypischeBaurate =
                determineIdealtypischeBaurateForWertAndTyp(
                    geschossflaecheWohnen,
                    IdealtypischeBaurateTyp.GESCHOSSFLAECHE_WOHNEN
                );
        } else {
            final String errorMessage =
                "Es konnten keine idealtypischen Bauraten für Wohneinheiten oder für geschoßfläche Wohnen ermittelt werden.";
            final var exception = new EntityNotFoundException(errorMessage);
            log.error(errorMessage, exception);
            throw exception;
        }
        return idealtypischeBaurate;
    }

    /**
     * Ermittelt die idealtypische Baurate mit den gegebenen Parameter.
     *
     * @param wert
     * @param typ
     * @return die idealtypische Baurate für Wohneinheiten für den gegebenen Wert und den Typ.
     * @throws EntityNotFoundException falls für die gegebenen Parameter keine idealtypische Baurate ermittelt werden kann.
     */
    protected IdealtypischeBaurate determineIdealtypischeBaurateForWertAndTyp(
        final BigDecimal wert,
        final IdealtypischeBaurateTyp typ
    ) throws EntityNotFoundException {
        return idealtypischeBaurateRepository
            .findByTypAndVonLessThanEqualAndBisExklusivGreaterThan(typ, wert)
            .orElseThrow(() -> {
                final StringBuilder errorMessage = new StringBuilder()
                    .append("Für den Wert von ")
                    .append(wert)
                    .append(" des Typs ")
                    .append(typ.getBezeichnung())
                    .append("  konnte keine idealtypische Baurate ermittelt werden.");
                final var exception = new EntityNotFoundException(errorMessage.toString());
                log.error(errorMessage.toString(), exception);
                return exception;
            });
    }

    /**
     * @param gesamtwert
     * @param rate
     * @return den auf die nächste ganze Zahl gerundeten Wert der Rate
     */
    protected BigDecimal calculateRoundedDownRatenwertForGesamtwertAndRate(
        final BigDecimal gesamtwert,
        final BigDecimal rate
    ) {
        return gesamtwert.multiply(rate).setScale(0, RoundingMode.DOWN);
    }
}
