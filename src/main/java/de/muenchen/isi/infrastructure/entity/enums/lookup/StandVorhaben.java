/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum StandVorhaben implements ILookup {
    UNSPECIFIED(ILookup.UNSPECIFIED, new String[] {}),
    GRUNDSATZ_ECKDATENBESCHLUSS("Grundsatz- und Eckdatenbeschluss", new String[] { "Grundsatz", "Eckdatenbeschluss" }),
    AUFSTELLUNGSBESCHLUSS("Aufstellungsbeschluss", new String[] { "Aufstellungsbeschluss" }),
    PARAG_3_1_BAUGB(
        "§ 3.1 BauGB – Unterrichtung Öff.",
        new String[] { "§ 3.1 BauGB", "Unterrichtung Öff.", "Unterrichtung" }
    ),
    PARAG_3_2_BAUGB("§ 3.2 BauGB – öff. Auslegung", new String[] { "§ 3.2 BauGB", "öff. Auslegung", "Auslegung" }),
    PARAG_4_1_2_BAUGB("§ 4.1/2 BauGB – TÖB, Behördenbet.", new String[] { "§ 4.1/2 BauGB", "TÖB" }),
    BILLIGUNGSBESCHLUSS("Billigungsbeschluss", new String[] { "Billigungsbeschluss" }),
    SATZUNGSBESCHLUSS("Satzungsbeschluss", new String[] { "Satzungsbeschluss" }),
    BPLAN_IN_KRAFT(
        "Bplan in Kraft getreten – Baurecht vorhanden",
        new String[] { "Bplan", "in Kraft getreten", "Baurecht", "vorhanden" }
    ),
    VORBESCHEID_EINGEREICHT("Vorbescheid eingereicht", new String[] { "Vorbescheid", "eingereicht" }),
    BAUANTRAG_EINGEREICHT("Bauantrag eingereicht", new String[] { "Bauantrag", "eingereicht" }),
    BAUGENEHMIGUNG_ERTEILT("Baugenehmigung erteilt", new String[] { "Baugenehmigung", "erteilt" }),
    BAUBEGINN_ANGEZEIGT("Baubeginn angezeigt", new String[] { "Baubeginn", "angezeigt" }),
    BAUFERTIGSTELLUNG_GEPLANT("Baufertigstellung geplant", new String[] { "Baufertigstellung", "geplant" }),
    BAUFERTIGSTELLUNG_ANGEZEIGT("Baufertigstellung angezeigt", new String[] { "Baufertigstellung", "angezeigt" });

    @Getter
    private final String bezeichnung;

    @Getter
    private final String[] suggestions;
}
