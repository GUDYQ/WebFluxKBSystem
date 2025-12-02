package org.example.webfluxlearning.base.exception.login;

public class PasswordErrorException extends UserLoginException {
    public PasswordErrorException(String message) {
        super(message);
    }
}
