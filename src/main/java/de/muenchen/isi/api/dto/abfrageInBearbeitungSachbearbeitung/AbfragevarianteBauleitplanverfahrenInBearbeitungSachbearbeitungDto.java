/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung;

import de.muenchen.isi.api.dto.AbfragevarianteSachbearbeitungDto;
import de.muenchen.isi.api.dto.abfrageAngelegt.AbfragevarianteBauleitplanverfahrenAngelegtDto;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungDto
    extends AbfragevarianteBauleitplanverfahrenAngelegtDto {

    @NotNull
    @Valid
    private AbfragevarianteSachbearbeitungDto abfragevarianteSachbearbeitung;
}
