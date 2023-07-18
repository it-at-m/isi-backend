package de.muenchen.isi.domain.service.search;

import de.muenchen.isi.domain.mapper.SearchDomainMapper;
import de.muenchen.isi.domain.model.BaseEntityModel;
import de.muenchen.isi.domain.model.search.SearchQueryForEntitiesModel;
import de.muenchen.isi.domain.model.search.SearchResultsModel;
import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Grundschule;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.GsNachmittagBetreuung;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.HausFuerKinder;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kindergarten;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kinderkrippe;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Mittelschule;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.search.engine.search.common.BooleanOperator;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SucheService {

    private static final Set<Class<? extends Annotation>> SEARCH_INDEX_ANNOTATION = Set.of(
        IndexedEmbedded.class,
        FullTextField.class
    );

    private final EntityManager entityManager;

    private final SearchDomainMapper searchDomainMapper;

    public SearchResultsModel searchForEntities(final SearchQueryForEntitiesModel searchQueryInformation) {
        final List<Class<? extends BaseEntity>> searchableEntities = getSearchableEntities(searchQueryInformation);
        final var searchResults =
            this.searchForEntities(searchableEntities, searchQueryInformation.getSearchQuery())
                .map(searchDomainMapper::model2SearchResultModel)
                .collect(Collectors.toList());
        final var model = new SearchResultsModel();
        model.setSearchResults(searchResults);
        return model;
    }

    protected Stream<? extends BaseEntityModel> searchForEntities(
        final List<Class<? extends BaseEntity>> searchableEntities,
        final String searchQuery
    ) {
        final var searchableAttributes = getNamesOfSearchableAttributes(searchableEntities);
        final var adaptedSearchQuery = this.createAdaptedSearchQuery(searchQuery);

        return Search
            .session(entityManager.getEntityManagerFactory().createEntityManager())
            .search(searchableEntities)
            .where(function ->
                function
                    // https://docs.jboss.org/hibernate/stable/search/reference/en-US/html_single/#search-dsl-predicate-simple-query-string
                    .simpleQueryString()
                    .fields(searchableAttributes)
                    .matching(adaptedSearchQuery)
                    .defaultOperator(BooleanOperator.AND)
            )
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
                // Abfügen eines Wildcardprefix
                .map(lowerCaseQueryWord -> lowerCaseQueryWord + "*")
                // Trennen der Wildcard-Suchwörter mit Leerzeichen
                .collect(Collectors.joining(StringUtils.SPACE));
        log.debug("Die erstellte Suchquery: {}", adaptedSearchQuery);
        return adaptedSearchQuery;
    }

    protected String[] getNamesOfSearchableAttributes(List<Class<? extends BaseEntity>> searchableEntities) {
        final var searchableAttributes = searchableEntities
            .stream()
            .flatMap(classSearchablEntity ->
                this.getNamesOfSearchableAttributes(classSearchablEntity, SEARCH_INDEX_ANNOTATION, "").stream()
            )
            .distinct()
            .toArray(String[]::new);
        log.debug("Die Namen aller suchbaren Attribute: {}", Arrays.toString(searchableAttributes));
        return searchableAttributes;
    }

    protected Set<String> getNamesOfSearchableAttributes(
        final Class<?> clazz,
        final Collection<Class<? extends Annotation>> searchIndexAnnotation,
        final String attributePath
    ) {
        final var fieldNamesAbfrage = searchIndexAnnotation
            .stream()
            .flatMap(annotation -> {
                final Stream<Field> fields = FieldUtils.getFieldsListWithAnnotation(clazz, annotation).stream();
                final String fieldDivider = StringUtils.isEmpty(attributePath) ? "" : ".";
                if (annotation.equals(IndexedEmbedded.class)) {
                    return fields.flatMap(field -> {
                        final var newClassLevel = attributePath + fieldDivider + field.getName();
                        return this.getNamesOfSearchableAttributes(
                                field.getType(),
                                searchIndexAnnotation,
                                newClassLevel
                            )
                            .stream();
                    });
                } else {
                    return fields.map(field -> attributePath + fieldDivider + field.getName());
                }
            })
            .collect(Collectors.toSet());
        log.debug("Die Namen der suchbaren Attribute in {}: {}", clazz.getSimpleName(), fieldNamesAbfrage);
        return fieldNamesAbfrage;
    }

    protected List<Class<? extends BaseEntity>> getSearchableEntities(
        final SearchQueryForEntitiesModel searchQueryInformation
    ) {
        final List<Class<? extends BaseEntity>> searchableEntities = new ArrayList<>();
        if (BooleanUtils.isTrue(searchQueryInformation.getSelectInfrastrukturabfrage())) {
            searchableEntities.add(Infrastrukturabfrage.class);
        }
        if (BooleanUtils.isTrue(searchQueryInformation.getSelectBauvorhaben())) {
            searchableEntities.add(Bauvorhaben.class);
        }
        if (BooleanUtils.isTrue(searchQueryInformation.getSelectInfrastruktureinrichtung())) {
            searchableEntities.addAll(
                List.of(
                    Grundschule.class,
                    GsNachmittagBetreuung.class,
                    HausFuerKinder.class,
                    Kindergarten.class,
                    Kinderkrippe.class,
                    Mittelschule.class
                )
            );
        }
        return searchableEntities;
    }
}
