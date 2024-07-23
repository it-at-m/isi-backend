package de.muenchen.isi.infrastructure.adapter.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import org.hibernate.search.mapper.pojo.bridge.builtin.impl.DefaultEnumBridge;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith({ MockitoExtension.class, OutputCaptureExtension.class })
@MockitoSettings(strictness = Strictness.LENIENT)
class StadtbezirkNummerValueBridgeTest {

    private final StadtbezirkNummerValueBridge stringToIntegerValueBridge = new StadtbezirkNummerValueBridge();

    @Test
    void toIndexedValue() {
        assertThat(stringToIntegerValueBridge.toIndexedValue("01", null), is("1"));
        assertThat(stringToIntegerValueBridge.toIndexedValue("    01     ", null), is("1"));
        assertThat(stringToIntegerValueBridge.toIndexedValue("001", null), is("1"));
        assertThat(stringToIntegerValueBridge.toIndexedValue("1", null), is("1"));
        assertThat(stringToIntegerValueBridge.toIndexedValue("15", null), is("15"));
        assertThat(stringToIntegerValueBridge.toIndexedValue(" 15  ", null), is("15"));
        assertThat(stringToIntegerValueBridge.toIndexedValue("-15", null), is("-15"));
        assertThat(stringToIntegerValueBridge.toIndexedValue(" -15  ", null), is("-15"));
        assertThat(stringToIntegerValueBridge.toIndexedValue("1234567", null), is("1234567"));
        assertThat(stringToIntegerValueBridge.toIndexedValue(" 1234567  ", null), is("1234567"));
        assertThat(stringToIntegerValueBridge.toIndexedValue("0", null), is("0"));
        assertThat(stringToIntegerValueBridge.toIndexedValue("  0  ", null), is("0"));
        assertThat(stringToIntegerValueBridge.toIndexedValue(null, null), is(nullValue()));
        assertThat(stringToIntegerValueBridge.toIndexedValue("x", null), is("x"));
        assertThat(stringToIntegerValueBridge.toIndexedValue("   x   ", null), is("x"));
        assertThat(stringToIntegerValueBridge.toIndexedValue(" ", null), is(nullValue()));
    }

    @Test
    void toNormalizedStadtbezirknummer() {
        assertThat(StadtbezirkNummerValueBridge.toNormalizedStadtbezirknummer("01"), is("1"));
        assertThat(StadtbezirkNummerValueBridge.toNormalizedStadtbezirknummer("    01     "), is("1"));
        assertThat(StadtbezirkNummerValueBridge.toNormalizedStadtbezirknummer("001"), is("1"));
        assertThat(StadtbezirkNummerValueBridge.toNormalizedStadtbezirknummer("1"), is("1"));
        assertThat(StadtbezirkNummerValueBridge.toNormalizedStadtbezirknummer("15"), is("15"));
        assertThat(StadtbezirkNummerValueBridge.toNormalizedStadtbezirknummer(" 15  "), is("15"));
        assertThat(StadtbezirkNummerValueBridge.toNormalizedStadtbezirknummer("-15"), is("-15"));
        assertThat(StadtbezirkNummerValueBridge.toNormalizedStadtbezirknummer(" -15  "), is("-15"));
        assertThat(StadtbezirkNummerValueBridge.toNormalizedStadtbezirknummer("1234567"), is("1234567"));
        assertThat(StadtbezirkNummerValueBridge.toNormalizedStadtbezirknummer(" 1234567  "), is("1234567"));
        assertThat(StadtbezirkNummerValueBridge.toNormalizedStadtbezirknummer("0"), is("0"));
        assertThat(StadtbezirkNummerValueBridge.toNormalizedStadtbezirknummer("  0  "), is("0"));
        assertThat(StadtbezirkNummerValueBridge.toNormalizedStadtbezirknummer(null), is(nullValue()));
        assertThat(StadtbezirkNummerValueBridge.toNormalizedStadtbezirknummer("x"), is("x"));
        assertThat(StadtbezirkNummerValueBridge.toNormalizedStadtbezirknummer("   x   "), is("x"));
        assertThat(StadtbezirkNummerValueBridge.toNormalizedStadtbezirknummer(" "), is(nullValue()));
    }

    @Test
    void isCompatibleWith() {
        assertThat(stringToIntegerValueBridge.isCompatibleWith(stringToIntegerValueBridge), is(true));
        assertThat(stringToIntegerValueBridge.isCompatibleWith(new StadtbezirkNummerValueBridge()), is(true));
        assertThat(
            stringToIntegerValueBridge.isCompatibleWith(new DefaultEnumBridge<>(StatusAbfrage.class)),
            is(false)
        );
        assertThat(stringToIntegerValueBridge.isCompatibleWith(null), is(false));
    }
}
