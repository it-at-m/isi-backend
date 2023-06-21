package de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt;

import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import java.util.List;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class InfrastrukturabfrageAngelegtModel {

    private Long version;

    private AbfrageAngelegtModel abfrage;

    private UncertainBoolean sobonRelevant;

    private SobonVerfahrensgrundsaetzeJahr sobonJahr;

    private List<AbfragevarianteAngelegtModel> abfragevarianten;

    private String aktenzeichenProLbk;

    private UncertainBoolean offiziellerVerfahrensschritt;

    private String displayName;
}
