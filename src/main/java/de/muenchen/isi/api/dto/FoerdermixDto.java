/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto;


import lombok.Data;
import java.util.List;

@Data
public class FoerdermixDto {

    private List<FoerderartDto> foerderarten;

}
