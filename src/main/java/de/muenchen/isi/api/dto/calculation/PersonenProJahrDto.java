package de.muenchen.isi.api.dto.calculation;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PersonenProJahrDto {

    @NotNull
    private String jahr; // JJJJ

    @NotNull
    private BigDecimal anzahlPersonenGesamt;
}
