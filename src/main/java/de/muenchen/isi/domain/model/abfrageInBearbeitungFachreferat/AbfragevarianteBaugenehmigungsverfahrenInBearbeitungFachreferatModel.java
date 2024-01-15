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
public class AbfragevarianteBaugenehmigungsverfahrenInBearbeitungFachreferatModel {

    private UUID id;

    private Long version;

    private ArtAbfrage artAbfragevariante;

    private List<BedarfsmeldungFachreferateModel> bedarfsmeldungFachreferate;

    // Kindertagesbetreuung

    private boolean ausgeloesterBedarfImBaugebietBeruecksichtigenKita;

    private boolean ausgeloesterBedarfMitversorgungImBplanKita;

    private boolean ausgeloesterBedarfMitversorgungInBestEinrichtungenKita;

    private boolean ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauKita;

    // Schule

    private boolean ausgeloesterBedarfImBaugebietBeruecksichtigenSchule;

    private boolean ausgeloesterBedarfMitversorgungImBplanSchule;

    private boolean ausgeloesterBedarfMitversorgungInBestEinrichtungenSchule;

    private boolean ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauSchule;

    private String hinweisVersorgung;
}
