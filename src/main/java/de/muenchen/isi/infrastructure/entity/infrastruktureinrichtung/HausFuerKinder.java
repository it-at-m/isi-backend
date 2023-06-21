/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
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
public class HausFuerKinder extends BaseEntity {

    @Embedded
    public Infrastruktureinrichtung infrastruktureinrichtung;

    @Column(nullable = false)
    private Integer anzahlKinderkrippePlaetze;

    @Column(nullable = false)
    private Integer anzahlKindergartenPlaetze;

    @Column(nullable = false)
    private Integer anzahlHortPlaetze;

    @Column(nullable = false)
    private Integer anzahlKinderkrippeGruppen;

    @Column(nullable = false)
    private Integer anzahlKindergartenGruppen;

    @Column(nullable = false)
    private Integer anzahlHortGruppen;

    @Column(nullable = true)
    private Integer wohnungsnaheKinderkrippePlaetze;

    @Column(nullable = true)
    private Integer wohnungsnaheKindergartenPlaetze;

    @Column(nullable = true)
    private Integer wohnungsnaheHortPlaetze;
}
