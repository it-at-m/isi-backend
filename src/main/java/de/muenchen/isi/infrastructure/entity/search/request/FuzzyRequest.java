package de.muenchen.isi.infrastructure.entity.search.request;

import lombok.Data;

/*

{
          "fuzziness": 0
        }

 */
@Data
public class FuzzyRequest {

    private int fuzziness;
}
