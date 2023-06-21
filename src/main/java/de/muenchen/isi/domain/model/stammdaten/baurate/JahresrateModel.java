package de.muenchen.isi.domain.model.stammdaten.baurate;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class JahresrateModel {

    private Integer jahr;

    private BigDecimal rate;
}
