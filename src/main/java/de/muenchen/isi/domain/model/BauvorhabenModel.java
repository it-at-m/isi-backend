package de.muenchen.isi.domain.model;

import de.muenchen.isi.domain.model.common.AdresseModel;
import de.muenchen.isi.domain.model.common.VerortungModel;
import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.BaugebietArt;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Planungsrecht;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVorhaben;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class BauvorhabenModel extends BaseEntityModel {

    private String nameVorhaben;

    private BigDecimal grundstuecksgroesse;

    private StandVorhaben standVorhaben;

    private String bauvorhabenNummer;

    private AdresseModel adresse;

    private VerortungModel verortung;

    private String allgemeineOrtsangabe;

    private String bebauungsplannummer;

    private String fisNummer;

    private String anmerkung;

    private UncertainBoolean sobonRelevant;

    private SobonVerfahrensgrundsaetzeJahr sobonJahr;

    private Planungsrecht planungsrecht;

    private List<BaugebietArt> artFnp;

    private List<DokumentModel> dokumente;

    private AbfragevarianteModel relevanteAbfragevariante;
}
