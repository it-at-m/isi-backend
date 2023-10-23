/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.enums.lookup.Einrichtungstraeger;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HausFuerKinderDto extends InfrastruktureinrichtungDto {

    private Integer anzahlKinderkrippePlaetze;

    private Integer anzahlKindergartenPlaetze;

    private Integer anzahlHortPlaetze;

    private Integer anzahlKinderkrippeGruppen;

    private Integer anzahlKindergartenGruppen;

    private Integer anzahlHortGruppen;

    private Integer wohnungsnaheKinderkrippePlaetze;

    private Integer wohnungsnaheKindergartenPlaetze;

    private Integer wohnungsnaheHortPlaetze;

    private Einrichtungstraeger einrichtungstraeger;
}
