package de.muenchen.isi.domain.model.stammdaten;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EinwohnerJeWohnungModel {

    private Integer jahrNachErsterstellung;

    private BigDecimal anzahl;

}
