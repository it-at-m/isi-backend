package de.muenchen.isi.domain.model;

import de.muenchen.isi.domain.model.common.AdresseModel;
import de.muenchen.isi.domain.model.common.BearbeitendePersonModel;
import de.muenchen.isi.domain.model.common.VerortungMultiPolygonModel;
import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtBaulicheNutzung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Verfahrensstand;
import de.muenchen.isi.infrastructure.entity.enums.lookup.WesentlicheRechtsgrundlage;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BauvorhabenModel extends BaseEntityModel {

    private BearbeitendePersonModel bearbeitendePerson;

    private String nameVorhaben;

    private BigDecimal grundstuecksgroesse;

    private Verfahrensstand verfahrensstand;

    private String verfahrensstandFreieEingabe;

    private String bauvorhabenNummer;

    private AdresseModel adresse;

    private VerortungMultiPolygonModel verortung;

    private String bebauungsplannummer;

    private String fisNummer;

    private String anmerkung;

    private UncertainBoolean sobonRelevant;

    private SobonVerfahrensgrundsaetzeJahr sobonJahr;

    private List<WesentlicheRechtsgrundlage> wesentlicheRechtsgrundlage;

    private String wesentlicheRechtsgrundlageFreieEingabe;

    private List<ArtBaulicheNutzung> artFnp;

    private String artFnpFreieEingabe;

    private List<DokumentModel> dokumente;

    private UUID relevanteAbfragevariante;
}
