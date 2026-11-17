package de.muenchen.isi.domain.service.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.domain.model.enums.SortAttribute;
import de.muenchen.isi.domain.model.search.request.SearchQueryAndSortingModel;
import java.util.List;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Prüft die Sortierung der Suche gegen ein echtes Elasticsearch.
 * <p>
 * Das Indexfeld {@code fristBearbeitung} existiert nur in den Indizes der Abfragen. Eine Suche über Abfragen,
 * Vorhaben und Infrastruktureinrichtungen muss trotzdem nach der Bearbeitungsfrist sortieren können.
 */
@EnabledIf(expression = "#{environment.getProperty('run.local.integration.test') == 'true'}")
@SpringBootTest(classes = { IsiBackendApplication.class })
@ActiveProfiles(profiles = { TestConstants.SPRING_UNIT_TEST_PROFILE, TestConstants.SPRING_NO_SECURITY_PROFILE })
class SearchSortierungIntegrationTest {

    // Gleiches Image wie im lokalen Stack und in SearchAnalyzerIntegrationTest.
    private static final ElasticsearchContainer ELASTICSEARCH = new ElasticsearchContainer(
        DockerImageName.parse("docker.io/library/elasticsearch:8.11.4").asCompatibleSubstituteFor(
            "docker.elastic.co/elasticsearch/elasticsearch"
        )
    )
        .withEnv("xpack.security.enabled", "false")
        .withEnv("ES_JAVA_OPTS", "-Xms512m -Xmx512m");

    static {
        ELASTICSEARCH.start();
    }

    @DynamicPropertySource
    static void elasticsearchProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.properties.hibernate.search.enabled", () -> "true");
        registry.add("spring.jpa.properties.hibernate.search.backend.hosts", ELASTICSEARCH::getHttpHostAddress);
    }

    @Autowired
    private EntitySearchService entitySearchService;

    @ParameterizedTest
    @Transactional
    @EnumSource(SortOrder.class)
    void sortiertAbfragenVorhabenUndInfrastruktureinrichtungenNachBearbeitungsfrist(final SortOrder sortOrder)
        throws Exception {
        final var query = new SearchQueryAndSortingModel();
        query.setSelectBauleitplanverfahren(true);
        query.setSelectBaugenehmigungsverfahren(true);
        query.setSelectWeiteresVerfahren(true);
        query.setSelectBauvorhaben(true);
        query.setSelectGrundschule(true);
        query.setSelectGsNachmittagBetreuung(true);
        query.setSelectHausFuerKinder(true);
        query.setSelectKindergarten(true);
        query.setSelectKinderkrippe(true);
        query.setSelectMittelschule(true);
        query.setSelectNachbarschaftstreff(true);
        query.setPage(1);
        query.setPageSize(10);
        query.setSortBy(SortAttribute.FRIST_BEARBEITUNG);
        query.setSortOrder(sortOrder);

        assertThat(entitySearchService.searchForEntities(query).getSearchResults(), empty());
    }

    /**
     * Der Stadtbezirksfilter muss auch für Infrastruktureinrichtungen allein funktionieren. Fehlt das Indexfeld in
     * allen durchsuchten Indizes, bricht Hibernate Search die Suche ab.
     */
    @Test
    @Transactional
    void filtertInfrastruktureinrichtungenNachStadtbezirk() throws Exception {
        final var query = new SearchQueryAndSortingModel();
        query.setSelectBauleitplanverfahren(false);
        query.setSelectBaugenehmigungsverfahren(false);
        query.setSelectWeiteresVerfahren(false);
        query.setSelectBauvorhaben(false);
        query.setSelectGrundschule(true);
        query.setSelectGsNachmittagBetreuung(false);
        query.setSelectHausFuerKinder(false);
        query.setSelectKindergarten(false);
        query.setSelectKinderkrippe(true);
        query.setSelectMittelschule(false);
        query.setSelectNachbarschaftstreff(false);
        query.setFilterStadtbezirkNummer(List.of("06"));
        query.setPage(1);
        query.setPageSize(10);
        query.setSortBy(SortAttribute.LAST_MODIFIED_DATE_TIME);
        query.setSortOrder(SortOrder.DESC);

        assertThat(entitySearchService.searchForEntities(query).getSearchResults(), empty());
    }
}
