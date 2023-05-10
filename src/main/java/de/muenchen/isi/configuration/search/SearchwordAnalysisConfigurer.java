package de.muenchen.isi.configuration.search;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurationContext;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;

/**
 * https://docs.jboss.org/hibernate/search/6.1/reference/en-US/html_single/#backend-elasticsearch-analysis-analyzers
 */
@NoArgsConstructor
@Data
public class SearchwordAnalysisConfigurer implements ElasticsearchAnalysisConfigurer {

    @Override
    public void configure(final ElasticsearchAnalysisConfigurationContext context) {
        context
            .analyzer("searchwordSuggestionAnalyzer")
            .custom()
            .tokenizer("whitespace")
            .tokenFilters("lowercase_searchword_suggestion", "ngram_searchword_suggestion");

        // https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lowercase-tokenizer.html
        context.tokenFilter("lowercase_searchword_suggestion").type("lowercase");

        // https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-edgengram-tokenizer.html
        context
            .tokenFilter("edge_ngram_searchword_suggestion")
            .type("edge_ngram")
            .param("min_gram", "1")
            .param("max_gram", "512");
    }
}
