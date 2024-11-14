package de.muenchen.isi.domain.model.abfrageEinplanungBedarfe;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BauleitplanverfahrenEinplanungBedarfeModel extends AbfrageEinplanungBedarfeModel {

    private List<AbfragevarianteBauleitplanverfahrenEinplanungBedarfeModel> abfragevariantenBauleitplanverfahren;

    private List<
            AbfragevarianteBauleitplanverfahrenEinplanungBedarfeModel
    > abfragevariantenSachbearbeitungBauleitplanverfahren;
}
