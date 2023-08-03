package de.muenchen.isi.domain.model.search.request;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/*

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


 */

@Data
public class CompleteSuggestionRequest {

    private String _source;

    private Map<String, SuggestionRequest> suggest = new HashMap<>();
}
