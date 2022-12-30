/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model.infrastruktureinrichtung;

import de.muenchen.isi.domain.model.BaseEntityModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HausFuerKinderModel extends BaseEntityModel {

    private InfrastruktureinrichtungModel infrastruktureinrichtung;

    private Integer anzahlKinderkrippePlaetze;

    private Integer anzahlKindergartenPlaetze;

    private Integer anzahlHortPlaetze;

    private Integer anzahlKinderkrippeGruppen;

    private Integer anzahlKindergartenGruppen;

    private Integer anzahlHortGruppen;

    private Integer wohnungsnaheKinderkrippePlaetze;

    private Integer wohnungsnaheKindergartenPlaetze;

    private Integer wohnungsnaheHortPlaetze;

}