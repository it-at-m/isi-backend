package de.muenchen.isi.domain.model.abfrageSachbearbeitungInBearbeitungSachbearbeitung;

import de.muenchen.isi.domain.model.AbfragevarianteSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfragevarianteAngelegtModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AbfragevarianteInBearbeitungSachbearbeitungModel extends AbfragevarianteAngelegtModel {

    private AbfragevarianteSachbearbeitungModel abfragevarianteSachbearbeitung;
}
