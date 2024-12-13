/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model.abfrageStartBearbeitung;

import de.muenchen.isi.domain.model.abfrageAngelegt.AbfragevarianteBaugenehmigungsverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.bauratendatei.BauratendateiInputModel;
import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AbfragevarianteBaugenehmigungsverfahrenStartBearbeitungModel
    extends AbfragevarianteBaugenehmigungsverfahrenAngelegtModel {

    private SobonOrientierungswertJahr sobonOrientierungswertJahrPlanungsursaechlich;

    private LocalDate stammdatenGueltigAb;

    private String anmerkung;

    private Boolean hasBauratendateiInput;

    private String anmerkungBauratendateiInput;

    private BauratendateiInputModel bauratendateiInputBasis;

    private List<BauratendateiInputModel> bauratendateiInput;

    private List<DokumentModel> dokumente;
}
