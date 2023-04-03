package de.muenchen.isi.infrastructure.adapter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SobonVerfahrensgrundsaetzeJahrConverterTest {

    private final SobonVerfahrensgrundsaetzeJahrConverter sobonVerfahrensgrundsaetzeJahrConverter =
        new SobonVerfahrensgrundsaetzeJahrConverter();

    @Test
    void convert() throws CsvDataTypeMismatchException {
        assertThat(
            this.sobonVerfahrensgrundsaetzeJahrConverter.convert("vor 2014"),
            is(SobonVerfahrensgrundsaetzeJahr.DAVOR)
        );
        assertThat(
            this.sobonVerfahrensgrundsaetzeJahrConverter.convert("2014"),
            is(SobonVerfahrensgrundsaetzeJahr.JAHR_2014)
        );
        assertThat(
            this.sobonVerfahrensgrundsaetzeJahrConverter.convert("2017"),
            is(SobonVerfahrensgrundsaetzeJahr.JAHR_2017)
        );
        assertThat(
            this.sobonVerfahrensgrundsaetzeJahrConverter.convert("2017 plus"),
            is(SobonVerfahrensgrundsaetzeJahr.JAHR_2017_PLUS)
        );
        assertThat(
            this.sobonVerfahrensgrundsaetzeJahrConverter.convert("2021"),
            is(SobonVerfahrensgrundsaetzeJahr.JAHR_2021)
        );
        Assertions.assertThrows(
            CsvDataTypeMismatchException.class,
            () -> this.sobonVerfahrensgrundsaetzeJahrConverter.convert("not-valid")
        );
    }
}
