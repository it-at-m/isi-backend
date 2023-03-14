/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.entity.enums.lookup.Planungsrecht;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
public class Abfragevariante extends BaseEntity {

    @Column(nullable = false)
    private Integer abfragevariantenNr;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Planungsrecht planungsrecht;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal geschossflaecheWohnen;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal geschossflaecheWohnenGenehmigt;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal geschossflaecheWohnenFestgesetzt;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal geschossflaecheWohnenSoBoNursaechlich;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal geschossflaecheWohnenBestandswohnbaurecht;

    @Column(nullable = true)
    private Integer gesamtanzahlWe;

    @Column(nullable = true)
    private Integer anzahlWeBaurechtlichGenehmigt;

    @Column(nullable = true)
    private Integer anzahlWeBaurechtlichFestgesetzt;

    @Column(nullable = false)
    private Integer realisierungVon; // JJJJ

    @Column(nullable = false)
    private Integer realisierungBis; // JJJJ

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal geschossflaecheGenossenschaftlicheWohnungen;

    @Column(nullable = false)
    private Boolean sonderwohnformen;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal geschossflaecheStudentenwohnungen;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal geschossflaecheSeniorenwohnungen;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal geschossflaecheSonstiges;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "abfragevariante_id")
    private List<Bauabschnitt> bauabschnitte;
}
