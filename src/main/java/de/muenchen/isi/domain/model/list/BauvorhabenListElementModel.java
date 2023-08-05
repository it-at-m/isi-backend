package de.muenchen.isi.domain.model.list;

import de.muenchen.isi.domain.model.common.StadtbezirkModel;
import de.muenchen.isi.domain.model.search.response.SearchResultModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVorhaben;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BauvorhabenListElementModel extends SearchResultModel {

    private UUID id;

    private String nameVorhaben;

    private Set<StadtbezirkModel> stadtbezirke;

    private BigDecimal grundstuecksgroesse;

    private StandVorhaben standVorhaben;
}
