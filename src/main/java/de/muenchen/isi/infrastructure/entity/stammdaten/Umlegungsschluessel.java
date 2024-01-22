package de.muenchen.isi.infrastructure.entity.stammdaten;

import de.muenchen.isi.infrastructure.entity.Foerderart;
import java.util.Set;
import lombok.Data;

@Data
public class Umlegungsschluessel {

    private Set<Foerderart> foerderarten;
}
