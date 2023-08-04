package de.muenchen.isi.infrastructure.adapter.search;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.ValueBindingContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class StringCustomSuggesterBinder implements CustomSuggestionBinder {

    @Override
    public void bind(final ValueBindingContext<?> context) {
        this.bind(String.class, new StringValueCompletionBridge(), context);
    }

    private static class StringValueCompletionBridge implements ValueBridge<String, JsonElement> {

        @Override
        public JsonElement toIndexedValue(final String value, final ValueBridgeToIndexedValueContext context) {
            final var jsonObject = new JsonObject();
            final var jsonArray = new JsonArray();
            if (StringUtils.isNotEmpty(value)) {
                Arrays.stream(StringUtils.split(value)).map(JsonPrimitive::new).forEach(jsonArray::add);
            }
            jsonObject.add("input", jsonArray);
            return jsonObject;
        }
    }
}
