package de.muenchen.isi.domain.exception;

public class StringLengthExceededException extends Exception {

    public StringLengthExceededException(String message) {
        super(message);
    }

    public StringLengthExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
