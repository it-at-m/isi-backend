package de.muenchen.isi.infrastructure.repository.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.search.request.CompleteSuggestionRequest;
import de.muenchen.isi.infrastructure.entity.search.request.CompletionRequest;
import de.muenchen.isi.infrastructure.entity.search.request.FuzzyRequest;
import de.muenchen.isi.infrastructure.entity.search.request.SuggestionRequest;
import de.muenchen.isi.infrastructure.entity.search.response.CompleteSuggestionResponse;
import de.muenchen.isi.infrastructure.entity.search.response.OptionResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.hibernate.search.backend.elasticsearch.ElasticsearchBackend;
import org.hibernate.search.mapper.orm.Search;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SearchwordSuggesterRepository {

    private final EntityManager entityManager;

    /*

#REquest

{
  "_source": "not-available-name",
  "suggest": {
    "abfrage_nameAbfrage_completion_suggetion" : {
      "text" : "dasd",
      "completion" : {
        "field" : "abfrage.nameAbfrage_completion_suggetion",
        "size": 5,
        "fuzzy": {
          "fuzziness": 0
        }
      }
    }
  }
}


#Response

{
  "took" : 0,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 0,
      "relation" : "eq"
    },
    "max_score" : null,
    "hits" : [ ]
  },
  "suggest" : {
    "abfrage_nameAbfrage_completion_suggetion" : [
      {
        "text" : "dasd",
        "offset" : 0,
        "length" : 4,
        "options" : [
          {
            "text" : "Dasdudi",
            "_index" : "infrastrukturabfrage-000001",
            "_type" : "_doc",
            "_id" : "19b5d833-54d5-4a5d-9ada-4b40d23e0eb3",
            "_score" : 4.0,
            "_source" : { }
          }
        ]
      }
    ]
  }
}

         */

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
        try (final var restClient = elasticsearchBackend.client(RestClient.class)) {
            return attributesForSearchableEntities
                .entrySet()
                .parallelStream()
                .map(attributesForSearchableEntity ->
                    this.doSearchForSearchwordSuggestion(
                            attributesForSearchableEntity.getKey(),
                            attributesForSearchableEntity.getValue(),
                            singleWordQuery,
                            restClient
                        )
                )
                .flatMap(completeSuggestionResponse ->
                    completeSuggestionResponse
                        .getSuggest()
                        .values()
                        .stream()
                        .flatMap(attributeSuggestionResponse -> attributeSuggestionResponse.getOptions().stream())
                        .map(OptionResponse::getText)
                )
                .distinct();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    protected CompleteSuggestionResponse doSearchForSearchwordSuggestion(
        final Class<? extends BaseEntity> searchableEntity,
        List<String> searchableAttributes,
        final String singleWordQuery,
        final RestClient restClient
    ) {
        try {
            final var completionSuggestionRequestBody =
                this.createCompleteSuggestionRequestBody(searchableAttributes, singleWordQuery);
            final var bodyAsString = new ObjectMapper().writeValueAsString(completionSuggestionRequestBody);
            final var pathToSearchableIndex = this.getPathToSearchableIndex(searchableEntity);
            final var request = new Request("POST", pathToSearchableIndex);
            request.setJsonEntity(bodyAsString);
            try (final var inputstream = restClient.performRequest(request).getEntity().getContent()) {
                final var jsonResponseBody = IOUtils.toString(inputstream, StandardCharsets.UTF_8);
                return CompleteSuggestionResponse.fromJson(jsonResponseBody);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getPathToSearchableIndex(final Class<? extends BaseEntity> searchableEntity) {
        return StringUtils.lowerCase(searchableEntity.getSimpleName()) + "-read/_search";
    }

    private CompleteSuggestionRequest createCompleteSuggestionRequestBody(
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
}
