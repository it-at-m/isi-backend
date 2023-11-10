package de.muenchen.isi.domain.model.calculation;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WohneinheitenProJahrModel {

    // JJJJ
    private Integer jahr;

    // BigDecimal wird genutzt, um Nachkommastellen für weitere Berechnungen beizubehalten.
    private BigDecimal wohneinheiten;
}
