package de.muenchen.isi.infrastructure.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Foerderart {

    private String bezeichnung;

    @Column(precision = 5, scale = 2)
    private BigDecimal anteilProzent;
}
