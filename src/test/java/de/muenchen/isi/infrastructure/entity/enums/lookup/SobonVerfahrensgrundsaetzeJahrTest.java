package de.muenchen.isi.infrastructure.entity.enums.lookup;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class SobonVerfahrensgrundsaetzeJahrTest {

    @Test
    void findByBezeichnung() {
        var result = SobonVerfahrensgrundsaetzeJahr.findByBezeichnung("no_valid_enum");
        assertThat(result, is(Optional.empty()));

        result = SobonVerfahrensgrundsaetzeJahr.findByBezeichnung("1997");
        assertThat(result, is(Optional.of(SobonVerfahrensgrundsaetzeJahr.JAHR_1997)));
    }
}
