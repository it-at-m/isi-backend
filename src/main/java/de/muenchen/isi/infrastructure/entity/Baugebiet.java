package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtBaulicheNutzung;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
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
    private ArtBaulicheNutzung artBaulicheNutzung;

    @Column(nullable = false)
    private Integer realisierungVon; // JJJJ

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal gfWohnenGeplant;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal gfWohnenBaurechtlichGenehmigt;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal gfWohnenBaurechtlichFestgesetzt;

    @Column(nullable = true)
    private Integer weGeplant;

    @Column(nullable = true)
    private Integer weBaurechtlichGenehmigt;

    @Column(nullable = true)
    private Integer weBaurechtlichFestgesetzt;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "baugebiet_id")
    @OrderBy("jahr asc")
    private List<Baurate> bauraten;

    @Column(nullable = false)
    private Boolean technical;
}
