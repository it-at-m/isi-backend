/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.abfrageBedarfsmeldungErfolgt;

import de.muenchen.isi.api.dto.BedarfsmeldungDto;
import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class AbfragevarianteBauleitplanverfahrenBedarfsmeldungErfolgtDto {

    private UUID id;

    private Long version;

    @NotUnspecified
    private ArtAbfrage artAbfragevariante;

    private List<@Valid BedarfsmeldungDto> bedarfsmeldungAbfrageersteller;
}
