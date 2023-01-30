package de.muenchen.isi.domain.exception;

public class StateMachineTransitionFailedException extends RuntimeException {

    public StateMachineTransitionFailedException(final Exception exception, final String message) {
        super(message, exception);
    }
}
