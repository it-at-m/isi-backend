/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.enums.lookup.Einrichtungstraeger;
import lombok.Data;

@Data
public class SchuleModel {

    private Integer anzahlKlassen;

    private Integer anzahlPlaetze;

    private Einrichtungstraeger einrichtungstraeger;
}
