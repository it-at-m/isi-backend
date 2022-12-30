package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ArtDokument implements ILookup {

    UNSPECIFIED(ILookup.UNSPECIFIED),
    EMAIL("E-Mail"),
    BESCHLUSS("Beschluss(entwurf)"),
    ANLAGE("Anlage zu Beschluss(entwurf)"),
    ANTRAG("Antrag"),
    KARTE("Kartenausschnitt / Lageplan"),
    STELLUNGNAHME("Stellungnahme"),
    DATEN_BAUVORHABEN("Planungsdaten Bauvorhaben"),
    GEBAEUDEPLAN("Gebäudeplan und Ansicht"),
    BERECHNUNG("Berechnung"),
    INFOS_BAUGENEHMIGUNG("Infos zur Baugenehmigung"),
    PRESSEARTIKEL("Presseartikel"),
    SONSTIGES("Sonstiges");

    @Getter
    private final String bezeichnung;

}
