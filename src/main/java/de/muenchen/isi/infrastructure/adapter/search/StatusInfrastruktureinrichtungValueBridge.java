package de.muenchen.isi.infrastructure.adapter.search;

import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeFromIndexedValueContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

/**
 * ValueBridge um Entitätsattribute vom Typ {@link StatusInfrastruktureinrichtung} für eine Volltextsuche indizieren zu können.
 * Die zu indizierenden Werte werden aus dem Typattribut {@link StatusInfrastruktureinrichtung#getBezeichnung()} entnommen.
 */
public class StatusInfrastruktureinrichtungValueBridge implements ValueBridge<StatusInfrastruktureinrichtung, String> {

    @Override
    public String toIndexedValue(
        final StatusInfrastruktureinrichtung value,
        final ValueBridgeToIndexedValueContext context
    ) {
        return value != null ? value.getBezeichnung() : null;
    }

    @Override
    public StatusInfrastruktureinrichtung fromIndexedValue(
        final String value,
        final ValueBridgeFromIndexedValueContext context
    ) {
        return value != null ? StatusInfrastruktureinrichtung.fromString(value) : null;
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
