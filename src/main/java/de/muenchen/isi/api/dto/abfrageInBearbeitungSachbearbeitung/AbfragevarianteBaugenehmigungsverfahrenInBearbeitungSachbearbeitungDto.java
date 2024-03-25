/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung;

import de.muenchen.isi.api.dto.abfrageAngelegt.AbfragevarianteBaugenehmigungsverfahrenAngelegtDto;
import de.muenchen.isi.api.dto.bauratendatei.BauratendateiInputDto;
import de.muenchen.isi.api.dto.bauratendatei.WithBauratendateiInputDto;
import de.muenchen.isi.api.validation.BauratendateiInputsValid;
import de.muenchen.isi.api.validation.NotUnspecified;
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
@BauratendateiInputsValid
public class AbfragevarianteBaugenehmigungsverfahrenInBearbeitungSachbearbeitungDto
    extends AbfragevarianteBaugenehmigungsverfahrenAngelegtDto
    implements WithBauratendateiInputDto {

    @NotUnspecified
    @NotNull
    private SobonOrientierungswertJahr sobonOrientierungswertJahr;

    @NotNull
    private LocalDate stammdatenGueltigAb;

    @Size(max = 1000, message = "Es sind maximal {max} Zeichen erlaubt")
    private String anmerkung;

    @NotNull
    private Boolean hasBauratendateiInputs;

    private String anmerkungBauratendateiInputs;

    private BauratendateiInputDto bauratendateiInputBasis;

    private List<BauratendateiInputDto> bauratendateiInputs;
}
