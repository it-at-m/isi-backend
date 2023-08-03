package de.muenchen.isi.domain.service.search;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.search.SearchQueryForEntitiesModel;
import de.muenchen.isi.domain.model.search.SuchwortSuggestionsModel;
import de.muenchen.isi.domain.model.search.request.CompleteSuggestionRequest;
import de.muenchen.isi.domain.model.search.request.CompletionRequest;
import de.muenchen.isi.domain.model.search.request.FuzzyRequest;
import de.muenchen.isi.domain.model.search.request.SuggestionRequest;
import de.muenchen.isi.infrastructure.entity.BaseEntity;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.hibernate.search.backend.elasticsearch.ElasticsearchBackend;
import org.hibernate.search.engine.backend.Backend;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.mapping.SearchMapping;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchWordSuggesterService {

    private static final int MAX_NUMBER_OF_SUGGESTION = 20;

    private final EntityManager entityManager;

    private final SearchPreparationService searchPreparationService;

    /*

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

    /**
     *
     *
     * @param searchQueryInformation mit der Suchquery bestehend aus einem Wort. Es dürfen sich keine Leerzeichen zwischen den einzelnen Buchstaben befinden.
     * @return die Suchwortvorschläge für das im Parameter gegebene Wort.
     */
    public SuchwortSuggestionsModel searchForSearchwordSuggestion(
        final SearchQueryForEntitiesModel searchQueryInformation
    ) throws EntityNotFoundException {
        final List<Class<? extends BaseEntity>> searchableEntities = searchPreparationService.getSearchableEntities(
            searchQueryInformation
        );
        final var foundSuchwortSuggestions =
            this.doSearchForSearchwordSuggestion(searchableEntities, searchQueryInformation.getSearchQuery())
                .collect(Collectors.toList());
        final var model = new SuchwortSuggestionsModel();
        model.setSuchwortSuggestions(foundSuchwortSuggestions);
        return model;
    }

    /**
     * Diese Methode führt die Suche zur Ermittlung der Suchwortvorschläge durch. Die Suche wird für die Entität  durchgeführt.
     *
     * @param singleWordQuery als Query bestehend aus einem Wort. Es dürfen sich keine Leerzeichen zwischen den einzelnen Buchstaben befinden.
     * @return die Suchwortvorschläge für das im Parameter gegebene Wort.
     */
    public Stream<String> doSearchForSearchwordSuggestion(
        final List<Class<? extends BaseEntity>> searchableEntities,
        final String singleWordQuery
    ) {
        SearchMapping searchMapping = Search.mapping(entityManager.getEntityManagerFactory());
        Backend backend = searchMapping.backend();
        ElasticsearchBackend elasticsearchBackend = backend.unwrap(ElasticsearchBackend.class);
        try (RestClient restClient = elasticsearchBackend.client(RestClient.class)) {
            return searchableEntities
                .stream()
                .flatMap(searchableEntity ->
                    this.doSearchForSearchwordSuggestion(searchableEntity, singleWordQuery, restClient)
                );
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    protected Stream<String> doSearchForSearchwordSuggestion(
        final Class<? extends BaseEntity> searchableEntity,
        final String singleWordQuery,
        final RestClient restClient
    ) {
        final var searchabelAttributes = searchPreparationService.getNamesOfSearchableAttributesForSearchwordSuggestion(
            searchableEntity
        );
        final var pathToSearchableIndex = getPathToSearchableIndex(searchableEntity);
        Request request = new Request("POST", pathToSearchableIndex);
        request.setJsonEntity(
            "{\n" +
            "  \"_source\": \"not-available-name\",\n" +
            "  \"suggest\": {\n" +
            "    \"abfrage_nameAbfrage_completion_suggetion\" : {\n" +
            "      \"text\" : \"dasd\",\n" +
            "      \"completion\" : {\n" +
            "        \"field\" : \"abfrage.nameAbfrage_completion_suggetion\",\n" +
            "        \"size\": 5,\n" +
            "        \"fuzzy\": {\n" +
            "          \"fuzziness\": 0\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}"
        );
        try {
            Response response = restClient.performRequest(request);
            try (final var inputstream = response.getEntity().getContent()) {
                return Stream.of(IOUtils.toString(inputstream, StandardCharsets.UTF_8));
            } catch (final Exception ex) {
                throw new RuntimeException(ex);
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
