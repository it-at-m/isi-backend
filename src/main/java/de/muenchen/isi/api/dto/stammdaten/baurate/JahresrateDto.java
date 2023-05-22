package de.muenchen.isi.api.dto.stammdaten.baurate;

import java.math.BigDecimal;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.Data;

@Data
public class JahresrateDto {

    @Min(1)
    private Integer jahr;

    @Min(0)
    @Max(1)
    private BigDecimal rate;
}
