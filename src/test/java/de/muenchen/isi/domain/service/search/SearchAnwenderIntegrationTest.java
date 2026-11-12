package de.muenchen.isi.domain.service.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.enums.SortAttribute;
import de.muenchen.isi.domain.model.search.request.SearchQueryAndSortingModel;
import de.muenchen.isi.domain.model.search.response.AbfrageSearchResultModel;
import de.muenchen.isi.domain.model.search.response.BauvorhabenSearchResultModel;
import de.muenchen.isi.domain.model.search.response.InfrastruktureinrichtungSearchResultModel;
import de.muenchen.isi.domain.model.search.response.SearchResultModel;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.WeiteresVerfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.AnlassPlanung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Verfahrensstand;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kinderkrippe;
import de.muenchen.isi.infrastructure.repository.AbfrageRepository;
import de.muenchen.isi.infrastructure.repository.BauvorhabenRepository;
import de.muenchen.isi.infrastructure.repository.InfrastruktureinrichtungRepository;
import de.muenchen.isi.security.AuthenticationUtils;
import java.time.LocalDate;
import java.util.List;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Prüft gegen ein echtes Elasticsearch, dass Nutzer mit ausschließlich der Rolle Anwender in den Suchergebnissen nur
 * erledigte Abfragen erhalten, Vorhaben und Infrastruktureinrichtungen aber weiterhin gefunden werden.
 */
@EnabledIf(expression = "#{environment.getProperty('run.local.integration.test') == 'true'}")
@SpringBootTest(classes = { IsiBackendApplication.class })
@ActiveProfiles(profiles = { TestConstants.SPRING_UNIT_TEST_PROFILE, TestConstants.SPRING_NO_SECURITY_PROFILE })
class SearchAnwenderIntegrationTest {

