package de.muenchen.isi.infrastructure.adapter.search;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import java.util.Arrays;
import java.util.Objects;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.ValueBindingContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

/**
 * Binder um Entitätsattribute vom Typ {@link StandVerfahren} für eine Completion-Suggestion indizieren zu können.
 * Die Suchwortvorschläge werden aus {@link StandVerfahren#getSuggestions} extrahiert.
 */
public class StandVerfahrenSuggestionBinder implements CompletionSuggestionBinder {

    @Override
    public void bind(final ValueBindingContext<?> context) {
        this.bind(StandVerfahren.class, new StandVerfahrenValueCompletionBridge(), context);
    }

    private static class StandVerfahrenValueCompletionBridge implements ValueBridge<StandVerfahren, JsonElement> {

        @Override
        public JsonElement toIndexedValue(final StandVerfahren value, final ValueBridgeToIndexedValueContext context) {
            final var jsonObject = new JsonObject();
            final var jsonArray = new JsonArray();
            if (!Objects.equals(StandVerfahren.UNSPECIFIED, value)) {
                Arrays.stream(value.getSuggestions()).map(JsonPrimitive::new).forEach(jsonArray::add);
            }
            jsonObject.add("input", jsonArray);
            return jsonObject;
        }
    }
}
