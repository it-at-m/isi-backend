package de.muenchen.isi.domain.model.stammdaten.baurate;

import de.muenchen.isi.domain.model.BaseEntityModel;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IdealtypischeBaurateModel extends BaseEntityModel {

    private Long wohneinheitenVon;

    private Long wohneinheitenBisEinschliesslich;

    private BigDecimal geschossflaecheWohnenVon;

    private BigDecimal geschossflaecheWohnenBisEinschliesslich;

    private List<JahresrateModel> jahresraten;
}
