package de.muenchen.isi.domain.service.search;

import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SucheService {

    private final EntityManager entityManager;

    private String[] searchableAttributes;

    public List<? extends BaseEntity> searchForEntities(final String searchQuery) {
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
            .fetchAllHits();
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
        Set<Class<? extends Annotation>> searchIndexAnnotation = Set.of(FullTextField.class);

        final var fieldNamesAbfrage = searchIndexAnnotation
            .stream()
            .flatMap(annotation -> FieldUtils.getFieldsListWithAnnotation(Abfrage.class, annotation).stream())
            .map(Field::getName)
            .collect(Collectors.toSet());
        log.debug("Die Namen der suchbaren Attribute in der Abfrage: {}", fieldNamesAbfrage);

        final var fieldNamesBauvorhaben = searchIndexAnnotation
            .stream()
            .flatMap(annotation -> FieldUtils.getFieldsListWithAnnotation(Bauvorhaben.class, annotation).stream())
            .map(Field::getName)
            .collect(Collectors.toSet());
        log.debug("Die Namen der suchbaren Attribute in der Bauvorhaben: {}", fieldNamesBauvorhaben);

        final var fieldNamesEinrichtung = searchIndexAnnotation
            .stream()
            .flatMap(annotation ->
                FieldUtils.getFieldsListWithAnnotation(Infrastruktureinrichtung.class, annotation).stream()
            )
            .map(Field::getName)
            .collect(Collectors.toSet());
        log.debug("Die Namen der suchbaren Attribute in der Infrastruktureinrichtung: {}", fieldNamesEinrichtung);

        this.searchableAttributes =
            SetUtils
                .union(SetUtils.union(fieldNamesAbfrage, fieldNamesBauvorhaben), fieldNamesEinrichtung)
                .toArray(String[]::new);
        log.debug("Die Namen aller suchbaren Attribute: {}", Arrays.toString(this.searchableAttributes));
    }
}
