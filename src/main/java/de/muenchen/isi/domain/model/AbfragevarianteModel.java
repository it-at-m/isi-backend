package de.muenchen.isi.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class AbfragevarianteModel extends BaseEntityModel {

    private Integer abfragevariantenNr;

    private String name;
}
