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
@WohnungsnahePlaetzeValid
public class KindergartenDto extends InfrastruktureinrichtungDto {

    @NotNull
    @NotUnspecified
    private Einrichtungstraeger einrichtungstraeger;

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

    private Integer kindergartenplaetzeNachBE;

    private Integer ueberregionaleKindergartenplaetze;

    private Integer belegungKindergartenplaetze;

    private Integer sollBelegungKindergartenplaetzeKePlus;

    private Integer sollBelegungKindergartenplaetze;

    private Integer integrationsplaetzeKindergartenKePlus;

    private Integer integrationsplaetzeKindergarten;

    private Integer belegungIntegrationsplaetzeKindergarten;

    private String linkKePlus;
}
