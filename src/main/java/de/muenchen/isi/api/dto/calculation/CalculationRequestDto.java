package de.muenchen.isi.api.dto.calculation;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class CalculationRequestDto {

    @NotNull
    private UUID abfrageId;

    @NotNull
    private UUID abfragevarianteId;
}
