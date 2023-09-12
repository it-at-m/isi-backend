package de.muenchen.isi.api.dto.search.request;

import de.muenchen.isi.domain.model.enums.SortAttribute;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SearchQueryAndSortingDto extends SearchQueryDto {

    @NotNull
    private SortAttribute sortBy;

    @NotNull
    private SortOrder sortOrder;
}
