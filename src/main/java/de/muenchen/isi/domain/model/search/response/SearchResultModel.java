package de.muenchen.isi.domain.model.search.response;

import de.muenchen.isi.domain.model.common.Wgs84Model;
import de.muenchen.isi.domain.model.enums.SearchResultType;
import lombok.Data;

@Data
public abstract class SearchResultModel {

    private SearchResultType type;

    private Wgs84Model coordinate;
}
