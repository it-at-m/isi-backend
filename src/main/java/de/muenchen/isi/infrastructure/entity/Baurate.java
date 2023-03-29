/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.math.BigDecimal;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Baurate extends BaseEntity {

    @Column(nullable = false)
    private Integer jahr; // JJJJ

    @Column(nullable = true)
    private Integer anzahlWeGeplant;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal geschossflaecheWohnenGeplant;

    @OneToOne(cascade = CascadeType.ALL)
    private Foerdermix foerdermix;

}
