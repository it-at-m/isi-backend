package de.muenchen.isi.infrastructure.adapter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import de.muenchen.isi.infrastructure.entity.enums.Altersklasse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AltersklasseConverterTest {

    private final AltersklasseConverter altersklasseConverter = new AltersklasseConverter();

    @Test
    void convert() throws CsvDataTypeMismatchException {
        assertThat(this.altersklasseConverter.convert("0-2"), is(Altersklasse.NULL_ZWEI));
        assertThat(this.altersklasseConverter.convert("3-6,5"), is(Altersklasse.DREI_SECHSEINHALB));
        assertThat(this.altersklasseConverter.convert("10,5-15"), is(Altersklasse.ZEHNEINHALB_FUENFZEHN));
        assertThat(this.altersklasseConverter.convert("16-18"), is(Altersklasse.SECHSZEHN_ACHTZEHN));
        assertThat(this.altersklasseConverter.convert("alle EWO"), is(Altersklasse.ALLE_EWO));
        Assertions.assertThrows(
            CsvDataTypeMismatchException.class,
            () -> this.altersklasseConverter.convert("not-valid")
        );
    }
}
