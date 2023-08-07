package de.muenchen.isi.infrastructure.adapter.search;

import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class StatusInfrastruktureinrichtungValueBridge implements ValueBridge<StatusInfrastruktureinrichtung, String> {

    @Override
    public String toIndexedValue(StatusInfrastruktureinrichtung value, ValueBridgeToIndexedValueContext context) {
        return value != null ? value.getBezeichnung() : null;
    }
}
