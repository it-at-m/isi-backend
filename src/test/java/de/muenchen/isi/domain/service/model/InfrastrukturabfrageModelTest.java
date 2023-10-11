package de.muenchen.isi.domain.service.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.model.AbfrageAltModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import org.junit.jupiter.api.Test;

public class InfrastrukturabfrageModelTest {

    @Test
    void displayNameInfrastrukturabfrage() {
        final InfrastrukturabfrageModel model = new InfrastrukturabfrageModel();
        model.setAbfrage(new AbfrageAltModel());
        model.getAbfrage().setNameAbfrage("Test Name der Infrastrukturabfrage");
        model.getAbfrage().setBebauungsplannummer("Test BPlan. 12345");
        model.setAktenzeichenProLbk("Test AKZ: 54321");
        assertThat(
            model.getDisplayName(),
            is("Test Name der Infrastrukturabfrage - BPlan.: Test BPlan. 12345 - AZ: Test AKZ: 54321")
        );
    }
}
