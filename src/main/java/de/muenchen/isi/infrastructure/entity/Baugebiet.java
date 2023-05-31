package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.entity.enums.lookup.BaugebietTyp;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Baugebiet extends BaseEntity {

    @Column(nullable = false)
    private String bezeichnung;

    @Column(nullable = false)
    private BaugebietTyp baugebietTyp;

    @Column(nullable = true)
    private Integer gesamtanzahlWe;

    @Column(nullable = true)
    private Integer anzahlWohneinheitenBaurechtlichGenehmigt;

    @Column(nullable = true)
    private Integer anzahlWohneinheitenBaurechtlichFestgesetzt;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal geschossflaecheWohnen;

    @Column(nullable = true)
    private BigDecimal geschossflaecheWohnenGenehmigt;

    @Column(nullable = true)
    private BigDecimal geschossflaecheWohnenFestgesetzt;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "baugebiet_id")
    private List<Baurate> bauraten;

    @Column(nullable = false)
    private Boolean technical;
}
