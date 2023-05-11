package de.muenchen.isi.domain.model.abfrageAngelegt;

import de.muenchen.isi.domain.model.BaseEntityModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class InfrastrukturabfrageRequestModel extends BaseEntityModel {

    private AbfrageRequestModel abfrage;

    private UncertainBoolean sobonRelevant;

    private SobonVerfahrensgrundsaetzeJahr sobonJahr;

    private List<AbfragevarianteRequestModel> abfragevarianten;

    private String aktenzeichenProLbk;

    private UncertainBoolean offiziellerVerfahrensschritt;

    private String displayName;
}
