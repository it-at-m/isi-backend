package de.muenchen.isi.infrastructure.adapter.search;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahrenBauleitplanverfahren;
import java.util.Arrays;
import java.util.Objects;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.ValueBindingContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

/**
 * Binder um Entitätsattribute vom Typ {@link StandVerfahrenBauleitplanverfahren} für eine Completion-Suggestion indizieren zu können.
 * Die Suchwortvorschläge werden aus {@link StandVerfahrenBauleitplanverfahren#getSuggestions} extrahiert.
 */
public class StandVerfahrenBauleitplanverfahrenSuggestionBinder implements CompletionSuggestionBinder {

    @Override
    public void bind(final ValueBindingContext<?> context) {
        this.bind(
                StandVerfahrenBauleitplanverfahren.class,
                new StandVerfahrenBauleitplanverfahrenValueCompletionBridge(),
                context
            );
    }

    private static class StandVerfahrenBauleitplanverfahrenValueCompletionBridge
        implements ValueBridge<StandVerfahrenBauleitplanverfahren, JsonElement> {

        @Override
        public JsonElement toIndexedValue(
            final StandVerfahrenBauleitplanverfahren value,
            final ValueBridgeToIndexedValueContext context
        ) {
            final var jsonObject = new JsonObject();
            final var jsonArray = new JsonArray();
            if (!Objects.equals(StandVerfahrenBauleitplanverfahren.UNSPECIFIED, value)) {
                Arrays.stream(value.getSuggestions()).map(JsonPrimitive::new).forEach(jsonArray::add);
            }
            jsonObject.add("input", jsonArray);
            return jsonObject;
        }
    }
}
