package de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import lombok.Data;

@Data
public class AbfrageInBearbeitungSachbearbeitungModel {

    private Long version;

    private ArtAbfrage artAbfrage;
}
