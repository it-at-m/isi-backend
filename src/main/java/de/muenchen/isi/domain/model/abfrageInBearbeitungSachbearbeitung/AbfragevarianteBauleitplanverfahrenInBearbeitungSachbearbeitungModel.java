/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung;

import de.muenchen.isi.domain.model.abfrageAngelegt.AbfragevarianteBauleitplanverfahrenAngelegtModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungModel
    extends AbfragevarianteBauleitplanverfahrenAngelegtModel {

    private BigDecimal gfWohnenPlanungsursaechlich;

    private SobonOrientierungswertJahr sobonOrientierungswertJahr;

    private LocalDate stammdatenGueltigAb;

    private String anmerkung;
}
