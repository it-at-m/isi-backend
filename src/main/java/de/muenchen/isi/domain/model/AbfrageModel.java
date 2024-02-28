package de.muenchen.isi.domain.model;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class AbfrageModel extends BaseEntityModel {

    private ArtAbfrage artAbfrage;

    private String name;

    private StatusAbfrage statusAbfrage;

    private String anmerkung;

    private UUID bauvorhaben;

    private String sub;

    private String linkEakte;
}
