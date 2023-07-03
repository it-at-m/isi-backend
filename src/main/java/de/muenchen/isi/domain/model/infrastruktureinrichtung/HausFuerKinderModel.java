/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model.infrastruktureinrichtung;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@DiscriminatorValue("HausFuerKinder")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HausFuerKinderModel extends InfrastruktureinrichtungModel {

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
