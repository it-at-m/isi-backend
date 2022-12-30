package de.muenchen.isi.domain.exception;

public class EntityNotFoundException extends Exception {

    public EntityNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundException(final String message) {
        super(message);
    }

}
