package de.muenchen.isi.domain.exception;

public class UniqueViolationException extends Exception {

    public UniqueViolationException(final String message, Throwable cause) {
        super(message, cause);
    }

    public UniqueViolationException(final String message) {
        super(message);
    }
}
