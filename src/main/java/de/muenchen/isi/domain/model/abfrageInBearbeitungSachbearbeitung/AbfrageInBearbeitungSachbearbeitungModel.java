package de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import java.util.UUID;
import lombok.Data;

@Data
public abstract class AbfrageInBearbeitungSachbearbeitungModel {

    private Long version;

    private ArtAbfrage artAbfrage;

    private UUID bauvorhaben;
}
