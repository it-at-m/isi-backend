package de.muenchen.isi.domain.model.calculation;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class WohneinheitenProJahrModel {

    private Integer jahr; // JJJJ

    private BigDecimal wohneinheiten;
}
