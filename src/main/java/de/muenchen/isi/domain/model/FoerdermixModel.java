/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model;

import java.util.List;
import lombok.Data;

@Data
public class FoerdermixModel {

    private String bezeichnungJahr;

    private String bezeichnung;

    private List<FoerderartModel> foerderarten;
}
