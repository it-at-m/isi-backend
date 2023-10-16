package de.muenchen.isi.infrastructure.entity.search.response;

/*
          {
            "text" : "Dasdudi",
            "_index" : "bauleitplanverfahren-000001",
            "_type" : "_doc",
            "_id" : "19b5d833-54d5-4a5d-9ada-4b40d23e0eb3",
            "_score" : 4.0,
            "_source" : { }
          }
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OptionResponse {

    private String text;
}
