package de.muenchen.isi.infrastructure.adapter.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.isi.infrastructure.entity.common.Adresse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeFromIndexedValueContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

/**
 * ValueBridge für Hibernate Search, um {@link Adresse}-Objekte in den Suchindex
 * und wieder zurück zu konvertieren.
 *
 * Hibernate Search kann in Lucene/Elasticsearch nur einfache Typen wie String
 * oder Zahlen speichern. Komplexe Objekte wie {@link Adresse} müssen daher
 * serialisiert werden.
 *
 * Diese Bridge:
 * - wandelt eine {@link Adresse} beim Indizieren in einen JSON-String um
 *   ({@code toIndexedValue}),
 * - liest den JSON-String bei Projektionen wieder zurück in eine {@link Adresse}
 *   ({@code fromIndexedValue}).
 *
 * Damit können {@link Adresse}-Felder auch in Hibernate Search Projections
 * (z. B. @ProjectionConstructor-Records) korrekt befüllt werden.
 */
@Slf4j
public class AdresseValueBridge implements ValueBridge<Adresse, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String toIndexedValue(final Adresse value, final ValueBridgeToIndexedValueContext context) {
        return toJson(value);
    }

    @Override
    public Adresse fromIndexedValue(final String indexedValue, final ValueBridgeFromIndexedValueContext context) {
        return fromJson(indexedValue);
    }

    public static String toJson(final Adresse value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("Fehler beim Serialisieren von Adresse: {}", e.getMessage());
            return null;
        }
    }

    public static Adresse fromJson(final String json) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, Adresse.class);
        } catch (JsonProcessingException e) {
            log.error("Fehler beim Deserialisieren von Adresse: {}", e.getMessage());
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
