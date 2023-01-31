package de.muenchen.isi.domain.exception;

public class StateMachineTransitionFailedException extends RuntimeException {

    public StateMachineTransitionFailedException(final String message, final Exception exception) {
        super(message, exception);
    }

    public StateMachineTransitionFailedException(final String message) {
        super(message);
    }

}
