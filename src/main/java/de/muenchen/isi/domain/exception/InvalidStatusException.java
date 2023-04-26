package de.muenchen.isi.domain.exception;

public class InvalidStatusException extends Exception {

    public InvalidStatusException(final String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidStatusException(final String message) {
        super(message);
    }
}
