package de.muenchen.isi.domain.exception;

public class InvalidTypeException extends Exception {

    public InvalidTypeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidTypeException(final String message) {
        super(message);
    }
}
