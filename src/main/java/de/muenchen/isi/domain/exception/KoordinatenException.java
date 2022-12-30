package de.muenchen.isi.domain.exception;

public class KoordinatenException extends Exception {

    public KoordinatenException(final String message, Throwable cause) {
        super(message, cause);
    }

    public KoordinatenException(final String message) {
        super(message);
    }
}
