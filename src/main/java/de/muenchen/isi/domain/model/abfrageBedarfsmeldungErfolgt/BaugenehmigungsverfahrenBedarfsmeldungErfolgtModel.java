package de.muenchen.isi.domain.model.abfrageBedarfsmeldungErfolgt;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaugenehmigungsverfahrenBedarfsmeldungErfolgtModel extends AbfrageBedarfsmeldungErfolgtModel {

    private List<
        AbfragevarianteBaugenehmigungsverfahrenBedarfsmeldungErfolgtModel
    > abfragevariantenBaugenehmigungsverfahren;

    private List<
        AbfragevarianteBaugenehmigungsverfahrenBedarfsmeldungErfolgtModel
    > abfragevariantenSachbearbeitungBaugenehmigungsverfahren;
}
