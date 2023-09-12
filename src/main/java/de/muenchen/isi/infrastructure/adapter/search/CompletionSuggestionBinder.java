package de.muenchen.isi.infrastructure.adapter.search;

import com.google.gson.JsonElement;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.ValueBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.ValueBinder;

/**
 * Der Hibernate-Search-Binder, um den Datentyp <T> einem Elasticsearch-Index-Attribut zuordnen zu können.
 * Das Indexattribut dient dann zur Durchführung einer Elasticsearch-Completion-Suggestion.
 */
public interface CompletionSuggestionBinder extends ValueBinder {
    default <T> void bind(
        final Class<T> clazz,
        final ValueBridge<T, JsonElement> valueBridge,
        final ValueBindingContext<?> context
    ) {
        context.bridge(
            clazz,
            valueBridge,
            context
                .typeFactory()
                .extension(ElasticsearchExtension.get())
                .asNative()
                .mapping("{\"type\": \"completion\"}")
        );
    }
}
