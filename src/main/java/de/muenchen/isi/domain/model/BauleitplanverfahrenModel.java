package de.muenchen.isi.domain.model;

import de.muenchen.isi.domain.model.common.AdresseModel;
import de.muenchen.isi.domain.model.common.VerortungMultiPolygonModel;
import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Bauratenmethodik;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Verfahrensstand;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BauleitplanverfahrenModel extends AbfrageModel {

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

    private LocalDate start42Verfahren;

    private Boolean start42VerfahrenDatumUnbekannt;

    private Bauratenmethodik bauratenmethodikVorbelegung;

    private List<AbfragevarianteBauleitplanverfahrenModel> abfragevariantenBauleitplanverfahren;

    private List<AbfragevarianteBauleitplanverfahrenModel> abfragevariantenSachbearbeitungBauleitplanverfahren;

    /**
     * @return der zusammengesetzte Name der Abfrage
     */
    public String getDisplayName() {
        final var displayName = new StringBuilder(this.getName());
        if (!StringUtils.isEmpty(this.getBebauungsplannummer())) {
            displayName.append(String.format(" - BPlan.: %s", this.getBebauungsplannummer()));
        }
        return displayName.toString();
    }
}
