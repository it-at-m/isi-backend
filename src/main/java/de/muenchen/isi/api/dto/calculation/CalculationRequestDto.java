package de.muenchen.isi.api.dto.calculation;

import java.time.LocalDate;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CalculationRequestDto {

    @NotNull
    private UUID abfrageId;

    @NotNull
    private UUID abfragevarianteId;

    @NotNull
    private LocalDate gueltigAb;
}
