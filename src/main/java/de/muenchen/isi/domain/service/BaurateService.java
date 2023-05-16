package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.mapper.BaurateDomainMapper;
import de.muenchen.isi.domain.model.BaurateModel;
import de.muenchen.isi.infrastructure.entity.stammdaten.baurate.IdealtypischeBaurate;
import de.muenchen.isi.infrastructure.repository.BaurateRepository;
import de.muenchen.isi.infrastructure.repository.stammdaten.IdealtypischeBaurateRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BaurateService {

    private final BaurateDomainMapper baurateDomainMapper;

    private final BaurateRepository baurateRepository;

    private final IdealtypischeBaurateRepository idealtypischeBaurateRepository;

    /**
     *
     * @param realisierungsbeginn
     * @param wohneinheiten
     * @param geschossflaecheWohnen
     * @return
     * @throws EntityNotFoundException
     */
    public List<BaurateModel> determineBauraten(
        final Integer realisierungsbeginn,
        final Long wohneinheiten,
        final BigDecimal geschossflaecheWohnen
    ) throws EntityNotFoundException {
        final var idealtypischeBaurate = determineIdealtypischeBaurate(wohneinheiten, geschossflaecheWohnen);

        final var jahresraten = idealtypischeBaurate.getJahresraten();
        final var bauraten = new ArrayList<BaurateModel>();

        var partialSumWohneinheiten = BigDecimal.ZERO;
        var partialSumGeschossflaecheWohnen = BigDecimal.ZERO;

        BigDecimal rateWohneinheiten;
        BigDecimal rateGeschossflaecheWohnen;

        // Ermittlung der Bauraten
        for (var index = 0; index < jahresraten.size(); index++) {
            final var jahresrate = jahresraten.get(index);
            final var baurate = new BaurateModel();
            baurate.setJahr(jahresrate.getJahr() - 1 + realisierungsbeginn);

            if (index < jahresraten.size() - 1) {
                // Abrunden der Raten.
                rateWohneinheiten =
                    calculateRoundedDownRatenwertForGesamtwertAndRate(
                        BigDecimal.valueOf(wohneinheiten),
                        jahresrate.getRate()
                    );
                partialSumWohneinheiten = partialSumWohneinheiten.add(rateWohneinheiten);
                baurate.setAnzahlWeGeplant(rateWohneinheiten.intValue());

                rateGeschossflaecheWohnen =
                    calculateRoundedDownRatenwertForGesamtwertAndRate(geschossflaecheWohnen, jahresrate.getRate());
                partialSumGeschossflaecheWohnen = partialSumGeschossflaecheWohnen.add(rateGeschossflaecheWohnen);
                baurate.setGeschossflaecheWohnenGeplant(rateGeschossflaecheWohnen);
            } else {
                // Ermitteln der letzten Rate auf Basis der partiellen Summe.
                rateWohneinheiten = BigDecimal.valueOf(wohneinheiten).min(partialSumWohneinheiten);
                baurate.setAnzahlWeGeplant(rateWohneinheiten.intValue());

                rateGeschossflaecheWohnen = geschossflaecheWohnen.min(partialSumGeschossflaecheWohnen);
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
     * @throws EntityNotFoundException falls für die gegebenen Parameter keine Baurate ermittelt werden kann.
     */
    protected IdealtypischeBaurate determineIdealtypischeBaurate(
        final Long wohneinheiten,
        final BigDecimal geschossflaecheWohnen
    ) throws EntityNotFoundException {
        final Optional<IdealtypischeBaurate> idealtypischeBaurateOpt;
        final StringBuilder errorMessage = new StringBuilder();
        if (ObjectUtils.isNotEmpty(wohneinheiten)) {
            idealtypischeBaurateOpt =
                idealtypischeBaurateRepository.findByWohneinheitenVonLessThanEqualAndWohneinheitenBisEinschliesslichGreaterThanEqual(
                    wohneinheiten
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
                idealtypischeBaurateRepository.findByGeschossflaecheWohnenVonLessThanEqualAndGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
                    geschossflaecheWohnen
                );
            if (idealtypischeBaurateOpt.isEmpty()) {
                errorMessage.append(
                    "Für die Geschossfläche Wohnen von " +
                    wohneinheiten +
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

    public List<BaurateModel> getBauraten() {
        return this.baurateRepository.findAllByOrderByJahrDesc()
            .map(this.baurateDomainMapper::entity2Model)
            .collect(Collectors.toList());
    }

    /**
     * Die Methode gibt ein {@link BaurateModel} identifiziert durch die ID zurück.
     *
     * @param id zum Identifizieren des {@link BaurateModel}.
     * @return {@link BaurateModel}.
     */
    public BaurateModel getBaurateById(final UUID id) throws EntityNotFoundException {
        final var optEntity = this.baurateRepository.findById(id);
        final var entity = optEntity.orElseThrow(() -> {
            final var message = "Baurate nicht gefunden.";
            log.error(message);
            return new EntityNotFoundException(message);
        });
        return this.baurateDomainMapper.entity2Model(entity);
    }

    /**
     * Diese Methode speichert ein {@link BaurateModel}.
     *
     * @param baurate zum Speichern
     * @return das gespeicherte {@link BaurateModel}
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     */
    public BaurateModel saveBaurate(final BaurateModel baurate) throws OptimisticLockingException {
        var entity = this.baurateDomainMapper.model2entity(baurate);
        try {
            entity = this.baurateRepository.saveAndFlush(entity);
        } catch (final ObjectOptimisticLockingFailureException exception) {
            final var message = "Die Daten wurden in der Zwischenzeit geändert. Bitte laden Sie die Daten neu";
            throw new OptimisticLockingException(message, exception);
        }
        return this.baurateDomainMapper.entity2Model(entity);
    }

    /**
     * Diese Methode updated ein {@link BaurateModel}.
     *
     * @param baurate zum Updaten
     * @return das geupdatete {@link BaurateModel}
     * @throws EntityNotFoundException falls die Abfrage identifiziert durch die {@link BaurateModel#getId()} nicht gefunden wird
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     */
    public BaurateModel updateBaurate(final BaurateModel baurate)
        throws EntityNotFoundException, OptimisticLockingException {
        this.getBaurateById(baurate.getId());
        return this.saveBaurate(baurate);
    }

    /**
     * Diese Methode löscht ein {@link BaurateModel}.
     *
     * @param id zum Identifizieren des {@link BaurateModel}.
     * @throws EntityNotFoundException falls die Abfrage identifiziert durch die {@link BaurateModel#getId()} nicht gefunden wird.
     */
    public void deleteBaurateById(final UUID id) throws EntityNotFoundException {
        final var model = this.getBaurateById(id);
        this.baurateRepository.deleteById(model.getId());
    }
}
