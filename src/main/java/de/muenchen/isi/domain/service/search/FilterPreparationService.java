package de.muenchen.isi.domain.service.search;

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
        final var filterableAttributes = new HashSet<String>();
        filterableAttributes.add("verortung.stadtbezirke.name");
        filterableAttributes.add("verortung.kitaplanungsbereiche.kitaPlbT");
        filterableAttributes.add("verortung.grundschulsprengel.nummer");
        filterableAttributes.add("verortung.mittelschulsprengel.nummer");
        return filterableAttributes;
    }

    protected static Set<String> getNamesOfFilterableAttributesForBaugenehmigungsverfahren() {
        final var filterableAttributes = new HashSet<String>();
        filterableAttributes.add("verortung.stadtbezirke.name");
        filterableAttributes.add("verortung.kitaplanungsbereiche.kitaPlbT");
        filterableAttributes.add("verortung.grundschulsprengel.nummer");
        filterableAttributes.add("verortung.mittelschulsprengel.nummer");
        return filterableAttributes;
    }

    protected static Set<String> getNamesOfFilterableAttributesForWeiteresVerfahren() {
        final var filterableAttributes = new HashSet<String>();
        filterableAttributes.add("verortung.stadtbezirke.name");
        filterableAttributes.add("verortung.kitaplanungsbereiche.kitaPlbT");
        filterableAttributes.add("verortung.grundschulsprengel.nummer");
        filterableAttributes.add("verortung.mittelschulsprengel.nummer");
        return filterableAttributes;
    }

    protected static Set<String> getNamesOfFilterableAttributesForBauvorhaben() {
        final var filterableAttributes = new HashSet<String>();
        filterableAttributes.add("verortung.stadtbezirke.name");
        filterableAttributes.add("verortung.kitaplanungsbereiche.kitaPlbT");
        filterableAttributes.add("verortung.grundschulsprengel.nummer");
        filterableAttributes.add("verortung.mittelschulsprengel.nummer");
        return filterableAttributes;
    }

    protected static Set<String> getNamesOfFilterableAttributesForInfrastruktureinrichtung() {
        final var filterableAttributes = new HashSet<String>();
        filterableAttributes.add("verortung.stadtbezirke.name");
        filterableAttributes.add("verortung.kitaplanungsbereiche.kitaPlbT");
        filterableAttributes.add("verortung.grundschulsprengel.nummer");
        filterableAttributes.add("verortung.mittelschulsprengel.nummer");
        return filterableAttributes;
    }
}
