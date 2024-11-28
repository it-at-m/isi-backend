package de.muenchen.isi.domain.model.abfrageEinpflegenBedarfsmeldung;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class WeiteresVerfahrenEinpflegenBedarfsmeldungModel extends AbfrageEinpflegenBedarfsmeldungModel {

    private List<AbfragevarianteWeiteresVerfahrenEinpflegenBedarfsmeldungModel> abfragevariantenWeiteresVerfahren;

    private List<
        AbfragevarianteWeiteresVerfahrenEinpflegenBedarfsmeldungModel
    > abfragevariantenSachbearbeitungWeiteresVerfahren;
}
