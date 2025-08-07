package de.muenchen.isi.infrastructure.adapter.search;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeFromIndexedValueContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class ArtAbfrageValueBridge implements ValueBridge<ArtAbfrage, String> {

    @Override
    public String toIndexedValue(ArtAbfrage value, ValueBridgeToIndexedValueContext valueBridgeToIndexedValueContext) {
        return value != null ? value.getBezeichnung() : null;
    }

    @Override
    public ArtAbfrage fromIndexedValue(String value, ValueBridgeFromIndexedValueContext context) {
        return value != null ? ArtAbfrage.valueOf(value) : null;
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
