package de.muenchen.isi.infrastructure.entity.enums.lookup;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class SobonVerfahrensgrundsaetzeJahrTest {

    @Test
    void findByBezeichnung() {
        var result = SobonVerfahrensgrundsaetzeJahr.findByBezeichnung("no_valid_enum");
        assertThat(result, is(Optional.empty()));

        result = SobonVerfahrensgrundsaetzeJahr.findByBezeichnung("2014");
        assertThat(result, is(Optional.of(SobonVerfahrensgrundsaetzeJahr.JAHR_2014)));
    }

}
