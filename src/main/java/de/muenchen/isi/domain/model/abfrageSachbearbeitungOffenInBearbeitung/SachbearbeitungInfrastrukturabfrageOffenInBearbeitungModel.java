package de.muenchen.isi.domain.model.abfrageSachbearbeitungOffenInBearbeitung;

import de.muenchen.isi.domain.model.BaseEntityModel;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SachbearbeitungInfrastrukturabfrageOffenInBearbeitungModel extends BaseEntityModel {

    private SachbearbeitungAbfrageOffenInBearbeitungModel abfrage;

    private List<SachbearbeitungAbfragevarianteOffenInBearbeitungModel> abfragevarianten;
}
