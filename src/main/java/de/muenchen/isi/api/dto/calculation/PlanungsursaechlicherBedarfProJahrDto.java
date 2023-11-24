package de.muenchen.isi.api.dto.calculation;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlanungsursaechlicherBedarfProJahrDto {

    @NotNull
    private String jahr; // JJJJ

    @NotNull
    private BigDecimal anzahlKinderGesamt;

    @NotNull
    private BigDecimal anzahlKinderZuVersorgen;

    @NotNull
    private BigDecimal anzahlGruppen;
}
