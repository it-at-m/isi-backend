package de.muenchen.isi.domain.exception;

public class CalculationException extends Exception {

    public CalculationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CalculationException(final String message) {
        super(message);
    }
}
