package de.muenchen.isi.infrastructure.entity.search.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MultisearchResponse {

    private List<CompleteSuggestionResponse> responses;

    @JsonIgnore
    public static MultisearchResponse fromJson(final String multisearchResponse) throws JsonProcessingException {
        return new ObjectMapper().readValue(multisearchResponse, MultisearchResponse.class);
    }
}
