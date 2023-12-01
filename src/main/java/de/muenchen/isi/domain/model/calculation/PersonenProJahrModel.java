package de.muenchen.isi.domain.model.calculation;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PersonenProJahrModel {

    private String jahr; // JJJJ

    private BigDecimal anzahlPersonenGesamt;
}
