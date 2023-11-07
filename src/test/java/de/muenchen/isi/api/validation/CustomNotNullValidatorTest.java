package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.infrastructure.entity.Bauleitplanverfahren;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CustomNotNullValidatorTest {

    private CustomNotNullValidator customNotNullValidator = new CustomNotNullValidator();

    @Test
    void isValid() {
        assertThat(this.customNotNullValidator.isValid(Set.of(), null), is(true));
        assertThat(this.customNotNullValidator.isValid(List.of(), null), is(true));
        assertThat(this.customNotNullValidator.isValid(List.of("q"), null), is(true));
        assertThat(this.customNotNullValidator.isValid("", null), is(true));
        assertThat(this.customNotNullValidator.isValid("test", null), is(true));
        assertThat(this.customNotNullValidator.isValid(new Bauleitplanverfahren(), null), is(true));
        assertThat(this.customNotNullValidator.isValid(null, null), is(false));
    }
}
