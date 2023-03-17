/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model;

import de.muenchen.isi.infrastructure.entity.Foerderart;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class FoerdermixModel {

    private List<FoerderartModel> foerderarten;

}
