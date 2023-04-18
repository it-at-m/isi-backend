package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.AbfragevarianteDto;
import de.muenchen.isi.api.dto.InfrastrukturabfrageDto;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Planungsrecht;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class GeschossflaecheWohnenSobonUrsaechlichValidatorTest {

    private final GeschossflaecheWohnenSobonUrsaechlichValidator geschossflaecheWohnenSobonUrsaechlichValidator =
        new GeschossflaecheWohnenSobonUrsaechlichValidator();

    @Test
    void isValidSobonRelevant() {
        final InfrastrukturabfrageDto infrastrukturabfrage = new InfrastrukturabfrageDto();
        infrastrukturabfrage.setSobonRelevant(UncertainBoolean.TRUE);
        assertThat(this.geschossflaecheWohnenSobonUrsaechlichValidator.isValid(infrastrukturabfrage, null), is(true));

        infrastrukturabfrage.setAbfragevarianten(new ArrayList<AbfragevarianteDto>());

        final AbfragevarianteDto abfragevariantePlanungsrechtParag11 = new AbfragevarianteDto();
        abfragevariantePlanungsrechtParag11.setPlanungsrecht(Planungsrecht.BPLAN_PARAG_11);
        abfragevariantePlanungsrechtParag11.setGeschossflaecheWohnenSoBoNursaechlich(BigDecimal.TEN);
        infrastrukturabfrage.getAbfragevarianten().add(abfragevariantePlanungsrechtParag11);
        assertThat(this.geschossflaecheWohnenSobonUrsaechlichValidator.isValid(infrastrukturabfrage, null), is(true));

        final AbfragevarianteDto abfragevariantePlanungsrechtParag12 = new AbfragevarianteDto();
        abfragevariantePlanungsrechtParag12.setPlanungsrecht(Planungsrecht.BPLAN_PARAG_12);
        abfragevariantePlanungsrechtParag12.setGeschossflaecheWohnenSoBoNursaechlich(BigDecimal.ZERO);
        infrastrukturabfrage.getAbfragevarianten().add(abfragevariantePlanungsrechtParag12);
        assertThat(this.geschossflaecheWohnenSobonUrsaechlichValidator.isValid(infrastrukturabfrage, null), is(true));

        abfragevariantePlanungsrechtParag12.setGeschossflaecheWohnenSoBoNursaechlich(null);
        assertThat(this.geschossflaecheWohnenSobonUrsaechlichValidator.isValid(infrastrukturabfrage, null), is(false));
    }

    @Test
    void isValidAbfrageNotSobonRelevant() {
        final InfrastrukturabfrageDto infrastrukturabfrage = new InfrastrukturabfrageDto();
        infrastrukturabfrage.setSobonRelevant(UncertainBoolean.FALSE);
        assertThat(this.geschossflaecheWohnenSobonUrsaechlichValidator.isValid(infrastrukturabfrage, null), is(true));

        infrastrukturabfrage.setAbfragevarianten(new ArrayList<AbfragevarianteDto>());

        final AbfragevarianteDto abfragevariantePlanungsrechtParag11 = new AbfragevarianteDto();
        abfragevariantePlanungsrechtParag11.setPlanungsrecht(Planungsrecht.BPLAN_PARAG_11);
        abfragevariantePlanungsrechtParag11.setGeschossflaecheWohnenSoBoNursaechlich(BigDecimal.TEN);
        infrastrukturabfrage.getAbfragevarianten().add(abfragevariantePlanungsrechtParag11);
        assertThat(this.geschossflaecheWohnenSobonUrsaechlichValidator.isValid(infrastrukturabfrage, null), is(true));

        final AbfragevarianteDto abfragevariantePlanungsrechtParag12 = new AbfragevarianteDto();
        abfragevariantePlanungsrechtParag12.setPlanungsrecht(Planungsrecht.BPLAN_PARAG_12);
        abfragevariantePlanungsrechtParag12.setGeschossflaecheWohnenSoBoNursaechlich(null);
        infrastrukturabfrage.getAbfragevarianten().add(abfragevariantePlanungsrechtParag12);
        assertThat(this.geschossflaecheWohnenSobonUrsaechlichValidator.isValid(infrastrukturabfrage, null), is(true));
    }

    @Test
    void isValidAbfragevarianteNotSobonRelevant() {
        final InfrastrukturabfrageDto infrastrukturabfrage = new InfrastrukturabfrageDto();
        infrastrukturabfrage.setSobonRelevant(UncertainBoolean.TRUE);
        assertThat(this.geschossflaecheWohnenSobonUrsaechlichValidator.isValid(infrastrukturabfrage, null), is(true));

        infrastrukturabfrage.setAbfragevarianten(new ArrayList<AbfragevarianteDto>());

        final AbfragevarianteDto abfragevariantePlanungsrechtParag30 = new AbfragevarianteDto();
        abfragevariantePlanungsrechtParag30.setPlanungsrecht(Planungsrecht.BPLAN_PARAG_30);
        abfragevariantePlanungsrechtParag30.setGeschossflaecheWohnenSoBoNursaechlich(BigDecimal.TEN);
        infrastrukturabfrage.getAbfragevarianten().add(abfragevariantePlanungsrechtParag30);

        final AbfragevarianteDto abfragevariantePlanungsrechtParag34 = new AbfragevarianteDto();
        abfragevariantePlanungsrechtParag34.setPlanungsrecht(Planungsrecht.NACHVERD_PARAG_34);
        infrastrukturabfrage.getAbfragevarianten().add(abfragevariantePlanungsrechtParag34);

        assertThat(this.geschossflaecheWohnenSobonUrsaechlichValidator.isValid(infrastrukturabfrage, null), is(true));
    }
}
