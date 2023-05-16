package de.muenchen.isi.api.dto.stammdaten.baurate;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class JahresrateDto {

    private Integer jahr;

    private BigDecimal rate;
}
