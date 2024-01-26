package de.muenchen.isi.domain.exception;

public class GeometryOperationFailedException extends Exception {

    public GeometryOperationFailedException(final String message) {
        super(message);
    }

    public GeometryOperationFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
