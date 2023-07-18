package de.muenchen.isi.domain.service.search;

import de.muenchen.isi.domain.mapper.SearchDomainMapper;
import de.muenchen.isi.domain.model.BaseEntityModel;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.SetUtils;
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

    private final EntityManager entityManager;

    private String[] searchableAttributes;

    private SearchDomainMapper searchDomainMapper;

    public List<? extends BaseEntityModel> searchForEntities(final String searchQuery) {
        final var adaptedSearchQuery = this.createAdaptedSearchQuery(searchQuery);

        final var searchSession = Search.session(entityManager.getEntityManagerFactory().createEntityManager());

        return searchSession
            .search(List.of(Infrastrukturabfrage.class, Bauvorhaben.class, Infrastruktureinrichtung.class))
            .where(f ->
                f
                    // https://docs.jboss.org/hibernate/stable/search/reference/en-US/html_single/#search-dsl-predicate-simple-query-string
                    .simpleQueryString()
                    .fields(searchableAttributes)
                    .matching(adaptedSearchQuery)
                    .defaultOperator(BooleanOperator.AND)
            )
            .fetchAllHits()
            .stream()
            .map(searchDomainMapper::entity2Model)
            .collect(Collectors.toList());
    }

    protected String createAdaptedSearchQuery(final String searchQuery) {
        var adaptedSearchQuery = StringUtils.trimToEmpty(searchQuery);
        final var singleQueryWords = StringUtils.split(adaptedSearchQuery, StringUtils.SPACE);
        adaptedSearchQuery =
            Arrays
                .stream(singleQueryWords)
                // Abfügen eines Wildcardprefix
                .map(queryWord -> queryWord + "*")
                // Trennen der Wildcard-Suchwörter mit Leerzeichen
                .collect(Collectors.joining(StringUtils.SPACE));
        log.debug("Die erstellte Suchquery: {}", adaptedSearchQuery);
        return adaptedSearchQuery;
    }

    @PostConstruct
    protected void setNamesOfSearchableAttributes() {
        Set<Class<? extends Annotation>> searchIndexAnnotation = Set.of(IndexedEmbedded.class, FullTextField.class);

        final var fieldNamesAbfrage = getNamesOfSearchableAttributes(
            Infrastrukturabfrage.class,
            searchIndexAnnotation,
            ""
        );
        final var fieldNamesBauvorhaben = getNamesOfSearchableAttributes(Bauvorhaben.class, searchIndexAnnotation, "");
        final var fieldNamesEinrichtung = getNamesOfSearchableAttributes(
            Infrastruktureinrichtung.class,
            searchIndexAnnotation,
            ""
        );

        this.searchableAttributes =
            SetUtils
                .union(SetUtils.union(fieldNamesAbfrage, fieldNamesBauvorhaben), fieldNamesEinrichtung)
                .toArray(String[]::new);
        log.debug("Die Namen aller suchbaren Attribute: {}", Arrays.toString(this.searchableAttributes));
    }

    protected Set<String> getNamesOfSearchableAttributes(
        final Class<?> clazz,
        final Collection<Class<? extends Annotation>> searchIndexAnnotation,
        final String classLevel
    ) {
        final var fieldNamesAbfrage = searchIndexAnnotation
            .stream()
            .flatMap(annotation -> {
                final Stream<Field> fields = FieldUtils.getFieldsListWithAnnotation(clazz, annotation).stream();
                final String fieldDivider = StringUtils.isEmpty(classLevel) ? "" : ".";
                if (annotation.equals(IndexedEmbedded.class)) {
                    return fields.flatMap(field -> {
                        final var newClassLevel = classLevel + fieldDivider + field.getName();
                        return this.getNamesOfSearchableAttributes(
                                field.getType(),
                                searchIndexAnnotation,
                                newClassLevel
                            )
                            .stream();
                    });
                } else {
                    return fields.map(field -> classLevel + fieldDivider + field.getName());
                }
            })
            .collect(Collectors.toSet());
        log.debug("Die Namen der suchbaren Attribute in {}: {}", clazz.getSimpleName(), fieldNamesAbfrage);
        return fieldNamesAbfrage;
    }
}
