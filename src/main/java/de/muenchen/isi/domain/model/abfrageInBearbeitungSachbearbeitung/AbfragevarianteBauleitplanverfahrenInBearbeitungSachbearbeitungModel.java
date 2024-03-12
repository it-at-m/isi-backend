/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung;

import de.muenchen.isi.domain.model.BauratendateiInputModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfragevarianteBauleitplanverfahrenAngelegtModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungModel
    extends AbfragevarianteBauleitplanverfahrenAngelegtModel {

    private SobonOrientierungswertJahr sobonOrientierungswertJahr;

    private LocalDate stammdatenGueltigAb;

    private String anmerkung;

    private Boolean hasBauratendateiInputs;

    private String anmerkungBauratendateiInputs;

    private List<BauratendateiInputModel> bauratendateiInputs;
}
