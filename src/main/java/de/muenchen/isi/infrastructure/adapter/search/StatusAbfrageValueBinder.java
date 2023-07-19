package de.muenchen.isi.infrastructure.adapter.search;

import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.ValueBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.ValueBinder;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class StatusAbfrageValueBinder implements ValueBinder {

    @Override
    public void bind(ValueBindingContext<?> context) {
        context.bridge(StatusAbfrage.class, new StatusAbfrageValueBridge());
    }

    private static class StatusAbfrageValueBridge implements ValueBridge<StatusAbfrage, String> {

        @Override
        public String toIndexedValue(StatusAbfrage value, ValueBridgeToIndexedValueContext context) {
            return value.getBezeichnung();
        }
    }
}
