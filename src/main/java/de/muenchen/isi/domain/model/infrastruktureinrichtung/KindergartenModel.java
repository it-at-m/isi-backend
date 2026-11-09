/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.enums.lookup.Einrichtungstraeger;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class KindergartenModel extends InfrastruktureinrichtungModel {

    private Einrichtungstraeger einrichtungstraeger;

    private UncertainBoolean sobonRelevant;

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
