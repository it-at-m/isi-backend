package de.muenchen.isi.domain.model.abfrageBedarfsmeldungErfolgt;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import lombok.Data;

@Data
public abstract class AbfrageBedarfsmeldungErfolgtModel {

    private Long version;

    private ArtAbfrage artAbfrage;
}
