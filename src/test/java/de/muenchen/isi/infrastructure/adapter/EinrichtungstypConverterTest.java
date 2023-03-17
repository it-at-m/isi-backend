package de.muenchen.isi.infrastructure.adapter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import de.muenchen.isi.infrastructure.entity.enums.Einrichtungstyp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EinrichtungstypConverterTest {

    private final EinrichtungstypConverter einrichtungstypConverter = new EinrichtungstypConverter();

    @Test
    void convert() throws CsvDataTypeMismatchException {
        assertThat(this.einrichtungstypConverter.convert("Kinderkrippe"), is(Einrichtungstyp.KINDERKRIPPE));
        assertThat(this.einrichtungstypConverter.convert("Kindergarten"), is(Einrichtungstyp.KINDERGARTEN));
        assertThat(this.einrichtungstypConverter.convert("Kinderhort"), is(Einrichtungstyp.KINDERHORT));
        assertThat(this.einrichtungstypConverter.convert("Grundschule"), is(Einrichtungstyp.GRUNDSCHULE));
        assertThat(this.einrichtungstypConverter.convert("N.N."), is(Einrichtungstyp.N_N));
        Assertions.assertThrows(
            CsvDataTypeMismatchException.class,
            () -> this.einrichtungstypConverter.convert("not-valid")
        );
    }
}
