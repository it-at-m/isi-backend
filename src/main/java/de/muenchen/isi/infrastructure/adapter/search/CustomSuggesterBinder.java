package de.muenchen.isi.infrastructure.adapter.search;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.ValueBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.ValueBinder;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeFromIndexedValueContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class CustomSuggesterBinder implements ValueBinder {

    @Override
    public void bind(ValueBindingContext<?> context) {
        context.bridge(
            String.class,
            new CompletionBridge(),
            context
                .typeFactory()
                .extension(ElasticsearchExtension.get())
                .asNative()
                .mapping("{\"type\": \"completion\"}")
        );
    }

    private static class CompletionBridge implements ValueBridge<String, JsonElement> {

        @Override
        public JsonElement toIndexedValue(String value, ValueBridgeToIndexedValueContext context) {
            return value == null ? null : new JsonPrimitive(value);
        }

        @Override
        public String fromIndexedValue(JsonElement value, ValueBridgeFromIndexedValueContext context) {
            return value == null ? null : value.getAsString();
        }
    }
}
