package de.muenchen.isi.infrastructure.entity;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import lombok.Data;

@Embeddable
@Data
public class Foerderart {

    private String bezeichnung;

    private BigDecimal anteilProzent;
}
