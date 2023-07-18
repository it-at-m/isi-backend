package de.muenchen.isi.api.dto.search;

import java.util.List;
import lombok.Data;

@Data
public class SearchResultsDto {

    private List<SearchResultDto> searchResults;
}
