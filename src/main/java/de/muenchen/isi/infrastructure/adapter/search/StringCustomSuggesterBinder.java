package de.muenchen.isi.infrastructure.adapter.search;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.ValueBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.ValueBinder;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class StringCustomSuggesterBinder implements ValueBinder {

    @Override
    public void bind(ValueBindingContext<?> context) {
        context.bridge(
            String.class,
            new StringValueCompletionBridge(),
            context
                .typeFactory()
                .extension(ElasticsearchExtension.get())
                .asNative()
                .mapping("{\"type\": \"completion\"}")
        );
    }

    private static class StringValueCompletionBridge implements ValueBridge<String, JsonElement> {

        @Override
        public JsonElement toIndexedValue(String value, ValueBridgeToIndexedValueContext context) {
            final var jsonArray = new JsonArray();
            Arrays.stream(StringUtils.split(value)).map(JsonPrimitive::new).forEach(jsonArray::add);
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("input", jsonArray);
            return value == null ? null : jsonObject;
        }
    }
}
