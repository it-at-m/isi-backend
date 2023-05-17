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
import java.util.Optional;
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
     * Bauratenermittlung auf Basis der idealtypische Bauraten.
     *
     * @param realisierungsbeginn
     * @param wohneinheiten
     * @param geschossflaecheWohnen
     * @return die Bauraten auf Basis der idealtypische Bauraten ermitelt durch die in den Parameter gegebenen Informationen.
     * @throws EntityNotFoundException falls für die gegebenen Parameter keine idealtypische Bauraten ermittelt werden kann.
     */
    public List<BaurateModel> determineBauraten(
        final Integer realisierungsbeginn,
        final Long wohneinheiten,
        final BigDecimal geschossflaecheWohnen
    ) throws EntityNotFoundException {
        final var idealtypischeBaurate = determineIdealtypischeBaurate(wohneinheiten, geschossflaecheWohnen);
        final var bauraten = new ArrayList<BaurateModel>();

        var partialSumWohneinheiten = BigDecimal.ZERO;
        var partialSumGeschossflaecheWohnen = BigDecimal.ZERO;
        BigDecimal rateWohneinheiten;
        BigDecimal rateGeschossflaecheWohnen;

        // Ermittlung der Bauraten
        final var jahresraten = idealtypischeBaurate.getJahresraten();
        for (var index = 0; index < jahresraten.size(); index++) {
            final var jahresrate = jahresraten.get(index);
            final var baurate = new BaurateModel();
            baurate.setJahr(jahresrate.getJahr() - 1 + realisierungsbeginn);

            if (index < jahresraten.size() - 1) {
                // Abrunden der Raten.
                if (ObjectUtils.isNotEmpty(wohneinheiten)) {
                    rateWohneinheiten =
                        calculateRoundedDownRatenwertForGesamtwertAndRate(
                            BigDecimal.valueOf(wohneinheiten),
                            jahresrate.getRate()
                        );
                    partialSumWohneinheiten = partialSumWohneinheiten.add(rateWohneinheiten);
                    baurate.setAnzahlWeGeplant(rateWohneinheiten.intValue());
                }

                rateGeschossflaecheWohnen =
                    calculateRoundedDownRatenwertForGesamtwertAndRate(geschossflaecheWohnen, jahresrate.getRate());
                partialSumGeschossflaecheWohnen = partialSumGeschossflaecheWohnen.add(rateGeschossflaecheWohnen);
                baurate.setGeschossflaecheWohnenGeplant(rateGeschossflaecheWohnen);
            } else {
                // Ermitteln der letzten Rate auf Basis der partiellen Summe.
                if (ObjectUtils.isNotEmpty(wohneinheiten)) {
                    rateWohneinheiten = BigDecimal.valueOf(wohneinheiten).subtract(partialSumWohneinheiten);
                    baurate.setAnzahlWeGeplant(rateWohneinheiten.intValue());
                }

                rateGeschossflaecheWohnen = geschossflaecheWohnen.subtract(partialSumGeschossflaecheWohnen);
                baurate.setGeschossflaecheWohnenGeplant(rateGeschossflaecheWohnen);
            }
            bauraten.add(baurate);
        }

        return bauraten;
    }

    /**
     * Ermittelt die idealtypische Baurate mit den gegebenen Parameter.
     *
     * @param wohneinheiten
     * @param geschossflaecheWohnen
     * @return die idealtypische Baurate für Wohneinheiten falls ein Wert gesetzt, andernfalls wird die idealtypische Baurate
     * auf Basis der geschossfläche Wohnen ermittelt.
     * @throws EntityNotFoundException falls für die gegebenen Parameter keine idealtypische Baurate ermittelt werden kann.
     */
    protected IdealtypischeBaurate determineIdealtypischeBaurate(
        final Long wohneinheiten,
        final BigDecimal geschossflaecheWohnen
    ) throws EntityNotFoundException {
        final Optional<IdealtypischeBaurate> idealtypischeBaurateOpt;
        final StringBuilder errorMessage = new StringBuilder();
        if (ObjectUtils.isNotEmpty(wohneinheiten)) {
            idealtypischeBaurateOpt =
                idealtypischeBaurateRepository.findByTypAndVonLessThanEqualAndBisExklusivGreaterThan(
                    IdealtypischeBaurateTyp.WOHNEINHEITEN,
                    BigDecimal.valueOf(wohneinheiten)
                );
            if (idealtypischeBaurateOpt.isEmpty()) {
                errorMessage.append(
                    "Für die Anzahl von " +
                    wohneinheiten +
                    " Wohneinheiten konnte keine idealtypische Baurate ermittelt werden."
                );
            }
        } else {
            idealtypischeBaurateOpt =
                idealtypischeBaurateRepository.findByTypAndVonLessThanEqualAndBisExklusivGreaterThan(
                    IdealtypischeBaurateTyp.GESCHOSSFLAECHE_WOHNEN,
                    geschossflaecheWohnen
                );
            if (idealtypischeBaurateOpt.isEmpty()) {
                errorMessage.append(
                    "Für die Geschossfläche Wohnen von " +
                    (ObjectUtils.isNotEmpty(geschossflaecheWohnen) ? geschossflaecheWohnen.doubleValue() : null) +
                    " qm konnte keine idealtypische Baurate ermittelt werden."
                );
            }
        }

        return idealtypischeBaurateOpt.orElseThrow(() -> {
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
