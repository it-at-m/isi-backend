package de.muenchen.isi.api.dto.abfrageInBearbeitungFachreferat;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import lombok.Data;

@Data
public class AbfrageInBearbeitungFachreferatDto {

    private Long version;

    private ArtAbfrage artAbfrage;
}
