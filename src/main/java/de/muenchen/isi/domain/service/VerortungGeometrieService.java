package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.exception.GeometryOperationFailedException;
import de.muenchen.isi.domain.model.common.FlurstueckModel;
import de.muenchen.isi.domain.model.common.GemarkungModel;
import de.muenchen.isi.domain.model.common.MultiPolygonGeometryModel;
import de.muenchen.isi.domain.model.common.VerortungMultiPolygonModel;
import de.muenchen.isi.domain.model.common.WithVerortungMultiPolygon;
import de.muenchen.isi.infrastructure.entity.common.VerortungFlurstueck;
import de.muenchen.isi.infrastructure.entity.common.VerortungGeometrie;
import de.muenchen.isi.infrastructure.repository.common.VerortungGeometrieRepository;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.io.WKBWriter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Verwaltet Umgriff und Flurstück-Polygone einer Verortung. Sie liegen getrennt von der Gebietszuordnung,
 * damit Statuswechsel, Berechnung und Suche keine Polygone laden.
 * <p>
 * Models aus dem Service tragen deshalb keine Polygone. Das Formular bekommt sie über {@link #ergaenzen}.
 */
@Service
@RequiredArgsConstructor
public class VerortungGeometrieService {

    private final VerortungGeometrieRepository verortungGeometrieRepository;

    private final KoordinatenService koordinatenService;

    /**
     * Gleicht die gespeicherte Geometrie mit der Verortung ab.
     * <ul>
     *   <li>Ohne Verortung wird die Geometrie gelöscht.</li>
     *   <li>Ohne Umgriff wurde die Geometrie nicht geladen und bleibt unverändert. Über die API kann das nicht
     *       vorkommen, dort ist der Umgriff Pflicht.</li>
     *   <li>Sonst wird nur geschrieben, wenn sich die Geometrie geändert hat.</li>
     * </ul>
     *
     * @param eigentuemerId ID des Bauvorhabens oder der Abfrage
     * @param verortung     die Verortung des Eigentümers
     */
    @Transactional
    public void synchronisieren(final UUID eigentuemerId, final VerortungMultiPolygonModel verortung) {
        if (verortung == null) {
            verortungGeometrieRepository.findById(eigentuemerId).ifPresent(verortungGeometrieRepository::delete);
            return;
        }
        if (verortung.getMultiPolygon() == null) {
            return;
        }
        final var umgriff = toJts(verortung.getMultiPolygon());
        final var flurstuecke = toFlurstuecke(verortung);
        final var hash = berechneHash(umgriff, flurstuecke);

        final var geometrie = verortungGeometrieRepository.findById(eigentuemerId).orElseGet(() -> {
            final var neu = new VerortungGeometrie();
            neu.setId(eigentuemerId);
            return neu;
        });
        if (hash.equals(geometrie.getGeometrieHash())) {
            return;
        }
        geometrie.setUmgriff(umgriff);
        geometrie.setGeometrieHash(hash);
        geometrie.getFlurstuecke().clear();
        geometrie.getFlurstuecke().addAll(flurstuecke);
        verortungGeometrieRepository.save(geometrie);
    }

    /**
     * Ergänzt Umgriff und Flurstück-Polygone für das Formular.
     *
     * @param eigentuemerId ID, unter der die Geometrie gespeichert ist
     * @param eigentuemer   Model, dessen Verortung ergänzt wird. Darf keine Verortung haben.
     */
    @Transactional(readOnly = true)
    public void ergaenzen(final UUID eigentuemerId, final WithVerortungMultiPolygon eigentuemer) {
        final var verortung = eigentuemer.getVerortung();
        if (verortung == null) {
            return;
        }
        verortungGeometrieRepository.findById(eigentuemerId).ifPresent(geometrie -> {
            verortung.setMultiPolygon(koordinatenService.createMultiPolygonModel(geometrie.getUmgriff()));
            final var polygone = geometrie
                .getFlurstuecke()
                .stream()
                .collect(
                    Collectors.toMap(
                        flurstueck -> schluessel(flurstueck.getGemarkungNummer(), flurstueck.getNummer()),
                        VerortungFlurstueck::getGeometrie,
                        (erstes, zweites) -> erstes
                    )
                );
            ergaenzeFlurstuecke(verortung, polygone);
        });
    }

    /**
     * @param eigentuemerIds IDs der Bauvorhaben
     * @return die Umgriffe je ID, für die eine Geometrie vorhanden ist. Lädt keine Flurstücke.
     */
    @Transactional(readOnly = true)
    public Map<UUID, MultiPolygonGeometryModel> findeUmgriffe(final Collection<UUID> eigentuemerIds) {
        if (eigentuemerIds.isEmpty()) {
            return Map.of();
        }
        final Map<UUID, MultiPolygonGeometryModel> umgriffe = new HashMap<>();
        verortungGeometrieRepository
            .findAllById(eigentuemerIds)
            .forEach(geometrie ->
                umgriffe.put(geometrie.getId(), koordinatenService.createMultiPolygonModel(geometrie.getUmgriff()))
            );
        return umgriffe;
    }

    /**
     * Die Hashwerte der Flurstücke und Gemarkungen hängen vom Polygon ab. Die Mengen werden deshalb neu aufgebaut.
     */
    private void ergaenzeFlurstuecke(
        final VerortungMultiPolygonModel verortung,
        final Map<String, MultiPolygon> polygone
    ) {
        if (verortung.getGemarkungen() == null) {
            return;
        }
        final var gemarkungen = new LinkedHashSet<GemarkungModel>();
        for (final var gemarkung : verortung.getGemarkungen()) {
            if (gemarkung.getFlurstuecke() != null) {
                final var flurstuecke = new LinkedHashSet<FlurstueckModel>();
                for (final var flurstueck : gemarkung.getFlurstuecke()) {
                    final var polygon = polygone.get(
                        schluessel(gemarkungNummer(gemarkung, flurstueck), flurstueck.getNummer())
                    );
                    if (polygon != null) {
                        flurstueck.setMultiPolygon(koordinatenService.createMultiPolygonModel(polygon));
                    }
                    flurstuecke.add(flurstueck);
                }
                gemarkung.setFlurstuecke(flurstuecke);
            }
            gemarkungen.add(gemarkung);
        }
        verortung.setGemarkungen(gemarkungen);
    }

    private List<VerortungFlurstueck> toFlurstuecke(final VerortungMultiPolygonModel verortung) {
        final Map<String, VerortungFlurstueck> flurstuecke = new HashMap<>();
        for (final var gemarkung : CollectionUtils.emptyIfNull(verortung.getGemarkungen())) {
            for (final var flurstueck : CollectionUtils.emptyIfNull(gemarkung.getFlurstuecke())) {
                if (flurstueck.getMultiPolygon() == null) {
                    continue;
                }
                final var nummer = gemarkungNummer(gemarkung, flurstueck);
                flurstuecke.computeIfAbsent(schluessel(nummer, flurstueck.getNummer()), key -> {
                    final var entity = new VerortungFlurstueck();
                    entity.setGemarkungNummer(nummer);
                    entity.setNummer(flurstueck.getNummer());
                    entity.setGeometrie(toJts(flurstueck.getMultiPolygon()));
                    return entity;
                });
            }
        }
        return new ArrayList<>(flurstuecke.values());
    }

    private MultiPolygon toJts(final MultiPolygonGeometryModel model) {
        try {
            return koordinatenService.createMultiPolygon(model);
        } catch (final GeometryOperationFailedException exception) {
            throw new IllegalArgumentException(exception.getMessage(), exception);
        }
    }

    private static BigDecimal gemarkungNummer(final GemarkungModel gemarkung, final FlurstueckModel flurstueck) {
        return flurstueck.getGemarkungNummer() != null ? flurstueck.getGemarkungNummer() : gemarkung.getNummer();
    }

    /**
     * Unabhängig von der Skalierung der Nummer, da numeric-Spalten und JSON diese unterschiedlich liefern können.
     */
    private static String schluessel(final BigDecimal gemarkungNummer, final String nummer) {
        final var gemarkung = gemarkungNummer == null ? "" : gemarkungNummer.stripTrailingZeros().toPlainString();
        return gemarkung + "|" + Objects.toString(nummer, "");
    }

    private static String berechneHash(final MultiPolygon umgriff, final List<VerortungFlurstueck> flurstuecke) {
        try {
            final var digest = MessageDigest.getInstance("SHA-256");
            final var writer = new WKBWriter();
            digest.update(writer.write(umgriff));
            flurstuecke
                .stream()
                .sorted(
                    Comparator.comparing(flurstueck ->
                        schluessel(flurstueck.getGemarkungNummer(), flurstueck.getNummer())
                    )
                )
                .forEach(flurstueck -> {
                    digest.update(
                        schluessel(flurstueck.getGemarkungNummer(), flurstueck.getNummer()).getBytes(
                            StandardCharsets.UTF_8
                        )
                    );
                    digest.update(writer.write(flurstueck.getGeometrie()));
                });
            return HexFormat.of().formatHex(digest.digest());
        } catch (final NoSuchAlgorithmException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
