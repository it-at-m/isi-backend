package de.muenchen.isi.domain.service.search;

import de.muenchen.isi.domain.model.search.request.SearchQueryAndSortingModel;
import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.Baugenehmigungsverfahren;
import de.muenchen.isi.infrastructure.entity.Bauleitplanverfahren;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.WeiteresVerfahren;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Grundschule;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.GsNachmittagBetreuung;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.HausFuerKinder;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kindergarten;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kinderkrippe;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Mittelschule;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.GenericBooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilterPreparationService {

    public String[] getNamesOfFilterableAttributes(final List<Class<? extends BaseEntity>> filterableEntities) {
        final var filterableAttributes = new HashSet<String>();

        if (CollectionUtils.containsAny(filterableEntities, Set.of(Bauleitplanverfahren.class))) {
            filterableAttributes.addAll(getNamesOfFilterableAttributesForBauleitplanverfahren());
        }
        if (CollectionUtils.containsAny(filterableEntities, Set.of(Baugenehmigungsverfahren.class))) {
            filterableAttributes.addAll(getNamesOfFilterableAttributesForBaugenehmigungsverfahren());
        }
        if (CollectionUtils.containsAny(filterableEntities, Set.of(WeiteresVerfahren.class))) {
            filterableAttributes.addAll(getNamesOfFilterableAttributesForWeiteresVerfahren());
        }
        if (CollectionUtils.containsAny(filterableEntities, Set.of(Bauvorhaben.class))) {
            filterableAttributes.addAll(getNamesOfFilterableAttributesForBauvorhaben());
        }
        if (
            CollectionUtils.containsAny(
                filterableEntities,
                Set.of(
                    Grundschule.class,
                    GsNachmittagBetreuung.class,
                    HausFuerKinder.class,
                    Kindergarten.class,
                    Kinderkrippe.class,
                    Mittelschule.class
                )
            )
        ) {
            filterableAttributes.addAll(getNamesOfFilterableAttributesForInfrastruktureinrichtung());
        }

        return filterableAttributes.toArray(String[]::new);
    }

    protected static Set<String> getNamesOfFilterableAttributesForBauleitplanverfahren() {
        final var filterableAttributes = new HashSet<>(getNamesOfFilterableVerortungAttributes());
        return filterableAttributes;
    }

    protected static Set<String> getNamesOfFilterableAttributesForBaugenehmigungsverfahren() {
        final var filterableAttributes = new HashSet<>(getNamesOfFilterableVerortungAttributes());
        return filterableAttributes;
    }

    protected static Set<String> getNamesOfFilterableAttributesForWeiteresVerfahren() {
        final var filterableAttributes = new HashSet<>(getNamesOfFilterableVerortungAttributes());
        return filterableAttributes;
    }

    protected static Set<String> getNamesOfFilterableAttributesForBauvorhaben() {
        final var filterableAttributes = new HashSet<>(getNamesOfFilterableVerortungAttributes());
        return filterableAttributes;
    }

    protected static Set<String> getNamesOfFilterableAttributesForInfrastruktureinrichtung() {
        final var filterableAttributes = new HashSet<>(getNamesOfFilterableVerortungAttributes());
        return filterableAttributes;
    }

    protected static Set<String> getNamesOfFilterableVerortungAttributes() {
        final var filterableAttributes = new HashSet<String>();
        filterableAttributes.add("verortung.stadtbezirke.name");
        filterableAttributes.add("verortung.kitaplanungsbereiche.kitaPlbT");
        filterableAttributes.add("verortung.grundschulsprengel.nummer");
        filterableAttributes.add("verortung.mittelschulsprengel.nummer");
        return filterableAttributes;
    }

    /**
     * Mittels ausschließlicher "must"-Verkettung wird verundet: https://docs.jboss.org/hibernate/search/7.0/reference/en-US/html_single/#search-dsl-predicate-boolean-and
     *
     * Mittels ausschließlicher "should"-Verkettung wird verodert: https://docs.jboss.org/hibernate/search/7.0/reference/en-US/html_single/#search-dsl-predicate-boolean-or
     *
     * Vorgehen in Methode:
     *
     * Die unterschiedlichen Attribute wie "verortung.stadtbezirke.name", "verortung.kitaplanungsbereiche.kitaPlbT", ... werden verundet.
     *
     * Die einzelnene Werte für ein Attribut werden verodert.
     *
     * @param searchPredicateFactory
     * @param searchQueryAndSortingInformation
     * @return
     */
    public GenericBooleanPredicateClausesStep createFilterFunction(
        final SearchPredicateFactory searchPredicateFactory,
        final SearchQueryAndSortingModel searchQueryAndSortingInformation
    ) {
        var booleanPredicateSteps = searchPredicateFactory.bool();
        if (CollectionUtils.isNotEmpty(searchQueryAndSortingInformation.getFilterStadtbezirkNummer())) {
            booleanPredicateSteps =
                booleanPredicateSteps.must(f -> {
                    var bool = f.bool();
                    for (final var nummerStadtbezirk : searchQueryAndSortingInformation.getFilterStadtbezirkNummer()) {
                        bool =
                            bool.should(f2 ->
                                f2.match().field("verortung.stadtbezirke.nummer").matching(nummerStadtbezirk)
                            );
                    }
                    return bool;
                });
        }
        if (CollectionUtils.isNotEmpty(searchQueryAndSortingInformation.getFilterKitaplanungsbereichKitaPlbT())) {
            booleanPredicateSteps =
                booleanPredicateSteps.must(f -> {
                    var bool = f.bool();
                    for (final var kitaPlb : searchQueryAndSortingInformation.getFilterKitaplanungsbereichKitaPlbT()) {
                        bool =
                            bool.should(f2 ->
                                f2.match().field("verortung.kitaplanungsbereiche.kitaPlbT").matching(kitaPlb)
                            );
                    }
                    return bool;
                });
        }
        if (CollectionUtils.isNotEmpty(searchQueryAndSortingInformation.getFilterGrundschulsprengelNummer())) {
            booleanPredicateSteps =
                booleanPredicateSteps.must(f -> {
                    var bool = f.bool();
                    for (final var grundschulsprengel : searchQueryAndSortingInformation.getFilterGrundschulsprengelNummer()) {
                        bool =
                            bool.should(f2 ->
                                f2.match().field("verortung.grundschulsprengel.nummer").matching(grundschulsprengel)
                            );
                    }
                    return bool;
                });
        }
        if (CollectionUtils.isNotEmpty(searchQueryAndSortingInformation.getFilterMittelschulsprengelNummer())) {
            booleanPredicateSteps =
                booleanPredicateSteps.must(f -> {
                    var bool = f.bool();
                    for (final var mittelschulsprengel : searchQueryAndSortingInformation.getFilterMittelschulsprengelNummer()) {
                        bool =
                            bool.should(f2 ->
                                f2.match().field("verortung.mittelschulsprengel.nummer").matching(mittelschulsprengel)
                            );
                    }
                    return bool;
                });
        }
        return booleanPredicateSteps;
    }

    public boolean shouldBeFiltered(final SearchQueryAndSortingModel searchQueryAndSortingInformation) {
        return (
            CollectionUtils.isNotEmpty(searchQueryAndSortingInformation.getFilterStadtbezirkNummer()) ||
            CollectionUtils.isNotEmpty(searchQueryAndSortingInformation.getFilterKitaplanungsbereichKitaPlbT()) ||
            CollectionUtils.isNotEmpty(searchQueryAndSortingInformation.getFilterGrundschulsprengelNummer()) ||
            CollectionUtils.isNotEmpty(searchQueryAndSortingInformation.getFilterMittelschulsprengelNummer())
        );
    }
}
