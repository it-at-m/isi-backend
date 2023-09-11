package de.muenchen.isi.infrastructure.adapter.search;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.ValueBindingContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

/**
 * Binder um Entitätsattribute vom Typ {@link Integer} für eine Completion-Suggestion indizieren zu können.
 */
public class IntegerSuggestionBinder implements CompletionSuggestionBinder {

    @Override
    public void bind(final ValueBindingContext<?> context) {
        this.bind(Integer.class, new IntegerValueCompletionBridge(), context);
    }

    private static class IntegerValueCompletionBridge implements ValueBridge<Integer, JsonElement> {

        @Override
        public JsonElement toIndexedValue(final Integer value, final ValueBridgeToIndexedValueContext context) {
            final var jsonObject = new JsonObject();
            final var jsonArray = new JsonArray();
            if (ObjectUtils.isNotEmpty(value)) {
                jsonArray.add(value.toString());
            }
            jsonObject.add("input", jsonArray);
            return jsonObject;
        }
    }
}
