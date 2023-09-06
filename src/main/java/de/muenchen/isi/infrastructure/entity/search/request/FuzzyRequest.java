package de.muenchen.isi.infrastructure.entity.search.request;

import lombok.Data;

/*
        {
          "fuzziness": "AUTO"
        }
        oder
        {
          "fuzziness": 1
        }
 */
@Data
public class FuzzyRequest {

    private String fuzziness;
}
