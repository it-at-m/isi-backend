package de.muenchen.isi.domain.exception;

public class IllegalChangeException extends Exception {

    public IllegalChangeException(final String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalChangeException(final String message) {
        super(message);
    }

}
