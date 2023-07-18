package de.muenchen.isi.api.dto.list;

import de.muenchen.isi.api.dto.search.SearchResultDto;
import de.muenchen.isi.domain.model.common.StadtbezirkModel;
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
public class BauvorhabenListElementDto extends SearchResultDto {

    private UUID id;

    private Set<StadtbezirkModel> stadtbezirke;

    private BigDecimal grundstuecksgroesse;

    private StandVorhaben standVorhaben;
}
