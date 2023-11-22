package de.muenchen.isi.domain.model.calculation;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanungsursachlicheWohneinheitenModel {

    private String foerderart;

    private Integer jahr; // JJJJ

    private BigDecimal wohneinheiten;
}
