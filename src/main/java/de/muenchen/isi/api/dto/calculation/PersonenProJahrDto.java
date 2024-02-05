package de.muenchen.isi.api.dto.calculation;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class PersonenProJahrDto {

    @NotNull
    private String jahr; // JJJJ

    @NotNull
    private BigDecimal anzahlPersonenGesamt;
}
