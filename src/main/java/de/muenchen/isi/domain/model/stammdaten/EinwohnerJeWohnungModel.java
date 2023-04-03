package de.muenchen.isi.domain.model.stammdaten;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EinwohnerJeWohnungModel {

    private Integer jahrNachErsterstellung;

    private BigDecimal anzahl;
}
