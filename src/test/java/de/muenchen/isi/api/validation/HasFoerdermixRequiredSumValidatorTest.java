package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.FoerdermixDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class HasFoerdermixRequiredSumValidatorTest {

    private final HasFoerdermixRequiredSumValidator hasFoerdermixRequiredSumValidator = new HasFoerdermixRequiredSumValidator();

    @Test
    void isValid() {
        var foerdermix = new FoerdermixDto();
        foerdermix.setAnteilFreifinanzierterGeschosswohnungsbau(BigDecimal.valueOf(60));
        foerdermix.setAnteilGefoerderterMietwohnungsbau(BigDecimal.valueOf(40.00));
        foerdermix.setAnteilMuenchenModell(BigDecimal.valueOf(0.00));
        foerdermix.setAnteilPreisgedaempfterMietwohnungsbau(BigDecimal.ZERO);
        foerdermix.setAnteilKonzeptionellerMietwohnungsbau(BigDecimal.valueOf(0));
        foerdermix.setAnteilBaugemeinschaften(BigDecimal.valueOf(0));
        foerdermix.setAnteilEinUndZweifamilienhaeuser(BigDecimal.valueOf(0));
        assertThat(
                this.hasFoerdermixRequiredSumValidator.isValid(foerdermix, null),
                is(true)
        );

        foerdermix = new FoerdermixDto();
        foerdermix.setAnteilFreifinanzierterGeschosswohnungsbau(null);
        foerdermix.setAnteilGefoerderterMietwohnungsbau(BigDecimal.valueOf(99.99));
        foerdermix.setAnteilMuenchenModell(BigDecimal.valueOf(0.01));
        foerdermix.setAnteilPreisgedaempfterMietwohnungsbau(null);
        foerdermix.setAnteilKonzeptionellerMietwohnungsbau(null);
        foerdermix.setAnteilBaugemeinschaften(null);
        foerdermix.setAnteilEinUndZweifamilienhaeuser(BigDecimal.valueOf(0));
        assertThat(
                this.hasFoerdermixRequiredSumValidator.isValid(foerdermix, null),
                is(true)
        );

        foerdermix = new FoerdermixDto();
        foerdermix.setAnteilFreifinanzierterGeschosswohnungsbau(null);
        foerdermix.setAnteilGefoerderterMietwohnungsbau(BigDecimal.valueOf(99.99));
        foerdermix.setAnteilMuenchenModell(BigDecimal.valueOf(0.01));
        foerdermix.setAnteilPreisgedaempfterMietwohnungsbau(null);
        foerdermix.setAnteilKonzeptionellerMietwohnungsbau(null);
        foerdermix.setAnteilBaugemeinschaften(null);
        foerdermix.setAnteilEinUndZweifamilienhaeuser(BigDecimal.valueOf(0));
        assertThat(
                this.hasFoerdermixRequiredSumValidator.isValid(foerdermix, null),
                is(true)
        );

        foerdermix = new FoerdermixDto();
        foerdermix.setAnteilFreifinanzierterGeschosswohnungsbau(null);
        foerdermix.setAnteilGefoerderterMietwohnungsbau(BigDecimal.valueOf(99.99));
        foerdermix.setAnteilMuenchenModell(BigDecimal.valueOf(0.02));
        foerdermix.setAnteilPreisgedaempfterMietwohnungsbau(null);
        foerdermix.setAnteilKonzeptionellerMietwohnungsbau(null);
        foerdermix.setAnteilBaugemeinschaften(null);
        foerdermix.setAnteilEinUndZweifamilienhaeuser(BigDecimal.valueOf(0));
        assertThat(
                this.hasFoerdermixRequiredSumValidator.isValid(foerdermix, null),
                is(false)
        );

        foerdermix = new FoerdermixDto();
        foerdermix.setAnteilFreifinanzierterGeschosswohnungsbau(null);
        foerdermix.setAnteilGefoerderterMietwohnungsbau(BigDecimal.valueOf(99.99));
        foerdermix.setAnteilMuenchenModell(null);
        foerdermix.setAnteilPreisgedaempfterMietwohnungsbau(null);
        foerdermix.setAnteilKonzeptionellerMietwohnungsbau(null);
        foerdermix.setAnteilBaugemeinschaften(null);
        foerdermix.setAnteilEinUndZweifamilienhaeuser(BigDecimal.valueOf(0));
        assertThat(
                this.hasFoerdermixRequiredSumValidator.isValid(foerdermix, null),
                is(false)
        );

        foerdermix = new FoerdermixDto();
        foerdermix.setAnteilFreifinanzierterGeschosswohnungsbau(null);
        foerdermix.setAnteilGefoerderterMietwohnungsbau(null);
        foerdermix.setAnteilMuenchenModell(null);
        foerdermix.setAnteilPreisgedaempfterMietwohnungsbau(null);
        foerdermix.setAnteilKonzeptionellerMietwohnungsbau(null);
        foerdermix.setAnteilBaugemeinschaften(null);
        foerdermix.setAnteilEinUndZweifamilienhaeuser(null);
        assertThat(
                this.hasFoerdermixRequiredSumValidator.isValid(foerdermix, null),
                is(false)
        );

        assertThat(
                this.hasFoerdermixRequiredSumValidator.isValid(null, null),
                is(true)
        );
    }

}
