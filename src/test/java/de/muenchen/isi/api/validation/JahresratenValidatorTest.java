package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.stammdaten.baurate.JahresrateDto;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class JahresratenValidatorTest {

    private final JahresratenValidator jahresratenValidator = new JahresratenValidator();

    @Test
    void isValid() {
        var jahresraten = new ArrayList<JahresrateDto>();
        var jahresrate = new JahresrateDto();
        jahresrate.setRate(BigDecimal.valueOf(0.25));
        jahresraten.add(jahresrate);
        jahresrate = new JahresrateDto();
        jahresrate.setRate(BigDecimal.valueOf(0.25));
        jahresraten.add(jahresrate);
        jahresrate = new JahresrateDto();
        jahresrate.setRate(BigDecimal.valueOf(0.25));
        jahresraten.add(jahresrate);
        jahresrate = new JahresrateDto();
        jahresrate.setRate(BigDecimal.valueOf(0.25));
        jahresraten.add(jahresrate);
        assertThat(this.jahresratenValidator.isValid(jahresraten, null), is(true));

        jahresraten = new ArrayList<>();
        jahresrate = new JahresrateDto();
        jahresrate.setRate(BigDecimal.valueOf(0.25));
        jahresraten.add(jahresrate);
        jahresrate = new JahresrateDto();
        jahresrate.setRate(BigDecimal.valueOf(0.25));
        jahresraten.add(jahresrate);
        jahresrate = new JahresrateDto();
        jahresrate.setRate(BigDecimal.valueOf(0.25));
        jahresraten.add(jahresrate);
        jahresrate = new JahresrateDto();
        jahresrate.setRate(BigDecimal.valueOf(0.249999999999));
        jahresraten.add(jahresrate);
        assertThat(this.jahresratenValidator.isValid(jahresraten, null), is(false));

        jahresraten = new ArrayList<>();
        jahresrate = new JahresrateDto();
        jahresrate.setRate(BigDecimal.valueOf(0.25));
        jahresraten.add(jahresrate);
        jahresrate = new JahresrateDto();
        jahresrate.setRate(BigDecimal.valueOf(0.25));
        jahresraten.add(jahresrate);
        jahresrate = new JahresrateDto();
        jahresrate.setRate(BigDecimal.valueOf(0.25));
        jahresraten.add(jahresrate);
        jahresrate = new JahresrateDto();
        jahresrate.setRate(BigDecimal.valueOf(0.250000000001));
        jahresraten.add(jahresrate);
        assertThat(this.jahresratenValidator.isValid(jahresraten, null), is(false));

        jahresraten = new ArrayList<>();
        jahresrate = new JahresrateDto();
        jahresrate.setRate(BigDecimal.valueOf(0.25));
        jahresraten.add(jahresrate);
        jahresrate = new JahresrateDto();
        jahresrate.setRate(BigDecimal.valueOf(0.25));
        jahresraten.add(jahresrate);
        jahresrate = new JahresrateDto();
        jahresrate.setRate(BigDecimal.valueOf(0.25));
        jahresraten.add(jahresrate);
        jahresrate = new JahresrateDto();
        jahresrate.setRate(BigDecimal.valueOf(0.25));
        jahresraten.add(jahresrate);
        jahresrate = new JahresrateDto();
        // Wird durch Attributvalidierung abgefangen.
        jahresrate.setRate(null);
        jahresraten.add(jahresrate);
        assertThat(this.jahresratenValidator.isValid(jahresraten, null), is(true));
    }
}
