package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.abfrageAngelegt.BauleitplanverfahrenAngelegtDto;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class Start42VerfahrenValidatorTest {

    private final Start42VerfahrenValidator validator = new Start42VerfahrenValidator();

    @Test
    void isValidDatumGesetztUndUnbekanntFalse() {
        final var abfrage = new BauleitplanverfahrenAngelegtDto();
        abfrage.setStart42Verfahren(LocalDate.of(2026, 3, 1));
        abfrage.setStart42VerfahrenDatumUnbekannt(false);
        assertThat(validator.isValid(abfrage, null), is(true));
    }

    @Test
    void isValidDatumNullUndUnbekanntTrue() {
        final var abfrage = new BauleitplanverfahrenAngelegtDto();
        abfrage.setStart42Verfahren(null);
        abfrage.setStart42VerfahrenDatumUnbekannt(true);
        assertThat(validator.isValid(abfrage, null), is(true));
    }

    @Test
    void isValidDatumGesetztUndUnbekanntTrue() {
        final var abfrage = new BauleitplanverfahrenAngelegtDto();
        abfrage.setStart42Verfahren(LocalDate.of(2026, 3, 1));
        abfrage.setStart42VerfahrenDatumUnbekannt(true);
        assertThat(validator.isValid(abfrage, null), is(false));
    }

    @Test
    void isValidDatumNullUndUnbekanntFalse() {
        final var abfrage = new BauleitplanverfahrenAngelegtDto();
        abfrage.setStart42Verfahren(null);
        abfrage.setStart42VerfahrenDatumUnbekannt(false);
        assertThat(validator.isValid(abfrage, null), is(false));
    }

    @Test
    void isValidDatumNullUndUnbekanntNull() {
        final var abfrage = new BauleitplanverfahrenAngelegtDto();
        abfrage.setStart42Verfahren(null);
        abfrage.setStart42VerfahrenDatumUnbekannt(null);
        assertThat(validator.isValid(abfrage, null), is(false));
    }

    @Test
    void isValidValueNull() {
        assertThat(validator.isValid(null, null), is(true));
    }
}
