/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@DiscriminatorValue(InfrastruktureinrichtungTyp.Values.HAUS_FUER_KINDER)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HausFuerKinder extends Infrastruktureinrichtung {

    @Column(nullable = true)
    private Integer anzahlKinderkrippePlaetze;

    @Column(nullable = true)
    private Integer anzahlKindergartenPlaetze;

    @Column(nullable = true)
    private Integer anzahlHortPlaetze;

    @Column(nullable = true)
    private Integer anzahlKinderkrippeGruppen;

    @Column(nullable = true)
    private Integer anzahlKindergartenGruppen;

    @Column(nullable = true)
    private Integer anzahlHortGruppen;

    @Column(nullable = true)
    private Integer wohnungsnaheKinderkrippePlaetze;

    @Column(nullable = true)
    private Integer wohnungsnaheKindergartenPlaetze;

    @Column(nullable = true)
    private Integer wohnungsnaheHortPlaetze;
}
