package de.muenchen.isi.domain.model;

import de.muenchen.isi.domain.model.common.AdresseModel;
import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.BaugebietTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Planungsrecht;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVorhaben;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import java.util.List;
import lombok.Data;

@Data
public class BauvorhabenModel extends BaseEntityModel {

    private String nameVorhaben;

    private String eigentuemer;

    private Long grundstuecksgroesse;

    private StandVorhaben standVorhaben;

    private String bauvorhabenNummer;

    private AdresseModel adresse;

    private String allgemeineOrtsangabe;

    private String bebauungsplannummer;

    private String fisNummer;

    private String anmerkung;

    private UncertainBoolean sobonRelevant;

    private Planungsrecht planungsrecht;

    private List<BaugebietTyp> artFnp;

    private List<DokumentModel> dokumente;
}
