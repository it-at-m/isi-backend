package de.muenchen.isi.infrastructure.entity;

import lombok.Data;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@Data
public class Foerderart extends  BaseEntity {

    private String bezeichnung;

    private BigDecimal anteilProzent;

}
