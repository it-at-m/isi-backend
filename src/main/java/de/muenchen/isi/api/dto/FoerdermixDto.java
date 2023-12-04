/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

@Data
public class FoerdermixDto {

    @NotEmpty
    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String bezeichnungJahr;

    @NotEmpty
    @Size(max = 80, message = "Es sind maximal {max} Zeichen erlaubt")
    private String bezeichnung;

    @UniqueElements(message = "Die Förderart existiert bereits.")
    private List<FoerderartDto> foerderarten;
}
