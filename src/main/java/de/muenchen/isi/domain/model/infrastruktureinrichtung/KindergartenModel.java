/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.enums.lookup.Einrichtungstraeger;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class KindergartenModel extends InfrastruktureinrichtungModel {

    private Integer anzahlKindergartenPlaetze;

    private Integer anzahlKindergartenGruppen;

    private Integer wohnungsnaheKindergartenPlaetze;

    private Einrichtungstraeger einrichtungstraeger;
}
