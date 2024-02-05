package de.muenchen.isi.domain.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MimeTypeNotAllowedException extends Exception {

    public MimeTypeNotAllowedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MimeTypeNotAllowedException(final String message) {
        super(message);
    }
}
