package de.muenchen.isi.domain.model;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class FoerderartModel {

    private String bezeichnung;

    private BigDecimal anteilProzent;
}
