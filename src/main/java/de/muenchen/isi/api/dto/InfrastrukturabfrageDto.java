/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto;

import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class InfrastrukturabfrageDto extends BaseEntityDto {

    @Valid
    @NotNull
    private AbfrageDto abfrage;

    @NotNull
    @NotUnspecified
    private UncertainBoolean sobonRelevant;

    private SobonVerfahrensgrundsaetzeJahr sobonJahr;

    @NotEmpty
    @Size(min = 1, max = 5)
    private List<@Valid @NotNull AbfragevarianteDto> abfragevarianten;

    private String aktenzeichenProLbk;

    @NotNull
    @NotUnspecified
    private UncertainBoolean offiziellerVerfahrensschritt;

    private String displayName;
}