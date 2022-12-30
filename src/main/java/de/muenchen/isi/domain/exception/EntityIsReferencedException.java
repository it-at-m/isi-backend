package de.muenchen.isi.domain.exception;

public class EntityIsReferencedException extends Exception {

    public EntityIsReferencedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EntityIsReferencedException(final String message) {
        super(message);
    }

}
