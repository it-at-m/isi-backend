package de.muenchen.isi.domain.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class FileHandlingWithS3FailedException extends Exception {

    private final HttpStatus statusCode;

    public FileHandlingWithS3FailedException(final String message, final HttpStatus statusCode, final Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public FileHandlingWithS3FailedException(final String message, final HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
