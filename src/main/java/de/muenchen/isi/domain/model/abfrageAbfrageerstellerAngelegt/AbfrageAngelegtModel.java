package de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt;

import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.common.AdresseModel;
import de.muenchen.isi.domain.model.common.VerortungModel;
import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVorhaben;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class AbfrageAngelegtModel {

    private List<DokumentModel> dokumente;

    private String allgemeineOrtsangabe;

    private AdresseModel adresse;

    private VerortungModel verortung;

    private LocalDate fristStellungnahme;

    private String anmerkung;

    private String bebauungsplannummer;

    private String nameAbfrage;

    private StandVorhaben standVorhaben;

    private BauvorhabenModel bauvorhaben;
}
