package de.muenchen.isi.infrastructure.entity;

import java.math.BigDecimal;
import javax.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Foerderart {

    private String bezeichnung;

    private BigDecimal anteilProzent;
}
