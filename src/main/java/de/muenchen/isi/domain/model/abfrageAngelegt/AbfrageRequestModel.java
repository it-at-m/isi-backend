package de.muenchen.isi.domain.model.abfrageAngelegt;

import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.common.AdresseModel;
import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVorhaben;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Data
@ToString(callSuper = true)
public class AbfrageRequestModel {

    private List<DokumentModel> dokumente;

    private String allgemeineOrtsangabe;

    private AdresseModel adresse;


    private LocalDate fristStellungnahme;

    private String anmerkung;

    private String bebauungsplannummer;
    
    private String nameAbfrage;

    private StandVorhaben standVorhaben;
    private BauvorhabenModel bauvorhaben;
}
