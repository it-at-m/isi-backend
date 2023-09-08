package de.muenchen.isi.infrastructure.adapter.search;

import java.util.Objects;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

/**
 * ValueBridge um Entitätsattribute vom Typ {@link Integer} für eine Volltextsuche indizieren zu können.
 */
public class IntegerToStringValueBridge implements ValueBridge<Integer, String> {

    @Override
    public String toIndexedValue(Integer value, ValueBridgeToIndexedValueContext context) {
        return Objects.toString(value, null);
    }
}
