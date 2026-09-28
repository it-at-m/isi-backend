package de.muenchen.isi.infrastructure.entity.search.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@Data
public class IndexRequest {

    private String index;

    @JsonIgnore
    public String toJson() {
        return new JsonMapper().writeValueAsString(this);
    }
}
