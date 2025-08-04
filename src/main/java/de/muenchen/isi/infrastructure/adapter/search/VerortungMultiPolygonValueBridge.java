package de.muenchen.isi.infrastructure.adapter.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.isi.infrastructure.entity.common.VerortungMultiPolygon;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeFromIndexedValueContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

@Slf4j
public class VerortungMultiPolygonValueBridge implements ValueBridge<VerortungMultiPolygon, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String toIndexedValue(final VerortungMultiPolygon value, final ValueBridgeToIndexedValueContext context) {
        return toJson(value);
    }

    @Override
    public VerortungMultiPolygon fromIndexedValue(
        final String indexedValue,
        final ValueBridgeFromIndexedValueContext context
    ) {
        return fromJson(indexedValue);
    }

    public static String toJson(final VerortungMultiPolygon value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("Fehler beim Serialisieren von VerortungMultiPolygon: {}", e.getMessage());
            return null;
        }
    }

    public static VerortungMultiPolygon fromJson(final String json) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, VerortungMultiPolygon.class);
        } catch (JsonProcessingException e) {
            log.error("Fehler beim Deserialisieren von VerortungMultiPolygon: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean isCompatibleWith(final ValueBridge<?, ?> other) {
        return other != null && getClass().equals(other.getClass());
    }
}
