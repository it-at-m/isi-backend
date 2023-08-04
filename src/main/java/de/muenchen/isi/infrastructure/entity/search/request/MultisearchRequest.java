package de.muenchen.isi.infrastructure.entity.search.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import lombok.Data;

@Data
public class MultisearchRequest {

    private Map<IndexRequest, CompleteSuggestionRequest> multisearchIndexAndCompleteSuggestionPair;

    /**
     *
     * @return
     * @throws JsonProcessingException
     */
    public String toMultiSearchRequestBody() throws JsonProcessingException {
        final var multisearchRequestBody = new StringBuffer();
        for (final var indexAndCompletionSuggestionPair : multisearchIndexAndCompleteSuggestionPair.entrySet()) {
            multisearchRequestBody
                .append(indexAndCompletionSuggestionPair.getKey().toJson())
                .append("\n")
                .append(indexAndCompletionSuggestionPair.getValue().toJson())
                .append("\n");
        }
        return multisearchRequestBody.toString();
    }
}
