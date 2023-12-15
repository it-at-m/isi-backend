package de.muenchen.isi.domain.model.abfrageErledigtMitFachreferat;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class WeiteresVerfahrenErledigtMitFachreferatModel extends AbfrageErledigtMitFachreferatModel {

    private List<AbfragevarianteWeiteresVerfahrenErledigtMitFachreferatModel> abfragevariantenWeiteresVerfahren;

    private List<
        AbfragevarianteWeiteresVerfahrenErledigtMitFachreferatModel
    > abfragevariantenSachbearbeitungWeiteresVerfahren;
}
