package de.muenchen.isi.domain.service.search;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.mapper.SearchDomainMapper;
import de.muenchen.isi.domain.model.BaseEntityModel;
import de.muenchen.isi.domain.model.search.SearchQueryForEntitiesModel;
import de.muenchen.isi.domain.model.search.SearchResultsModel;
import de.muenchen.isi.domain.model.search.SuchwortSuggestionsModel;
import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.engine.search.common.BooleanOperator;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.mapper.orm.Search;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private static final int MAX_NUMBER_OF_SUGGESTION = 20;

    private final EntityManager entityManager;

    private final SearchPreparationService searchPreparationService;

    private final SearchDomainMapper searchDomainMapper;

    /**
     *
     *
     * @param searchQueryInformation mit der Suchquery bestehend aus einem Wort. Es dürfen sich keine Leerzeichen zwischen den einzelnen Buchstaben befinden.
     * @return die Suchwortvorschläge für das im Parameter gegebene Wort.
     */
    public SuchwortSuggestionsModel searchForSearchwordSuggestion(
        final SearchQueryForEntitiesModel searchQueryInformation
    ) throws EntityNotFoundException {
        final List<Class<? extends BaseEntity>> searchableEntities = searchPreparationService.getSearchableEntities(
            searchQueryInformation
        );
        final var foundSuchwortSuggestions =
            this.doSearchForSearchwordSuggestion(searchableEntities, searchQueryInformation.getSearchQuery())
                .collect(Collectors.toList());
        final var model = new SuchwortSuggestionsModel();
        model.setSuchwortSuggestions(foundSuchwortSuggestions);
        return model;
    }

    /**
     * Diese Methode führt die Suche zur Ermittlung der Suchwortvorschläge durch. Die Suche wird für die Entität  durchgeführt.
     *
     * @param singleWordQuery als Query bestehend aus einem Wort. Es dürfen sich keine Leerzeichen zwischen den einzelnen Buchstaben befinden.
     * @return die Suchwortvorschläge für das im Parameter gegebene Wort.
     */
    public Stream<String> doSearchForSearchwordSuggestion(
        final List<Class<? extends BaseEntity>> searchableEntities,
        final String singleWordQuery
    ) {
        // Ermittlung der suchbaren Attribute je suchbarer Entität
        final var searchableAttributes = searchPreparationService.getNamesOfSearchableAttributes(searchableEntities);

        final var adaptedSingleWordQuery = StringUtils.lowerCase(StringUtils.trimToEmpty(singleWordQuery));

        /*

# Es muss neben dem Textattribut noch ein zusätzliches completion-Attribut vom Typ "completion" vorgehalten werden.
POST infrastrukturabfrage-read/_search
{
  "_source": "not-available-name",
  "suggest": {
    "abfrage.nameAbfrage_completion_suggetion" : {
      "text" : "dasd",
      "completion" : {
        "field" : "abfrage.nameAbfrage_completion_suggetion",
        "size": 5
        "fuzzy": {
          "fuzziness": 0
        }
      }
    }
  }
}



         */

        return Search
            .session(entityManager.getEntityManagerFactory().createEntityManager())
            .search(List.of(Infrastrukturabfrage.class))
            .extension(ElasticsearchExtension.get())
            .select(f -> f.composite().from(f.field("abfrage.nameAbfrage", String.class)).as(s -> s + s))
            // Verhindern der Entitätssuche
            .where(SearchPredicateFactory::matchNone)
            // Erstellen des SuggestionRequest
            .requestTransformer(context -> {
                context.parametersMap().clear();
                final var jsonBody = context.body();
                jsonBody.entrySet().clear();

                System.err.println(context.parametersMap().toString());
            })
            .fetchAllHits()
            .stream();
    }

    /**
     *
     *
     *
     * @param searchQueryInformation mit der Suchquery und den zu durchsuchenden Entitäten.
     * @return die Sucherbenisse für die zu suchenden Entitäten.
     * @throws EntityNotFoundException
     */
    public SearchResultsModel searchForEntities(final SearchQueryForEntitiesModel searchQueryInformation)
        throws EntityNotFoundException {
        final List<Class<? extends BaseEntity>> searchableEntities = searchPreparationService.getSearchableEntities(
            searchQueryInformation
        );
        final var searchResults =
            this.doSearchForEntities(searchableEntities, searchQueryInformation.getSearchQuery())
                .map(searchDomainMapper::model2SearchResultModel)
                .collect(Collectors.toList());
        final var model = new SearchResultsModel();
        model.setSearchResults(searchResults);
        return model;
    }

    /**
     * Diese Methode führt die Entitätssuche für die im Parameter gegebene Entitäten auf Basis der Suchquery durch.
     *
     * @param searchableEntities nach welchen gesucht werden soll.
     * @param searchQuery die Suchquery für die Entitätssuche.
     * @return
     */
    protected Stream<? extends BaseEntityModel> doSearchForEntities(
        final List<Class<? extends BaseEntity>> searchableEntities,
        final String searchQuery
    ) {
        // Ermittlung der suchbaren Attribute je suchbarer Entität
        final var searchableAttributes = searchPreparationService.getNamesOfSearchableAttributes(searchableEntities);
        // Anpassen der Suchquery
        final var adaptedSearchQuery = this.createAdaptedSearchQueryForSimpleQueryStringSearch(searchQuery);

        return Search
            .session(entityManager.getEntityManagerFactory().createEntityManager())
            .search(searchableEntities)
            .where(function -> {
                if (StringUtils.isNotEmpty(searchQuery)) {
                    // Suche entsprechend der gegebenen Query.
                    return function
                        // https://docs.jboss.org/hibernate/stable/search/reference/en-US/html_single/#search-dsl-predicate-simple-query-string
                        .simpleQueryString()
                        .fields(searchableAttributes)
                        .matching(adaptedSearchQuery)
                        // Es werden nur die Entitäten als Suchergebnis zurückgegeben, welche alle Suchwörter der Suchquery beinhalten.
                        .defaultOperator(BooleanOperator.AND);
                } else {
                    // Zurückgeben aller Entitäten.
                    return function.matchAll();
                }
            })
            .sort(function -> function.field("lastModifiedDateTime").desc())
            .fetchAllHits()
            .stream()
            .map(searchDomainMapper::entity2Model);
    }

    /**
     * Diese Methode passt die im Parameter gegebene Suchquery an, um diese bei der Simple-Query-String-Suche verwenden zu können.
     *
     * @param searchQuery zum anpassen.
     * @return die für die Simple-Query-String-Suche angepasste Suchquery.
     */
    protected String createAdaptedSearchQueryForSimpleQueryStringSearch(final String searchQuery) {
        final var splittedSearchQuery = StringUtils.split(StringUtils.trimToEmpty(searchQuery));
        final var adaptedSearchQuery = Arrays
            .stream(splittedSearchQuery)
            .map(searchQueryArtifact -> searchQueryArtifact + "*")
            .collect(Collectors.joining(StringUtils.SPACE));
        log.debug("Die erstellte Suchquery: {}", adaptedSearchQuery);
        return adaptedSearchQuery;
    }
}
