package de.muenchen.isi.infrastructure.entity.stammdaten.baurate;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class Jahresrate {

    private Integer jahr;

    private BigDecimal rate;
}
