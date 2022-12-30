package de.muenchen.isi.infrastructure.adapter;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import de.muenchen.isi.infrastructure.entity.enums.Wohnungstyp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class WohnungstypConverterTest {

    private final WohnungstypConverter wohnungstypConverter = new WohnungstypConverter();

    @Test
    void convert() throws CsvDataTypeMismatchException {
        assertThat(this.wohnungstypConverter.convert("GW-freifinanziert"), is(Wohnungstyp.GW_FREIFINANZEIRT));
        assertThat(this.wohnungstypConverter.convert("GW-gefördert"), is(Wohnungstyp.GW_GEFOERDERT));
        assertThat(this.wohnungstypConverter.convert("MM-Eigentum"), is(Wohnungstyp.MM_EIGENTUM));
        assertThat(this.wohnungstypConverter.convert("MM-Miete"), is(Wohnungstyp.MM_MIETE));
        assertThat(this.wohnungstypConverter.convert("1-2-FH"), is(Wohnungstyp.EINS_ZWEI_FH));
        Assertions.assertThrows(CsvDataTypeMismatchException.class, () -> this.wohnungstypConverter.convert("not-valid"));

    }

}