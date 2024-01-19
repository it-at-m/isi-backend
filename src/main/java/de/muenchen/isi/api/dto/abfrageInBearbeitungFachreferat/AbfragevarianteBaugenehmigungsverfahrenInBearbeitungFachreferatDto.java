/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.abfrageInBearbeitungFachreferat;

import de.muenchen.isi.api.dto.BedarfsmeldungDto;
import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class AbfragevarianteBaugenehmigungsverfahrenInBearbeitungFachreferatDto {

    private UUID id;

    private Long version;

    @NotUnspecified
    private ArtAbfrage artAbfragevariante;

    private List<@Valid BedarfsmeldungDto> bedarfsmeldungFachreferate;

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
