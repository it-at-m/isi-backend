package de.muenchen.isi.api.dto.stammdaten.baurate;

import de.muenchen.isi.api.dto.BaseEntityDto;
import de.muenchen.isi.infrastructure.entity.enums.IdealtypischeBaurateTyp;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IdealtypischeBaurateDto extends BaseEntityDto {

    private BigDecimal von;

    private BigDecimal bisExklusiv;

    private IdealtypischeBaurateTyp typ;

    private List<JahresrateDto> jahresraten;
}
