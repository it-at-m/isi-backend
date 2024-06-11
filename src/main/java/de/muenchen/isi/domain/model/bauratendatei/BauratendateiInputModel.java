package de.muenchen.isi.domain.model.bauratendatei;

import de.muenchen.isi.domain.model.BaseEntityModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenProFoerderartProJahrModel;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BauratendateiInputModel extends BaseEntityModel {

    private List<String> grundschulsprengel;

    private List<String> mittelschulsprengel;

    private List<String> viertel;

    private List<WohneinheitenProFoerderartProJahrModel> wohneinheiten;
}
