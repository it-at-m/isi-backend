package de.muenchen.isi.domain.model.search.request;

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
