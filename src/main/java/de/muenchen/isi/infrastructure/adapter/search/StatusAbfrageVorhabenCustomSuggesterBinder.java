package de.muenchen.isi.infrastructure.adapter.search;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import java.util.Arrays;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.ValueBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.ValueBinder;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class StatusAbfrageVorhabenCustomSuggesterBinder implements ValueBinder {

    @Override
    public void bind(final ValueBindingContext<?> context) {
        context.bridge(
            StatusAbfrage.class,
            new StatusAbfrageValueCompletionBridge(),
            context
                .typeFactory()
                .extension(ElasticsearchExtension.get())
                .asNative()
                .mapping("{\"type\": \"completion\"}")
        );
    }

    private static class StatusAbfrageValueCompletionBridge implements ValueBridge<StatusAbfrage, JsonElement> {

        @Override
        public JsonElement toIndexedValue(final StatusAbfrage value, final ValueBridgeToIndexedValueContext context) {
            final var jsonObject = new JsonObject();
            final var jsonArray = new JsonArray();
            if (ObjectUtils.isNotEmpty(value)) {
                Arrays
                    .stream(StringUtils.split(value.getBezeichnung()))
                    .map(JsonPrimitive::new)
                    .forEach(jsonArray::add);
            }
            jsonObject.add("input", jsonArray);
            return jsonObject;
        }
    }
}
