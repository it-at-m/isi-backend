package de.muenchen.isi.domain.model;

import de.muenchen.isi.domain.model.common.AdresseModel;
import de.muenchen.isi.domain.model.common.VerortungModel;
import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtBaulicheNutzung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.infrastructure.entity.enums.lookup.WesentlicheRechtsgrundlage;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class BauvorhabenModel extends BaseEntityModel {

    private String nameVorhaben;

    private BigDecimal grundstuecksgroesse;

    private StandVerfahren standVerfahren;

    private String bauvorhabenNummer;

    private AdresseModel adresse;

    private VerortungModel verortung;

    private String bebauungsplannummer;

    private String fisNummer;

    private String anmerkung;

    private UncertainBoolean sobonRelevant;

    private SobonVerfahrensgrundsaetzeJahr sobonJahr;

    private List<WesentlicheRechtsgrundlage> wesentlicheRechtsgrundlage;

    private List<ArtBaulicheNutzung> artFnp;

    private List<DokumentModel> dokumente;

    private AbfragevarianteModel relevanteAbfragevariante;
}
