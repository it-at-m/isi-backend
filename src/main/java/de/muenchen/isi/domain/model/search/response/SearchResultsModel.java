package de.muenchen.isi.domain.model.search.response;

import java.util.List;
import lombok.Data;

@Data
public class SearchResultsModel {

    private List<SearchResultModel> searchResults;

    private Long numberOfPages;
}
