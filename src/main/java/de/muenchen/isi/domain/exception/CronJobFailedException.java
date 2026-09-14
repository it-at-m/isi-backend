package de.muenchen.isi.domain.exception;

public class CronJobFailedException extends RuntimeException {

    public CronJobFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CronJobFailedException(final String message) {
        super(message);
    }
}
