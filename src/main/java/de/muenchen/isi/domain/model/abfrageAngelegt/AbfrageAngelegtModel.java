package de.muenchen.isi.domain.model.abfrageAngelegt;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import java.util.UUID;
import lombok.Data;

@Data
public class AbfrageAngelegtModel {

    private Long version;

    private ArtAbfrage artAbfrage;

    private String name;

    private String anmerkung;

    private UUID bauvorhaben;
}
