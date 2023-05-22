package de.muenchen.isi.api.dto.stammdaten.baurate;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import lombok.Data;

@Data
public class JahresrateDto {

    @Min(1)
    private Integer jahr;

    @Schema(description = "Die Rate repräsentiert eine Prozentzahl großer den Wert 0 und kleiner gleich 1.")
    @Positive
    @Max(1)
    private BigDecimal rate;
}
