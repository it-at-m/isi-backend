package de.muenchen.isi.domain.model.abfrageBedarfsmeldungErfolgt;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BauleitplanverfahrenBedarfsmeldungErfolgtModel extends AbfrageBedarfsmeldungErfolgtModel {

    private List<AbfragevarianteBauleitplanverfahrenBedarfsmeldungErfolgtModel> abfragevariantenBauleitplanverfahren;

    private List<
        AbfragevarianteBauleitplanverfahrenBedarfsmeldungErfolgtModel
    > abfragevariantenSachbearbeitungBauleitplanverfahren;
}
