package de.muenchen.isi.api.dto.calculation;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
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
