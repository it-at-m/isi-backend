package de.muenchen.isi.domain.exception;

public class ReportingException extends Exception {

    public ReportingException(String message) {
        super(message);
    }

    public ReportingException(String message, Throwable cause) {
        super(message, cause);
    }
}
