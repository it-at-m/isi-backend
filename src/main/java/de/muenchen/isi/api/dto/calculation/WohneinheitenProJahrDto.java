package de.muenchen.isi.api.dto.calculation;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class WohneinheitenProJahrDto {

    private Integer jahr; // JJJJ

    private BigDecimal wohneinheiten;
}
