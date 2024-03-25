package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.mapper.BauratendateiDomainMapper;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.BaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.WeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.bauratendatei.BauratendateiInputModel;
import de.muenchen.isi.domain.model.bauratendatei.WithBauratendateiInputsModel;
import de.muenchen.isi.domain.model.calculation.BedarfeForAbfragevarianteModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenProFoerderartProJahrModel;
import de.muenchen.isi.domain.model.common.GrundschulsprengelModel;
import de.muenchen.isi.domain.model.common.MittelschulsprengelModel;
import de.muenchen.isi.domain.model.common.VerortungModel;
import de.muenchen.isi.domain.model.common.ViertelModel;
import de.muenchen.isi.domain.service.calculation.CalculationService;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BauratendateiInputService {

    private final BauratendateiDomainMapper bauratendateiDomainMapper;

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
                this.setOrRemoveBaurateninput(abfragevariante, bauleitplanverfahren.getVerortung(), bedarfe);
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
                this.setOrRemoveBaurateninput(abfragevariante, baugenehmigungsverfahren.getVerortung(), bedarfe);
            }
        } else if (ArtAbfrage.WEITERES_VERFAHREN.equals(abfrage.getArtAbfrage())) {
            final var weiteresVerfahren = (WeiteresVerfahrenModel) abfrage;
            final var abfragevarianten = ListUtils.union(
                ListUtils.emptyIfNull(weiteresVerfahren.getAbfragevariantenWeiteresVerfahren()),
                ListUtils.emptyIfNull(weiteresVerfahren.getAbfragevariantenSachbearbeitungWeiteresVerfahren())
            );
            for (final var abfragevariante : abfragevarianten) {
                this.setOrRemoveBaurateninput(abfragevariante, weiteresVerfahren.getVerortung(), bedarfe);
            }
        }
    }

    /**
     *
     *
     * @param withBauratendateiInputs
     * @param verortung
     * @param bedarfe
     */
    protected void setOrRemoveBaurateninput(
        final WithBauratendateiInputsModel withBauratendateiInputs,
        final VerortungModel verortung,
        final Map<UUID, BedarfeForAbfragevarianteModel> bedarfe
    ) {
        // Zurücksetzen der Inputs für die Bauratendatei falls Checkbox nicht gewählt.
        if (BooleanUtils.isNotTrue(withBauratendateiInputs.getHasBauratendateiInputs())) {
            withBauratendateiInputs.setBauratendateiInputBasis(null);
            withBauratendateiInputs.setBauratendateiInputs(List.of());
        }

        // Ermitteln der Inputs für die Bauratendatei auf Basis der Berechnung der langfristigen Bedarfe.
        final var newBauratendateiInput = createBauratendateiInput(verortung, bedarfe, withBauratendateiInputs.getId());

        // Neusetzen der Inputs für die Bauratendatei falls diese nicht mit den langfristigen Bedarfen übereinstimmen.
        if (!equals(newBauratendateiInput, withBauratendateiInputs.getBauratendateiInputs())) {
            withBauratendateiInputs.setBauratendateiInputBasis(newBauratendateiInput);
            final var bauratendateiInputs = new ArrayList<BauratendateiInputModel>();
            bauratendateiInputs.add(bauratendateiDomainMapper.cloneDeep(newBauratendateiInput));
            withBauratendateiInputs.setBauratendateiInputs(bauratendateiInputs);
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

        final var bauratendateiInput = new BauratendateiInputModel();
        bauratendateiInput.setGrundschulsprengel(grundschulsprengel);
        bauratendateiInput.setMittelschulsprengel(mittelschulsprengel);
        bauratendateiInput.setViertel(viertel);
        bauratendateiInput.setWohneinheiten(wohneinheiten);
        return bauratendateiInput;
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
        return (
                bedarfe != null &&
                bedarfe.get(abfragevarianteId) != null &&
                bedarfe.get(abfragevarianteId).getLangfristigerPlanungsursaechlicherBedarf() != null
            )
            ? ListUtils.emptyIfNull(
                bedarfe.get(abfragevarianteId).getLangfristigerPlanungsursaechlicherBedarf().getWohneinheiten()
            )
            : List.of();
    }

    /**
     * Summiert die Wohneinheiten je Jahr und Förderart für die gegebenen {@link BauratendateiInputModel}.
     *
     * @param inputs zum summieren je Jahr und Förderart.
     * @return eine Map mit Key konkateniert aus dem Jahr und der Förderart und dem Value als Summe der Wohneinheiten.
     */
    public Map<String, BigDecimal> sumWohneinheitenOfBauratendateiInputs(final Stream<BauratendateiInputModel> inputs) {
        return inputs
            .flatMap(bauratendateiInput -> bauratendateiInput.getWohneinheiten().stream())
            .collect(
                Collectors.groupingBy(
                    this::concatJahrAndFoerderart,
                    Collectors.reducing(
                        BigDecimal.ZERO,
                        WohneinheitenProFoerderartProJahrModel::getWohneinheiten,
                        BigDecimal::add
                    )
                )
            );
    }

    /**
     * Vergleicht die Summe der Wohneinheiten je Förderart und Jahr des Parameter "basis"
     * mit der Summe der Wohneinheiten je Förderart und Jahr im Parameter "inputs".
     *
     * @param basis zum Vergleich mit Paramter "inputs".
     * @param inputs zum Vergleich mit Paramter "basis".
     * @return true falls die Summen der Wohneinheiten je Förderart und Jahr übereinstimmen, andernfalls false.
     */
    public boolean equals(final BauratendateiInputModel basis, final List<BauratendateiInputModel> inputs) {
        final var sumBasis = sumWohneinheitenOfBauratendateiInputs(basis == null ? Stream.empty() : Stream.of(basis));
        final var sumInputs = sumWohneinheitenOfBauratendateiInputs(ListUtils.emptyIfNull(inputs).stream());
        return this.equals(sumBasis, sumInputs);
    }

    /**
     * Vergleicht die Summe der Wohneinheiten je Förderart und Jahr des Parameter "sumBasis"
     * mit der Summe der Wohneinheiten je Förderart und Jahr im Parameter "sumInputs".
     *
     * @param sumBasis zum Vergleich mit Paramter "sumInputs".
     * @param sumInputs zum Vergleich mit Paramter "sumBasis".
     * @return true falls die Summen der Wohneinheiten je Förderart und Jahr übereinstimmen, andernfalls false.
     */
    protected boolean equals(final Map<String, BigDecimal> sumBasis, final Map<String, BigDecimal> sumInputs) {
        if (sumBasis.size() != sumInputs.size()) {
            return false;
        }

        for (final var sumBasisEntry : sumBasis.entrySet()) {
            final var numberOfWohneinheitenInputs = sumInputs.get(sumBasisEntry.getKey());
            if (ObjectUtils.isEmpty(numberOfWohneinheitenInputs)) {
                return false;
            } else if (numberOfWohneinheitenInputs.compareTo(sumBasisEntry.getValue()) != 0) {
                return false;
            }
        }

        return true;
    }

    protected String concatJahrAndFoerderart(
        final WohneinheitenProFoerderartProJahrModel wohneinheitenProFoerderartProJahr
    ) {
        return String.join(
            "",
            wohneinheitenProFoerderartProJahr.getJahr(),
            wohneinheitenProFoerderartProJahr.getFoerderart()
        );
    }
}
