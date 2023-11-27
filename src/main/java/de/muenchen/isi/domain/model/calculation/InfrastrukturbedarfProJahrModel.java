package de.muenchen.isi.domain.model.calculation;

import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InfrastrukturbedarfProJahrModel extends PersonenProJahrModel {

    private BigDecimal anzahlPersonenZuVersorgen;

    private BigDecimal anzahlGruppen;
}
