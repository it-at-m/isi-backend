/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model.infrastruktureinrichtung;

import de.muenchen.isi.domain.model.BaugebietModel;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.common.AdresseModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Einrichtungstraeger;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InfrastruktureinrichtungModel {

    private Long lfdNr;

    private BauvorhabenModel bauvorhaben;

    private String allgemeineOrtsangabe;

    private AdresseModel adresse;

    private String nameEinrichtung;

    private Integer fertigstellungsjahr; // JJJJ

    private StatusInfrastruktureinrichtung status;

    private Einrichtungstraeger einrichtungstraeger;

    private BigDecimal flaecheGesamtgrundstueck;

    private BigDecimal flaecheTeilgrundstueck;

    private BaugebietModel zugeordnetesBaugebiet;

}