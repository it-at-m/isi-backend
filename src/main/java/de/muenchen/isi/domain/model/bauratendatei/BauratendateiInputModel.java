package de.muenchen.isi.domain.model.bauratendatei;

import de.muenchen.isi.domain.model.BaseEntityModel;
import java.util.List;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BauratendateiInputModel extends BaseEntityModel {

    private Set<String> grundschulsprengel;

    private Set<String> mittelschulsprengel;

    private Set<String> viertel;

    private List<BauratendateiWohneinheitenModel> wohneinheiten;
}
