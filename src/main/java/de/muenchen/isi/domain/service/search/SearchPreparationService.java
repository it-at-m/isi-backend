package de.muenchen.isi.domain.service.search;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.search.SearchQueryForEntitiesModel;
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
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.NonStandardField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ScaledNumberField;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchPreparationService {

    private static final Set<Class<? extends Annotation>> SEARCH_INDEX_ANNOTATION = Set.of(
        IndexedEmbedded.class,
        GenericField.class,
        FullTextField.class,
        KeywordField.class,
        ScaledNumberField.class,
        NonStandardField.class
    );

    protected String[] getNamesOfSearchableAttributes(List<Class<? extends BaseEntity>> searchableEntities) {
        final var searchableAttributes = searchableEntities
            .stream()
            .flatMap(classSearchablEntity ->
                this.getNamesOfSearchableAttributes(classSearchablEntity, SEARCH_INDEX_ANNOTATION, "").stream()
            )
            .collect(Collectors.toSet());
        if (searchableEntities.contains(Infrastrukturabfrage.class)) {
            searchableAttributes.add("abfrage.adresse.strasseHausnummer");
        }
        if (
            searchableEntities.contains(Infrastrukturabfrage.class) &&
            searchableEntities.size() > 1 ||
            !searchableEntities.contains(Infrastrukturabfrage.class) &&
            searchableEntities.size() > 0
        ) {
            searchableAttributes.add("adresse.strasseHausnummer");
        }
        searchableAttributes.removeAll(Set.of("createdDateTime", "lastModifiedDateTime"));
        log.debug("Die Namen aller suchbaren Attribute: {}", searchableAttributes);
        return searchableAttributes.toArray(String[]::new);
    }

    protected Set<String> getNamesOfSearchableAttributes(
        final Class<?> clazz,
        final Collection<Class<? extends Annotation>> searchIndexAnnotation,
        final String attributePath
    ) {
        final var fieldNames = searchIndexAnnotation
            .stream()
            .flatMap(annotation -> {
                final Stream<Field> fields = FieldUtils.getFieldsListWithAnnotation(clazz, annotation).stream();
                final String fieldDivider = StringUtils.isEmpty(attributePath) ? "" : ".";
                if (annotation.equals(IndexedEmbedded.class)) {
                    return fields.flatMap(field -> {
                        final Class<?> fieldType;
                        if (Collection.class.isAssignableFrom(field.getType())) {
                            final var genericType = (ParameterizedType) field.getGenericType();
                            fieldType = (Class<?>) genericType.getActualTypeArguments()[0];
                        } else {
                            fieldType = field.getType();
                        }
                        final var newClassLevel = attributePath + fieldDivider + field.getName();
                        return this.getNamesOfSearchableAttributes(fieldType, searchIndexAnnotation, newClassLevel)
                            .stream();
                    });
                } else {
                    return fields.map(field -> attributePath + fieldDivider + field.getName());
                }
            })
            .collect(Collectors.toSet());
        log.debug("Die Namen der suchbaren Attribute in {}: {}", clazz.getSimpleName(), fieldNames);
        return fieldNames;
    }

    protected List<Class<? extends BaseEntity>> getSearchableEntities(
        final SearchQueryForEntitiesModel searchQueryInformation
    ) throws EntityNotFoundException {
        final List<Class<? extends BaseEntity>> searchableEntities = new ArrayList<>();
        if (BooleanUtils.isTrue(searchQueryInformation.getSelectInfrastrukturabfrage())) {
            searchableEntities.add(Infrastrukturabfrage.class);
        }
        if (BooleanUtils.isTrue(searchQueryInformation.getSelectBauvorhaben())) {
            searchableEntities.add(Bauvorhaben.class);
        }
        if (BooleanUtils.isTrue(searchQueryInformation.getSelectGrundschule())) {
            searchableEntities.add(Grundschule.class);
        }
        if (BooleanUtils.isTrue(searchQueryInformation.getSelectGsNachmittagBetreuung())) {
            searchableEntities.add(GsNachmittagBetreuung.class);
        }
        if (BooleanUtils.isTrue(searchQueryInformation.getSelectHausFuerKinder())) {
            searchableEntities.add(HausFuerKinder.class);
        }
        if (BooleanUtils.isTrue(searchQueryInformation.getSelectKindergarten())) {
            searchableEntities.add(Kindergarten.class);
        }
        if (BooleanUtils.isTrue(searchQueryInformation.getSelectKinderkrippe())) {
            searchableEntities.add(Kinderkrippe.class);
        }
        if (BooleanUtils.isTrue(searchQueryInformation.getSelectMittelschule())) {
            searchableEntities.add(Mittelschule.class);
        }
        if (searchableEntities.isEmpty()) {
            final var message = "Es wurde keine Entität für die Suche markiert.";
            final var exception = new EntityNotFoundException(message);
            log.error(message, exception);
            throw exception;
        }
        return searchableEntities;
    }
}