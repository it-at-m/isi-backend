package de.muenchen.isi.api.validation;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class IsFilepathWithoutLeadingPathdividerValidatorTest {

    private final IsFilepathWithoutLeadingPathdividerValidator isFilepathWithoutLeadingPathdividerValidator = new IsFilepathWithoutLeadingPathdividerValidator();

    @Test
    void isValid() {
        final var notValidFilePath = "/outerFolder/innerFolder/thefile.csv";
        assertThat(
                this.isFilepathWithoutLeadingPathdividerValidator.isValid(notValidFilePath, null),
                is(false)
        );

        final var validFilePath = "outerFolder/innerFolder/thefile.csv";
        assertThat(
                this.isFilepathWithoutLeadingPathdividerValidator.isValid(validFilePath, null),
                is(true)
        );

        assertThat(
                this.isFilepathWithoutLeadingPathdividerValidator.isValid(null, null),
                is(true)
        );
    }
}