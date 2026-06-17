package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import de.muenchen.isi.infrastructure.entity.enums.lookup.Bauratenmethodik;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class BauratenmethodikDefaultingServiceTest {

    private final BauratenmethodikDefaultingService service = new BauratenmethodikDefaultingService();

    @Test
    void determineDefaultBeforeStichtag() {
        final var result = service.determineDefault(LocalDate.of(2026, 6, 30), false);
        assertThat(result, is(Bauratenmethodik.ALTE_BAURATENMETHODIK));
    }

    @Test
    void determineDefaultAtStichtag() {
        final var result = service.determineDefault(LocalDate.of(2026, 7, 1), false);
        assertThat(result, is(Bauratenmethodik.NEUE_BAURATENMETHODIK));
    }

    @Test
    void determineDefaultAfterStichtag() {
        final var result = service.determineDefault(LocalDate.of(2026, 9, 1), false);
        assertThat(result, is(Bauratenmethodik.NEUE_BAURATENMETHODIK));
    }

    @Test
    void determineDefaultDatumUnbekannt() {
        final var result = service.determineDefault(LocalDate.of(2025, 1, 1), true);
        assertThat(result, is(nullValue()));
    }

    @Test
    void determineDefaultDatumNull() {
        final var result = service.determineDefault(null, false);
        assertThat(result, is(nullValue()));
    }
}
