/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.infrastruktureinrichtung;

import de.muenchen.isi.api.validation.WohnungsnahePlaetzeValid;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtGsNachmittagBetreuung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Einrichtungstraeger;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@WohnungsnahePlaetzeValid
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

    private Einrichtungstraeger einrichtungstraeger;
}
