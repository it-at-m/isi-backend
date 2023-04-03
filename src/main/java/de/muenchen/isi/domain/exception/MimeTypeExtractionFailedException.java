package de.muenchen.isi.domain.exception;

import lombok.Data;

@Data
public class MimeTypeExtractionFailedException extends Exception {

    public MimeTypeExtractionFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MimeTypeExtractionFailedException(final String message) {
        super(message);
    }
}
