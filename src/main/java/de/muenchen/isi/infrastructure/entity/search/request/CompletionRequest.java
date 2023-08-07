package de.muenchen.isi.infrastructure.entity.search.request;

import lombok.Data;

/*
      {
        "field" : "abfrage.nameAbfrage_searchword_suggetion",
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
