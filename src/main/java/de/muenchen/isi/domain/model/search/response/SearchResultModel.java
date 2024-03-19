package de.muenchen.isi.domain.model.search.response;

import de.muenchen.isi.domain.model.common.WGS84Model;
import de.muenchen.isi.domain.model.enums.SearchResultType;
import lombok.Data;

@Data
public abstract class SearchResultModel {

    private SearchResultType type;

    private WGS84Model coordinate;
}
