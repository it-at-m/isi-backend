package de.muenchen.isi.infrastructure.adapter.search;

import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVorhaben;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class StandVorhabenValueBridge implements ValueBridge<StandVorhaben, String> {

    @Override
    public String toIndexedValue(StandVorhaben value, ValueBridgeToIndexedValueContext context) {
        return value.getBezeichnung();
    }
}