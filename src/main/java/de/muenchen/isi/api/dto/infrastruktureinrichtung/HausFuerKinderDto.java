/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.infrastruktureinrichtung;

import de.muenchen.isi.api.dto.BaseEntityDto;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HausFuerKinderDto extends BaseEntityDto {

    @Valid
    @NotNull
    private InfrastruktureinrichtungDto infrastruktureinrichtung;

    @NotNull
    private Integer anzahlKinderkrippePlaetze;

    @NotNull
    private Integer anzahlKindergartenPlaetze;

    @NotNull
    private Integer anzahlHortPlaetze;

    @NotNull
    private Integer anzahlKinderkrippeGruppen;

    @NotNull
    private Integer anzahlKindergartenGruppen;

    @NotNull
    private Integer anzahlHortGruppen;

    private Integer wohnungsnaheKinderkrippePlaetze;

    private Integer wohnungsnaheKindergartenPlaetze;

    private Integer wohnungsnaheHortPlaetze;
}
