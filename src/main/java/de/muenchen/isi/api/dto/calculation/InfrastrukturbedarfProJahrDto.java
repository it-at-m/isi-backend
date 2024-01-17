package de.muenchen.isi.api.dto.calculation;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InfrastrukturbedarfProJahrDto extends PersonenProJahrDto {

    @NotNull
    private BigDecimal anzahlPersonenZuVersorgen;

    @NotNull
    private BigDecimal anzahlGruppen;
}
