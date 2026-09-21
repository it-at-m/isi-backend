package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum AnlassPlanung implements ILookup {
    UNSPECIFIED(ILookup.UNSPECIFIED, new String[] {}),
    NEUBAU_BEBAUUNGSPLAN("Neubau (Bebauungsplan)", new String[] { "Neubau", "Bebauungsplan" }),
    NEUBAU_BAU_GB("Neubau (§34 BauGB u.a.)", new String[] { "Neubau", "§34", "BauGB", "u.a." }),
    BAUPROGRAMM("Bauprogramm", new String[] { "Bauprogramm" }),
    NEUE_EINRICHTUNG_PRIVATER_TRAEGER(
        "Neue Einrichtung privater Träger",
        new String[] { "Neue", "Einrichtung", "privater", "Träger" }
    ),
    REDUZIERUNG_PLAETZE_BEI_BESTANDSEINRICHTUNG(
        "Reduzierung Plätze bei Bestandseinrichtung",
        new String[] { "Reduzierung", "Plätze", "bei", "Bestandseinrichtung" }
    ),
    ERWEITERUNG_BESTEHENDER_EINRICHTUNG(
        "Erweiterung bestehender Einrichtung",
        new String[] { "Erweiterung", "bestehender", "Einrichtung" }
    ),
    SCHLIESSUNG("Schließung", new String[] { "Schließung" });

    @Getter
    private final String bezeichnung;

    @Getter
    private final String[] suggestions;
}
