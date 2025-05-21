package de.muenchen.isi.infrastructure.adapter.search;

import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeFromIndexedValueContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

/**
 * ValueBridge um Entitätsattribute vom Typ {@link StatusAbfrage} für eine Volltextsuche indizieren zu können.
 * Die zu indizierenden Werte werden aus dem Typattribut {@link StatusAbfrage#getBezeichnung()} entnommen.
 */
public class StatusAbfrageValueBridge implements ValueBridge<StatusAbfrage, String> {

    @Override
    public String toIndexedValue(final StatusAbfrage value, final ValueBridgeToIndexedValueContext context) {
        return value != null ? value.getBezeichnung() : null;
    }

    @Override
    public StatusAbfrage fromIndexedValue(final String value, final ValueBridgeFromIndexedValueContext context) {
        return value != null ? StatusAbfrage.fromString(value) : null;
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
