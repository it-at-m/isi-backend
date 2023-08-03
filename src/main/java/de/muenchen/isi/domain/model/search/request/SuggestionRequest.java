package de.muenchen.isi.domain.model.search.request;

/*

{
      "text" : "dasd",
      "completion" : {
        "field" : "abfrage.nameAbfrage_completion_suggetion",
        "size": 5,
        "fuzzy": {
          "fuzziness": 0
        }
      }
    }


 */

import lombok.Data;

@Data
public class SuggestionRequest {

    private String text;

    private CompletionRequest completion;
}
