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
import java.util.List;
import java.util.Map;
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

    private final EntityManager entityManager;

    public Stream<String> doSearchForSearchwordSuggestion(
        final Map<Class<? extends BaseEntity>, List<String>> attributesForSearchableEntities,
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

    protected MultisearchResponse doSearchForSearchwordSuggestion(
        final Map<Class<? extends BaseEntity>, List<String>> attributesForSearchableEntities,
        final String singleWordQuery,
        final RestClient restClient
    ) {
        try {
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

    private MultisearchRequest createMultisearchResponseRequestBody(
        final Map<Class<? extends BaseEntity>, List<String>> attributesForSearchableEntities,
        final String singleWordQuery
    ) {
        final var multisearchIndexAndCompleteSuggestionPair = new HashMap<IndexRequest, CompleteSuggestionRequest>();
        for (final var attributesForSearchableEntity : attributesForSearchableEntities.entrySet()) {
            multisearchIndexAndCompleteSuggestionPair.put(
                this.createIndexRequest(attributesForSearchableEntity.getKey()),
                this.createCompleteSuggestionRequest(attributesForSearchableEntity.getValue(), singleWordQuery)
            );
        }
        final var body = new MultisearchRequest();
        body.setMultisearchIndexAndCompleteSuggestionPair(multisearchIndexAndCompleteSuggestionPair);
        return body;
    }

    private IndexRequest createIndexRequest(final Class<? extends BaseEntity> searchableEntity) {
        final var indexRequest = new IndexRequest();
        indexRequest.setIndex(getSearchableIndex(searchableEntity));
        return indexRequest;
    }

    private CompleteSuggestionRequest createCompleteSuggestionRequest(
        final List<String> searchableAttributes,
        final String singleWordQuery
    ) {
        final var body = new CompleteSuggestionRequest();
        body.set_source("unknown");
        final var suggests = searchableAttributes
            .stream()
            .map(searchableAttribute -> createSuggestionRequest(searchableAttribute, singleWordQuery))
            .collect(
                Collectors.toMap(
                    suggestionRequest -> suggestionRequest.getCompletion().getField(),
                    suggestionRequest -> suggestionRequest
                )
            );
        body.setSuggest(suggests);
        return body;
    }

    private SuggestionRequest createSuggestionRequest(final String searchableAttribute, final String singleWordQuery) {
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

    private String getSearchableIndex(final Class<? extends BaseEntity> searchableEntity) {
        return StringUtils.lowerCase(searchableEntity.getSimpleName()) + "-read";
    }
}
