package de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt;

import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import java.util.List;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class AbfrageerstellungInfrastrukturabfrageAngelegtModel {

    private Long version;

    private AbfrageerstellungAbfrageAngelegtModel abfrage;

    private UncertainBoolean sobonRelevant;

    private SobonVerfahrensgrundsaetzeJahr sobonJahr;

    private List<AbfrageerstellungAbfragevarianteAngelegtModel> abfragevarianten;

    private String aktenzeichenProLbk;

    private UncertainBoolean offiziellerVerfahrensschritt;

    private String displayName;
}
