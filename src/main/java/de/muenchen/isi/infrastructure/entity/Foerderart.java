package de.muenchen.isi.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import lombok.Data;

@Embeddable
@Data
public class Foerderart {

    private String bezeichnung;

    @Column(precision = 5, scale = 2)
    private BigDecimal anteilProzent;
}
