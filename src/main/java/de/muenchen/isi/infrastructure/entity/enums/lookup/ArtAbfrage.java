/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.enums.lookup;

import de.muenchen.isi.infrastructure.entity.Abfrage;
import jakarta.persistence.DiscriminatorValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ArtAbfrage implements ILookup {
    UNSPECIFIED(ILookup.UNSPECIFIED),

    BAULEITPLANVERFAHREN("Bauleitplanverfahren"),

    BAUGENEHMIGUNGSVERFAHREN("Baugenehmigungsverfahren"),

    WEITERES_VERFAHREN("Weiteres Verfahren");

    @Getter
    private final String bezeichnung;

    /**
     * Werden in den Entitäten der {@link Abfrage} als {@link DiscriminatorValue} verwendet.
     */
    public static class Values {

        public static final String BAULEITPLANVERFAHREN = "BAULEITPLANVERFAHREN";

        public static final String BAUGENEHMIGUNGSVERFAHREN = "BAUGENEHMIGUNGSVERFAHREN";

        public static final String WEITERES_VERFAHREN = "WEITERES_VERFAHREN";
    }
}
