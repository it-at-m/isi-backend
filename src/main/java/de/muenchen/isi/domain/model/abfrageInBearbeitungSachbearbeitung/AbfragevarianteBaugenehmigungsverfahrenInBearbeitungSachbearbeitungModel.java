/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung;

import de.muenchen.isi.domain.model.abfrageAngelegt.AbfragevarianteBaugenehmigungsverfahrenAngelegtModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AbfragevarianteBaugenehmigungsverfahrenInBearbeitungSachbearbeitungModel
    extends AbfragevarianteBaugenehmigungsverfahrenAngelegtModel {

    private SobonOrientierungswertJahr sobonOrientierungswertJahr;

    private LocalDate stammdatenGueltigAb;

    private String anmerkung;
}
