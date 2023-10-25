package de.muenchen.isi.api.dto.search.response;

import de.muenchen.isi.api.dto.common.StadtbezirkDto;
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
public class BauvorhabenSearchResultDto extends SearchResultDto {

    private UUID id;

    private String nameVorhaben;

    private Set<StadtbezirkDto> stadtbezirke;

    private BigDecimal grundstuecksgroesse;

    private StandVerfahren standVerfahren;
}
