package de.muenchen.isi.domain.model.abfrageBedarfsmeldungErfolgt;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class WeiteresVerfahrenBedarfsmeldungErfolgtModel extends AbfrageBedarfsmeldungErfolgtModel {

    private List<AbfragevarianteWeiteresVerfahrenBedarfsmeldungErfolgtModel> abfragevariantenWeiteresVerfahren;

    private List<
        AbfragevarianteWeiteresVerfahrenBedarfsmeldungErfolgtModel
    > abfragevariantenSachbearbeitungWeiteresVerfahren;
}
