package de.muenchen.isi.infrastructure.adapter.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeFromIndexedValueContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

/**
 * ValueBridge um Entitätsattribute vom Typ {@link StatusAbfrage} für eine Volltextsuche indizieren zu können.
 * Die zu indizierenden Werte werden aus dem Typattribut {@link StatusAbfrage#getBezeichnung()} entnommen.
 */
@Slf4j
public class StatusAbfrageValueBridge implements ValueBridge<StatusAbfrage, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String toIndexedValue(final StatusAbfrage value, final ValueBridgeToIndexedValueContext context) {
        return toJson(value);
    }

    @Override
    public StatusAbfrage fromIndexedValue(final String indexedValue, final ValueBridgeFromIndexedValueContext context) {
        return fromJson(indexedValue);
    }

    public static String toJson(final StatusAbfrage value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("Fehler beim Serialisieren von StatusAbfrage: {}", e.getMessage());
            return null;
        }
    }

    public static StatusAbfrage fromJson(final String json) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, StatusAbfrage.class);
        } catch (JsonProcessingException e) {
            log.error("Fehler beim Deserialisieren von StatusAbfrage: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean isCompatibleWith(final ValueBridge<?, ?> other) {
        return other != null && getClass().equals(other.getClass());
    }
}
