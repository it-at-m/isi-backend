package de.muenchen.isi.domain.exception;

public class FileHandlingFailedException extends Exception {

    public FileHandlingFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public FileHandlingFailedException(final String message) {
        super(message);
    }

}
