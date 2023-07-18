package de.muenchen.isi.domain.model.search;

import java.util.List;
import lombok.Data;

@Data
public class SearchResultsModel {

    private List<SearchResultModel> searchResults;
}
