package de.muenchen.isi.infrastructure.adapter.search;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.ValueBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.ValueBinder;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class IntegerCustomSuggesterBinder implements ValueBinder {

    @Override
    public void bind(final ValueBindingContext<?> context) {
        context.bridge(
            Integer.class,
            new IntegerCustomSuggesterBridge(),
            context
                .typeFactory()
                .extension(ElasticsearchExtension.get())
                .asNative()
                .mapping("{\"type\": \"completion\"}")
        );
    }

    private static class IntegerCustomSuggesterBridge implements ValueBridge<Integer, JsonElement> {

        @Override
        public JsonElement toIndexedValue(final Integer value, final ValueBridgeToIndexedValueContext context) {
            final var jsonArray = new JsonArray();
            CollectionUtils
                .emptyIfNull(value == null ? null : List.of(value.toString()))
                .stream()
                .map(JsonPrimitive::new)
                .forEach(jsonArray::add);
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("input", jsonArray);
            return value == null ? null : jsonObject;
        }
    }
}
