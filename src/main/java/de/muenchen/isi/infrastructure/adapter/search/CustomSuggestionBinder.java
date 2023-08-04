package de.muenchen.isi.infrastructure.adapter.search;

import com.google.gson.JsonElement;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.ValueBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.ValueBinder;

public interface CustomSuggestionBinder extends ValueBinder {
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
