package de.muenchen.isi.infrastructure.entity.search.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class CompleteSuggestionRequest {

    private String _source;

    private Map<String, SuggestionRequest> suggest = new HashMap<>();

    @JsonIgnore
    public String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }
}
