package de.muenchen.isi.infrastructure.entity;

import lombok.Data;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
@Data
public class Foerderart {

    private String bezeichnung;

    private BigDecimal anteilProzent;

}
