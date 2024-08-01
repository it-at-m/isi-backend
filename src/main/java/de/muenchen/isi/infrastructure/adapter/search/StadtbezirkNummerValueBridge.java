package de.muenchen.isi.infrastructure.adapter.search;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

/**
 * ValueBridge um die Nummer des Stadtbezirks (z.B. 01 oder 12) ohne führende "0"en indizieren zu können.
 */
public class StadtbezirkNummerValueBridge implements ValueBridge<String, String> {

    /**
     * @param value als mögliche Stringrepräsentation eines Integers.
     * @param context wird nicht benötigt.
     * @return Handelt es sich beim Parameter value um einen Integer so werden die führenden "0"en
     * entfernt und als String zurückgegeben, andernfalls wird der String getrimmt zurückgegeben.
     */
    @Override
    public String toIndexedValue(final String value, final ValueBridgeToIndexedValueContext context) {
        return toNormalizedStadtbezirknummer(value);
    }

    /**
     * @param value als mögliche Stringrepräsentation eines Integers.
     * @return Handelt es sich beim Parameter value um einen Integer so werden die führenden "0"en
     * entfernt und als String zurückgegeben, andernfalls wird der String getrimmt zurückgegeben.
     */
    public static String toNormalizedStadtbezirknummer(final String value) {
        final var trimmedValue = StringUtils.trimToNull(value);
        return NumberUtils.isParsable(trimmedValue)
            ? Integer.valueOf(NumberUtils.toInt(trimmedValue)).toString()
            : trimmedValue;
    }

    @Override
    public boolean isCompatibleWith(final ValueBridge<?, ?> other) {
        return other != null && getClass().equals(other.getClass());
    }
}
