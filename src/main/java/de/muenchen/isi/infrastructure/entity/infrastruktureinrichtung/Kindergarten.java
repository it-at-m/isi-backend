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
@DiscriminatorValue(InfrastruktureinrichtungTyp.Values.KINDERGARTEN)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Kindergarten extends Infrastruktureinrichtung {

    @Column(nullable = false)
    private Integer anzahlKindergartenPlaetze;

    @Column(nullable = false)
    private Integer anzahlKindergartenGruppen;

    @Column(nullable = true)
    private Integer wohnungsnaheKindergartenPlaetze;
}
