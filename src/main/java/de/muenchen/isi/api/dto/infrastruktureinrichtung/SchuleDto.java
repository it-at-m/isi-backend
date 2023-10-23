/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.enums.lookup.Einrichtungstraeger;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SchuleDto {

    @NotNull
    private Integer anzahlKlassen;

    @NotNull
    private Integer anzahlPlaetze;

    private Einrichtungstraeger einrichtungstraeger;
}
