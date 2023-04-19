/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto;

import java.util.List;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

@Data
public class FoerdermixDto {

    @UniqueElements(message = "Die Förderart existiert bereits.")
    private List<FoerderartDto> foerderarten;
}
