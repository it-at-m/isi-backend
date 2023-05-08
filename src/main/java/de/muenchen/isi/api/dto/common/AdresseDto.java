/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.common;

import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private Wgs84Dto coordinate;
}
