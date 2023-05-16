package de.muenchen.isi.api.dto.stammdaten.baurate;

import de.muenchen.isi.api.dto.BaseEntityDto;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IdealtypischeBaurateDto extends BaseEntityDto {

    private Long wohneinheitenVon;

    private Long wohneinheitenBisEinschliesslich;

    private BigDecimal geschossflaecheWohnenVon;

    private BigDecimal geschossflaecheWohnenBisEinschliesslich;

    private List<JahresrateDto> jahresraten;
}
