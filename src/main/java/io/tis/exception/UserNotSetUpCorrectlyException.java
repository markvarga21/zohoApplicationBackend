package io.tis.exception;

public class UserNotSetUpCorrectlyException extends RuntimeException {
    public UserNotSetUpCorrectlyException(String message) {
        super(message);
    }
}
