package de.muenchen.isi.domain.exception;

public class BauvorhabenNotReferencedException extends Exception {

    public BauvorhabenNotReferencedException(String message) {
        super(message);
    }

    public BauvorhabenNotReferencedException(String message, Throwable cause) {
        super(message, cause);
    }
}
