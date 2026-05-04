package de.muenchen.isi.domain.model.abfrageAngelegt;

import de.muenchen.isi.domain.model.common.AdresseModel;
import de.muenchen.isi.domain.model.common.VerortungMultiPolygonModel;
import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Verfahrensstand;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class WeiteresVerfahrenAngelegtModel extends AbfrageAngelegtModel {

    private String aktenzeichenProLbk;

    private String bebauungsplannummer;

    private UncertainBoolean sobonRelevant;

    private SobonVerfahrensgrundsaetzeJahr sobonJahr;

    private Verfahrensstand verfahrensstand;

    private String verfahrensstandFreieEingabe;

    private AdresseModel adresse;

    private VerortungMultiPolygonModel verortung;

    private List<DokumentModel> dokumente;

    private LocalDate fristBearbeitung;

    private UncertainBoolean mitzeichnungBeschlussentwurf;

    private List<AbfragevarianteWeiteresVerfahrenAngelegtModel> abfragevariantenWeiteresVerfahren;
}
