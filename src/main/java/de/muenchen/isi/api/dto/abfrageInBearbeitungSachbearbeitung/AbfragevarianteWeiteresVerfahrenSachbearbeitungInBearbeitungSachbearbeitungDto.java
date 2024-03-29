/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung;

import de.muenchen.isi.api.dto.common.SobonBerechnungDto;
import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.api.validation.SobonBerechnungValid;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Data;

@Data
public class AbfragevarianteWeiteresVerfahrenSachbearbeitungInBearbeitungSachbearbeitungDto {

    private UUID id;

    private Long version;

    @NotUnspecified
    private ArtAbfrage artAbfragevariante;

    @NotUnspecified
    @NotNull
    private SobonOrientierungswertJahr sobonOrientierungswertJahr;

    @SobonBerechnungValid
    private SobonBerechnungDto sobonBerechnung;

    @NotNull
    private LocalDate stammdatenGueltigAb;

    @Size(max = 1000, message = "Es sind maximal {max} Zeichen erlaubt")
    private String anmerkung;
}
