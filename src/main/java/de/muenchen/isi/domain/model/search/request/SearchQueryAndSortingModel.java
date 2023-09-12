package de.muenchen.isi.domain.model.search.request;

import de.muenchen.isi.domain.model.enums.SortAttribute;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SearchQueryAndSortingModel extends SearchQueryModel {

    private SortAttribute sortBy;

    private SortOrder sortOrder;
}
