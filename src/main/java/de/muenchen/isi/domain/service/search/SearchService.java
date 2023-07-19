package de.muenchen.isi.domain.service.search;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.mapper.SearchDomainMapper;
import de.muenchen.isi.domain.model.BaseEntityModel;
import de.muenchen.isi.domain.model.search.SearchQueryForEntitiesModel;
import de.muenchen.isi.domain.model.search.SearchResultsModel;
import de.muenchen.isi.domain.model.search.SuchwortModel;
import de.muenchen.isi.domain.model.search.SuchwortSuggestionsModel;
import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.search.Suchwort;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.engine.search.common.BooleanOperator;
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

    public SuchwortSuggestionsModel searchForSearchwordSuggestion(final String singleWordQuery) {
        final var foundSuchwortSuggestions =
            this.doSearchForSearchwordSuggestion(singleWordQuery)
                .map(SuchwortModel::getSuchwort)
                .collect(Collectors.toList());
        final var model = new SuchwortSuggestionsModel();
        model.setSuchwortSuggestions(foundSuchwortSuggestions);
        return model;
    }

    public Stream<SuchwortModel> doSearchForSearchwordSuggestion(final String singleWordQuery) {
        final var wildcardSingleWordQuery = StringUtils.lowerCase(StringUtils.trimToEmpty(singleWordQuery)) + "*";

        return Search
            .session(entityManager.getEntityManagerFactory().createEntityManager())
            .search(Suchwort.class)
            .where(function ->
                function
                    // https://docs.jboss.org/hibernate/stable/search/reference/en-US/html_single/#search-dsl-predicate-wildcard
                    .wildcard()
                    .field("suchwort")
                    .matching(wildcardSingleWordQuery)
            )
            .fetch(MAX_NUMBER_OF_SUGGESTION)
            .hits()
            .stream()
            .map(searchDomainMapper::entity2Model);
    }

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

    protected Stream<? extends BaseEntityModel> doSearchForEntities(
        final List<Class<? extends BaseEntity>> searchableEntities,
        final String searchQuery
    ) {
        final var searchableAttributes = searchPreparationService.getNamesOfSearchableAttributes(searchableEntities);
        final var adaptedSearchQuery = this.createAdaptedSearchQuery(searchQuery);

        return Search
            .session(entityManager.getEntityManagerFactory().createEntityManager())
            .search(searchableEntities)
            .where(function -> {
                if (StringUtils.isNotEmpty(searchQuery)) {
                    return function
                        // https://docs.jboss.org/hibernate/stable/search/reference/en-US/html_single/#search-dsl-predicate-simple-query-string
                        .simpleQueryString()
                        .fields(searchableAttributes)
                        .matching(adaptedSearchQuery)
                        .defaultOperator(BooleanOperator.AND);
                } else {
                    return function.matchAll();
                }
            })
            .sort(function -> function.field("lastModifiedDateTime").desc())
            .fetchAllHits()
            .stream()
            .map(searchDomainMapper::entity2Model);
    }

    protected String createAdaptedSearchQuery(final String searchQuery) {
        var adaptedSearchQuery = StringUtils.trimToEmpty(searchQuery);
        final var singleQueryWords = StringUtils.split(adaptedSearchQuery, StringUtils.SPACE);
        adaptedSearchQuery =
            Arrays
                .stream(singleQueryWords)
                .map(StringUtils::lowerCase)
                // Anfügen eines Wildcardprefix
                .map(lowerCaseQueryWord -> lowerCaseQueryWord + "*")
                // Trennen der Wildcard-Suchwörter mit Leerzeichen
                .collect(Collectors.joining(StringUtils.SPACE));
        log.debug("Die erstellte Suchquery: {}", adaptedSearchQuery);
        return adaptedSearchQuery;
    }
}
