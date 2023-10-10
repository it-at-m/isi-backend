package de.muenchen.isi.domain.model;

import de.muenchen.isi.domain.model.common.AdresseModel;
import de.muenchen.isi.domain.model.common.VerortungModel;
import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BauleitplanverfahrenModel extends BaseEntityModel {

    private String name;

    private String bebauungsplannummer;

    private UUID bauvorhaben;

    private UncertainBoolean sobonRelevant;

    private StandVerfahren standVerfahren;

    private String standVerfahrenFreieEingabe;

    private AdresseModel adresse;

    private VerortungModel verortung;

    private List<DokumentModel> dokumente;

    private LocalDate fristBearbeitung;

    private UncertainBoolean offizielleMitzeichnung;

    private String anmerkung;

    private List<AbfragevarianteBauleitplanverfahrenModel> abfragevarianten;

    private List<AbfragevarianteBauleitplanverfahrenModel> abfragevariantenSachbearbeitung;

    private StatusAbfrage statusAbfrage;
}