    // Gleiches Image und gleiche Einstellungen wie im SearchAnalyzerIntegrationTest.
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
        // Die gespeicherten Entitäten müssen unmittelbar nach dem Commit suchbar sein.
        registry.add("spring.jpa.properties.hibernate.search.indexing.plan.synchronization.strategy", () -> "sync");
    }

    @Autowired
    private EntitySearchService entitySearchService;

    @Autowired
    private AbfrageRepository abfrageRepository;

    @Autowired
    private BauvorhabenRepository bauvorhabenRepository;

    @Autowired
    private InfrastruktureinrichtungRepository infrastruktureinrichtungRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @MockitoSpyBean
    private AuthenticationUtils authenticationUtils;

    @BeforeEach
    void speichereVorgaenge() {
        abfrageRepository.saveAndFlush(createAbfrage("Abfrage angelegt", StatusAbfrage.ANGELEGT));
        abfrageRepository.saveAndFlush(createAbfrage("Abfrage in Bearbeitung", StatusAbfrage.EINPLANUNG_BEDARFE));
        abfrageRepository.saveAndFlush(createAbfrage("Abfrage erledigt mit", StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));
        abfrageRepository.saveAndFlush(createAbfrage("Abfrage erledigt ohne", StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));
        bauvorhabenRepository.saveAndFlush(createBauvorhaben());
        infrastruktureinrichtungRepository.saveAndFlush(createKinderkrippe());
    }

    @AfterEach
    void loescheVorgaenge() {
        infrastruktureinrichtungRepository.deleteAll();
        abfrageRepository.deleteAll();
        bauvorhabenRepository.deleteAll();
    }

    @Test
    void anwenderFindetNurErledigteAbfragenSowieVorhabenUndInfrastruktureinrichtungen() {
        Mockito.doReturn(true).when(authenticationUtils).isOnlyRoleAnwender();

        assertThat(
            sucheAlleVorgaenge(),
            containsInAnyOrder("Abfrage erledigt mit", "Abfrage erledigt ohne", "Vorhaben", "Kinderkrippe")
        );
    }

    @Test
    void andereRollenFindenAbfragenInJedemStatus() {
        Mockito.doReturn(false).when(authenticationUtils).isOnlyRoleAnwender();

        assertThat(
            sucheAlleVorgaenge(),
            containsInAnyOrder(
                "Abfrage angelegt",
                "Abfrage in Bearbeitung",
                "Abfrage erledigt mit",
                "Abfrage erledigt ohne",
                "Vorhaben",
                "Kinderkrippe"
            )
        );
    }

    @Test
    void anwenderFindetMitStatusfilterNurDieSchnittmenge() {
        Mockito.doReturn(true).when(authenticationUtils).isOnlyRoleAnwender();
        final var suche = createSucheAlleVorgaenge();
        suche.setSelectBauvorhaben(false);
        suche.setSelectKinderkrippe(false);
        suche.setFilterStatusAbfrage(List.of(StatusAbfrage.ANGELEGT, StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        assertThat(suche(suche), containsInAnyOrder("Abfrage erledigt mit"));
    }

    private List<String> sucheAlleVorgaenge() {
        return suche(createSucheAlleVorgaenge());
    }

    /**
     * Führt die Suche wie im Produktivbetrieb innerhalb einer Transaktion aus und liefert die Namen der Treffer.
     */
    private List<String> suche(final SearchQueryAndSortingModel suche) {
        return new TransactionTemplate(transactionManager).execute(status -> {
            try {
                return getNamen(entitySearchService.searchForEntities(suche).getSearchResults());
            } catch (final EntityNotFoundException exception) {
                throw new IllegalStateException(exception);
            }
        });
    }

    private static SearchQueryAndSortingModel createSucheAlleVorgaenge() {
        // Wie das Frontend werden alle Objektarten explizit an- oder abgewählt.
        final var suche = new SearchQueryAndSortingModel();
        suche.setSelectBauleitplanverfahren(false);
        suche.setSelectBaugenehmigungsverfahren(false);
        suche.setSelectWeiteresVerfahren(true);
        suche.setSelectBauvorhaben(true);
        suche.setSelectGrundschule(false);
        suche.setSelectGsNachmittagBetreuung(false);
        suche.setSelectHausFuerKinder(false);
        suche.setSelectKindergarten(false);
        suche.setSelectKinderkrippe(true);
        suche.setSelectMittelschule(false);
        suche.setSelectNachbarschaftstreff(false);
        suche.setSortBy(SortAttribute.CREATED_DATE_TIME);
        suche.setSortOrder(SortOrder.DESC);
        return suche;
    }

    private static List<String> getNamen(final List<SearchResultModel> suchergebnisse) {
        return suchergebnisse
            .stream()
            .map(
                suchergebnis ->
                    switch (suchergebnis) {
                        case AbfrageSearchResultModel abfrage -> abfrage.getName();
                        case BauvorhabenSearchResultModel bauvorhaben -> bauvorhaben.getNameVorhaben();
                        case InfrastruktureinrichtungSearchResultModel einrichtung -> einrichtung.getNameEinrichtung();
                        default -> throw new IllegalStateException("Unerwartetes Suchergebnis " + suchergebnis);
                    }
            )
            .toList();
    }

    private static WeiteresVerfahren createAbfrage(final String name, final StatusAbfrage statusAbfrage) {
        final var abfrage = new WeiteresVerfahren();
        abfrage.setName(name);
        abfrage.setStatusAbfrage(statusAbfrage);
        abfrage.setSub("1234");
        abfrage.setVerfahrensstand(Verfahrensstand.UNSPECIFIED);
        abfrage.setFristBearbeitung(LocalDate.of(2026, 12, 31));
        abfrage.setSobonRelevant(UncertainBoolean.FALSE);
        abfrage.setMitzeichnungBeschlussentwurf(UncertainBoolean.FALSE);
        return abfrage;
    }

    private static Bauvorhaben createBauvorhaben() {
        final var bauvorhaben = new Bauvorhaben();
        bauvorhaben.setNameVorhaben("Vorhaben");
        bauvorhaben.setVerfahrensstand(Verfahrensstand.UNSPECIFIED);
        bauvorhaben.setSobonRelevant(UncertainBoolean.FALSE);
        return bauvorhaben;
    }

    private static Kinderkrippe createKinderkrippe() {
        final var kinderkrippe = new Kinderkrippe();
        kinderkrippe.setNameEinrichtung("Kinderkrippe");
        kinderkrippe.setStatus(StatusInfrastruktureinrichtung.UNSPECIFIED);
        kinderkrippe.setAnlassPlanung(AnlassPlanung.UNSPECIFIED);
        kinderkrippe.setAnzahlKinderkrippePlaetze(10);
        kinderkrippe.setAnzahlKinderkrippeGruppen(1);
        return kinderkrippe;
    }
}
