package de.muenchen.isi.domain.model.abfrageEinplanungBedarfe;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaugenehmigungsverfahrenEinplanungBedarfeModel extends AbfrageEinplanungBedarfeModel {

    private List<
            AbfragevarianteBaugenehmigungsverfahrenEinplanungBedarfeModel
    > abfragevariantenBaugenehmigungsverfahren;

    private List<
            AbfragevarianteBaugenehmigungsverfahrenEinplanungBedarfeModel
    > abfragevariantenSachbearbeitungBaugenehmigungsverfahren;
}
