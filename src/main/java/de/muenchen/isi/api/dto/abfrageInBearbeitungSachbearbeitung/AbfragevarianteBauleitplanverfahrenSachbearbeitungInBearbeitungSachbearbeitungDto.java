/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung;

import de.muenchen.isi.api.dto.AbfragevarianteSachbearbeitungDto;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AbfragevarianteBauleitplanverfahrenSachbearbeitungInBearbeitungSachbearbeitungDto {

    private UUID id;

    private Long version;

    @NotNull
    @Valid
    private AbfragevarianteSachbearbeitungDto abfragevarianteSachbearbeitung;
}
