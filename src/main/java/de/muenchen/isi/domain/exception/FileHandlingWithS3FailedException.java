package de.muenchen.isi.domain.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.http.HttpStatusCode;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FileHandlingWithS3FailedException extends Exception {

    private final HttpStatusCode statusCode;

    public FileHandlingWithS3FailedException(
        final String message,
        final HttpStatusCode statusCode,
        final Throwable cause
    ) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public FileHandlingWithS3FailedException(final String message, final HttpStatusCode statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
