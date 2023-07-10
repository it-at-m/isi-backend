/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtGsNachmittagBetreuung;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GsNachmittagBetreuungDto extends InfrastruktureinrichtungDto {

    private ArtGsNachmittagBetreuung artGsNachmittagBetreuung;

    @NotNull
    @Min(0)
    private Integer anzahlHortPlaetze;

    @NotNull
    @Min(0)
    private Integer anzahlHortGruppen;

    @Min(0)
    private Integer wohnungsnaheHortPlaetze;
}
