/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;
import java.util.List;

@Data
public class FoerdermixDto extends BaseEntityDto{

    @UniqueElements(message = "Die Förderart existiert bereits.")
    private List<FoerderartDto> foerderarten;

}
