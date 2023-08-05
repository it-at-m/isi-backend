package de.muenchen.isi.domain.service.search;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.mapper.SearchDomainMapper;
import de.muenchen.isi.domain.model.BaseEntityModel;
import de.muenchen.isi.domain.model.enums.SortAttribute;
import de.muenchen.isi.domain.model.search.request.SearchQueryAndSortingModel;
import de.muenchen.isi.domain.model.search.response.SearchResultsModel;
import de.muenchen.isi.infrastructure.entity.BaseEntity;
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
public class EntitySearchService {

    private final EntityManager entityManager;

    private final SearchPreparationService searchPreparationService;

    private final SearchDomainMapper searchDomainMapper;

    /**
     *
     *
     *
     * @param searchQueryAndSortingInformation mit der Suchquery, den Sortierinformationen und den zu durchsuchenden Entitäten.
     * @return die Suchergebnisse in einer durch den Request definierten Reihenfolge.
     * @throws EntityNotFoundException
     */
    public SearchResultsModel searchForEntities(final SearchQueryAndSortingModel searchQueryAndSortingInformation)
        throws EntityNotFoundException {
        final var searchResults =
            this.doSearchForEntities(searchQueryAndSortingInformation)
                .map(searchDomainMapper::model2SearchResultModel)
                .collect(Collectors.toList());
        final var model = new SearchResultsModel();
        model.setSearchResults(searchResults);
        return model;
    }

    /**
     * Diese Methode führt die Entitätssuche für die im Parameter gegebene Entitäten auf Basis der Suchquery durch.
     *
     * @param searchQueryAndSortingInformation mit der Suchquery, den Sortierinformationen und den zu durchsuchenden Entitäten.
     * @return die Suchergebnisse in einer durch den Request definierten Reihenfolge.
     * @throws EntityNotFoundException
     */
    protected Stream<? extends BaseEntityModel> doSearchForEntities(
        final SearchQueryAndSortingModel searchQueryAndSortingInformation
    ) throws EntityNotFoundException {
        // Ermittlung der suchbaren Entitäten
        final List<Class<? extends BaseEntity>> searchableEntities = searchPreparationService.getSearchableEntities(
            searchQueryAndSortingInformation
        );
        // Ermittlung der suchbaren Attribute je suchbarer Entität
        final var searchableAttributes = searchPreparationService.getNamesOfSearchableAttributes(searchableEntities);
        // Anpassen der Suchquery
        final var adaptedSearchQuery =
            this.createAdaptedSearchQueryForSimpleQueryStringSearch(searchQueryAndSortingInformation.getSearchQuery());

        return Search
            .session(entityManager.getEntityManagerFactory().createEntityManager())
            .search(searchableEntities)
            .where(function -> {
                if (StringUtils.isNotEmpty(adaptedSearchQuery)) {
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
            // Sortierung der Suchergebnisse.
            // https://docs.jboss.org/hibernate/stable/search/reference/en-US/html_single/#query-sorting
            .sort(function -> {
                final var sortBy = searchQueryAndSortingInformation.getSortBy();
                final var sortOrder = searchQueryAndSortingInformation.getSortOrder();
                if (SortAttribute.NAME.equals(sortBy)) {
                    return function.field("name_sort").order(sortOrder);
                } else if (SortAttribute.CREATED_DATE_TIME.equals(sortBy)) {
                    return function.field("createdDateTime").order(sortOrder);
                } else {
                    return function.field("lastModifiedDateTime").order(sortOrder);
                }
            })
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
