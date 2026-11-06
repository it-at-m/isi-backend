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
public class KinderkrippeModel extends InfrastruktureinrichtungModel {

    private Einrichtungstraeger einrichtungstraeger;

    private Integer fertigstellungsmonat; // MM

    private UncertainBoolean sobonRelevant;

    private UncertainBoolean muenchnerKitafoerderung;

    private Boolean miniKita;

    private Boolean integrationseinrichtung;

    private String nameTraeger;

    private Integer gesamtplaetzeNachBE;

    private Integer kinderkrippenplaetzeNachBE;

    private Integer ueberregionaleKinderkrippenplaetze;

    private Integer belegungKinderkrippenplaetze;

    private Integer sollBelegungKinderkrippenplaetze;

    private Integer integrationsplaetzeKinderkrippe;

    private Integer integrationsplaetzeKinderkrippeLautBelegung;

    private String linkKePlus;
}
