package de.muenchen.isi.domain.service.calculation;

import de.muenchen.isi.domain.model.FoerderartModel;
import de.muenchen.isi.domain.model.FoerdermixModel;
import de.muenchen.isi.infrastructure.repository.stammdaten.UmlegungFoerderartenRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoerdermixUmlageService {

    private final UmlegungFoerderartenRepository umlegungFoerderartenRepository;

    /**
     * Manche Förderarten dürfen nicht direkt in Berechnungen einfließen, da für sie Erfahrungswerte fehlen.
     * Stattdessen müssen ihre Anteile in bestimmten Anteilen auf Förderarten mit Erfahrungswerten umgelegt werden.
     * Diese Methode kann so eine Umlegung durchführen.
     *
     * @param foerdermix Das {@link FoerdermixModel}, welches umgelegt werden soll.
     * @param gueltigAb Das Gültigkeitsdatum der Stammdaten, welche die Umlegung diktieren.
     * @return Ein neues {@link FoerdermixModel}, in dem die umzulegenden Förderarten auf andere Förderarten verteilt wurden.
     */
    public FoerdermixModel legeFoerdermixUm(final FoerdermixModel foerdermix, final LocalDate gueltigAb) {
        final var umgelegterFoerdermix = new FoerdermixModel();
        final var umgelegteFoerderarten = new ArrayList<FoerderartModel>();
        umgelegterFoerdermix.setFoerderarten(umgelegteFoerderarten);

        if (foerdermix.getFoerderarten() != null) {
            for (final var foerderart : foerdermix.getFoerderarten()) {
                if (foerderart.getBezeichnung() != null && foerderart.getAnteilProzent() != null) {
                    final var umlegung =
                        umlegungFoerderartenRepository.findFirstByBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                            foerderart.getBezeichnung(),
                            gueltigAb
                        );
                    if (umlegung.isPresent()) {
                        for (final var schluessel : umlegung.get().getUmlegungsschluessel()) {
                            final var umgelegterAnteil = foerderart
                                .getAnteilProzent()
                                .multiply(schluessel.getAnteilProzent());
                            mergeFoerderart(umgelegteFoerderarten, schluessel.getBezeichnung(), umgelegterAnteil);
                        }
                    } else {
                        mergeFoerderart(
                            umgelegteFoerderarten,
                            foerderart.getBezeichnung(),
                            foerderart.getAnteilProzent()
                        );
                    }
                }
            }
        }

        return umgelegterFoerdermix;
    }

    private void mergeFoerderart(
        final List<FoerderartModel> foerderarten,
        final String bezeichnung,
        final BigDecimal anteilProzent
    ) {
        final var foerderart = foerderarten
            .stream()
            .filter(bedarf -> bedarf.getBezeichnung().equals(bezeichnung))
            .findAny();

        if (foerderart.isEmpty()) {
            final var newFoerderart = new FoerderartModel();
            newFoerderart.setBezeichnung(bezeichnung);
            newFoerderart.setAnteilProzent(anteilProzent);
            foerderarten.add(newFoerderart);
        } else {
            foerderart.get().setAnteilProzent(foerderart.get().getAnteilProzent().add(anteilProzent));
        }
    }
}
