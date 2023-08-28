package de.muenchen.isi.domain.exception;

public class UserRoleNotAllowedException extends Exception {

    public UserRoleNotAllowedException(String message) {
        super(message);
    }

    public UserRoleNotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }
}
