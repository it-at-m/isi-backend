package de.muenchen.isi.domain.model.abfrageSachbearbeitungInBearbeitungSachbearbeitung;

import de.muenchen.isi.domain.model.AbfragevarianteSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfrageerstellungAbfragevarianteAngelegtModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SachbearbeitungAbfragevarianteInBearbeitungSachbearbeitungModel
    extends AbfrageerstellungAbfragevarianteAngelegtModel {

    private AbfragevarianteSachbearbeitungModel abfragevarianteSachbearbeitung;
}
