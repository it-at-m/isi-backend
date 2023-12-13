/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.abfrageInBearbeitungFachreferat;

import de.muenchen.isi.api.dto.BedarfsmeldungFachreferateDto;
import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import lombok.Data;

@Data
public class AbfragevarianteBauleitplanverfahrenInBearbeitungFachreferatDto {

    private UUID id;

    private Long version;

    @NotUnspecified
    private ArtAbfrage artAbfragevariante;

    private List<@Valid BedarfsmeldungFachreferateDto> bedarfsmeldungFachreferate;

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
