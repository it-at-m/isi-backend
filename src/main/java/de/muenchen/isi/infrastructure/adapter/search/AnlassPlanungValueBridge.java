package de.muenchen.isi.infrastructure.adapter.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.isi.infrastructure.entity.enums.lookup.AnlassPlanung;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeFromIndexedValueContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

@Slf4j
public class AnlassPlanungValueBridge implements ValueBridge<AnlassPlanung, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String toIndexedValue(final AnlassPlanung value, final ValueBridgeToIndexedValueContext context) {
        return toJson(value);
    }

    @Override
    public AnlassPlanung fromIndexedValue(final String indexedValue, final ValueBridgeFromIndexedValueContext context) {
        return fromJson(indexedValue);
    }

    public static String toJson(final AnlassPlanung value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("Fehler beim Serialisieren von AnlassPlanung: {}", e.getMessage());
            return null;
        }
    }

    public static AnlassPlanung fromJson(final String json) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, AnlassPlanung.class);
        } catch (JsonProcessingException e) {
            log.error("Fehler beim Deserialisieren von AnlassPlanung: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean isCompatibleWith(final ValueBridge<?, ?> other) {
        return other != null && getClass().equals(other.getClass());
    }

    @Override
    public boolean equals(Object o) {
        return o != null && this.getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
}
