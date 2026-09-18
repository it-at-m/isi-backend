package de.muenchen.isi.domain.exception;

public class MaxCreationsReachedException extends Exception {

    public MaxCreationsReachedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MaxCreationsReachedException(final String message) {
        super(message);
    }
}
