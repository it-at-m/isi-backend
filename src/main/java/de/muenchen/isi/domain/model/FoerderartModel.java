package de.muenchen.isi.domain.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class FoerderartModel extends BaseEntityModel {

    private String bezeichnung;

    private BigDecimal anteilProzent;

}
