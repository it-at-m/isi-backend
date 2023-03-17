/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum StandVorhaben implements ILookup {
    UNSPECIFIED(ILookup.UNSPECIFIED),
    GRUNDSATZ_ECKDATENBESCHLUSS("Grundsatz- und Eckdatenbeschluss"),
    AUFSTELLUNGSBESCHLUSS("Aufstellungsbeschluss"),
    PARAG_3_1_BAUGB("§ 3.1 BauGB – Unterrichtung Öff."),
    PARAG_3_2_BAUGB("§ 3.2 BauGB – öff. Auslegung"),
    PARAG_4_1_2_BAUGB("§ 4.1/2 BauGB – TÖB, Behördenbet."),
    BILLIGUNGSBESCHLUSS("Billigungsbeschluss"),
    SATZUNGSBESCHLUSS("Satzungsbeschluss"),
    BPLAN_IN_KRAFT("Bplan in Kraft getreten – Baurecht vorhanden"),
    VORBESCHEID_EINGEREICHT("Vorbescheid eingereicht"),
    BAUANTRAG_EINGEREICHT("Bauantrag eingereicht"),
    BAUGENEHMIGUNG_ERTEILT("Baugenehmigung erteilt"),
    BAUBEGINN_ANGEZEIGT("Baubeginn angezeigt"),
    BAUFERTIGSTELLUNG_GEPLANT("Baufertigstellung geplant"),
    BAUFERTIGSTELLUNG_ANGEZEIGT("Baufertigstellung angezeigt");

    @Getter
    private final String bezeichnung;
}
