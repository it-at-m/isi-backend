/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.infrastruktureinrichtung;

import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.api.validation.WohnungsnahePlaetzeValid;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Einrichtungstraeger;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class KinderkrippeDto extends InfrastruktureinrichtungDto {

    @NotNull
    @NotUnspecified
    private Einrichtungstraeger einrichtungstraeger;

    @Min(1)
    @Max(12)
    private Integer fertigstellungsmonat; // MM

    @NotNull
    @NotUnspecified
    private UncertainBoolean sobonRelevant;

    @NotNull
    @NotUnspecified
    private UncertainBoolean muenchnerKitafoerderung;

    private Boolean miniKita;

    private Boolean integrationseinrichtung;

    private String nameTraeger;

    private Integer gesamtplaetzeNachBE;

    private Integer kinderkrippenplaetzeNachBE;

    private Integer ueberregionaleKinderkrippenplaetze;

    private Integer belegungKinderkrippenplaetze;

    private Integer sollBelegungKinderkrippenplaetzeKePlus;

    private Integer sollBelegungKinderkrippenplaetze;

    private Integer integrationsplaetzeKinderkrippeKePlus;

    private Integer integrationsplaetzeKinderkrippe;

    private Integer belegungIntegrationsplaetzeKinderkrippe;

    private String linkKePlus;
}
