/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung;

import de.muenchen.isi.api.dto.abfrageAngelegt.AbfragevarianteWeiteresVerfahrenAngelegtDto;
import de.muenchen.isi.api.dto.bauratendatei.BauratendateiInputDto;
import de.muenchen.isi.api.dto.bauratendatei.WithBauratendateiInputDto;
import de.muenchen.isi.api.dto.common.SobonBerechnungDto;
import de.muenchen.isi.api.validation.BauratendateiInputValid;
import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.api.validation.SobonBerechnungValid;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@BauratendateiInputValid
public class AbfragevarianteWeiteresVerfahrenInBearbeitungSachbearbeitungDto
    extends AbfragevarianteWeiteresVerfahrenAngelegtDto
    implements WithBauratendateiInputDto {

    @NotUnspecified
    @NotNull
    private SobonOrientierungswertJahr sobonOrientierungswertJahr;

    @SobonBerechnungValid
    private SobonBerechnungDto sobonBerechnung;

    @NotNull
    private LocalDate stammdatenGueltigAb;

    @Size(max = 1000, message = "Es sind maximal {max} Zeichen erlaubt")
    private String anmerkung;

    @NotNull
    private Boolean hasBauratendateiInput;

    private String anmerkungBauratendateiInput;

    private BauratendateiInputDto bauratendateiInputBasis;

    private List<BauratendateiInputDto> bauratendateiInput;
}
