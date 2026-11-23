/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.enums.lookup.Einrichtungstraeger;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HausFuerKinderDto extends InfrastruktureinrichtungDto {

    private Einrichtungstraeger einrichtungstraeger;

    @NotNull
    private UncertainBoolean sobonRelevant;

    private Boolean muenchnerKitafoerderung;

    private Boolean miniKita;

    private Boolean kitz;

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

    private Integer kindergartenplaetzeNachBE;

    private Integer ueberregionaleKindergartenplaetze;

    private Integer belegungKindergartenplaetze;

    private Integer sollBelegungKindergartenplaetzeKePlus;

    private Integer sollBelegungKindergartenplaetze;

    private Integer integrationsplaetzeKindergartenKePlus;

    private Integer integrationsplaetzeKindergarten;

    private Integer belegungIntegrationsplaetzeKindergarten;

    private Integer hortplaetzeNachBE;

    private Integer ueberregionaleHortplaetze;

    private Integer belegungHort;

    private Integer sollBelegungHortKePlus;

    private Integer sollBelegungHort;

    private Integer integrationsplaetzeHortKePlus;

    private Integer integrationsplaetzeHort;

    private Integer belegungIntegrationsplaetzeHort;

    private String linkKePlus;
}
