package de.muenchen.isi.domain.model.search.request;

import lombok.Data;

/*

{
        "field" : "abfrage.nameAbfrage_completion_suggetion",
        "size": 5,
        "fuzzy": {
          "fuzziness": 0
        }
      }

 */

@Data
public class CompletionRequest {

    private String field;

    private int size;

    private FuzzyRequest fuzzy;
}
