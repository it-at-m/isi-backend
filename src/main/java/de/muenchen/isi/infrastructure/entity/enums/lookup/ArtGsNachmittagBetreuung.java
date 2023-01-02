package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ArtGsNachmittagBetreuung implements ILookup {

    HORT("Hort"),
    KOOPERATIVER_GANZTAG_FLEXIBLE_VARIANTE("Kooperativer Ganztag – flexibel"),
    KOOPERATIVER_GANZTAG_RHYTHMISIERTE_VARIANTE("Kooperativer Ganztag – rhythmisiert"),
    TAGESHEIM("Tagesheim"),
    MITTAGSBETREUUNG("Mittagsbetreuung");

    @Getter
    private final String bezeichnung;

}
