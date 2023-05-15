package de.muenchen.isi.domain.model.stammdaten.baurate;

import de.muenchen.isi.domain.model.BaseEntityModel;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IdealtypischeBaurateModel extends BaseEntityModel {

    private SelectionRangeModel rangeWohneinheiten;

    private SelectionRangeModel rangeGeschossflaecheWohnen;

    private List<JahresrateModel> jahresraten;
}
