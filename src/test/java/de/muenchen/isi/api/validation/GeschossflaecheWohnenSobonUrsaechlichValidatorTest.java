package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.AbfragevarianteResponseDto;
import de.muenchen.isi.api.dto.InfrastrukturabfrageResponseDto;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Planungsrecht;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class GeschossflaecheWohnenSobonUrsaechlichValidatorTest {

    private final GeschossflaecheWohnenSobonUrsaechlichValidator geschossflaecheWohnenSobonUrsaechlichValidator =
            new GeschossflaecheWohnenSobonUrsaechlichValidator();

    @Test
    void isValidSobonRelevant() {
        final InfrastrukturabfrageResponseDto infrastrukturabfrage = new InfrastrukturabfrageResponseDto();
        infrastrukturabfrage.setSobonRelevant(UncertainBoolean.TRUE);
        assertThat(this.geschossflaecheWohnenSobonUrsaechlichValidator.isValid(infrastrukturabfrage, null), is(true));

        infrastrukturabfrage.setAbfragevarianten(new ArrayList<AbfragevarianteResponseDto>());

        final AbfragevarianteResponseDto abfragevariantePlanungsrechtParag11 = new AbfragevarianteResponseDto();
        abfragevariantePlanungsrechtParag11.setPlanungsrecht(Planungsrecht.BPLAN_PARAG_11);
        abfragevariantePlanungsrechtParag11.setGeschossflaecheWohnenSoBoNursaechlich(BigDecimal.TEN);
        infrastrukturabfrage.getAbfragevarianten().add(abfragevariantePlanungsrechtParag11);
        assertThat(this.geschossflaecheWohnenSobonUrsaechlichValidator.isValid(infrastrukturabfrage, null), is(true));

        final AbfragevarianteResponseDto abfragevariantePlanungsrechtParag12 = new AbfragevarianteResponseDto();
        abfragevariantePlanungsrechtParag12.setPlanungsrecht(Planungsrecht.BPLAN_PARAG_12);
        abfragevariantePlanungsrechtParag12.setGeschossflaecheWohnenSoBoNursaechlich(BigDecimal.ZERO);
        infrastrukturabfrage.getAbfragevarianten().add(abfragevariantePlanungsrechtParag12);
        assertThat(this.geschossflaecheWohnenSobonUrsaechlichValidator.isValid(infrastrukturabfrage, null), is(true));

        abfragevariantePlanungsrechtParag12.setGeschossflaecheWohnenSoBoNursaechlich(null);
        assertThat(this.geschossflaecheWohnenSobonUrsaechlichValidator.isValid(infrastrukturabfrage, null), is(false));
    }

    @Test
    void isValidAbfrageNotSobonRelevant() {
        final InfrastrukturabfrageResponseDto infrastrukturabfrage = new InfrastrukturabfrageResponseDto();
        infrastrukturabfrage.setSobonRelevant(UncertainBoolean.FALSE);
        assertThat(this.geschossflaecheWohnenSobonUrsaechlichValidator.isValid(infrastrukturabfrage, null), is(true));

        infrastrukturabfrage.setAbfragevarianten(new ArrayList<AbfragevarianteResponseDto>());

        final AbfragevarianteResponseDto abfragevariantePlanungsrechtParag11 = new AbfragevarianteResponseDto();
        abfragevariantePlanungsrechtParag11.setPlanungsrecht(Planungsrecht.BPLAN_PARAG_11);
        abfragevariantePlanungsrechtParag11.setGeschossflaecheWohnenSoBoNursaechlich(BigDecimal.TEN);
        infrastrukturabfrage.getAbfragevarianten().add(abfragevariantePlanungsrechtParag11);
        assertThat(this.geschossflaecheWohnenSobonUrsaechlichValidator.isValid(infrastrukturabfrage, null), is(true));

        final AbfragevarianteResponseDto abfragevariantePlanungsrechtParag12 = new AbfragevarianteResponseDto();
        abfragevariantePlanungsrechtParag12.setPlanungsrecht(Planungsrecht.BPLAN_PARAG_12);
        abfragevariantePlanungsrechtParag12.setGeschossflaecheWohnenSoBoNursaechlich(null);
        infrastrukturabfrage.getAbfragevarianten().add(abfragevariantePlanungsrechtParag12);
        assertThat(this.geschossflaecheWohnenSobonUrsaechlichValidator.isValid(infrastrukturabfrage, null), is(true));
    }

    @Test
    void isValidAbfragevarianteNotSobonRelevant() {
        final InfrastrukturabfrageResponseDto infrastrukturabfrage = new InfrastrukturabfrageResponseDto();
        infrastrukturabfrage.setSobonRelevant(UncertainBoolean.TRUE);
        assertThat(this.geschossflaecheWohnenSobonUrsaechlichValidator.isValid(infrastrukturabfrage, null), is(true));

        infrastrukturabfrage.setAbfragevarianten(new ArrayList<AbfragevarianteResponseDto>());

        final AbfragevarianteResponseDto abfragevariantePlanungsrechtParag30 = new AbfragevarianteResponseDto();
        abfragevariantePlanungsrechtParag30.setPlanungsrecht(Planungsrecht.BPLAN_PARAG_30);
        abfragevariantePlanungsrechtParag30.setGeschossflaecheWohnenSoBoNursaechlich(BigDecimal.TEN);
        infrastrukturabfrage.getAbfragevarianten().add(abfragevariantePlanungsrechtParag30);

        final AbfragevarianteResponseDto abfragevariantePlanungsrechtParag34 = new AbfragevarianteResponseDto();
        abfragevariantePlanungsrechtParag34.setPlanungsrecht(Planungsrecht.NACHVERD_PARAG_34);
        infrastrukturabfrage.getAbfragevarianten().add(abfragevariantePlanungsrechtParag34);

        assertThat(this.geschossflaecheWohnenSobonUrsaechlichValidator.isValid(infrastrukturabfrage, null), is(true));
    }
}
