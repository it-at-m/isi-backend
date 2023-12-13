/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat;

import de.muenchen.isi.domain.model.BedarfsmeldungFachreferateModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class AbfragevarianteBauleitplanverfahrenInBearbeitungFachreferatModel {

    private UUID id;

    private Long version;

    private ArtAbfrage artAbfragevariante;

    private List<BedarfsmeldungFachreferateModel> bedarfsmeldungFachreferate;

    // Kindertagesbetreuung

    private boolean ausgelBedarfImBaugebietBeruecksichtigenKita;

    private boolean ausgelBedarfMitversorgungImBplanKita;

    private boolean ausgelBedarfMitversorgungInBestEinrichtungenKita;

    private boolean ausgelBedarfMitversorgungInBestEinrichtungenNachAusbauKita;

    // Schule

    private boolean ausgelBedarfImBaugebietBeruecksichtigenSchule;

    private boolean ausgelBedarfMitversorgungImBplanSchule;

    private boolean ausgelBedarfMitversorgungInBestEinrichtungenSchule;

    private boolean ausgelBedarfMitversorgungInBestEinrichtungenNachAusbauSchule;

    private String hinweisVersorgung;
}
