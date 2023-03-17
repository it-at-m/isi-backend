package de.muenchen.isi.infrastructure.entity;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class Foerderart extends  BaseEntity {

    private String bezeichnung;

    private BigDecimal anteilProzent;

}
