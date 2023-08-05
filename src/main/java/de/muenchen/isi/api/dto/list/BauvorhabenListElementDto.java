package de.muenchen.isi.api.dto.list;

import de.muenchen.isi.api.dto.common.StadtbezirkDto;
import de.muenchen.isi.api.dto.search.response.SearchResultDto;
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

    private String nameVorhaben;

    private Set<StadtbezirkDto> stadtbezirke;

    private BigDecimal grundstuecksgroesse;

    private StandVorhaben standVorhaben;
}
