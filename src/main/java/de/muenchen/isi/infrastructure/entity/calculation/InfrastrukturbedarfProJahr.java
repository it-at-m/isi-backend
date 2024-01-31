package de.muenchen.isi.infrastructure.entity.calculation;

import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InfrastrukturbedarfProJahr extends PersonenProJahr {

    private BigDecimal anzahlPersonenZuVersorgen;

    private BigDecimal anzahlGruppen;
}
