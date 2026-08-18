package de.muenchen.isi.domain.model.abfrageEinpflegenBedarfsmeldung;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BauleitplanverfahrenEinpflegenBedarfsmeldungModel extends AbfrageEinpflegenBedarfsmeldungModel {

    private List<AbfragevarianteBauleitplanverfahrenEinpflegenBedarfsmeldungModel> abfragevariantenBauleitplanverfahren;

    private List<AbfragevarianteBauleitplanverfahrenEinpflegenBedarfsmeldungModel> abfragevariantenSachbearbeitungBauleitplanverfahren;
}
