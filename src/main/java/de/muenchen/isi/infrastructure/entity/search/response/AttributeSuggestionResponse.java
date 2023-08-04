package de.muenchen.isi.infrastructure.entity.search.response;

import java.util.List;
/*

      {
        "text" : "dasd",
        "offset" : 0,
        "length" : 4,
        "options" : [
          {
            "text" : "Dasdudi",
            "_index" : "infrastrukturabfrage-000001",
            "_type" : "_doc",
            "_id" : "19b5d833-54d5-4a5d-9ada-4b40d23e0eb3",
            "_score" : 4.0,
            "_source" : { }
          }
        ]
      }


 */

import lombok.Data;

@Data
public class AttributeSuggestionResponse {

    private List<OptionResponse> options;
}
