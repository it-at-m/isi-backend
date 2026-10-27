package de.muenchen.isi.configuration.search;

import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurationContext;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;

public class ElasticsearchAnalysisConfig implements ElasticsearchAnalysisConfigurer {

    /**
     * Analyzer, der nach Unterstrich (_) splittet
     */
    @Override
    public void configure(ElasticsearchAnalysisConfigurationContext context) {
        // Tokenizer der nach Underscore (_) splittet
        context.tokenizer("underscore_tokenizer").type("pattern").param("pattern", "_");

        // Analyzer, der den underscore_tokenizer verwendet.
        context.analyzer("isi_text_analyzer").custom().tokenizer("underscore_tokenizer");
    }
}
