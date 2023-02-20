/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdresseDto {

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String plz;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String ort;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String strasse;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String hausnummer;

}