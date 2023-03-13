package de.muenchen.isi.infrastructure.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Foerderart extends  BaseEntity {

    private String bezeichnung;

    private BigDecimal anteilProzent;

    @ManyToOne
    private Foerdermix foerdermix;

    @OneToMany(mappedBy = "foerderart")
    private List<Baurate> bauraten;

}
