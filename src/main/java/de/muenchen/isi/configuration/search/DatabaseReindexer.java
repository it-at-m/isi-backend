package de.muenchen.isi.configuration.search;

import de.muenchen.isi.domain.model.search.request.SearchQueryModel;
import de.muenchen.isi.domain.service.search.SearchPreparationService;
import javax.persistence.EntityManager;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.mapper.orm.Search;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Diese Klasse führt nach dem Boot des Backends bei korrekt gesetzter Property "spring.jpa.properties.hibernate.search.reindex-database" eine Reindizierung der Datenbank durch.
 *
 * Eine Reindizierung der in der Datenbank gespeicherten Entiäten ist z.B. dann erforderlich, falls eine Datenmodell- oder Indexänderung vorgenommen wurde.
 */
@Component
@Data
@Slf4j
public class DatabaseReindexer implements CommandLineRunner {

    private final boolean reindexDatabase;

    private final EntityManager entityManager;

    private final SearchPreparationService searchPreparationService;

    /**
     * Ctor.
     *
     * @param reindexDatabase als Property zum Triggern der Neuindexierung des Suchindex für alle indizierten Entitäten.
     *                        Der Wert "true" führt die Neuindexierung des Suchindex nach dem Hochfahren des Backends durch.
     *                        Der Wert "false" verhindert eine Neuindexierung des Suchindex nach dem Hochfahren des Backends.
     * @param entityManager zur Neuindexierung des Suchindex.
     * @param searchPreparationService zur Ermittlung der zu indizierenden Entitätklassen.
     */
    public DatabaseReindexer(
        @Value("${spring.jpa.properties.hibernate.search.reindex-database:false}") final boolean reindexDatabase,
        final EntityManager entityManager,
        final SearchPreparationService searchPreparationService
    ) {
        this.reindexDatabase = reindexDatabase;
        this.entityManager = entityManager;
        this.searchPreparationService = searchPreparationService;
    }

    @Override
    public void run(final String... args) throws Exception {
        if (reindexDatabase) {
            log.info("Neuindexierung des Suchindex für die Datenbank gestartet.");
            final var searchQueryModel = getSearchQueryModelForAllEntities();
            final var searchableEntities = searchPreparationService.getSearchableEntities(searchQueryModel);
            Search
                .mapping(entityManager.getEntityManagerFactory())
                .scope(searchableEntities)
                .massIndexer()
                .startAndWait();
            log.info("Neuindexierung des Suchindex für die Datenbank abgeschlossen.");
        }
    }

    private SearchQueryModel getSearchQueryModelForAllEntities() {
        final var searchQueryModel = new SearchQueryModel();
        searchQueryModel.setSelectBauleitplanverfahren(true);
        searchQueryModel.setSelectBauvorhaben(true);
        searchQueryModel.setSelectGrundschule(true);
        searchQueryModel.setSelectGsNachmittagBetreuung(true);
        searchQueryModel.setSelectHausFuerKinder(true);
        searchQueryModel.setSelectKindergarten(true);
        searchQueryModel.setSelectKinderkrippe(true);
        searchQueryModel.setSelectMittelschule(true);
        return searchQueryModel;
    }
}
