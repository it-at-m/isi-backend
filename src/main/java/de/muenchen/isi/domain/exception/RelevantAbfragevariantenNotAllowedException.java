package de.muenchen.isi.domain.exception;

public class RelevantAbfragevariantenNotAllowedException extends Exception {

    public RelevantAbfragevariantenNotAllowedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public RelevantAbfragevariantenNotAllowedException(final String message) {
        super(message);
    }
}
