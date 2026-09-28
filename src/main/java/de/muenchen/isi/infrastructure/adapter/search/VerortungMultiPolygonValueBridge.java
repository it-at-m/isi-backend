package de.muenchen.isi.infrastructure.adapter.search;

import de.muenchen.isi.infrastructure.entity.common.VerortungMultiPolygon;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeFromIndexedValueContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

/**
 * ValueBridge für Hibernate Search, um {@link VerortungMultiPolygon}-Objekte in den Suchindex
 * und wieder zurück zu konvertieren.
 *
 * Hibernate Search kann in Lucene/Elasticsearch nur einfache Typen wie String
 * oder Zahlen speichern. Komplexe Objekte wie {@link VerortungMultiPolygon} müssen daher
 * serialisiert werden.
 *
 * Diese Bridge:
 * - wandelt eine {@link VerortungMultiPolygon} beim Indizieren in einen JSON-String um
 *   ({@code toIndexedValue}),
 * - liest den JSON-String bei Projektionen wieder zurück in eine {@link VerortungMultiPolygon}
 *   ({@code fromIndexedValue}).
 *
 * Damit können {@link VerortungMultiPolygon}-Felder auch in Hibernate Search Projections
 * (z. B. @ProjectionConstructor-Records) korrekt befüllt werden.
 */
@Slf4j
public class VerortungMultiPolygonValueBridge implements ValueBridge<VerortungMultiPolygon, String> {

    private static final ObjectMapper objectMapper = new JsonMapper();

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
        } catch (JacksonException e) {
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
        } catch (JacksonException e) {
            log.error("Fehler beim Deserialisieren von VerortungMultiPolygon: {}", e.getMessage());
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
