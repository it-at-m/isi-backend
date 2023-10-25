package de.muenchen.isi.infrastructure.entity.search.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Map;
import lombok.Data;

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
            "_index" : "bauleitplanverfahren-000001",
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
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompleteSuggestionResponse {

    private Map<String, List<AttributeSuggestionResponse>> suggest;
}
