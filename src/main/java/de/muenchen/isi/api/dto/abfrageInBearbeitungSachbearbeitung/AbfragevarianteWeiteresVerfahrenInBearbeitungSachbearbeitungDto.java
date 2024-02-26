/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung;

import de.muenchen.isi.api.dto.FoerdermixDto;
import de.muenchen.isi.api.dto.abfrageAngelegt.AbfragevarianteWeiteresVerfahrenAngelegtDto;
import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.api.validation.SobonFoerdermixWeiteresverfahrenAVValid;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SobonFoerdermixWeiteresverfahrenAVValid
public class AbfragevarianteWeiteresVerfahrenInBearbeitungSachbearbeitungDto
    extends AbfragevarianteWeiteresVerfahrenAngelegtDto {

    @NotUnspecified
    @NotNull
    private SobonOrientierungswertJahr sobonOrientierungswertJahr;

    private Boolean isASobonBerechnung;

    private FoerdermixDto sobonFoerdermix;

    @NotNull
    private LocalDate stammdatenGueltigAb;

    private String anmerkung;
}
