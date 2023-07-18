package de.muenchen.isi.domain.model.search;

import de.muenchen.isi.domain.model.enums.SearchResultType;
import lombok.Data;

@Data
public abstract class SearchResultModel {

    private SearchResultType type;
}
