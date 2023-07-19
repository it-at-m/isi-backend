package de.muenchen.isi.domain.service.search;

import de.muenchen.isi.domain.mapper.SearchDomainMapper;
import de.muenchen.isi.domain.model.search.SuchwortModel;
import de.muenchen.isi.domain.model.search.SuchwortSuggestionsModel;
import de.muenchen.isi.infrastructure.entity.Abfragevariante;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.search.Suchwort;
import de.muenchen.isi.infrastructure.repository.search.SuchwortRepository;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.mapper.orm.Search;
import org.springframework.stereotype.Service;

/**
 * Der Service dient dazu, die für die Suchwortvorschläge nötigen Suchwörter je Entität in der Datenbank zu speichern und zu extrahieren.
 */
@Service
@RequiredArgsConstructor
public class SuchwortService {

    private static final int MAX_NUMBER_OF_SUGGESTION = 20;

    private final SuchwortRepository suchwortRepository;

    private final EntityManager entityManager;

    private final SearchDomainMapper searchDomainMapper;

    public SuchwortSuggestionsModel searchForSearchwordSuggestion(final String singleWordQuery) {
        final var suchwortSuggestions = new SuchwortSuggestionsModel();
        final var foundSuchwortSuggestions =
            this.doSearchForSearchwordSuggestion(singleWordQuery)
                .map(SuchwortModel::getSuchwort)
                .collect(Collectors.toList());
        suchwortSuggestions.setSuchwortSuggestions(foundSuchwortSuggestions);
        return suchwortSuggestions;
    }

    public Stream<SuchwortModel> doSearchForSearchwordSuggestion(final String singleWordQuery) {
        final var wildcardQuery = StringUtils.lowerCase(StringUtils.trimToEmpty(singleWordQuery)) + "*";

        return Search
            .session(entityManager.getEntityManagerFactory().createEntityManager())
            .search(Suchwort.class)
            .where(function ->
                function
                    // https://docs.jboss.org/hibernate/stable/search/reference/en-US/html_single/#search-dsl-predicate-wildcard
                    .wildcard()
                    .field("suchwort")
                    .matching(wildcardQuery)
            )
            .fetch(MAX_NUMBER_OF_SUGGESTION)
            .hits()
            .stream()
            .map(searchDomainMapper::entity2Model);
    }

    public void deleteOldSearchwordsAndAddNewSearchwords(final Infrastrukturabfrage infrastrukturabfrage) {
        final Set<String> suchwoerter = new HashSet<>();
        CollectionUtils.addIgnoreNull(suchwoerter, infrastrukturabfrage.getAbfrage().getNameAbfrage());
        CollectionUtils.addIgnoreNull(
            suchwoerter,
            infrastrukturabfrage.getAbfrage().getStatusAbfrage().getBezeichnung()
        );
        CollectionUtils
            .emptyIfNull(infrastrukturabfrage.getAbfragevarianten())
            .stream()
            .forEach(abfragevariante -> suchwoerter.addAll(getSearchwords(abfragevariante)));
        CollectionUtils
            .emptyIfNull(infrastrukturabfrage.getAbfragevariantenSachbearbeitung())
            .stream()
            .forEach(abfragevariante -> suchwoerter.addAll(getSearchwords(abfragevariante)));
        deleteOldSearchwordsAndAddNewSearchwords(infrastrukturabfrage.getId(), suchwoerter);
    }

    public Set<String> getSearchwords(final Abfragevariante abfragevariante) {
        final Set<String> suchwoerter = new HashSet<>();
        CollectionUtils.addIgnoreNull(suchwoerter, Objects.toString(abfragevariante.getRealisierungVon(), null));
        return suchwoerter;
    }

    public void deleteOldSearchwordsAndAddNewSearchwords(final Bauvorhaben bauvorhaben) {
        final Set<String> suchwoerter = new HashSet<>();
        CollectionUtils.addIgnoreNull(suchwoerter, bauvorhaben.getNameVorhaben());
        deleteOldSearchwordsAndAddNewSearchwords(bauvorhaben.getId(), suchwoerter);
    }

    public void deleteOldSearchwordsAndAddNewSearchwords(final Infrastruktureinrichtung infrastruktureinrichtung) {
        final Set<String> suchwoerter = new HashSet<>();
        CollectionUtils.addIgnoreNull(suchwoerter, infrastruktureinrichtung.getNameEinrichtung());
        CollectionUtils.addIgnoreNull(suchwoerter, infrastruktureinrichtung.getStatus().getBezeichnung());
        deleteOldSearchwordsAndAddNewSearchwords(infrastruktureinrichtung.getId(), suchwoerter);
    }

    public void deleteOldSearchwordsAndAddNewSearchwords(final UUID id, Set<String> suchwoerter) {
        suchwortRepository.deleteAllByReferenceId(id);
        final Set<Suchwort> bauvorhabenSuchwoerter = suchwoerter
            .stream()
            .map(suchwort -> new Suchwort(suchwort, id))
            .collect(Collectors.toSet());
        suchwortRepository.saveAllAndFlush(bauvorhabenSuchwoerter);
    }
}
