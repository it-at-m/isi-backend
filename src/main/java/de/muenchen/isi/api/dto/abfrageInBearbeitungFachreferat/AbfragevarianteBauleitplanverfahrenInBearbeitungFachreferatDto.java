/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.abfrageInBearbeitungFachreferat;

import de.muenchen.isi.api.dto.BedarfsmeldungFachreferateDto;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import lombok.Data;

@Data
public class AbfragevarianteBauleitplanverfahrenInBearbeitungFachreferatDto {

    private UUID id;

    private Long version;

    private List<@Valid BedarfsmeldungFachreferateDto> bedarfsmeldungFachreferate;
}
