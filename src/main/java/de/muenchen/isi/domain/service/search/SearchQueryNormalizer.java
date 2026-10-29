package de.muenchen.isi.domain.service.search;

/**
 * Dient zur Normalisierung von benutzerseitigen Suchanfragen vor der Tokenisierung
 * und Anpassung an das Simple Query String-Format.
 *
 * HINWEIS: Die hier durchgeführte Normalisierung muss mit der Konfiguration des Analyzers
 * auf in de.muenchen.isi.configuration.search.ElasticsearchAnalysisConfig übereinstimmen.
 * Insbesondere gilt: Wenn Unterstriche im Analyzer als Token-Trenner behandelt werden,
 * muss der Normalizer Unterstriche auch auf der Query-Seite auftrennen (oder umgekehrt).
 * Beide Konfigurationen müssen synchron gehalten werden.
 */

public final class SearchQueryNormalizer {

    private SearchQueryNormalizer() {}

    /**
     * Normalisiert die Eingabezeichenkette für die Tokenisierung:
     * - wandelt null in einen leeren String um
     * - ersetzt Unterstriche durch Leerzeichen, damit sie als Trenner behandelt werden
     * - entfernt führende und nachfolgende Leerzeichen
     *
     * @param input rohe Benutzereingabe
     * @return normalisierter String, bereit zur Tokenisierung
     */
    public static String normalizeForTokenization(final String input) {
        if (input == null) {
            return "";
        }
        return input.replace('_', ' ').trim();
    }
}
