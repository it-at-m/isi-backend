package de.muenchen.isi.infrastructure.adapter.search;

import de.muenchen.isi.infrastructure.entity.common.Wgs84;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeFromIndexedValueContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class Wgs84ValueBridge implements ValueBridge<Wgs84, String> {

    @Override
    public String toIndexedValue(final Wgs84 value, final ValueBridgeToIndexedValueContext context) {
        return value != null ? value.toString() : null;
    }

    @Override
    public Wgs84 fromIndexedValue(final String value, final ValueBridgeFromIndexedValueContext context) {
        return value != null ? Wgs84.fromString(value) : null;
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
