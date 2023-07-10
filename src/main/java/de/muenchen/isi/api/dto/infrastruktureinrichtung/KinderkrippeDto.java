/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.infrastruktureinrichtung;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class KinderkrippeDto extends InfrastruktureinrichtungDto {

    @NotNull
    private Integer anzahlKinderkrippePlaetze;

    @NotNull
    private Integer anzahlKinderkrippeGruppen;

    private Integer wohnungsnaheKinderkrippePlaetze;
}
