package de.muenchen.isi.domain.model.abfrageEinplanungBedarfe;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import lombok.Data;

@Data
public abstract class AbfrageEinplanungBedarfeModel {

    private Long version;

    private ArtAbfrage artAbfrage;
}
