package de.muenchen.isi.domain.model.abfrageAngelegt;

import de.muenchen.isi.domain.model.common.AdresseModel;
import de.muenchen.isi.domain.model.common.VerortungModel;
import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaugenehmigungsverfahrenAngelegtModel extends AbfrageAngelegtModel {

    private String aktenzeichenProLbk;

    private String bebauungsplannummer;

    private StandVerfahren standVerfahren;

    private String standVerfahrenFreieEingabe;

    private AdresseModel adresse;

    private VerortungModel verortung;

    private List<DokumentModel> dokumente;

    private LocalDate fristBearbeitung;

    private List<AbfragevarianteBaugenehmigungsverfahrenAngelegtModel> abfragevarianten;
}
