package de.muenchen.isi.domain.model;

import de.muenchen.isi.domain.model.calculation.BauratendateiWohneinheitenModel;
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

    private List<BauratendateiWohneinheitenModel> wohneinheiten;
}
