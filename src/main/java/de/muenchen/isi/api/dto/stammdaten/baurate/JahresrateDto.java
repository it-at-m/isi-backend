package de.muenchen.isi.api.dto.stammdaten.baurate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class JahresrateDto {

    @NotNull
    @Min(1)
    private Integer jahr;

    @Schema(description = "Die Rate repräsentiert eine Prozentzahl größer den Wert 0 und kleiner gleich 1.")
    @NotNull
    @Positive
    @Max(1)
    private BigDecimal rate;
}
