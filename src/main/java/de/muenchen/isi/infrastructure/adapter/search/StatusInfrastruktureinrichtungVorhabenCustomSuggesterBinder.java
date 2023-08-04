package de.muenchen.isi.infrastructure.adapter.search;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.ValueBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.ValueBinder;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class StatusInfrastruktureinrichtungVorhabenCustomSuggesterBinder implements ValueBinder {

    @Override
    public void bind(final ValueBindingContext<?> context) {
        context.bridge(
            StatusInfrastruktureinrichtung.class,
            new StandVorhabenValueCompletionBridge(),
            context
                .typeFactory()
                .extension(ElasticsearchExtension.get())
                .asNative()
                .mapping("{\"type\": \"completion\"}")
        );
    }

    private static class StandVorhabenValueCompletionBridge
        implements ValueBridge<StatusInfrastruktureinrichtung, JsonElement> {

        @Override
        public JsonElement toIndexedValue(
            final StatusInfrastruktureinrichtung value,
            final ValueBridgeToIndexedValueContext context
        ) {
            final var jsonObject = new JsonObject();
            final var jsonArray = new JsonArray();
            if (!Objects.equals(StatusInfrastruktureinrichtung.UNSPECIFIED, value)) {
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
