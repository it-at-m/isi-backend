/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung;

import de.muenchen.isi.api.dto.abfrageAngelegt.AbfragevarianteBauleitplanverfahrenAngelegtDto;
import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.domain.model.BauratendateiInputModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungDto
    extends AbfragevarianteBauleitplanverfahrenAngelegtDto {

    @NotUnspecified
    @NotNull
    private SobonOrientierungswertJahr sobonOrientierungswertJahr;

    @NotNull
    private LocalDate stammdatenGueltigAb;

    private String anmerkung;

    private Boolean hasBauratenDateiInputs;

    private String anmerkungBauratenDateiInputs;

    private List<BauratendateiInputModel> bauratendateiInputs;
}
