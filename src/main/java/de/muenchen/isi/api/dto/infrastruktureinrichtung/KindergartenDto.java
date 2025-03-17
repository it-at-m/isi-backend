/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.infrastruktureinrichtung;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.muenchen.isi.api.validation.WohnungsnahePlaetzeValid;
import de.muenchen.isi.infrastructure.entity.enums.EntityType;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Einrichtungstraeger;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@WohnungsnahePlaetzeValid
public class KindergartenDto extends InfrastruktureinrichtungDto {

    @JsonIgnore
    private EntityType entityType = EntityType.KINDERGARTEN;

    @NotNull
    private Integer anzahlKindergartenPlaetze;

    @NotNull
    private Integer anzahlKindergartenGruppen;

    private Integer wohnungsnaheKindergartenPlaetze;

    private Einrichtungstraeger einrichtungstraeger;
}
