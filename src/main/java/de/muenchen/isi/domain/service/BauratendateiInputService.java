package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.BaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.WeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.bauratendatei.BauratendateiInputModel;
import de.muenchen.isi.domain.model.calculation.BedarfeForAbfragevarianteModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenProFoerderartProJahrModel;
import de.muenchen.isi.domain.model.common.GrundschulsprengelModel;
import de.muenchen.isi.domain.model.common.MittelschulsprengelModel;
import de.muenchen.isi.domain.model.common.VerortungModel;
import de.muenchen.isi.domain.model.common.ViertelModel;
import de.muenchen.isi.domain.service.calculation.CalculationService;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BauratendateiInputService {

    /**
     * Setzt für jede Abfragevariante einer Abfrage die BauratendateiInput.
     *
     * @param abfrage Die Abfrage, deren Abfragevarianten befüllt werden sollen.
     * @param bedarfe Die Bedarfe der Abfrage, erstellt durch {@link CalculationService#calculateBedarfeForEachAbfragevarianteOfAbfrage(AbfrageModel)}.
     */
    public void setBauratendateiInputBasisForEachAbfragevariante(
        final AbfrageModel abfrage,
        final Map<UUID, BedarfeForAbfragevarianteModel> bedarfe
    ) {
        if (ArtAbfrage.BAULEITPLANVERFAHREN.equals(abfrage.getArtAbfrage())) {
            final var bauleitplanverfahren = (BauleitplanverfahrenModel) abfrage;
            final var abfragevarianten = ListUtils.union(
                ListUtils.emptyIfNull(bauleitplanverfahren.getAbfragevariantenBauleitplanverfahren()),
                ListUtils.emptyIfNull(bauleitplanverfahren.getAbfragevariantenSachbearbeitungBauleitplanverfahren())
            );
            for (final var abfragevariante : abfragevarianten) {
                final var bauratendateiInput = createBauratendateiInput(
                    bauleitplanverfahren.getVerortung(),
                    bedarfe,
                    abfragevariante.getId()
                );
                if (ObjectUtils.isNotEmpty(bauratendateiInput)) {
                    final var bauratendateiInputs = ListUtils.emptyIfNull(abfragevariante.getBauratendateiInputs());
                    bauratendateiInputs.add(bauratendateiInput);
                    abfragevariante.setBauratendateiInputs(bauratendateiInputs);
                }
            }
        } else if (ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN.equals(abfrage.getArtAbfrage())) {
            final var baugenehmigungsverfahren = (BaugenehmigungsverfahrenModel) abfrage;
            final var abfragevarianten = ListUtils.union(
                ListUtils.emptyIfNull(baugenehmigungsverfahren.getAbfragevariantenBaugenehmigungsverfahren()),
                ListUtils.emptyIfNull(
                    baugenehmigungsverfahren.getAbfragevariantenSachbearbeitungBaugenehmigungsverfahren()
                )
            );
            for (final var abfragevariante : abfragevarianten) {
                final var bauratendateiInput = createBauratendateiInput(
                    baugenehmigungsverfahren.getVerortung(),
                    bedarfe,
                    abfragevariante.getId()
                );
                if (ObjectUtils.isNotEmpty(bauratendateiInput)) {
                    final var bauratendateiInputs = ListUtils.emptyIfNull(abfragevariante.getBauratendateiInputs());
                    bauratendateiInputs.add(bauratendateiInput);
                    abfragevariante.setBauratendateiInputs(bauratendateiInputs);
                }
            }
        } else if (ArtAbfrage.WEITERES_VERFAHREN.equals(abfrage.getArtAbfrage())) {
            final var weiteresVerfahren = (WeiteresVerfahrenModel) abfrage;
            final var abfragevarianten = ListUtils.union(
                ListUtils.emptyIfNull(weiteresVerfahren.getAbfragevariantenWeiteresVerfahren()),
                ListUtils.emptyIfNull(weiteresVerfahren.getAbfragevariantenSachbearbeitungWeiteresVerfahren())
            );
            for (final var abfragevariante : abfragevarianten) {
                final var bauratendateiInput = createBauratendateiInput(
                    weiteresVerfahren.getVerortung(),
                    bedarfe,
                    abfragevariante.getId()
                );
                if (ObjectUtils.isNotEmpty(bauratendateiInput)) {
                    final var bauratendateiInputs = ListUtils.emptyIfNull(abfragevariante.getBauratendateiInputs());
                    bauratendateiInputs.add(bauratendateiInput);
                    abfragevariante.setBauratendateiInputs(bauratendateiInputs);
                }
            }
        }
    }

    /**
     * Erstellt basierend auf bestimmten Daten ein {@link BauratendateiInputModel}.
     *
     * @param verortung Die Verortung der Abfrage.
     * @param bedarfe Die Bedarfe der Abfrage, erstellt durch {@link CalculationService#calculateBedarfeForEachAbfragevarianteOfAbfrage(AbfrageModel)}.
     * @param abfragevarianteId Die Id der Abfragevariante, auf die sich der BauratendateiInput bezieht.
     * @return Einen neuen BauratendateiInput.
     */
    public BauratendateiInputModel createBauratendateiInput(
        final VerortungModel verortung,
        final Map<UUID, BedarfeForAbfragevarianteModel> bedarfe,
        final UUID abfragevarianteId
    ) {
        final var grundschulsprengel = getGrundschulsprengel(verortung);
        final var mittelschulsprengel = getMittelschulsprengel(verortung);
        final var viertel = getViertel(verortung);
        final var wohneinheiten = getWohneinheiten(bedarfe, abfragevarianteId);

        if (
            CollectionUtils.isEmpty(grundschulsprengel) &&
            CollectionUtils.isEmpty(mittelschulsprengel) &&
            CollectionUtils.isEmpty(viertel) &&
            CollectionUtils.isEmpty(wohneinheiten)
        ) {
            return null;
        } else {
            final var bauratendateiInput = new BauratendateiInputModel();
            bauratendateiInput.setGrundschulsprengel(grundschulsprengel);
            bauratendateiInput.setMittelschulsprengel(mittelschulsprengel);
            bauratendateiInput.setViertel(viertel);
            bauratendateiInput.setWohneinheiten(wohneinheiten);
            return bauratendateiInput;
        }
    }

    /**
     * Extrahiert die Nummern der in der Verortung beinhalteten Grundschulsprengel.
     *
     * @param verortung zur Extraktion der Grundschulsprengel.
     * @return die Nummern der Grundschulsprengel ansonsten ein leeres Set.
     */
    protected Set<String> getGrundschulsprengel(final VerortungModel verortung) {
        return ObjectUtils.isNotEmpty(verortung)
            ? CollectionUtils
                .emptyIfNull(verortung.getGrundschulsprengel())
                .stream()
                .map(GrundschulsprengelModel::getNummer)
                .filter(ObjectUtils::isNotEmpty)
                .map(Object::toString)
                .collect(Collectors.toSet())
            : Set.of();
    }

    /**
     * Extrahiert die Nummern der in der Verortung beinhalteten Mittelschulsprengel.
     *
     * @param verortung zur Extraktion der Mittelschulsprengel.
     * @return die Nummern der Mittelschulsprengel ansonsten ein leeres Set.
     */
    protected Set<String> getMittelschulsprengel(final VerortungModel verortung) {
        return ObjectUtils.isNotEmpty(verortung)
            ? CollectionUtils
                .emptyIfNull(verortung.getMittelschulsprengel())
                .stream()
                .map(MittelschulsprengelModel::getNummer)
                .filter(ObjectUtils::isNotEmpty)
                .map(Object::toString)
                .collect(Collectors.toSet())
            : Set.of();
    }

    /**
     * Extrahiert die Nummern der in der Verortung beinhalteten Viertel.
     *
     * @param verortung zur Extraktion der Viertel.
     * @return die Nummern der Viertel ansonsten ein leeres Set.
     */
    protected Set<String> getViertel(final VerortungModel verortung) {
        return ObjectUtils.isNotEmpty(verortung)
            ? CollectionUtils
                .emptyIfNull(verortung.getViertel())
                .stream()
                .map(ViertelModel::getNummer)
                .filter(ObjectUtils::isNotEmpty)
                .collect(Collectors.toSet())
            : Set.of();
    }

    /**
     * Extrahiert die Wohneinheiten der Abfragevariante.
     *
     * @param bedarfe zur Extraktion der Wohneinheiten.
     * @param abfragevarianteId zur Extraktion der Wohneinheiten.
     * @return die Wohneinheiten der Abfragevariante identifiziert durch die ID ansonsten eine leere Liste.
     */
    protected List<WohneinheitenProFoerderartProJahrModel> getWohneinheiten(
        final Map<UUID, BedarfeForAbfragevarianteModel> bedarfe,
        final UUID abfragevarianteId
    ) {
        if (
            bedarfe != null &&
            bedarfe.get(abfragevarianteId) != null &&
            bedarfe.get(abfragevarianteId).getLangfristigerPlanungsursaechlicherBedarf() != null
        ) {
            return ListUtils.emptyIfNull(
                bedarfe.get(abfragevarianteId).getLangfristigerPlanungsursaechlicherBedarf().getWohneinheiten()
            );
        } else {
            return List.of();
        }
    }

    public Map<String, Map<String, BigDecimal>> sumWohneinheitenOfBauratendateiInputs(
        Stream<BauratendateiInputModel> inputs
    ) {
        final var wohneinheitenSum = new HashMap<String, Map<String, BigDecimal>>();
        inputs.forEach(input -> {
            // Summieren der Wohneinheiten
            for (final var wohneinheiten : CollectionUtils.emptyIfNull(input.getWohneinheiten())) {
                wohneinheitenSum.merge(
                    wohneinheiten.getFoerderart(),
                    new HashMap<>(Map.of(wohneinheiten.getJahr(), wohneinheiten.getWohneinheiten())),
                    (present, current) -> {
                        present.merge(wohneinheiten.getJahr(), wohneinheiten.getWohneinheiten(), BigDecimal::add);
                        return present;
                    }
                );
            }
        });
        return wohneinheitenSum;
    }
}
