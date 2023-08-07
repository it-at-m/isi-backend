package de.muenchen.isi.infrastructure.repository.search;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.search.request.CompleteSuggestionRequest;
import de.muenchen.isi.infrastructure.entity.search.request.CompletionRequest;
import de.muenchen.isi.infrastructure.entity.search.request.FuzzyRequest;
import de.muenchen.isi.infrastructure.entity.search.request.IndexRequest;
import de.muenchen.isi.infrastructure.entity.search.request.MultisearchRequest;
import de.muenchen.isi.infrastructure.entity.search.request.SuggestionRequest;
import de.muenchen.isi.infrastructure.entity.search.response.MultisearchResponse;
import de.muenchen.isi.infrastructure.entity.search.response.OptionResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.hibernate.search.backend.elasticsearch.ElasticsearchBackend;
import org.hibernate.search.mapper.orm.Search;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class SearchwordSuggesterRepository {

    public static final String ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION = "_searchword_suggestion";

    private final EntityManager entityManager;

    /**
     * Ermittelt durch einen direkten Zugriff auf Elasticsearch die Suchwortvorschläge.
     *
     * @param attributesForSearchableEntities die Attribute je zu durchsuchender Entität für welche Suchwortvorschlage ermittelt werden sollen.
     * @param singleWordQuery als Suchquery. Es dürfen sich keine Leerzeichen zwischen den einzelnen Buchstaben befinden.
     * @return die Suchwortvorschläge.
     */
    public Stream<String> doSearchForSearchwordSuggestion(
        final Map<Class<? extends BaseEntity>, Set<String>> attributesForSearchableEntities,
        final String singleWordQuery
    ) {
        // Verwendung des Elasticsearch-RestClients für direkten Indexzugriff.
        // https://docs.jboss.org/hibernate/stable/search/reference/en-US/html_single/#elasticsearch-client-access
        final var elasticsearchBackend = Search
            .mapping(entityManager.getEntityManagerFactory())
            .backend()
            .unwrap(ElasticsearchBackend.class);
        final var restClient = elasticsearchBackend.client(RestClient.class);

        return this.doSearchForSearchwordSuggestion(attributesForSearchableEntities, singleWordQuery, restClient)
            .getResponses()
            .stream()
            .flatMap(completeSuggestionResponse ->
                completeSuggestionResponse
                    .getSuggest()
                    .values()
                    .stream()
                    .flatMap(attributeSuggestionsResponse ->
                        attributeSuggestionsResponse
                            .stream()
                            .flatMap(attributeSuggestionResponse -> attributeSuggestionResponse.getOptions().stream())
                    )
                    .map(OptionResponse::getText)
            )
            .distinct();
    }

    /**
     * Die Suchwortvorschläge werden über einen Elasticsearch-Completion-Suggester ermittelt.
     *
     * @param attributesForSearchableEntities die Attribute je zu durchsuchender Entität für welche Suchwortvorschlage ermittelt werden sollen.
     * @param singleWordQuery als Suchquery. Es dürfen sich keine Leerzeichen zwischen den einzelnen Buchstaben befinden.
     * @param restClient für den direkten Zugriff auf Elasticsearch.
     * @return die Suchwortvorschläge.
     */
    protected MultisearchResponse doSearchForSearchwordSuggestion(
        final Map<Class<? extends BaseEntity>, Set<String>> attributesForSearchableEntities,
        final String singleWordQuery,
        final RestClient restClient
    ) {
        try {
            // Erstellen eines Multisearch-Request-Body um gleichzeitig über mehrere Indizes suchen zu können.
            // https://www.elastic.co/guide/en/elasticsearch/reference/current/search-multi-search.html
            // Je entitätsbezogenen Suchindex wird die Suche basierend auf dem completion-suggester in die Multisearch-Suche aufgenommen.
            // https://www.elastic.co/guide/en/elasticsearch/reference/current/search-suggesters.html#completion-suggester
            final var multisearchRequest =
                this.createMultisearchResponseRequestBody(attributesForSearchableEntities, singleWordQuery);
            final var request = new Request("POST", "_msearch");
            request.setJsonEntity(multisearchRequest.toMultiSearchRequestBody());
            try (final var inputstream = restClient.performRequest(request).getEntity().getContent()) {
                final var jsonResponseBody = IOUtils.toString(inputstream, StandardCharsets.UTF_8);
                return MultisearchResponse.fromJson(jsonResponseBody);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Erstellt den Multisearch-Request-Body bestehend je zu durchsuchenden Entität aus dem completion-suggester-Requestbody.
     *
     * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-multi-search.html
     *
     * @param attributesForSearchableEntities die Attribute je zu durchsuchender Entität für welche Suchwortvorschlage ermittelt werden sollen.
     * @param singleWordQuery als Suchquery. Es dürfen sich keine Leerzeichen zwischen den einzelnen Buchstaben befinden.
     * @return den Multisearch-Request-Body.
     */
    protected MultisearchRequest createMultisearchResponseRequestBody(
        final Map<Class<? extends BaseEntity>, Set<String>> attributesForSearchableEntities,
        final String singleWordQuery
    ) {
        final var multisearchIndexAndCompleteSuggestionPair = new HashMap<IndexRequest, CompleteSuggestionRequest>();
        for (final var attributesForSearchableEntity : attributesForSearchableEntities.entrySet()) {
            multisearchIndexAndCompleteSuggestionPair.put(
                this.createIndexRequest(attributesForSearchableEntity.getKey()),
                this.createCompletionSuggestionRequest(attributesForSearchableEntity.getValue(), singleWordQuery)
            );
        }
        final var body = new MultisearchRequest();
        body.setMultisearchIndexAndCompleteSuggestionPair(multisearchIndexAndCompleteSuggestionPair);
        return body;
    }

    /**
     * Erstellt für den Multisearch-Request-Body den indexbezogenen Requestbodybestandteil einer im parameter gegebenen Entität.
     *
     * @param searchableEntity für den indexbezogenen Requestbodybestandteil des Multisearch-Request-Body.
     * @return den indexbezogenen Requestbodybestandteil.
     */
    protected IndexRequest createIndexRequest(final Class<? extends BaseEntity> searchableEntity) {
        final var indexRequest = new IndexRequest();
        indexRequest.setIndex(getSearchableIndex(searchableEntity));
        return indexRequest;
    }

    /**
     * Erstellt den Request-Body für einen Completion-Suggestion-Request welcher als entitätsbezogener Bestandteil
     * im Multisearch-Request-Body eingebunden wird.
     *
     * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-suggesters.html#completion-suggester
     *
     * @param searchableAttributes die zu durchsuchenden Attribute.
     * @param singleWordQuery als Suchquery. Es dürfen sich keine Leerzeichen zwischen den einzelnen Buchstaben befinden.
     * @return den Body für einen Completion-Suggestion-Request.
     */
    protected CompleteSuggestionRequest createCompletionSuggestionRequest(
        final Set<String> searchableAttributes,
        final String singleWordQuery
    ) {
        final var completeSuggestionRequest = new CompleteSuggestionRequest();
        completeSuggestionRequest.set_source("unknown");
        final var suggests = searchableAttributes
            .stream()
            .map(searchableAttribute -> createSuggestionRequest(searchableAttribute, singleWordQuery))
            .collect(
                Collectors.toMap(
                    suggestionRequest -> suggestionRequest.getCompletion().getField(),
                    suggestionRequest -> suggestionRequest
                )
            );
        completeSuggestionRequest.setSuggest(suggests);
        return completeSuggestionRequest;
    }

    protected SuggestionRequest createSuggestionRequest(
        final String searchableAttribute,
        final String singleWordQuery
    ) {
        final var fuzzy = new FuzzyRequest();
        fuzzy.setFuzziness(3);
        final var completion = new CompletionRequest();
        completion.setField(searchableAttribute);
        completion.setSize(5);
        completion.setFuzzy(fuzzy);
        final var suggestion = new SuggestionRequest();
        suggestion.setText(singleWordQuery);
        suggestion.setCompletion(completion);
        return suggestion;
    }

    protected String getSearchableIndex(final Class<? extends BaseEntity> searchableEntity) {
        return StringUtils.lowerCase(searchableEntity.getSimpleName()) + "-read";
    }
}
