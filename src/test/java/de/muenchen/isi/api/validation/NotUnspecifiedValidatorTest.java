package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ILookup;
import org.junit.jupiter.api.Test;

public class NotUnspecifiedValidatorTest {

    private final NotUnspecifiedValidator validator = new NotUnspecifiedValidator();

    @Test
    void isValid() {
        assertThat(this.validator.isValid(null, null), is(false));
        assertThat(this.validator.isValid(() -> null, null), is(false));
        assertThat(this.validator.isValid(() -> ILookup.UNSPECIFIED, null), is(false));
        assertThat(this.validator.isValid(() -> "", null), is(true));
    }
}
