package de.muenchen.isi.domain.exception;

public class CsvAttributeErrorException extends Exception {

    public CsvAttributeErrorException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CsvAttributeErrorException(final String message) {
        super(message);
    }

}
