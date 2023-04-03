package de.muenchen.isi.api.validation;

<<<<<<< HEAD
import de.muenchen.isi.api.dto.FoerderartDto;
import de.muenchen.isi.api.dto.FoerdermixDto;
import de.muenchen.isi.infrastructure.entity.Foerderart;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

=======
>>>>>>> dev
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.FoerdermixDto;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class HasFoerdermixRequiredSumValidatorTest {

    private final HasFoerdermixRequiredSumValidator hasFoerdermixRequiredSumValidator =
        new HasFoerdermixRequiredSumValidator();

    @Test
    void isValid() {
        var foerdermix = new FoerdermixDto();
<<<<<<< HEAD
        FoerderartDto foerderart = new FoerderartDto();
        foerderart.setBezeichnung("AnteilMuenchenModell");
        foerderart.setAnteilProzent(BigDecimal.valueOf(40));

        FoerderartDto foerderart2 = new FoerderartDto();
        foerderart2.setBezeichnung("AnteilBaugemeinschaft");
        foerderart2.setAnteilProzent(BigDecimal.valueOf(60));

        List<FoerderartDto> foerderarten = new ArrayList<>(Arrays.asList(foerderart, foerderart2));
        foerdermix.setFoerderarten(foerderarten);

        assertThat(
                this.hasFoerdermixRequiredSumValidator.isValid(foerdermix, null),
                is(true)
        );


        foerderart.setAnteilProzent(BigDecimal.valueOf(99.99));
        foerderart2.setAnteilProzent(BigDecimal.valueOf(0.01));

        assertThat(
                this.hasFoerdermixRequiredSumValidator.isValid(foerdermix, null),
                is(true)
        );


        FoerderartDto foerderart3 = new FoerderartDto();
        foerderart3.setBezeichnung("AnteilEinUndZweifamilienhaeuser");
        foerderart3.setAnteilProzent(BigDecimal.valueOf(0));

        foerderarten = new ArrayList<>(Arrays.asList(foerderart, foerderart2,foerderart3));
        foerdermix.setFoerderarten(foerderarten);

        assertThat(
                this.hasFoerdermixRequiredSumValidator.isValid(foerdermix, null),
                is(true)
        );


        foerderart2.setAnteilProzent(BigDecimal.valueOf(0.02));

        assertThat(
                this.hasFoerdermixRequiredSumValidator.isValid(foerdermix, null),
                is(false)
        );


        foerderart.setAnteilProzent(null);
        foerderart2.setAnteilProzent(BigDecimal.ZERO);
        foerderart3.setAnteilProzent(BigDecimal.valueOf(99.99));

        assertThat(
                this.hasFoerdermixRequiredSumValidator.isValid(foerdermix, null),
                is(false)
        );
=======
        foerdermix.setAnteilFreifinanzierterGeschosswohnungsbau(BigDecimal.valueOf(60));
        foerdermix.setAnteilGefoerderterMietwohnungsbau(BigDecimal.valueOf(40.00));
        foerdermix.setAnteilMuenchenModell(BigDecimal.valueOf(0.00));
        foerdermix.setAnteilPreisgedaempfterMietwohnungsbau(BigDecimal.ZERO);
        foerdermix.setAnteilKonzeptionellerMietwohnungsbau(BigDecimal.valueOf(0));
        foerdermix.setAnteilBaugemeinschaften(BigDecimal.valueOf(0));
        foerdermix.setAnteilEinUndZweifamilienhaeuser(BigDecimal.valueOf(0));
        assertThat(this.hasFoerdermixRequiredSumValidator.isValid(foerdermix, null), is(true));

        foerdermix = new FoerdermixDto();
        foerdermix.setAnteilFreifinanzierterGeschosswohnungsbau(null);
        foerdermix.setAnteilGefoerderterMietwohnungsbau(BigDecimal.valueOf(99.99));
        foerdermix.setAnteilMuenchenModell(BigDecimal.valueOf(0.01));
        foerdermix.setAnteilPreisgedaempfterMietwohnungsbau(null);
        foerdermix.setAnteilKonzeptionellerMietwohnungsbau(null);
        foerdermix.setAnteilBaugemeinschaften(null);
        foerdermix.setAnteilEinUndZweifamilienhaeuser(BigDecimal.valueOf(0));
        assertThat(this.hasFoerdermixRequiredSumValidator.isValid(foerdermix, null), is(true));

        foerdermix = new FoerdermixDto();
        foerdermix.setAnteilFreifinanzierterGeschosswohnungsbau(null);
        foerdermix.setAnteilGefoerderterMietwohnungsbau(BigDecimal.valueOf(99.99));
        foerdermix.setAnteilMuenchenModell(BigDecimal.valueOf(0.01));
        foerdermix.setAnteilPreisgedaempfterMietwohnungsbau(null);
        foerdermix.setAnteilKonzeptionellerMietwohnungsbau(null);
        foerdermix.setAnteilBaugemeinschaften(null);
        foerdermix.setAnteilEinUndZweifamilienhaeuser(BigDecimal.valueOf(0));
        assertThat(this.hasFoerdermixRequiredSumValidator.isValid(foerdermix, null), is(true));

        foerdermix = new FoerdermixDto();
        foerdermix.setAnteilFreifinanzierterGeschosswohnungsbau(null);
        foerdermix.setAnteilGefoerderterMietwohnungsbau(BigDecimal.valueOf(99.99));
        foerdermix.setAnteilMuenchenModell(BigDecimal.valueOf(0.02));
        foerdermix.setAnteilPreisgedaempfterMietwohnungsbau(null);
        foerdermix.setAnteilKonzeptionellerMietwohnungsbau(null);
        foerdermix.setAnteilBaugemeinschaften(null);
        foerdermix.setAnteilEinUndZweifamilienhaeuser(BigDecimal.valueOf(0));
        assertThat(this.hasFoerdermixRequiredSumValidator.isValid(foerdermix, null), is(false));

        foerdermix = new FoerdermixDto();
        foerdermix.setAnteilFreifinanzierterGeschosswohnungsbau(null);
        foerdermix.setAnteilGefoerderterMietwohnungsbau(BigDecimal.valueOf(99.99));
        foerdermix.setAnteilMuenchenModell(null);
        foerdermix.setAnteilPreisgedaempfterMietwohnungsbau(null);
        foerdermix.setAnteilKonzeptionellerMietwohnungsbau(null);
        foerdermix.setAnteilBaugemeinschaften(null);
        foerdermix.setAnteilEinUndZweifamilienhaeuser(BigDecimal.valueOf(0));
        assertThat(this.hasFoerdermixRequiredSumValidator.isValid(foerdermix, null), is(false));

        foerdermix = new FoerdermixDto();
        foerdermix.setAnteilFreifinanzierterGeschosswohnungsbau(null);
        foerdermix.setAnteilGefoerderterMietwohnungsbau(null);
        foerdermix.setAnteilMuenchenModell(null);
        foerdermix.setAnteilPreisgedaempfterMietwohnungsbau(null);
        foerdermix.setAnteilKonzeptionellerMietwohnungsbau(null);
        foerdermix.setAnteilBaugemeinschaften(null);
        foerdermix.setAnteilEinUndZweifamilienhaeuser(null);
        assertThat(this.hasFoerdermixRequiredSumValidator.isValid(foerdermix, null), is(false));
>>>>>>> dev

        assertThat(this.hasFoerdermixRequiredSumValidator.isValid(null, null), is(true));
    }
}
