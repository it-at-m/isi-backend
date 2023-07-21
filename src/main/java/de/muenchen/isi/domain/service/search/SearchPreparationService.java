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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchPreparationService {

    protected String[] getNamesOfSearchableAttributes(final List<Class<? extends BaseEntity>> searchableEntities) {
        final var searchableAttributes = new HashSet<String>();
        if (CollectionUtils.containsAny(searchableEntities, Set.of(Infrastrukturabfrage.class))) {
            searchableAttributes.addAll(this.getNamesOfSearchableAttributesForInfrastrukturabfrage());
        }
        if (CollectionUtils.containsAny(searchableEntities, Set.of(Bauvorhaben.class))) {
            searchableAttributes.addAll(this.getNamesOfSearchableAttributesForBauvorhaben());
        }
        if (
            CollectionUtils.containsAny(
                searchableEntities,
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
            searchableAttributes.addAll(this.getNamesOfSearchableAttributesForInfrastruktureinrichtung());
        }
        log.debug("Die Namen aller suchbaren Attribute: {}", searchableAttributes);
        return searchableAttributes.toArray(String[]::new);
    }

    protected Set<String> getNamesOfSearchableAttributesForInfrastrukturabfrage() {
        final var searchableAttributes = new HashSet<String>();
        searchableAttributes.add("abfrage.adresse.strasseHausnummer");
        searchableAttributes.add("abfrage.verortung.stadtbezirke.name");
        searchableAttributes.add("abfrage.verortung.gemarkungen.name");
        searchableAttributes.add("abfrage.verortung.gemarkungen.flurstuecke.nummer");
        searchableAttributes.add("abfrage.statusAbfrage");
        searchableAttributes.add("abfrage.bebauungsplannummer");
        searchableAttributes.add("abfrage.nameAbfrage");
        searchableAttributes.add("abfragevarianten.realisierungVon");
        searchableAttributes.add("abfragevariantenSachbearbeitung.realisierungVon");
        log.debug("Die Namen aller suchbaren Attribute einer Infrastrukturabfrage: {}", searchableAttributes);
        return searchableAttributes;
    }

    protected Set<String> getNamesOfSearchableAttributesForBauvorhaben() {
        final var searchableAttributes = new HashSet<String>();
        searchableAttributes.add("nameVorhaben");
        searchableAttributes.add("standVorhaben");
        searchableAttributes.add("bauvorhabenNummer");
        searchableAttributes.add("adresse.strasseHausnummer");
        searchableAttributes.add("verortung.stadtbezirke.name");
        searchableAttributes.add("verortung.gemarkungen.name");
        searchableAttributes.add("verortung.gemarkungen.flurstuecke.nummer");
        searchableAttributes.add("bebauungsplannummer");
        log.debug("Die Namen aller suchbaren Attribute eines Bauvorhabens: {}", searchableAttributes);
        return searchableAttributes;
    }

    protected Set<String> getNamesOfSearchableAttributesForInfrastruktureinrichtung() {
        final var searchableAttributes = new HashSet<String>();
        searchableAttributes.add("adresse.strasseHausnummer");
        searchableAttributes.add("nameEinrichtung");
        searchableAttributes.add("status");
        log.debug("Die Namen aller suchbaren Attribute einer Infrastruktureinrichtung: {}", searchableAttributes);
        return searchableAttributes;
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
