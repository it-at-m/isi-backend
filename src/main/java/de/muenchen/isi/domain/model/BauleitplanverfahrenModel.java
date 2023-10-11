package de.muenchen.isi.domain.model;

import de.muenchen.isi.domain.model.common.AdresseModel;
import de.muenchen.isi.domain.model.common.VerortungModel;
import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
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
