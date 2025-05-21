package de.muenchen.isi.infrastructure.adapter.search;

import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class InfrastruktureinrichtungTypValueBridge implements ValueBridge<InfrastruktureinrichtungTyp, String> {

    @Override
    public String toIndexedValue(
        final InfrastruktureinrichtungTyp value,
        final ValueBridgeToIndexedValueContext context
    ) {
        return value != null ? value.getBezeichnung() : null;
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
