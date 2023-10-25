package de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import lombok.Data;

@Data
public class AbfrageInBearbeitungFachreferatModel {

    private Long version;

    private ArtAbfrage artAbfrage;
}
