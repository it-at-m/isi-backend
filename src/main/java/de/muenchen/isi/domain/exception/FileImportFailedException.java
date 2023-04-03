package de.muenchen.isi.domain.exception;

public class FileImportFailedException extends Exception {

    public FileImportFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public FileImportFailedException(final String message) {
        super(message);
    }
}
