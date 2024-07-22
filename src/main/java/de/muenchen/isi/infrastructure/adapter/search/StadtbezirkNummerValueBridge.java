package de.muenchen.isi.infrastructure.adapter.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

/**
 * ValueBridge um die Nummer des Stadtbezirks (z.B. 01 oder 12) für eine Volltextsuche indizieren zu können.
 *
 * Falls die Stadtbezirksnummer mit einer führenden "0" beginnt, so werden die Nummer zum einen mit der
 * führenden "0" und zum anderen ohne der führenden "0" indiziert.
 */
public class StadtbezirkNummerValueBridge implements ValueBridge<String, Collection<String>> {

    @Override
    public Collection<String> toIndexedValue(final String value, final ValueBridgeToIndexedValueContext context) {
        if (StringUtils.isNotEmpty(value)) {
            final var indexedNummerStadtbezirk = new HashSet<String>();
            indexedNummerStadtbezirk.add(value);
            final Integer integer = NumberUtils.toInt(value);
            if (integer != 0) {
                indexedNummerStadtbezirk.add(integer.toString());
            }
            return indexedNummerStadtbezirk;
        } else {
            return null;
        }
    }
}
