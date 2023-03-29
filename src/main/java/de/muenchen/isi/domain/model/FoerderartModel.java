package de.muenchen.isi.domain.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class FoerderartModel {

    private String bezeichnung;

    private BigDecimal anteilProzent;

}
