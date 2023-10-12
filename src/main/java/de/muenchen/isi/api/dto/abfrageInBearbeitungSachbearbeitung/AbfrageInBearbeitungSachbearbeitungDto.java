package de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import lombok.Data;

@Data
public class AbfrageInBearbeitungSachbearbeitungDto {

    private Long version;

    private ArtAbfrage artAbfrage;
}
