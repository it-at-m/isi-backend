package de.muenchen.isi.domain.model.stammdaten.baurate;

import de.muenchen.isi.domain.model.BaseEntityModel;
import de.muenchen.isi.infrastructure.entity.enums.IdealtypischeBaurateTyp;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IdealtypischeBaurateModel extends BaseEntityModel {

    private BigDecimal von;

    private BigDecimal bisEinschliesslich;

    private IdealtypischeBaurateTyp typ;

    private List<JahresrateModel> jahresraten;
}
