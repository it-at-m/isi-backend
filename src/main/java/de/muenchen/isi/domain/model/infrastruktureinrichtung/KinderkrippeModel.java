/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model.infrastruktureinrichtung;

import de.muenchen.isi.domain.model.BaseEntityModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class KinderkrippeModel extends BaseEntityModel {

    private InfrastruktureinrichtungModel infrastruktureinrichtung;

    private Integer anzahlKinderkrippePlaetze;

    private Integer anzahlKinderkrippeGruppen;

    private Integer wohnungsnaheKinderkrippePlaetze;
}
