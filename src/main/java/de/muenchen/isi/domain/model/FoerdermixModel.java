/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model;

import java.util.List;
import lombok.Data;

@Data
public class FoerdermixModel {

    private List<FoerderartModel> foerderarten;
}
