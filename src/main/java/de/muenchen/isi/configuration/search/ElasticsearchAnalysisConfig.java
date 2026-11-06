package de.muenchen.isi.configuration.search;

import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurationContext;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;

public class ElasticsearchAnalysisConfig implements ElasticsearchAnalysisConfigurer {

    /**
     * Grundsätzlich trennt der Standardtokenizer:
     *
     * Nach allen Whitespace-Zeichen (z. B. Leerzeichen, Tab, Zeilenumbruch)
     * Nach den meisten Satzzeichen (z. B. Punkt, Komma, Semikolon, Doppelpunkt, Ausrufezeichen, Fragezeichen, Klammern, Anführungszeichen)
     * Nach Sonderzeichen (z. B. @, #, $, %, &, *, +, =, /, , |, <, >, [, ], {, }, ^, ~, `, usw.)
     * Nicht getrennt wird:
     *
     * Bei Buchstaben- und Zahlenfolgen (z.B. „München2026“ bleibt ein Token)
     * Bei einigen Zeichen wie dem Unterstrich _ (dieser bleibt Teil des Tokens)
     *
     * Hier wird nun der Tokenizer um den Unterstrich (_) als Trennzeichen erweitert und in Kleinschrift gefiltert
     **/
    @Override
    public void configure(ElasticsearchAnalysisConfigurationContext context) {
        context.charFilter("underscore_to_space").type("mapping").param("mappings", "_=>\\u0020");

        context
            .analyzer("isi_text_analyzer")
            .custom()
            .tokenizer("standard")
            .charFilters("underscore_to_space")
            .tokenFilters("lowercase");
    }
}
