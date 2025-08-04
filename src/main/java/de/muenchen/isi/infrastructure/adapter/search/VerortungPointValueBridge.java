package de.muenchen.isi.infrastructure.adapter.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.isi.infrastructure.entity.common.VerortungPoint;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeFromIndexedValueContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

@Slf4j
public class VerortungPointValueBridge implements ValueBridge<VerortungPoint, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String toIndexedValue(
        VerortungPoint verortungPoint,
        ValueBridgeToIndexedValueContext valueBridgeToIndexedValueContext
    ) {
        return toJson(verortungPoint);
    }

    @Override
    public VerortungPoint fromIndexedValue(
        final String indexedValue,
        final ValueBridgeFromIndexedValueContext context
    ) {
        return fromJson(indexedValue);
    }

    public static String toJson(final VerortungPoint value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("Fehler beim Serialisieren von VerortungPoint: {}", e.getMessage());
            return null;
        }
    }

    public static VerortungPoint fromJson(final String json) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, VerortungPoint.class);
        } catch (JsonProcessingException e) {
            log.error("Fehler beim Deserialisieren von VerortungPoint: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean isCompatibleWith(ValueBridge<?, ?> other) {
        return other != null && getClass().equals(other.getClass());
    }
}
