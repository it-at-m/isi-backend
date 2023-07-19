package de.muenchen.isi.domain.service.search;

import de.muenchen.isi.infrastructure.entity.Abfragevariante;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.common.Flurstueck;
import de.muenchen.isi.infrastructure.entity.common.Gemarkung;
import de.muenchen.isi.infrastructure.entity.common.Stadtbezirk;
import de.muenchen.isi.infrastructure.entity.common.Verortung;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.search.Suchwort;
import de.muenchen.isi.infrastructure.repository.search.SuchwortRepository;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * Der Service dient dazu, die für die Suchwortvorschläge nötigen Suchwörter je Entität in der Datenbank zu speichern und zu extrahieren.
 */
@Service
@RequiredArgsConstructor
public class SuchwortService {

    private final SuchwortRepository suchwortRepository;

    public void deleteOldSearchwordsAndAddNewSearchwords(final Infrastrukturabfrage infrastrukturabfrage) {
        final Set<String> suchwoerter = new HashSet<>();
        CollectionUtils.addIgnoreNull(suchwoerter, infrastrukturabfrage.getAbfrage().getNameAbfrage());
        CollectionUtils.addIgnoreNull(
            suchwoerter,
            infrastrukturabfrage.getAbfrage().getStatusAbfrage().getBezeichnung()
        );
        CollectionUtils.addIgnoreNull(suchwoerter, infrastrukturabfrage.getAbfrage().getBebauungsplannummer());
        suchwoerter.addAll(getSearchwords(infrastrukturabfrage.getAbfrage().getAdresse()));
        suchwoerter.addAll(getSearchwords(infrastrukturabfrage.getAbfrage().getVerortung()));
        CollectionUtils
            .emptyIfNull(infrastrukturabfrage.getAbfragevarianten())
            .forEach(abfragevariante -> suchwoerter.addAll(getSearchwords(abfragevariante)));
        CollectionUtils
            .emptyIfNull(infrastrukturabfrage.getAbfragevariantenSachbearbeitung())
            .forEach(abfragevariante -> suchwoerter.addAll(getSearchwords(abfragevariante)));
        deleteOldSearchwordsAndAddNewSearchwords(infrastrukturabfrage.getId(), suchwoerter);
    }

    public void deleteOldSearchwordsAndAddNewSearchwords(final Bauvorhaben bauvorhaben) {
        final Set<String> suchwoerter = new HashSet<>();
        CollectionUtils.addIgnoreNull(suchwoerter, bauvorhaben.getNameVorhaben());
        suchwoerter.addAll(getSearchwords(bauvorhaben.getAdresse()));
        suchwoerter.addAll(getSearchwords(bauvorhaben.getVerortung()));
        deleteOldSearchwordsAndAddNewSearchwords(bauvorhaben.getId(), suchwoerter);
    }

    public void deleteOldSearchwordsAndAddNewSearchwords(final Infrastruktureinrichtung infrastruktureinrichtung) {
        final Set<String> suchwoerter = new HashSet<>();
        CollectionUtils.addIgnoreNull(suchwoerter, infrastruktureinrichtung.getNameEinrichtung());
        CollectionUtils.addIgnoreNull(suchwoerter, infrastruktureinrichtung.getStatus().getBezeichnung());
        suchwoerter.addAll(getSearchwords(infrastruktureinrichtung.getAdresse()));
        deleteOldSearchwordsAndAddNewSearchwords(infrastruktureinrichtung.getId(), suchwoerter);
    }

    protected Set<String> getSearchwords(final Abfragevariante abfragevariante) {
        final Set<String> suchwoerter = new HashSet<>();
        if (ObjectUtils.isNotEmpty(abfragevariante)) {
            CollectionUtils.addIgnoreNull(suchwoerter, Objects.toString(abfragevariante.getRealisierungVon(), null));
        }
        return suchwoerter;
    }

    protected Set<String> getSearchwords(final Verortung verortung) {
        final Set<String> suchwoerter = new HashSet<>();
        if (ObjectUtils.isNotEmpty(verortung)) {
            CollectionUtils
                .emptyIfNull(verortung.getStadtbezirke())
                .forEach(stadtbezirk -> suchwoerter.addAll(getSearchwords(stadtbezirk)));
            CollectionUtils
                .emptyIfNull(verortung.getGemarkungen())
                .forEach(gemarkung -> suchwoerter.addAll(getSearchwords(gemarkung)));
        }
        return suchwoerter;
    }

    protected Set<String> getSearchwords(final Stadtbezirk stadtbezirk) {
        final Set<String> suchwoerter = new HashSet<>();
        CollectionUtils.addIgnoreNull(suchwoerter, stadtbezirk.getName());
        return suchwoerter;
    }

    protected Set<String> getSearchwords(final Gemarkung gemarkung) {
        final Set<String> suchwoerter = new HashSet<>();
        CollectionUtils.addIgnoreNull(suchwoerter, gemarkung.getName());
        CollectionUtils
            .emptyIfNull(gemarkung.getFlurstuecke())
            .forEach(flurstueck -> suchwoerter.addAll(getSearchwords(flurstueck)));
        return suchwoerter;
    }

    protected Set<String> getSearchwords(final Flurstueck flurstueck) {
        final Set<String> suchwoerter = new HashSet<>();
        CollectionUtils.addIgnoreNull(suchwoerter, flurstueck.getNummer());
        return suchwoerter;
    }

    protected Set<String> getSearchwords(final Adresse adresse) {
        final Set<String> suchwoerter = new HashSet<>();
        if (ObjectUtils.isNotEmpty(adresse)) {
            if (ObjectUtils.isNotEmpty(adresse) && ObjectUtils.isNotEmpty(adresse.getStrasse())) {
                final var strasseHausnummer =
                    adresse.getStrasse() +
                    (
                        ObjectUtils.isNotEmpty(adresse.getHausnummer())
                            ? StringUtils.SPACE + adresse.getHausnummer()
                            : ""
                    );
                CollectionUtils.addIgnoreNull(suchwoerter, strasseHausnummer);
            }
        }
        return suchwoerter;
    }

    protected void deleteOldSearchwordsAndAddNewSearchwords(final UUID id, Set<String> suchwoerter) {
        suchwortRepository.deleteAllByReferenceId(id);
        final Set<Suchwort> bauvorhabenSuchwoerter = suchwoerter
            .stream()
            .map(suchwort -> new Suchwort(suchwort, id))
            .collect(Collectors.toSet());
        suchwortRepository.saveAllAndFlush(bauvorhabenSuchwoerter);
    }
}
