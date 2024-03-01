/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto;

import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

@Data
public class FoerdermixDto {

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String bezeichnungJahr;

    @Size(max = 80, message = "Es sind maximal {max} Zeichen erlaubt")
    private String bezeichnung;

    @UniqueElements(message = "Die Förderart existiert bereits.")
    private List<FoerderartDto> foerderarten;
}
