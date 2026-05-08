package de.muenchen.isi.infrastructure.adapter.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Verfahrensstand;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeFromIndexedValueContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

/**
 * ValueBridge um Entitätsattribute vom Typ {@link Verfahrensstand} für eine Volltextsuche indizieren zu können.
 * Die zu indizierenden Werte werden durch vollständige JSON-Serialisierung des {@link Verfahrensstand}-Enum-Objekts
 * mittels {@code objectMapper.writeValueAsString(Verfahrensstand)} erzeugt, da {@link Verfahrensstand#getBezeichnung()}
 * nicht mit {@code @JsonValue} annotiert ist. Dadurch werden alle Felder des Enums (u.a. {@code bezeichnung} und
 * {@code suggestions}) indiziert.
 */
@Slf4j
public class VerfahrensstandValueBridge implements ValueBridge<Verfahrensstand, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String toIndexedValue(final Verfahrensstand value, final ValueBridgeToIndexedValueContext context) {
        return toJson(value);
    }

    @Override
    public Verfahrensstand fromIndexedValue(
        final String indexedValue,
        final ValueBridgeFromIndexedValueContext context
    ) {
        return fromJson(indexedValue);
    }

    public static String toJson(final Verfahrensstand value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("Fehler beim Serialisieren von Verfahrensstand: {}", e.getMessage());
            return null;
        }
    }

    public static Verfahrensstand fromJson(final String json) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, Verfahrensstand.class);
        } catch (JsonProcessingException e) {
            log.error("Fehler beim Deserialisieren von Verfahrensstand: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean isCompatibleWith(final ValueBridge<?, ?> other) {
        return other != null && getClass().equals(other.getClass());
    }
}
