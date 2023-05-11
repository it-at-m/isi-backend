/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto;

import de.muenchen.isi.api.validation.GeschossflaecheWohnenSobonUrsaechlichValid;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@GeschossflaecheWohnenSobonUrsaechlichValid
public class InfrastrukturabfrageResponseDto extends BaseEntityDto {

    private AbfrageResponseDto abfrage;

    private UncertainBoolean sobonRelevant;

    private SobonVerfahrensgrundsaetzeJahr sobonJahr;

    private List<@Valid @NotNull AbfragevarianteResponseDto> abfragevarianten;

    private String aktenzeichenProLbk;

    private UncertainBoolean offiziellerVerfahrensschritt;

    private String displayName;
}
