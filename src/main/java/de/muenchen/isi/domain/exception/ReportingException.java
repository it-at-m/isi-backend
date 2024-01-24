package de.muenchen.isi.domain.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReportingException extends Exception {

    public ReportingException(final String message) {
        super(message);
    }

    public ReportingException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
