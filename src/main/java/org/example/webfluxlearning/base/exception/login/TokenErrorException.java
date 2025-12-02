package org.example.webfluxlearning.base.exception.login;

public class TokenErrorException extends UserLoginException {
    public TokenErrorException(String message) {
        super(message);
    }
}
