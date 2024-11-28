package de.muenchen.isi.domain.model.abfrageStartBearbeitung;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import java.util.UUID;
import lombok.Data;

@Data
public abstract class AbfrageStartBearbeitungModel {

    private Long version;

    private ArtAbfrage artAbfrage;

    private UUID bauvorhaben;

    private String linkEakte;
}
