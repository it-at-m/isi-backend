package de.muenchen.isi.domain.service.search;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.mapper.SearchDomainMapper;
import de.muenchen.isi.domain.model.enums.SortAttribute;
import de.muenchen.isi.domain.model.search.request.SearchQueryAndSortingModel;
import de.muenchen.isi.domain.model.search.response.SearchResultsModel;
import de.muenchen.isi.infrastructure.entity.BaseEntity;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.engine.search.common.BooleanOperator;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntitySearchService {

    /**
     * Die Zeichen zur Bestimmung der Word Boundaries orientieren sich am Unicode® Standard Annex #29.
     * https://unicode.org/reports/tr29/
     *
     * In der Konstanten befindeliche Word Boundaries:
     * https://unicode.org/reports/tr29/#Newline
     * https://unicode.org/reports/tr29/#ZWJ_WB
     * https://unicode.org/reports/tr29/#Format
     * https://unicode.org/reports/tr29/#ALetter
     * https://unicode.org/reports/tr29/#Single_Quote
     * https://unicode.org/reports/tr29/#Double_Quote
     * https://unicode.org/reports/tr29/#MidNumLet
     * https://unicode.org/reports/tr29/#MidLetter
     * https://unicode.org/reports/tr29/#MidNum
     * https://unicode.org/reports/tr29/#ExtendNumLetWB
     */
    private static final String UNICODE_WORD_BOUNDARIES =
        "- \u000B\u000C\u0085\u2028\u2029\u200D\u02C2\u02C5\u02D2\u02D7\u02DE\u02DF\u02E5\u02EB\u02ED\u02EF\u02FF\u055A\u055B\u055C\u055E\u058A\u05F3\u070F\uA708\uA716\uA720\uA721\uA789\uA78A\uAB5B\u0027\"\u002E\u2018\u2019\u2024\uFE52\uFF07\uFF0E\u003A\u00B7\u0387\u055F\u05F4\u2027\uFE13\uFE55\uFF1A\u066C\uFE50\uFE54\uFF0C\uFF1B\u202F";

    @PersistenceContext
    private EntityManager entityManager;

    private final SearchPreparationService searchPreparationService;

    private final SearchDomainMapper searchDomainMapper;

    /**
     * Diese Methode führt die paginierte Entitätssuche für die im Methodenparameter gegebenen Informationen durch.
     *
     * Falls beide Parameterattribute {@link SearchQueryAndSortingModel#getPage()} und {@link SearchQueryAndSortingModel#getPageSize()}
     * mit einem Wert versehen sind, wird eine paginierte Suche durchgeführt. Ist mindestens eines der eben genannten
     * Parameterattribute nicht gesetzt, so wird eine nicht paginierte Suche durchgeführt.
     *
     * @param searchQueryAndSortingInformation mit der Suchquery, den Sortier- und Seiteninformationen und den zu durchsuchenden Entitäten.
     * @return die Suchergebnisse in der im Methodenparameter definierten Reihenfolge.Die Suchergebnisse sind paginiert
     * sobald beide Parameterattribute {@link SearchQueryAndSortingModel#getPage()} und {@link SearchQueryAndSortingModel#getPageSize()}
     * gesetzt sind. Ist mindestens eines der eben genannten Parameterattribute nicht gesetzt, so sind die Suchergebnisse nicht paginiert.
     * @throws EntityNotFoundException falls keine zu durchsuchende Entität im Methodenparameter gewählt ist.
     */
    public SearchResultsModel searchForEntities(final SearchQueryAndSortingModel searchQueryAndSortingInformation)
        throws EntityNotFoundException {
        // Ermittlung der suchbaren Entitäten
        final List<Class<? extends BaseEntity>> searchableEntities = searchPreparationService.getSearchableEntities(
            searchQueryAndSortingInformation
        );
        // Ermittlung der suchbaren Attribute je suchbarer Entität
        final var searchableAttributes = searchPreparationService.getNamesOfSearchableAttributes(searchableEntities);
        // Anpassen der Suchquery
        final var adaptedSearchQuery =
            this.createAdaptedSearchQueryForSimpleQueryStringSearch(searchQueryAndSortingInformation.getSearchQuery());

        // Der Offset oder null falls keine Offsetberechnung möglich ist.
        // Ist keine Offsetberechnung möglich, so wird auch keine paginierte Suche durchgeführt.
        final Integer paginationOffset = calculateOffsetOrNullIfNoPaginationRequired(searchQueryAndSortingInformation);

        // Erstellen der Suchquery
        final var searchQueryOptions = Search
            .session(entityManager)
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
            });

        // Ausführen einer paginierten oder nicht-paginierten Suche.
        final SearchResult<BaseEntity> searchResult = ObjectUtils.isNotEmpty(paginationOffset)
            ? searchQueryOptions.fetch(paginationOffset, searchQueryAndSortingInformation.getPageSize())
            : searchQueryOptions.fetchAll();

        // Suchergebnisse extrahieren und zurückgeben.
        final var searchResults = searchResult
            .hits()
            .stream()
            .map(searchDomainMapper::entity2SearchResultModel)
            .collect(Collectors.toList());

        final var model = new SearchResultsModel();
        model.setSearchResults(searchResults);
        if (ObjectUtils.isNotEmpty(paginationOffset)) {
            final long numberOfTotalHits = searchResult.total().hitCount();
            final var numberOfPages = calculateNumberOfPages(
                numberOfTotalHits,
                searchQueryAndSortingInformation.getPageSize()
            );
            model.setNumberOfPages(numberOfPages);
            final var currentPage = searchQueryAndSortingInformation.getPage() > numberOfPages
                ? numberOfPages
                : searchQueryAndSortingInformation.getPage();
            model.setPage(currentPage);
        }
        return model;
    }

    /**
     * Diese Methode passt die im Parameter gegebene Suchquery an,
     * um diese bei der Simple-Query-String-Suche verwenden zu können.
     *
     * @param searchQuery zum anpassen.
     * @return die für die Simple-Query-String-Suche angepasste Suchquery.
     */
    protected String createAdaptedSearchQueryForSimpleQueryStringSearch(final String searchQuery) {
        final var splittedSearchQuery = StringUtils.split(
            StringUtils.trimToEmpty(searchQuery),
            UNICODE_WORD_BOUNDARIES
        );
        final var adaptedSearchQuery = Arrays
            .stream(splittedSearchQuery)
            .map(searchQueryArtifact -> searchQueryArtifact + "*")
            .collect(Collectors.joining(StringUtils.SPACE));
        log.debug("Die erstellte Suchquery: {}", adaptedSearchQuery);
        return adaptedSearchQuery;
    }

    /**
     * Die Methode berechnet den Offset für die paginierte Suche.
     *
     * @param searchQueryAndSortingModel zur Ermittlung des Offset.
     * @return ermittelt den Offset oder gibt null zurück, falls keine Offsetberechnung möglich ist.
     */
    protected Integer calculateOffsetOrNullIfNoPaginationRequired(
        final SearchQueryAndSortingModel searchQueryAndSortingModel
    ) {
        final var page = searchQueryAndSortingModel.getPage();
        final var pageSize = searchQueryAndSortingModel.getPageSize();
        final Integer offset;
        if (ObjectUtils.isNotEmpty(page) && ObjectUtils.isNotEmpty(pageSize)) {
            offset = (page - 1) * pageSize;
        } else {
            offset = null;
        }
        return offset;
    }

    protected Long calculateNumberOfPages(final long numberOfTotalHits, final int pageSize) {
        final long numberOfPages;
        if (numberOfTotalHits % pageSize == 0) {
            numberOfPages = numberOfTotalHits / pageSize;
        } else {
            numberOfPages = numberOfTotalHits / pageSize + 1;
        }
        return numberOfPages;
    }
}
