package de.muenchen.isi.domain.model.abfrageSachbearbeitungOffenInBearbeitung;

import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import java.util.List;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class SachbearbeitungAbfrageOffenInBearbeitungModel {

    private List<DokumentModel> dokumente;

    private String anmerkung;
}
