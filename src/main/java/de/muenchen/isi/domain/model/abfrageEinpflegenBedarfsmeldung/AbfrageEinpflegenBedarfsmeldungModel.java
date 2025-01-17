package de.muenchen.isi.domain.model.abfrageEinpflegenBedarfsmeldung;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import lombok.Data;

@Data
public abstract class AbfrageEinpflegenBedarfsmeldungModel {

    private Long version;

    private ArtAbfrage artAbfrage;
}
