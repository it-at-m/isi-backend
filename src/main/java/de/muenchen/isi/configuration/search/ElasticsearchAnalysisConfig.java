package de.muenchen.isi.configuration.search;

import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurationContext;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;

public class ElasticsearchAnalysisConfig implements ElasticsearchAnalysisConfigurer {

    @Override
    public void configure(ElasticsearchAnalysisConfigurationContext context) {
        // Tokenizer, der sowohl auf Unterstrichen als auch auf Whitespace splittet
        context.tokenizer("underscore_tokenizer").type("pattern").param("pattern", "[_\\s]+");

        // Analyzer, der den underscore_tokenizer verwendet und anschließend in Kleinbuchstaben umwandelt
        // (so werden unterschiedliche Groß-/Kleinschreibungen matchbar).
        context.analyzer("isi_text_analyzer").custom().tokenizer("underscore_tokenizer").tokenFilters("lowercase");
    }
}
