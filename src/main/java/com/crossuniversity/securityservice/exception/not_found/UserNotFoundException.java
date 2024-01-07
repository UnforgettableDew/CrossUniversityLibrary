package com.crossuniversity.securityservice.exception.not_found;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
