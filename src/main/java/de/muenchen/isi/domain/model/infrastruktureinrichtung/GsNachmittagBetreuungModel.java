/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtGsNachmittagBetreuung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Einrichtungstraeger;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GsNachmittagBetreuungModel extends InfrastruktureinrichtungModel {

    // TBD: Grundschulsprengel

    private ArtGsNachmittagBetreuung artGsNachmittagBetreuung;

    private Integer anzahlHortPlaetze;

    private Integer anzahlHortGruppen;

    private Integer wohnungsnaheHortPlaetze;

    private Einrichtungstraeger einrichtungstraeger;
}
