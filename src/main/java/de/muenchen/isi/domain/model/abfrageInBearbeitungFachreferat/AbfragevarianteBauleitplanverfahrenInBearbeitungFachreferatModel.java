/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat;

import de.muenchen.isi.domain.model.BedarfsmeldungFachreferateModel;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class AbfragevarianteBauleitplanverfahrenInBearbeitungFachreferatModel {

    private UUID id;

    private Long version;

    private List<BedarfsmeldungFachreferateModel> bedarfsmeldungFachreferate;
}
