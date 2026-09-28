package de.muenchen.isi.infrastructure.entity.search.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MultisearchResponse {

    private List<CompleteSuggestionResponse> responses;

    @JsonIgnore
    public static MultisearchResponse fromJson(final String multisearchResponse) {
        return new JsonMapper().readValue(multisearchResponse, MultisearchResponse.class);
    }
}
