package de.muenchen.isi.api.dto.search.response;

import java.util.List;
import lombok.Data;

@Data
public class SearchResultsDto {

    private List<SearchResultDto> searchResults;
}
