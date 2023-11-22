package de.muenchen.isi.domain.model.calculation;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PlanungsursaechlicherBedarfModel {

    private Integer jahr; // JJJJ

    private BigDecimal anzahlKinderGesamt;

    private BigDecimal anzahlKinderZuVersorgen;

    private BigDecimal anzahlGruppen;
}
