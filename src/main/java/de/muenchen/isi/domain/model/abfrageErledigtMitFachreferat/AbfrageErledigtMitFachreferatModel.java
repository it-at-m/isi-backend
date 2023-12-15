package de.muenchen.isi.domain.model.abfrageErledigtMitFachreferat;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import lombok.Data;

@Data
public abstract class AbfrageErledigtMitFachreferatModel {

    private Long version;

    private ArtAbfrage artAbfrage;
}
