/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung;

import de.muenchen.isi.domain.model.abfrageAngelegt.AbfragevarianteWeiteresVerfahrenAngelegtModel;
import de.muenchen.isi.domain.model.bauratendatei.BauratendateiInputModel;
import de.muenchen.isi.domain.model.common.SobonBerechnungModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AbfragevarianteWeiteresVerfahrenInBearbeitungSachbearbeitungModel
    extends AbfragevarianteWeiteresVerfahrenAngelegtModel {

    private SobonOrientierungswertJahr sobonOrientierungswertJahr;

    private SobonBerechnungModel sobonBerechnung;

    private LocalDate stammdatenGueltigAb;

    private String anmerkung;

    private Boolean hasBauratendateiInputs;

    private String anmerkungBauratendateiInputs;

    private BauratendateiInputModel bauratendateiInputBasis;

    private List<BauratendateiInputModel> bauratendateiInputs;
}
