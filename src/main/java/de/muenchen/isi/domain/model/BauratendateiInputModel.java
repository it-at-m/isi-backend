package de.muenchen.isi.domain.model;

import de.muenchen.isi.domain.model.calculation.WohneinheitenProFoerderartProJahrModel;
import java.util.List;
import lombok.Data;

@Data
public class BauratendateiInputModel {

    private String grundschulsprengel;

    private String mittelschulsprengel;

    private String viertel;

    private List<WohneinheitenProFoerderartProJahrModel> wohneinheiten;
}
