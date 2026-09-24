package de.muenchen.isi.domain.exception;

public class NotOwnerException extends Exception {

    public NotOwnerException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NotOwnerException(final String message) {
        super(message);
    }
}
