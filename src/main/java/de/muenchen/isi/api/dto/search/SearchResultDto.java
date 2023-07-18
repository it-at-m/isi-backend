package de.muenchen.isi.api.dto.search;

import de.muenchen.isi.domain.model.enums.SearchResultType;
import lombok.Data;

@Data
public abstract class SearchResultDto {

    private SearchResultType type;
}
