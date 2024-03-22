package de.muenchen.isi.domain.model.search.response;

import de.muenchen.isi.domain.model.common.MultiPolygonGeometryModel;
import de.muenchen.isi.domain.model.common.StadtbezirkModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BauvorhabenSearchResultModel extends SearchResultModel {

    private UUID id;

    private String nameVorhaben;

    private Set<StadtbezirkModel> stadtbezirke;

    private BigDecimal grundstuecksgroesse;

    private StandVerfahren standVerfahren;

    private MultiPolygonGeometryModel umgriff;
}
