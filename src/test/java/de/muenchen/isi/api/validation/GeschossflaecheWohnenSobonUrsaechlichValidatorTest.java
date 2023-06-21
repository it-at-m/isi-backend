package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfragevarianteAngelegtDto;
import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.InfrastrukturabfrageAngelegtDto;
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
        final var infrastrukturabfrage = new InfrastrukturabfrageAngelegtDto();
        infrastrukturabfrage.setSobonRelevant(UncertainBoolean.TRUE);
        assertThat(this.geschossflaecheWohnenSobonUrsaechlichValidator.isValid(infrastrukturabfrage, null), is(true));

        infrastrukturabfrage.setAbfragevarianten(new ArrayList<>());

        final var abfragevariantePlanungsrechtParag11 = new AbfragevarianteAngelegtDto();
        abfragevariantePlanungsrechtParag11.setPlanungsrecht(Planungsrecht.BPLAN_PARAG_11);
        abfragevariantePlanungsrechtParag11.setGeschossflaecheWohnenSoBoNursaechlich(BigDecimal.TEN);
        infrastrukturabfrage.getAbfragevarianten().add(abfragevariantePlanungsrechtParag11);
        assertThat(this.geschossflaecheWohnenSobonUrsaechlichValidator.isValid(infrastrukturabfrage, null), is(true));

        final var abfragevariantePlanungsrechtParag12 = new AbfragevarianteAngelegtDto();
        abfragevariantePlanungsrechtParag12.setPlanungsrecht(Planungsrecht.BPLAN_PARAG_12);
        abfragevariantePlanungsrechtParag12.setGeschossflaecheWohnenSoBoNursaechlich(BigDecimal.ZERO);
        infrastrukturabfrage.getAbfragevarianten().add(abfragevariantePlanungsrechtParag12);
        assertThat(this.geschossflaecheWohnenSobonUrsaechlichValidator.isValid(infrastrukturabfrage, null), is(true));

        abfragevariantePlanungsrechtParag12.setGeschossflaecheWohnenSoBoNursaechlich(null);
        assertThat(this.geschossflaecheWohnenSobonUrsaechlichValidator.isValid(infrastrukturabfrage, null), is(false));
    }

    @Test
    void isValidAbfrageNotSobonRelevant() {
        final var infrastrukturabfrage = new InfrastrukturabfrageAngelegtDto();
        infrastrukturabfrage.setSobonRelevant(UncertainBoolean.FALSE);
        assertThat(this.geschossflaecheWohnenSobonUrsaechlichValidator.isValid(infrastrukturabfrage, null), is(true));

        infrastrukturabfrage.setAbfragevarianten(new ArrayList<>());

        final var abfragevariantePlanungsrechtParag11 = new AbfragevarianteAngelegtDto();
        abfragevariantePlanungsrechtParag11.setPlanungsrecht(Planungsrecht.BPLAN_PARAG_11);
        abfragevariantePlanungsrechtParag11.setGeschossflaecheWohnenSoBoNursaechlich(BigDecimal.TEN);
        infrastrukturabfrage.getAbfragevarianten().add(abfragevariantePlanungsrechtParag11);
        assertThat(this.geschossflaecheWohnenSobonUrsaechlichValidator.isValid(infrastrukturabfrage, null), is(true));

        final var abfragevariantePlanungsrechtParag12 = new AbfragevarianteAngelegtDto();
        abfragevariantePlanungsrechtParag12.setPlanungsrecht(Planungsrecht.BPLAN_PARAG_12);
        abfragevariantePlanungsrechtParag12.setGeschossflaecheWohnenSoBoNursaechlich(null);
        infrastrukturabfrage.getAbfragevarianten().add(abfragevariantePlanungsrechtParag12);
        assertThat(this.geschossflaecheWohnenSobonUrsaechlichValidator.isValid(infrastrukturabfrage, null), is(true));
    }

    @Test
    void isValidAbfragevarianteNotSobonRelevant() {
        final var infrastrukturabfrage = new InfrastrukturabfrageAngelegtDto();
        infrastrukturabfrage.setSobonRelevant(UncertainBoolean.TRUE);
        assertThat(this.geschossflaecheWohnenSobonUrsaechlichValidator.isValid(infrastrukturabfrage, null), is(true));

        infrastrukturabfrage.setAbfragevarianten(new ArrayList<>());

        final var abfragevariantePlanungsrechtParag30 = new AbfragevarianteAngelegtDto();
        abfragevariantePlanungsrechtParag30.setPlanungsrecht(Planungsrecht.BPLAN_PARAG_30);
        abfragevariantePlanungsrechtParag30.setGeschossflaecheWohnenSoBoNursaechlich(BigDecimal.TEN);
        infrastrukturabfrage.getAbfragevarianten().add(abfragevariantePlanungsrechtParag30);

        final var abfragevariantePlanungsrechtParag34 = new AbfragevarianteAngelegtDto();
        abfragevariantePlanungsrechtParag34.setPlanungsrecht(Planungsrecht.NACHVERD_PARAG_34);
        infrastrukturabfrage.getAbfragevarianten().add(abfragevariantePlanungsrechtParag34);

        assertThat(this.geschossflaecheWohnenSobonUrsaechlichValidator.isValid(infrastrukturabfrage, null), is(true));
    }
}
