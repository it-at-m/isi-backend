package de.muenchen.isi.infrastructure.entity.calculation;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PersonenProJahr {

    private String jahr; // JJJJ

    private BigDecimal anzahlPersonenGesamt;
}
