/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung;

import de.muenchen.isi.domain.model.BauratendateiInputModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class AbfragevarianteBauleitplanverfahrenSachbearbeitungInBearbeitungSachbearbeitungModel {

    private UUID id;

    private Long version;

    private ArtAbfrage artAbfragevariante;

    private SobonOrientierungswertJahr sobonOrientierungswertJahr;

    private LocalDate stammdatenGueltigAb;

    private String anmerkung;

    private Boolean hasBauratendateiInputs;

    private String anmerkungBauratendateiInputs;

    private BauratendateiInputModel bauratendateiInputBasis;

    private List<BauratendateiInputModel> bauratendateiInputs;
}
