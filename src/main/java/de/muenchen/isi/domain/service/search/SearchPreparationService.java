package de.muenchen.isi.domain.service.search;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.search.request.SearchQueryModel;
import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Grundschule;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.GsNachmittagBetreuung;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.HausFuerKinder;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kindergarten;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kinderkrippe;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Mittelschule;
import de.muenchen.isi.infrastructure.repository.search.SearchwordSuggesterRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchPreparationService {

    /**
     * Diese Methode ermittelt auf Basis der im Parameter gegebenen Entitätsklasse
     * die für die Suchwortvorschläge suchbaren Attribute.
     *
     * @param searchableEntity zur Ermittlung der suchbaren Attribute.
     * @return die suchbaren Attribute der im Parameter gegebenen Entitätsklasse.
     */
    public Set<String> getNamesOfSearchableAttributesForSearchwordSuggestion(
        final Class<? extends BaseEntity> searchableEntity
    ) {
        return Arrays
            .stream(getNamesOfSearchableAttributes(List.of(searchableEntity)))
            .map(searchableAttribute ->
                searchableAttribute + SearchwordSuggesterRepository.ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION
            )
            .collect(Collectors.toSet());
    }

    /**
     * Diese Methode ermittelt auf Basis der im Parameter gegebenen Entitätsklassen die dazugehörigen suchbaren Attribute.
     *
     * @param searchableEntities zur Ermittlung der suchbaren Attribute.
     * @return die suchbaren Attribute.
     */
    public String[] getNamesOfSearchableAttributes(final List<Class<? extends BaseEntity>> searchableEntities) {
        final var searchableAttributes = new HashSet<String>();
        if (CollectionUtils.containsAny(searchableEntities, Set.of(Infrastrukturabfrage.class))) {
            searchableAttributes.addAll(getNamesOfSearchableAttributesForInfrastrukturabfrage());
        }
        if (CollectionUtils.containsAny(searchableEntities, Set.of(Bauvorhaben.class))) {
            searchableAttributes.addAll(getNamesOfSearchableAttributesForBauvorhaben());
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
            searchableAttributes.addAll(getNamesOfSearchableAttributesForInfrastruktureinrichtung());
        }
        return searchableAttributes.toArray(String[]::new);
    }

    protected static Set<String> getNamesOfSearchableAttributesForInfrastrukturabfrage() {
        final var searchableAttributes = new HashSet<String>();
        searchableAttributes.add("abfrage.adresse.strasse");
        searchableAttributes.add("abfrage.adresse.hausnummer");
        searchableAttributes.add("abfrage.verortung.stadtbezirke.name");
        searchableAttributes.add("abfrage.verortung.gemarkungen.name");
        searchableAttributes.add("abfrage.verortung.gemarkungen.flurstuecke.nummer");
        searchableAttributes.add("abfrage.statusAbfrage");
        searchableAttributes.add("abfrage.bebauungsplannummer");
        searchableAttributes.add("abfrage.nameAbfrage");
        searchableAttributes.add("abfragevarianten.realisierungVon");
        searchableAttributes.add("abfragevariantenSachbearbeitung.realisierungVon");
        return searchableAttributes;
    }

    protected static Set<String> getNamesOfSearchableAttributesForBauvorhaben() {
        final var searchableAttributes = new HashSet<String>();
        searchableAttributes.add("nameVorhaben");
        searchableAttributes.add("standVerfahren");
        searchableAttributes.add("bauvorhabenNummer");
        searchableAttributes.add("adresse.strasse");
        searchableAttributes.add("adresse.hausnummer");
        searchableAttributes.add("verortung.stadtbezirke.name");
        searchableAttributes.add("verortung.gemarkungen.name");
        searchableAttributes.add("verortung.gemarkungen.flurstuecke.nummer");
        searchableAttributes.add("bebauungsplannummer");
        return searchableAttributes;
    }

    protected static Set<String> getNamesOfSearchableAttributesForInfrastruktureinrichtung() {
        final var searchableAttributes = new HashSet<String>();
        searchableAttributes.add("adresse.strasse");
        searchableAttributes.add("adresse.hausnummer");
        searchableAttributes.add("nameEinrichtung");
        searchableAttributes.add("status");
        return searchableAttributes;
    }

    /**
     * Die Methode ermittelt die zu durchsuchenden Entitäten auf Basis des im Methodenparameter gegebenen Objekts.
     *
     * @param searchQueryInformation zum ermitteln der zu durchsuchenden Entitäten.
     * @return die zu durchsuchenden Entitäten.
     * @throws EntityNotFoundException falls im Objekt des Methodenparameters keine zu durchsuchende Entität markiert ist.
     */
    public List<Class<? extends BaseEntity>> getSearchableEntities(final SearchQueryModel searchQueryInformation)
        throws EntityNotFoundException {
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
