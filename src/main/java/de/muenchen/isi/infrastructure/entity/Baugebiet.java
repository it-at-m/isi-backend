package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtBaulicheNutzung;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(indexes = { @Index(name = "baugebiet_bauabschnitt_id_index", columnList = "bauabschnitt_id") })
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Baugebiet extends BaseEntity {

    @Column(nullable = false)
    private String bezeichnung;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ArtBaulicheNutzung artBaulicheNutzung;

    @Column(nullable = false)
    private Integer realisierungVon; // JJJJ

    @Column(precision = 10, scale = 2)
    private BigDecimal gfWohnenGeplant;

    @Column(precision = 10, scale = 2)
    private BigDecimal gfWohnenBaurechtlichGenehmigt;

    @Column(precision = 10, scale = 2)
    private BigDecimal gfWohnenBaurechtlichFestgesetzt;

    @Column
    private Integer weGeplant;

    @Column
    private Integer weBaurechtlichGenehmigt;

    @Column
    private Integer weBaurechtlichFestgesetzt;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "baugebiet_id")
    @OrderBy("jahr asc")
    private List<Baurate> bauraten;

    @Column(nullable = false)
    private Boolean technical;
}
