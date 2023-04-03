package de.muenchen.isi.domain.exception;

public class OptimisticLockingException extends Exception {

    public OptimisticLockingException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public OptimisticLockingException(final String message) {
        super(message);
    }
}
