/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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

    @Embedded
    private Foerdermix foerdermix;
}
