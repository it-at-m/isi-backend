package de.muenchen.isi.domain.service.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import jakarta.persistence.EntityManagerFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.hibernate.search.backend.elasticsearch.ElasticsearchBackend;
import org.hibernate.search.mapper.orm.Search;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

/**
 * Prüft die Analyzer der Suchfelder gegen ein echtes Elasticsearch.
 *
 * Hibernate Search legt die Indizes beim Start per drop-and-create an, d.h. die Analyzer stammen direkt aus
 * ElasticsearchAnalysisConfig und den @FullTextField-Annotationen der Entitäten. Über die _analyze-API wird
 * ermittelt, welche Tokens Elasticsearch für einen Feldwert tatsächlich im Index ablegt.
 */
@EnabledIf(expression = "#{environment.getProperty('run.local.integration.test') == 'true'}")
@SpringBootTest(classes = { IsiBackendApplication.class })
@ActiveProfiles(profiles = { TestConstants.SPRING_UNIT_TEST_PROFILE, TestConstants.SPRING_NO_SECURITY_PROFILE })
class SearchAnalyzerIntegrationTest {

    // Gleiches Image wie im lokalen Stack (isi_docker-compose.yml).
    // Voll qualifizierter Image-Name, damit auch Podman (ohne Short-Name-Auflösung auf docker.io) das Image findet.
    // Begrenzter Heap, da die Podman-/Docker-VM standardmäßig nur wenig Arbeitsspeicher hat.
    private static final ElasticsearchContainer ELASTICSEARCH = new ElasticsearchContainer(
        DockerImageName.parse("docker.io/library/elasticsearch:8.11.4").asCompatibleSubstituteFor(
            "docker.elastic.co/elasticsearch/elasticsearch"
        )
    )
        .withEnv("xpack.security.enabled", "false")
        .withEnv("ES_JAVA_OPTS", "-Xms512m -Xmx512m");

    private static final ObjectMapper OBJECT_MAPPER = new JsonMapper();

    static {
        ELASTICSEARCH.start();
    }

    @DynamicPropertySource
    static void elasticsearchProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.properties.hibernate.search.enabled", () -> "true");
        registry.add("spring.jpa.properties.hibernate.search.backend.hosts", ELASTICSEARCH::getHttpHostAddress);
    }

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private EntitySearchService entitySearchService;

    /**
     * Unterstriche müssen trennen, die bisherige Trennung an Satzzeichen (Bindestrich, Klammern, Slash, ...)
     * des Standard-Tokenizers muss erhalten bleiben.
     */
    @ParameterizedTest
    @CsvSource(
        delimiter = '|',
        value = {
            "bauvorhaben-read          | nameVorhaben    | Test_Fall             | test,fall",
            "bauvorhaben-read          | nameVorhaben    | _leading_trailing_    | leading,trailing",
            "bauvorhaben-read          | nameVorhaben    | Kita-Nord             | kita,nord",
            "bauvorhaben-read          | nameVorhaben    | Haus für Kinder (Süd) | haus,für,kinder,süd",
            "bauvorhaben-read          | nameVorhaben    | BV_2024/12            | bv,2024,12",
            "bauleitplanverfahren-read | name            | Test_Fall-Nord        | test,fall,nord",
            "kinderkrippe-read         | nameEinrichtung | Kita_Am-See           | kita,am,see",
        }
    )
    void namensfelderTrennenAnUnterstrichUndSatzzeichen(
        final String index,
        final String field,
        final String value,
        final String expectedTokens
    ) throws IOException {
        assertThat(analyze(index, field, value), contains(expectedTokens.split(",")));
    }

    /**
     * Die Suchanfrage wird für alle Felder an Unterstrichen aufgetrennt, daher müssen auch die Nummernfelder
     * im Index an Unterstrichen trennen. Sonst findet die Suche "BV_123" (-> "BV* 123*", AND) nichts.
     */
    @ParameterizedTest
    @CsvSource(
        delimiter = '|',
        value = {
            "bauvorhaben-read          | bauvorhabenNummer   | BV_123",
            "bauvorhaben-read          | bebauungsplannummer | BV_123",
            "bauleitplanverfahren-read | bebauungsplannummer | BV_123",
        }
    )
    void nummernfelderTrennenAnUnterstrich(final String index, final String field, final String value)
        throws IOException {
        assertThat(analyze(index, field, value), contains("bv", "123"));
    }

    /**
     * Die Tokens der Suchanfrage (Java BreakIterator) und die Tokens im Index (Elasticsearch-Analyzer) müssen
     * übereinstimmen. Da die Suche mit AND verknüpft, führt jede Abweichung dazu, dass Treffer fehlen.
     */
    @ParameterizedTest
    @ValueSource(strings = { "Test_Fall", "_leading_trailing_", "Kita-Nord", "Haus für Kinder (Süd)", "BV_2024/12" })
    void tokensDerSuchanfrageEntsprechenDenTokensImIndex(final String value) throws IOException {
        final var queryTokens = entitySearchService
            .tokenizeAccordingUnicodeAnnex29(value)
            .stream()
            .map(token -> token.toLowerCase(Locale.ROOT))
            .toList();

        assertThat(queryTokens, is(analyze("bauvorhaben-read", "nameVorhaben", value)));
    }

    /**
     * Liefert die Tokens, die Elasticsearch mit dem Analyzer des gegebenen Feldes für den Wert erzeugt.
     */
    private List<String> analyze(final String index, final String field, final String value) throws IOException {
        final var restClient = Search.mapping(entityManagerFactory)
            .backend()
            .unwrap(ElasticsearchBackend.class)
            .client(RestClient.class);

        final var request = new Request("POST", "/" + index + "/_analyze");
        request.setJsonEntity(OBJECT_MAPPER.writeValueAsString(Map.of("field", field, "text", value)));

        try (final var inputStream = restClient.performRequest(request).getEntity().getContent()) {
            final var tokens = new ArrayList<String>();
            OBJECT_MAPPER.readTree(inputStream)
                .get("tokens")
                .forEach(token -> tokens.add(token.get("token").asString()));
            return tokens;
        }
    }
}
