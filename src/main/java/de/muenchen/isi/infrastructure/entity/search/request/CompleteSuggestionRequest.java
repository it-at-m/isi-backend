package de.muenchen.isi.infrastructure.entity.search.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@Data
public class CompleteSuggestionRequest {

    private String _source;

    private Map<String, SuggestionRequest> suggest = new HashMap<>();

    @JsonIgnore
    public String toJson() {
        return new JsonMapper().writeValueAsString(this);
    }
}
