/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model;

import de.muenchen.isi.domain.model.common.AdresseModel;
import de.muenchen.isi.domain.model.common.VerortungModel;
import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVorhaben;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class AbfrageModel {

    private List<DokumentModel> dokumente;

    private String allgemeineOrtsangabe;

    private AdresseModel adresse;

    private VerortungModel verortung;

    private LocalDate fristStellungnahme;

    private String anmerkung;

    private StatusAbfrage statusAbfrage;

    private boolean schnellesSchliessenAbfrage;

    private String bebauungsplannummer;

    private String nameAbfrage;

    private StandVorhaben standVorhaben;

    private BauvorhabenModel bauvorhaben;

    /**
     * Gibt den zusammengesetzten Namen der Abfrage zurück
     *
     * @return der zusammengesetzte Namen
     */
    protected String getDisplayName() {
        final var displayName = new StringBuilder();
        displayName.append(this.getNameAbfrage());
        if (!StringUtils.isEmpty(this.getBebauungsplannummer())) {
            displayName.append(String.format(" - BPlan.: %s", this.getBebauungsplannummer()));
        }
        return displayName.toString();
    }
}
