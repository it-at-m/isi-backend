package de.muenchen.isi.domain.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MimeTypeExtractionFailedException extends Exception {

    public MimeTypeExtractionFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MimeTypeExtractionFailedException(final String message) {
        super(message);
    }
}
