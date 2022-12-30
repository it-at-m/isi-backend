package de.muenchen.isi.domain.exception;

public class AbfrageStatusNotAllowedException extends Exception {

    public AbfrageStatusNotAllowedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AbfrageStatusNotAllowedException(final String message) {
        super(message);
    }

}
