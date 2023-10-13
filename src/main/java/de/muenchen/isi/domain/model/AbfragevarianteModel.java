package de.muenchen.isi.domain.model;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class AbfragevarianteModel extends BaseEntityModel {

    private ArtAbfrage artAbfragevariante;

    private Integer abfragevariantenNr;

    private String name;
}
